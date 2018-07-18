package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;

/**
 * Created by normansyahputa on 2/22/18.
 */
@Deprecated
public class ShopProductSingleViewHolderOld extends ShopProductViewHolderOld {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_grid_single;

    public ShopProductSingleViewHolderOld(View itemView, ShopProductClickedListener shopProductClickedListener) {
        super(itemView, shopProductClickedListener);
    }

    @Override
    protected String getImageUrl(ShopProductViewModelOld shopProductViewModelOld) {
        String imageUrl = super.getImageUrl(shopProductViewModelOld);
        if (!TextUtils.isEmpty(shopProductViewModelOld.getImageUrl700())) {
            imageUrl = shopProductViewModelOld.getImageUrl700();
        }
        return imageUrl;
    }
}
