package com.tokopedia.travelcalendar.view.widget.quickfilter.custom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.R;
import com.tokopedia.travelcalendar.view.widget.quickfilter.QuickSingleFilterAdapter;
import com.tokopedia.travelcalendar.view.widget.quickfilter.QuickSingleFilterListener;

/**
 * Created by nabillasabbaha on 11/22/17.
 */

public class CustomViewQuickSingleFilterAdapter extends QuickSingleFilterAdapter {

    public CustomViewQuickSingleFilterAdapter(QuickSingleFilterListener actionListener) {
        super(actionListener);
    }

    @Override
    public CustomViewItemFilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_custom_quick_filter_view, parent, false);
        return new CustomViewItemFilterViewHolder(view, actionListener);
    }
}