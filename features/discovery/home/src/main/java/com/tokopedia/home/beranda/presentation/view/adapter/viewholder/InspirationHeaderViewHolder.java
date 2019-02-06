package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.InspirationHeaderViewModel;

public class InspirationHeaderViewHolder extends AbstractViewHolder<InspirationHeaderViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.inspiration_header;

    private TextView titleView;

    public InspirationHeaderViewHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.title);
    }

    @Override
    public void bind(InspirationHeaderViewModel element) {
        titleView.setText(element.getTitle());
    }
}
