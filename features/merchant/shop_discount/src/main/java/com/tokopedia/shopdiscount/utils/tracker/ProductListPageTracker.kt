package com.tokopedia.shopdiscount.utils.tracker

import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ProductListPageTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickAddProductEvent() {
        Tracker.Builder()
            .setBusinessUnit("physical goods")
            .setEventCategory("slash price page - list of products")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(userSession.shopId)
            .setEvent("clickPG")
            .setEventAction("click add product")
            .setEventLabel(EMPTY_STRING)
            .build()
            .send()
    }
}