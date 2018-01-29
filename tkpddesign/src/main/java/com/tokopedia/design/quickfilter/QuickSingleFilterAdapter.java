package com.tokopedia.design.quickfilter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.R;

/**
 * Created by nabillasabbaha on 11/22/17.
 */

public class QuickSingleFilterAdapter extends BaseQuickSingleFilterAdapter<ItemFilterViewHolder> {

    protected QuickSingleFilterListener actionListener;

    public QuickSingleFilterAdapter(QuickSingleFilterListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public ItemFilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quick_filter_view, parent, false);
        return new ItemFilterViewHolder(view, actionListener);
    }

    @Override
    public void onBindViewHolder(ItemFilterViewHolder holder, int position) {
        holder.renderItemViewHolder(filterList.get(position));
    }
}