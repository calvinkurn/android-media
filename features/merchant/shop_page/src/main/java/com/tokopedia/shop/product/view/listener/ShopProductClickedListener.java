package com.tokopedia.shop.product.view.listener;

import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;

/**
 * Created by normansyahputa on 2/24/18.
 */

public interface ShopProductClickedListener {

    void onWishListClicked(ShopProductViewModelOld shopProductViewModelOld);

    void onProductClicked(ShopProductViewModelOld shopProductViewModelOld);
}
