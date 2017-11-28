package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BannerViewModel;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class BannerViewHolder extends AbstractViewHolder<BannerViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.home_banner;

    public BannerViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(BannerViewModel element) {

    }
}
