package com.tokopedia.navigation.presentation.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.domain.model.Recomendation;
import com.tokopedia.productcard.ProductCardView;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.topads.sdk.view.ImpressedImageView;

/**
 * Author errysuprayogi on 13,March,2019
 */
class RecomendationViewHolder extends AbstractViewHolder<Recomendation> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_recomendation;
    private ProductCardView productCardView;
    private InboxAdapterListener listener;

    public RecomendationViewHolder(View itemView, InboxAdapterListener listener) {
        super(itemView);
        productCardView = itemView.findViewById(R.id.productCardView);
        this.listener = listener;
    }

    @Override
    public void bind(Recomendation element) {
        productCardView.setImageUrl(element.getImageUrl());
        productCardView.setTitle(element.getProductName());
        productCardView.setPrice(element.getPrice());
        productCardView.setTopAdsVisible(element.isTopAds());
        if (element.isTopAds()) {
            productCardView.imageView.setViewHintListener(element, new ImpressedImageView.ViewHintListener() {
                @Override
                public void onViewHint() {
                    new ImpresionTask().execute(element.getTrackerImageUrl());
                }
            });
        }
        productCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClickListener(element, getAdapterPosition());
                new ImpresionTask().execute(element.getClickUrl());
            }
        });
    }
}
