package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.SellViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by errysuprayogi on 12/5/17.
 */

public class SellViewHolder extends AbstractViewHolder<SellViewModel>  {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_sell;

    @BindView(R.id.title)
    TextView titleTxt;
    @BindView(R.id.subtitle)
    TextView subtitleTxt;
    @BindView(R.id.open_shop_btn)
    Button button;

    private HomeCategoryListener listener;

    public SellViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        this.listener = listener;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(SellViewModel element) {
        titleTxt.setText(element.getTitle());
        subtitleTxt.setText(element.getSubtitle());
        button.setText(element.getBtn_title());
    }

    @OnClick(R.id.open_shop_btn)
    void openShop(){
        listener.openShop();
    }
}
