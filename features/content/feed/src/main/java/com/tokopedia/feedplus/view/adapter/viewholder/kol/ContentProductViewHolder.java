package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.kol.ProductCommunicationViewModel;

/**
 * Created by yfsx on 02/01/18.
 */

public class ContentProductViewHolder extends AbstractViewHolder<ProductCommunicationViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.product_communication;

    private FeedPlus.View mainView;

    public ContentProductViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
        mainView = viewListener;
    }

    @Override
    public void bind(ProductCommunicationViewModel element) {
        initView();
    }

    private void initView() {
    }
}
