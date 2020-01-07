package com.tokopedia.shop.newproduct.view.listener;


import com.tokopedia.shop.newproduct.view.datamodel.ShopProductViewModel;

/**
 * Created by normansyahputa on 2/24/18.
 */

public interface ShopProductClickedListener {

    void onWishListClicked(ShopProductViewModel shopProductViewModel, int shopTrackType);

    void onProductClicked(ShopProductViewModel shopProductViewModel, int shopTrackType, int productPosition);
}
