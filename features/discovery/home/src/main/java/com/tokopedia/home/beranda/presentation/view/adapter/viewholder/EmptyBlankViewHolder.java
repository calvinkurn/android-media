package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;

/**
 * Created by meta on 22/03/18.
 */

public class EmptyBlankViewHolder extends AbstractViewHolder<DynamicChannelViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_blank;

    public EmptyBlankViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(DynamicChannelViewModel element) {

    }
}
