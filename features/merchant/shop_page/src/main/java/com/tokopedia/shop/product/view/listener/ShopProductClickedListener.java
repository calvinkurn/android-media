package com.tokopedia.shop.product.view.listener;

import com.tokopedia.shop.product.view.model.ShopProductViewModel;

/**
 * Created by normansyahputa on 2/24/18.
 */

public interface ShopProductClickedListener {

    void onWishListClicked(ShopProductViewModel shopProductViewModel, int shopTrackType);

    void onProductClicked(ShopProductViewModel shopProductViewModel, int shopTrackType, int productPosition);
}
