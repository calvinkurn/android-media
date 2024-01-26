package com.tokopedia.travelcalendar.view.widget.quickfilter.custom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.R;
import com.tokopedia.travelcalendar.view.widget.quickfilter.ItemFilterViewHolder;
import com.tokopedia.travelcalendar.view.widget.quickfilter.QuickSingleFilterAdapter;
import com.tokopedia.travelcalendar.view.widget.quickfilter.QuickSingleFilterListener;

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
