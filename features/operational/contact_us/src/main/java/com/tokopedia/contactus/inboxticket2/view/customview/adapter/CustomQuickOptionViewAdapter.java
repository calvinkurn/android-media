package com.tokopedia.contactus.inboxticket2.view.customview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.contactus.R;
import com.tokopedia.design.quickfilter.ItemFilterViewHolder;
import com.tokopedia.design.quickfilter.QuickSingleFilterAdapter;
import com.tokopedia.design.quickfilter.QuickSingleFilterListener;

public class CustomQuickOptionViewAdapter extends QuickSingleFilterAdapter {
    public CustomQuickOptionViewAdapter(QuickSingleFilterListener actionListener) {
        super(actionListener);
    }
    @Override
    public ItemFilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_option_item, parent, false);
        return new OptionItemViewHolder(view, actionListener);
    }
}
