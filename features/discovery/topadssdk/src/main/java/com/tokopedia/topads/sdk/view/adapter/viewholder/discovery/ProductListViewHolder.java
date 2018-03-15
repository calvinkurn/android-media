package com.tokopedia.topads.sdk.view.adapter.viewholder.discovery;

import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.utils.LabelLoader;
import com.tokopedia.topads.sdk.view.FlowLayout;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ProductListViewModel;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class ProductListViewHolder extends AbstractViewHolder<ProductListViewModel> implements View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_produt_list;
    private static final String TAG = ProductListViewHolder.class.getSimpleName();

    private LocalAdsClickListener itemClickListener;
    private Data data;
    private Context context;
    public LinearLayout badgeContainer;
    public FlowLayout labelContainer;
    public TextView productName;
    public TextView productPrice;
    public TextView shopName;
    public TextView shopLocation;
    public ImageView productImage;
    private ImageLoader imageLoader;
    private ImageView rating;
    private TextView reviewCount;

    public ProductListViewHolder(View itemView, ImageLoader imageLoader, LocalAdsClickListener itemClickListener) {
        super(itemView);
        this.itemClickListener = itemClickListener;
        this.imageLoader = imageLoader;
        context = itemView.getContext();
        badgeContainer = (LinearLayout) itemView.findViewById(R.id.badges_container);
        labelContainer = (FlowLayout) itemView.findViewById(R.id.label_container);
        productImage = (ImageView) itemView.findViewById(R.id.product_image);
        productName = (TextView) itemView.findViewById(R.id.title);
        productPrice = (TextView) itemView.findViewById(R.id.price);
        shopName = (TextView) itemView.findViewById(R.id.shop_name);
        shopLocation = (TextView) itemView.findViewById(R.id.location);
        rating = (ImageView) itemView.findViewById(R.id.rating);
        reviewCount = (TextView) itemView.findViewById(R.id.review_count);
        ((LinearLayout) itemView.findViewById(R.id.container)).setOnClickListener(this);
    }

    @Override
    public void bind(ProductListViewModel element) {
        data = element.getData();
        Product product = data.getProduct();
        if (product != null) {
            imageLoader.loadImage(product.getImage().getM_ecs(), product.getImage().getS_url(),
                    productImage);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                productName.setText(Html.fromHtml(product.getName(),
                        Html.FROM_HTML_MODE_LEGACY));
            } else {
                productName.setText(Html.fromHtml(product.getName()));
            }
            productPrice.setText(product.getPriceFormat());

            if (product.getLabels() != null) {
                LabelLoader.initLabel(context, labelContainer, product.getLabels());
            }
            if (data.getProduct().getProductRating() == 0) {
                rating.setVisibility(View.GONE);
                reviewCount.setVisibility(View.GONE);
            } else {
                rating.setVisibility(View.VISIBLE);
                reviewCount.setVisibility(View.VISIBLE);
                rating.setImageResource(
                        ImageLoader.getRatingDrawable(getStarCount(data.getProduct().getProductRating()))
                );
                reviewCount.setText("(" + data.getProduct().getCountReviewFormat() + ")");
            }
        }
        Shop shop = data.getShop();
        if (shop != null) {
            shopLocation.setText(shop.getLocation());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                shopName.setText(Html.fromHtml(shop.getName(),
                        Html.FROM_HTML_MODE_LEGACY));
            } else {
                shopName.setText(Html.fromHtml(shop.getName()));
            }
            if (shop.getBadges() != null) {
                imageLoader.loadBadge(badgeContainer, shop.getBadges());
            }
        }
    }

    private int getStarCount(int rating) {
        return Math.round(rating / 20f);
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null && v.getId() == R.id.container) {
            itemClickListener.onProductItemClicked(getAdapterPosition(), data);
        }
    }
}
