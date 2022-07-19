package com.tokopedia.feedcomponent.view.widget.shoprecom.cordinator

import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModelItem
import javax.inject.Inject

class ShopRecommendationImpressionCoordinator @Inject constructor() {

    private val mShopRecomImpress = mutableListOf<Pair<ShopRecomUiModelItem, Int>>()
    private var mUserId: String = ""

    fun saveShopRecomImpress(userId: String, shopImpress: List<Pair<ShopRecomUiModelItem, Int>>) {
        if (mUserId.isEmpty()) mUserId = userId
        mShopRecomImpress.addAll(shopImpress)
    }

    fun sendShopRecomImpress() {
        val finalShopRecom = mShopRecomImpress.distinctBy { it.first.id }

        if (finalShopRecom.isEmpty()) return

        analytic.impressionProfileRecommendation(mUserId, finalShopRecom)
        analytic.sendAll()

        mUserId = ""
        mShopRecomImpress.clear()
    }
}