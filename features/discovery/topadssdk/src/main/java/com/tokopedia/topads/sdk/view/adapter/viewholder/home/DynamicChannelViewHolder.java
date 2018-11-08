package com.tokopedia.topads.sdk.view.adapter.viewholder.home;

import android.os.Build;
import android.support.annotation.LayoutRes;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.ProductImage;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.view.ImpressedImageView;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.home.ProductDynamicChannelViewModel;

public class DynamicChannelViewHolder extends AbstractViewHolder<ProductDynamicChannelViewModel>
        implements View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_dynamic_channel_item;

    private ImpressedImageView imageView;
    private TextView cashbackTxt;
    private TextView priceTxt;
    private TextView productTxt;
    private LocalAdsClickListener itemClickListener;

    public DynamicChannelViewHolder(View itemView, LocalAdsClickListener itemClickListener) {
        super(itemView);
        this.itemClickListener = itemClickListener;
        imageView = (ImpressedImageView) itemView.findViewById(R.id.image);
        productTxt = itemView.findViewById(R.id.product_name);
        cashbackTxt = itemView.findViewById(R.id.bottom_label);
        priceTxt = itemView.findViewById(R.id.product_price);
    }

    @Override
    public void onClick(View v) {
        if(itemClickListener!=null){
            itemClickListener.onProductItemClicked(getAdapterPosition(), null);
        }
    }

    @Override
    public void bind(ProductDynamicChannelViewModel element) {
        ProductImage productImage = new ProductImage();
        productImage.setM_ecs(element.getImageUrl());
        productImage.setM_url(element.getImpressionUrl());
        imageView.setImage(productImage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            productTxt.setText(Html.fromHtml(element.getProductName(),
                    Html.FROM_HTML_MODE_LEGACY));
        } else {
            productTxt.setText(Html.fromHtml(element.getProductName()));
        }
        priceTxt.setText(element.getProductPrice());
        if(!element.getProductCashback().isEmpty()){
            cashbackTxt.setText(element.getProductCashback());
            cashbackTxt.setVisibility(View.VISIBLE);
        } else {
            cashbackTxt.setVisibility(View.GONE);
        }
    }
}
