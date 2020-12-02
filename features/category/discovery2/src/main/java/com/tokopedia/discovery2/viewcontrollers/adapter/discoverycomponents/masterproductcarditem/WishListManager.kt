package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery2.datamapper.getComponent

object WishListManager {
    fun onWishListUpdated(productCardOptionsModel: ProductCardOptionsModel,pageIdentifier:String) {
        getComponent(productCardOptionsModel.productId,pageIdentifier)?.data?.getOrNull(0)?.isWishList = productCardOptionsModel.wishlistResult.isAddWishlist
    }
}