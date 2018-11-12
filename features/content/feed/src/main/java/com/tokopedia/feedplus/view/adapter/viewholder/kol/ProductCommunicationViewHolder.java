package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.analytics.FeedAnalytics;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.kol.ProductCommunicationViewModel;

/**
 * @author by milhamj on 08/05/18.
 */

public class ProductCommunicationViewHolder extends
        AbstractViewHolder<ProductCommunicationViewModel> {

    private final ProductCommunicationAdapter adapter;

    @LayoutRes
    public static final int LAYOUT = R.layout.product_communication;


    public ProductCommunicationViewHolder(View itemView, FeedPlus.View viewListener, FeedAnalytics analytics) {
        super(itemView);
        RecyclerView productCommunicationRv = itemView.findViewById(R.id.product_communication_rv);
        adapter = new ProductCommunicationAdapter(getAdapterPosition(), viewListener, analytics);
        productCommunicationRv.setAdapter(adapter);
    }

    @Override
    public void bind(ProductCommunicationViewModel element) {
        adapter.setData(element.getItemViewModels());
    }
}
