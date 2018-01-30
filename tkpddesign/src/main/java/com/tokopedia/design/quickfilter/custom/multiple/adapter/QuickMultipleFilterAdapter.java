package com.tokopedia.design.quickfilter.custom.multiple.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.R;
import com.tokopedia.design.quickfilter.custom.multiple.adapter.viewholder.QuickMultipleFilterViewHolder;
import com.tokopedia.design.quickfilter.custom.multiple.listener.QuickMultipleItemListener;

/**
 * Created by yfsx on 30/01/18.
 */

public class QuickMultipleFilterAdapter extends BaseQuickMultipleFilterAdapter<QuickMultipleFilterViewHolder> {
    private QuickMultipleItemListener listener;

    public QuickMultipleFilterAdapter(QuickMultipleItemListener listener) {
        this.listener = listener;
    }

    @Override
    public QuickMultipleFilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quick_multiple_filter_view, parent, false);
        return new QuickMultipleFilterViewHolder(view, parent.getContext(), listener);
    }

    @Override
    public void onBindViewHolder(QuickMultipleFilterViewHolder holder, int position) {
        ((QuickMultipleFilterViewHolder) holder).renderItemViewHolder(filterList.get(position));
    }
}
