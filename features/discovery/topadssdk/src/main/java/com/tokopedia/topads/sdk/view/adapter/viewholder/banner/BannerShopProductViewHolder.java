package com.tokopedia.topads.sdk.view.adapter.viewholder.banner;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.topads.sdk.view.ImpressedImageView;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductViewModel;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShopProductViewHolder extends AbstractViewHolder<BannerShopProductViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_banner_shop_product;
    private static final String TAG = BannerShopProductViewHolder.class.getSimpleName();
    private ImageLoader imageLoader;
    private ImageView imageView;
    private TextView descTxt;
    private TextView priceTxt;
    private LinearLayout layoutContainer;
    private final TopAdsBannerClickListener topAdsBannerClickListener;


    public BannerShopProductViewHolder(View itemView, TopAdsBannerClickListener topAdsBannerClickListener) {
        super(itemView);
        this.topAdsBannerClickListener = topAdsBannerClickListener;
        imageLoader = new ImageLoader(itemView.getContext());
        imageView = itemView.findViewById(R.id.icon);
        descTxt = itemView.findViewById(R.id.description);
        priceTxt = itemView.findViewById(R.id.price);
        layoutContainer = itemView.findViewById(R.id.layout_container);
    }

    @Override
    public void bind(final BannerShopProductViewModel element) {
        final Product product = element.getProduct();
        imageLoader.loadImage(product.getImageProduct().getImageUrl(), product.getImageProduct().getImageUrl(), imageView);
        descTxt.setText(TopAdsBannerView.escapeHTML(element.getProduct().getName()));
        priceTxt.setText(element.getProduct().getPriceFormat());
        layoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topAdsBannerClickListener != null) {
                    topAdsBannerClickListener.onBannerAdsClicked(element.getProduct().getApplinks());
                    new ImpresionTask().execute(element.getProduct().getImageProduct().getImageClickUrl());
                }
            }
        });
    }

}
