package com.tokopedia.topads.sdk.view.adapter.viewholder.banner;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.topads.sdk.view.ImpressedImageView;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductViewModel;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShopProductViewHolder extends AbstractViewHolder<BannerShopProductViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_banner_shop_product;
    private static final String TAG = BannerShopProductViewHolder.class.getSimpleName();
    private ImpressedImageView imageView;
    private TextView descTxt;
    private TextView priceTxt;
    private final TopAdsBannerClickListener topAdsBannerClickListener;
    private final TopAdsItemImpressionListener impressionListener;
    private Context context;
    private View container;


    public BannerShopProductViewHolder(View itemView, TopAdsBannerClickListener topAdsBannerClickListener,
                                       TopAdsItemImpressionListener itemImpressionListener) {
        super(itemView);
        this.container = itemView;
        this.context = itemView.getContext();
        this.topAdsBannerClickListener = topAdsBannerClickListener;
        this.impressionListener = itemImpressionListener;
        imageView = itemView.findViewById(R.id.icon);
        descTxt = itemView.findViewById(R.id.description);
        priceTxt = itemView.findViewById(R.id.price);
    }

    @Override
    public void bind(final BannerShopProductViewModel element) {
        final Product product = element.getProduct();
        imageView.setImage(product.getImageProduct());
        imageView.setViewHintListener(new ImpressedImageView.ViewHintListener() {
            @Override
            public void onViewHint() {
                if (impressionListener != null) {
                    impressionListener.onImpressionProductAdsItem(getAdapterPosition(), product);
                }
            }
        });
        descTxt.setText(TopAdsBannerView.Companion.escapeHTML(element.getProduct().getName()));
        priceTxt.setText(element.getProduct().getPriceFormat());
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topAdsBannerClickListener != null) {
                    topAdsBannerClickListener.onBannerAdsClicked((getAdapterPosition()),
                            element.getProduct().getApplinks(), element.getCpmData());
                    new ImpresionTask().execute(element.getProduct().getImageProduct().getImageClickUrl());
                }
            }
        });
    }

}
