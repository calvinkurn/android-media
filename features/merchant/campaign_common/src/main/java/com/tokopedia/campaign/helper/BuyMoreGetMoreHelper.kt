package com.tokopedia.campaign.helper

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant

object BuyMoreGetMoreHelper {

    const val KEY_WAREHOUSE_IDS = "warehouse_ids"
    const val KEY_PRODUCT_IDS = "product_ids"

    /*
    ShopId & offerId are mandatory params
    while warehouseIds & productIds are optional, but if your page can pass the data, please do.
    those params will affecting the product anchoring in the OLP.
    */
    fun goToOfferLandingPage(
        context: Context,
        shopId: String,
        offerId: String,
        warehouseIds: ArrayList<Int> = arrayListOf(),
        productIds: ArrayList<Int> = arrayListOf()
    ) {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMechant.BUY_MORE_GET_MORE_OLP,
            shopId,
            offerId
        )
        intent.putIntegerArrayListExtra(KEY_WAREHOUSE_IDS, warehouseIds)
        intent.putIntegerArrayListExtra(KEY_PRODUCT_IDS, productIds)
        context.startActivity(intent)
    }
}
