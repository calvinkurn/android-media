package com.tokopedia.feedcomponent.shoprecom.cordinator

import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem
import javax.inject.Inject

class ShopRecomImpressCoordinator @Inject constructor() {

    private val mShopRecomImpress = mutableListOf<ShopRecomUiModelItem>()

    fun initiateShopImpress(
        shopImpress: ShopRecomUiModelItem,
        callback: (shopImpress: ShopRecomUiModelItem) -> Unit,
    ) {
        val findShopRecom = mShopRecomImpress.filter { it.id == shopImpress.id }
        if (!findShopRecom.isNullOrEmpty()) return
        callback.invoke(shopImpress)
        mShopRecomImpress.add(shopImpress)
    }

    fun sendTracker(callback: () -> Unit) {
        callback.invoke()
        if (!mShopRecomImpress.isNullOrEmpty()) mShopRecomImpress.clear()
    }

}
