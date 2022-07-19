package com.tokopedia.people.analytic.cordinator

import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModelItem
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import javax.inject.Inject

class ShopRecommendationImpressionCoordinator @Inject constructor(
    private val tracker: UserProfileTracker
) {

    private val mShopRecomImpress = mutableListOf<Pair<ShopRecomUiModelItem, Int>>()
    private var mUserId: String = ""

    fun saveShopRecomImpress(userId: String, shopImpress: List<Pair<ShopRecomUiModelItem, Int>>) {
        if (mUserId.isEmpty()) mUserId = userId
        mShopRecomImpress.addAll(shopImpress)
    }

    fun sendShopRecomImpress() {
        val finalShopRecom = mShopRecomImpress.distinctBy { it.first.id }

        if (finalShopRecom.isEmpty()) return

        tracker.impressionProfileRecommendation(mUserId, finalShopRecom)
        tracker.sendAll()

        mUserId = ""
        mShopRecomImpress.clear()
    }
}