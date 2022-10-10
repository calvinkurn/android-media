package com.tokopedia.people.analytic.cordinator

import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModelItem
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import timber.log.Timber
import javax.inject.Inject

class ShopRecomImpressCoordinator @Inject constructor(
    private val tracker: UserProfileTracker
) {

    private val mShopRecomImpress = mutableListOf<ShopRecomUiModelItem>()

    fun initiateShopImpress(userId: String, shopImpress: ShopRecomUiModelItem, position: Int) {
        val findShopRecom = mShopRecomImpress.filter { it.id == shopImpress.id }
        if (!findShopRecom.isNullOrEmpty()) return
        tracker.impressionProfileRecommendation(userId, shopImpress, position)
        mShopRecomImpress.add(shopImpress)
    }

    fun sendTracker() {
        tracker.sendAll()
        if (!mShopRecomImpress.isNullOrEmpty()) mShopRecomImpress.clear()
    }

}