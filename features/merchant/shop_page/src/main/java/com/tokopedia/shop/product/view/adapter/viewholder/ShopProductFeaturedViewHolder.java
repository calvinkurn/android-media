package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

/**
 * Created by normansyahputa on 2/22/18.
 */

public class ShopProductFeaturedViewHolder extends ShopProductViewHolder {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_featured;

    private ShopProductFeaturedListener shopProductFeaturedListener;

    public ShopProductFeaturedViewHolder(View itemView, ShopProductClickedListener shopProductClickedListener,
                                         ShopProductFeaturedListener shopProductFeaturedListener) {
        super(itemView, shopProductClickedListener);
        this.shopProductFeaturedListener = shopProductFeaturedListener;
    }

    @Override
    public void bind(final ShopProductViewModel shopProductViewModel) {
        super.bind(shopProductViewModel);
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopProductFeaturedListener.onProductImageFeaturedClickedTracking(shopProductViewModel, getAdapterPosition());
            }
        });

        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopProductFeaturedListener.onProductTitleFeaturedClickedTracking(shopProductViewModel, getAdapterPosition());
            }
        });
    }

    @Override
    protected void onWishlistClicked(ShopProductViewModel shopProductViewModel) {
        shopProductFeaturedListener.onFeatureWishlistClickedTracking(shopProductViewModel);
        super.onWishlistClicked(shopProductViewModel);
    }

    public interface ShopProductFeaturedListener {
        void onFeatureWishlistClickedTracking(ShopProductViewModel shopProductViewModel);
        void onProductImageFeaturedClickedTracking(ShopProductViewModel shopProductViewModel, int adapterPosition);

        void onProductTitleFeaturedClickedTracking(ShopProductViewModel shopProductViewModel, int adapterPosition);
    }
}
