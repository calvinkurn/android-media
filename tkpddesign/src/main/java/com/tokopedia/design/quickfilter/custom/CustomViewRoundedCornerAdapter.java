package com.tokopedia.design.quickfilter.custom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.R;
import com.tokopedia.design.quickfilter.ItemFilterViewHolder;
import com.tokopedia.design.quickfilter.QuickSingleFilterAdapter;
import com.tokopedia.design.quickfilter.QuickSingleFilterListener;

public class CustomViewRoundedCornerAdapter extends QuickSingleFilterAdapter {

    public CustomViewRoundedCornerAdapter(QuickSingleFilterListener actionListener) {
        super(actionListener);
    }

    @Override
    public CustomViewRoundedFilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_custom_rounded_filter_view, parent, false);
        return new CustomViewRoundedFilterViewHolder(view, actionListener);
    }

    @Override
    public void onBindViewHolder(ItemFilterViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}
