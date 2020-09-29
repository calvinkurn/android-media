package com.tokopedia.shop.product.view.listener;


import com.tokopedia.shop.product.view.datamodel.ShopProductViewModel;

/**
 * Created by normansyahputa on 2/24/18.
 */

interface ShopProductImpressionListener {
    fun onProductImpression(shopProductViewModel: ShopProductViewModel, shopTrackType: Int, productPosition: Int)

    fun getSelectedEtalaseName(): String
}
