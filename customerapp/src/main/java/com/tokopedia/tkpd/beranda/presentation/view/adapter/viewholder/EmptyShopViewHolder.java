package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.EmptyShopViewModel;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by errysuprayogi on 12/5/17.
 */

public class EmptyShopViewHolder extends AbstractViewHolder<EmptyShopViewModel>  {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_empty_shop;

    private HomeCategoryListener listener;

    public EmptyShopViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        this.listener = listener;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(EmptyShopViewModel element) {

    }

    @OnClick(R.id.open_shop_btn)
    void openShop(){
        listener.openShop();
    }
}
