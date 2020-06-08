package com.tokopedia.shop.product.view.listener;


import com.tokopedia.shop.product.view.datamodel.ShopProductViewModel;

/**
 * Created by normansyahputa on 2/24/18.
 */

public interface ShopProductImpressionListener {
    void onProductImpression(ShopProductViewModel shopProductViewModel, int shopTrackType, int productPosition);
}
