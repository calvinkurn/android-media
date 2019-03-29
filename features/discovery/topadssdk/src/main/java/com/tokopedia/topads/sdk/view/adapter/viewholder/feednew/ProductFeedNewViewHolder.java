package com.tokopedia.topads.sdk.view.adapter.viewholder.feednew;

import android.os.Build;
import android.support.annotation.LayoutRes;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.view.ImpressedImageView;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feednew.ProductFeedNewViewModel;

/**
 * @author by milhamj on 29/03/18.
 */

public class ProductFeedNewViewHolder extends AbstractViewHolder<ProductFeedNewViewModel>
        implements View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_product_feed_new;

    private Data data;
    private TextView productName;
    private ImpressedImageView productImage;
    private TextView productPrice;
    private LocalAdsClickListener itemClickListener;

    public ProductFeedNewViewHolder(View itemView, LocalAdsClickListener
            itemClickListener) {
        super(itemView);
        this.itemClickListener = itemClickListener;

        productImage = itemView.findViewById(R.id.product_image);
        productName = itemView.findViewById(R.id.title);
        productPrice = itemView.findViewById(R.id.price);

        itemView.setOnClickListener(this);
        itemView.findViewById(R.id.product_layout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            itemClickListener.onProductItemClicked(getAdapterPosition(), data);
        }
    }

    @Override
    public void bind(ProductFeedNewViewModel element) {
        data = element.getData();
        if (data.getProduct() != null && !TextUtils.isEmpty(data.getProduct().getId())) {
            bindProduct(data.getProduct());
        }

    }

    public void onViewRecycled() {
        ImageLoader.clearImage(productImage);
    }

    private void bindProduct(final Product product) {
        productImage.setImage(product.getImage());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            productName.setText(Html.fromHtml(product.getName(),
                    Html.FROM_HTML_MODE_LEGACY));
        } else {
            productName.setText(Html.fromHtml(product.getName()));
        }
        productPrice.setText(product.getPriceFormat());
    }
}
