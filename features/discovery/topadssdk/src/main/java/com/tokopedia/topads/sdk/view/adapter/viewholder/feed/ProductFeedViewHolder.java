package com.tokopedia.topads.sdk.view.adapter.viewholder.feed;

import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.PositionChangeListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.view.ImpressedImageView;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feed.ProductFeedViewModel;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class ProductFeedViewHolder extends AbstractViewHolder<ProductFeedViewModel> implements
        View.OnClickListener, PositionChangeListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_product_feed;
    private static final String TAG = ProductFeedViewHolder.class.getSimpleName();

    private LocalAdsClickListener itemClickListener;
    private Data data;
    private Context context;
    public TextView productName;
    public TextView productPrice;
    public ImpressedImageView productImage;
    private int adapterPosition = 0;
    private TopAdsItemImpressionListener impressionListener;
    private int clickPosition = RecyclerView.NO_POSITION;

    public ProductFeedViewHolder(View itemView, LocalAdsClickListener itemClickListener,
                                 TopAdsItemImpressionListener impressionListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.itemClickListener = itemClickListener;
        this.impressionListener = impressionListener;
        context = itemView.getContext();
        productImage = (ImpressedImageView) itemView.findViewById(R.id.product_image);
        productName = (TextView) itemView.findViewById(R.id.title);
        productPrice = (TextView) itemView.findViewById(R.id.price);
    }

    @Override
    public void bind(ProductFeedViewModel element) {
        data = element.getData();
        if (data.getProduct() != null && !TextUtils.isEmpty(data.getProduct().getId())) {
            bindProduct(data.getProduct());
        }
    }


    private void bindProduct(final Product product) {
        productImage.setImage(product.getImage());
        productImage.setViewHintListener(new ImpressedImageView.ViewHintListener() {
            @Override
            public void onViewHint() {
                if(impressionListener!=null){
                    impressionListener.onImpressionProductAdsItem((clickPosition < 0 ?
                            getAdapterPosition() : clickPosition), product);
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            productName.setText(Html.fromHtml(product.getName(),
                    Html.FROM_HTML_MODE_LEGACY));
        } else {
            productName.setText(Html.fromHtml(product.getName()));
        }
        productPrice.setText(product.getPriceFormat());
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            itemClickListener.onProductItemClicked(adapterPosition == RecyclerView.NO_POSITION ?
                    getAdapterPosition() : adapterPosition, data);
        }
    }

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }



    @Override
    public void onPositionChange(int position) {
        this.clickPosition = position;
    }

}
