package com.tokopedia.wallet.ovoactivationflashdeals


import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

import javax.inject.Inject

class InactiveOvoAnalytics @Inject constructor() {

    fun eventClickActivationOvoNow(productId: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                GENERIC_EVENT,
                Category.PDP,
                Action.CLICK_ACTIVATION_OVO,
                "")
        mapEvent[Constant.KEY_PRODUCT_ID] = productId
        mapEvent[Constant.KEY_USER_ID] = userId
        mapEvent[Constant.KEY_LOGGIN_STATUS] = (userId != "0").toString()
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventClickOvoLearnMore(productId: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                GENERIC_EVENT,
                Category.PDP,
                Action.CLICK_LEARN_MORE,
                "")
        mapEvent[Constant.KEY_PRODUCT_ID] = productId
        mapEvent[Constant.KEY_USER_ID] = userId
        mapEvent[Constant.KEY_LOGGIN_STATUS] = (userId != "0").toString()
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventClickTnc(productId: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                GENERIC_EVENT,
                Category.PDP,
                Action.CLICK_TNC,
                "")
        mapEvent[Constant.KEY_PRODUCT_ID] = productId
        mapEvent[Constant.KEY_USER_ID] = userId
        mapEvent[Constant.KEY_LOGGIN_STATUS] = (userId != "0").toString()
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    companion object {
        const val GENERIC_EVENT = "clickPDP"
    }

    object Category {
        const val PDP = "product detail page"
    }

    object Action {
        const val CLICK_ACTIVATION_OVO = "click - aktifkan ovo on pdp bottomsheet ovo activation"
        const val CLICK_LEARN_MORE = "click - pelajari selengkapnya on pdp bottomsheet ovo activation"
        const val CLICK_TNC = "click - syarat dan ketentuan ovo on pdp bottomsheet ovo activation"
    }

    object Constant {
        const val KEY_PRODUCT_ID = "productId"
        const val KEY_USER_ID = "userId"
        const val KEY_LOGGIN_STATUS = "isLoggedInStatus"
    }
}
