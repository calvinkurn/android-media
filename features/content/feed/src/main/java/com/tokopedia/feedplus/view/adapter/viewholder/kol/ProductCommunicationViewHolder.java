package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.kol.ProductCommunicationViewModel;

/**
 * @author by milhamj on 08/05/18.
 */

public class ProductCommunicationViewHolder extends
        AbstractViewHolder<ProductCommunicationViewModel> {

    private final FeedPlus.View viewListener;

    @LayoutRes
    public static final int LAYOUT = R.layout.product_communication;


    public ProductCommunicationViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(ProductCommunicationViewModel element) {

    }
}
