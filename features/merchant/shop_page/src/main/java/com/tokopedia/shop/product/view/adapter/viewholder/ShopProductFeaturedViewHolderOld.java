package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;

/**
 * Created by normansyahputa on 2/22/18.
 */
@Deprecated
public class ShopProductFeaturedViewHolderOld extends ShopProductViewHolderOld {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_featured;

    private ShopProductFeaturedListener shopProductFeaturedListener;

    public ShopProductFeaturedViewHolderOld(View itemView, ShopProductClickedListener shopProductClickedListener,
                                            ShopProductFeaturedListener shopProductFeaturedListener) {
        super(itemView, shopProductClickedListener);
        this.shopProductFeaturedListener = shopProductFeaturedListener;
    }

    @Override
    public void bind(final ShopProductViewModelOld shopProductViewModelOld) {
        super.bind(shopProductViewModelOld);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopProductFeaturedListener.onProductFeaturedClickedTracking(shopProductViewModelOld);
            }
        });
    }

    @Override
    protected void onWishlistClicked(ShopProductViewModelOld shopProductViewModelOld) {
        shopProductFeaturedListener.onFeatureWishlistClickedTracking(shopProductViewModelOld);
        super.onWishlistClicked(shopProductViewModelOld);
    }

    public interface ShopProductFeaturedListener {
        void onFeatureWishlistClickedTracking(ShopProductViewModelOld shopProductViewModelOld);
        void onProductFeaturedClickedTracking(ShopProductViewModelOld shopProductViewModelOld);
    }
}
