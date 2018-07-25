package com.tokopedia.shop.product.view.listener.newlistener;

import com.tokopedia.shop.product.view.model.newmodel.ShopProductViewModel;

/**
 * Created by normansyahputa on 2/24/18.
 */

public interface ShopProductClickedNewListener {

    void onWishListClicked(ShopProductViewModel shopProductViewModel, boolean isFromFeatured);

    void onProductClicked(ShopProductViewModel shopProductViewModel, boolean isFromFeatured);
}
