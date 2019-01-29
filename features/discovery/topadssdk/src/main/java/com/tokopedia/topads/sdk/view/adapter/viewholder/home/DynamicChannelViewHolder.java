package com.tokopedia.topads.sdk.view.adapter.viewholder.home;

import android.os.Build;
import android.support.annotation.LayoutRes;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.view.ImpressedImageView;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.home.ProductDynamicChannelViewModel;

public class DynamicChannelViewHolder extends AbstractViewHolder<ProductDynamicChannelViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_dynamic_channel_item;

    private ImpressedImageView imageView;
    private TextView cashbackTxt;
    private TextView priceTxt;
    private TextView productTxt;
    private LinearLayout container;
    private LocalAdsClickListener itemClickListener;
    private TopAdsItemImpressionListener impressionListener;

    public DynamicChannelViewHolder(View itemView, LocalAdsClickListener itemClickListener, TopAdsItemImpressionListener impressionListener) {
        super(itemView);
        this.itemClickListener = itemClickListener;
        this.impressionListener = impressionListener;
        imageView = (ImpressedImageView) itemView.findViewById(R.id.image);
        productTxt = itemView.findViewById(R.id.product_name);
        cashbackTxt = itemView.findViewById(R.id.bottom_label);
        priceTxt = itemView.findViewById(R.id.product_price);
        container = itemView.findViewById(R.id.container);
    }

    @Override
    public void bind(ProductDynamicChannelViewModel element) {
        try {
            imageView.setImage(element.getProductImage());
            imageView.setViewHintListener(new ImpressedImageView.ViewHintListener() {
                @Override
                public void onViewHint() {
                    if(impressionListener!=null){
                        Product product = new Product();
                        product.setName(element.getProductName());
                        product.setPriceFormat(element.getProductPrice());
                        product.setId(element.getProductId());
                        product.setApplinks(element.getApplink());
                        impressionListener.onImpressionProductAdsItem(getAdapterPosition(), product);
                    }
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                productTxt.setText(Html.fromHtml(element.getProductName(),
                        Html.FROM_HTML_MODE_LEGACY));
            } else {
                productTxt.setText(Html.fromHtml(element.getProductName()));
            }
            priceTxt.setText(element.getProductPrice());
            if (!element.getProductCashback().isEmpty()) {
                cashbackTxt.setText(element.getProductCashback());
                cashbackTxt.setVisibility(View.VISIBLE);
            } else {
                cashbackTxt.setVisibility(View.GONE);
            }
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        Data data = new Data();
                        data.setProductClickUrl(element.getProductClickUrl());
                        Product product = new Product();
                        product.setId(element.getProductId());
                        product.setName(element.getProductName());
                        product.setPriceFormat(element.getProductPrice());
                        product.setProductCashbackRate(element.getProductCashback());
                        product.setImage(element.getProductImage());
                        data.setProduct(product);
                        itemClickListener.onProductItemClicked(getAdapterPosition(), data);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
