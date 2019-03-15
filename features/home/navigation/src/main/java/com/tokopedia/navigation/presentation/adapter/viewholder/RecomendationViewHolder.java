package com.tokopedia.navigation.presentation.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.analytics.InboxGtmTracker;
import com.tokopedia.navigation.domain.model.Recomendation;
import com.tokopedia.navigation.presentation.adapter.InboxAdapterListener;
import com.tokopedia.productcard.ProductCardView;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.domain.model.Category;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.topads.sdk.view.ImpressedImageView;

/**
 * Author errysuprayogi on 13,March,2019
 */
public class RecomendationViewHolder extends AbstractViewHolder<Recomendation> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_recomendation;
    private ProductCardView productCardView;
    private InboxAdapterListener listener;
    private Context context;

    public RecomendationViewHolder(View itemView, InboxAdapterListener listener) {
        super(itemView);
        this.context = itemView.getContext();
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
                    if(element.isTopAds()) {
                        new ImpresionTask().execute(element.getTrackerImageUrl());
                        Product product = new Product();
                        product.setId(String.valueOf(element.getProductId()));
                        product.setName(element.getProductName());
                        product.setPriceFormat(element.getPrice());
                        product.setCategory(new Category(element.getDepartementId()));
                        TopAdsGtmTracker.getInstance().addInboxProductViewImpressions(product, getAdapterPosition());
                    } else {
                        InboxGtmTracker.getInstance().addInboxProductViewImpressions(element, getAdapterPosition());
                    }
                }
            });
        }
        productCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClickListener(element, getAdapterPosition());
                if(element.isTopAds()) {
                    new ImpresionTask().execute(element.getClickUrl());
                    Product product = new Product();
                    product.setId(String.valueOf(element.getProductId()));
                    product.setName(element.getProductName());
                    product.setPriceFormat(element.getPrice());
                    product.setCategory(new Category(element.getDepartementId()));
                    TopAdsGtmTracker.getInstance().eventInboxProductClick(context, product, getAdapterPosition());
                }
            }
        });
    }
}
