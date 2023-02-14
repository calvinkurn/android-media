package com.tokopedia.mvc.presentation.intro.util

import com.tokopedia.mvc.util.constant.MvcTrackerConstant
import com.tokopedia.mvc.util.constant.MvcTrackerConstant.TRACKER_ID
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class MvcIntroPageTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendMvcIntroPageCreateVoucherEvent() {
        Tracker.Builder()
            .setEvent(MvcTrackerConstant.MVC_EVENT)
            .setEventAction(CREATE_COUPON)
            .setEventCategory(EVENT_CATEGORY)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, "39390")
            .setBusinessUnit(MvcTrackerConstant.MVC_BUSINESS_UNIT)
            .setCurrentSite(MvcTrackerConstant.MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendMvcIntroPageArrowButton() {
        Tracker.Builder()
            .setEvent(MvcTrackerConstant.MVC_EVENT)
            .setEventAction(VIEW_MORE)
            .setEventCategory(EVENT_CATEGORY)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, "39391")
            .setBusinessUnit(MvcTrackerConstant.MVC_BUSINESS_UNIT)
            .setCurrentSite(MvcTrackerConstant.MVC_CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    companion object {
        private const val CREATE_COUPON = "click buat kupon"
        private const val VIEW_MORE = "click kembali"
        private const val EVENT_CATEGORY = "kupon toko saya - intro"
    }
}
