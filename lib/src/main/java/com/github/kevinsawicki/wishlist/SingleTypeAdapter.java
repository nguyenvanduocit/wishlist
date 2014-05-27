/*
 * Copyright 2012 Kevin Sawicki <kevinsawicki@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.kevinsawicki.wishlist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Adapter for lists where only a single view type is used
 *
 * @param <V>
 */
public abstract class SingleTypeAdapter<V> extends TypeAdapter {

    private final LayoutInflater inflater;

    private final int layout;

    private final int[] children;

    private final List<V> items = new ArrayList<V>();

    /**
     * Create adapter
     *
     * @param activity
     * @param layoutResourceId
     */
    public SingleTypeAdapter(final Activity activity, final int layoutResourceId) {
        this(activity.getLayoutInflater(), layoutResourceId);
    }

    /**
     * Create adapter
     *
     * @param context
     * @param layoutResourceId
     */
    public SingleTypeAdapter(final Context context, final int layoutResourceId) {
        this(LayoutInflater.from(context), layoutResourceId);
    }

    /**
     * Create adapter
     *
     * @param inflater
     * @param layoutResourceId
     */
    public SingleTypeAdapter(final LayoutInflater inflater,
                             final int layoutResourceId) {
        this.inflater = inflater;
        this.layout = layoutResourceId;

        int[] childIds = getChildViewIds();
        if (childIds == null)
            childIds = new int[0];
        children = childIds;
    }

    /**
     * Clear all items
     *
     * @return this adapter
     */
    public SingleTypeAdapter clear() {
        items.clear();

        notifyDataSetChanged();
        return this;
    }

    /**
     * Add item to adapter registered as the given type
     *
     * @param item
     * @return this adapter
     */
    public SingleTypeAdapter addItem(final V item) {
        items.add(item);

        notifyDataSetChanged();
        return this;
    }

    /**
     * Add items to adapter registered as the given type
     *
     * @param items
     * @return this adapter
     */
    public SingleTypeAdapter addItems(final Collection<V> items) {
        if (items == null || items.size() == 0)
            return this;

        for (V item : items)
            this.items.add(item);

        notifyDataSetChanged();
        return this;
    }

    /**
     * Add items to adapter registered as the given type
     *
     * @param items
     * @return this adapter
     */
    public SingleTypeAdapter setItems(final Collection<V> items) {
        if (items == null || items.size() == 0)
            return this;

        this.items.clear();
        addItems(items);

        notifyDataSetChanged();
        return this;
    }
    /**
     * Remove item at position
     *
     * @param position
     * @return this adapter
     */
    public SingleTypeAdapter removeItem(final int position) {
        if (position > 0 && position < items.size()
                && items.remove(position) != null)
            notifyDataSetChanged();
        return this;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public V getItem(final int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return items.get(position).hashCode();
    }

    /**
     * Get child view ids to store
     * <p/>
     * The index of each id in the returned array should be used when using the
     * helpers to update a specific child view
     *
     * @return ids
     */
    protected abstract int[] getChildViewIds();

    /**
     * Initialize view
     *
     * @param view
     * @return view
     */
    protected View initialize(final View view) {
        return super.initialize(view, children);
    }

    /**
     * Update view for item
     *
     * @param position
     * @param view
     * @param item
     */
    protected void update(int position, View view, V item) {
        setCurrentView(view);
        update(position, item);
    }

    /**
     * Update item
     *
     * @param position
     * @param item
     */
    protected abstract void update(int position, V item);

    @Override
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {
        if (convertView == null)
            convertView = initialize(inflater.inflate(layout, null));
        update(position, convertView, getItem(position));
        return convertView;
    }
}
