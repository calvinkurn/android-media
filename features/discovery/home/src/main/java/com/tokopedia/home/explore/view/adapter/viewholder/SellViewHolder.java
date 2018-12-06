package com.tokopedia.home.explore.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.explore.listener.CategoryAdapterListener;
import com.tokopedia.home.explore.view.adapter.viewmodel.SellViewModel;

/**
 * Created by errysuprayogi on 12/5/17.
 */

public class SellViewHolder extends AbstractViewHolder<SellViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_sell;

    TextView titleTxt;
    TextView subtitleTxt;
    Button button;

    private CategoryAdapterListener listener;

    public SellViewHolder(View itemView, final CategoryAdapterListener listener) {
        super(itemView);
        this.listener = listener;
        titleTxt = itemView.findViewById(R.id.title);
        subtitleTxt = itemView.findViewById(R.id.subtitle);
        button = itemView.findViewById(R.id.open_shop_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.openShop();
            }
        });
    }

    @Override
    public void bind(SellViewModel element) {
        titleTxt.setText(element.getTitle());
        subtitleTxt.setText(element.getSubtitle());
        button.setText(element.getBtn_title());
    }

}
