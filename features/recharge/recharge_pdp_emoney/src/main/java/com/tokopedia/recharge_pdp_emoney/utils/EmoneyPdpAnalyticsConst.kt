package com.tokopedia.recharge_pdp_emoney.utils

/**
 * @author by jessica on 06/05/21
 */
object EmoneyPdpAnalyticsConst {

    object Event {
        const val DIGITAL_GENERAL_EVENT = "digitalGeneralEvent"
        const val CLICK_EMONEY = "clickEmoney"
        const val CLICK_DIGITAL_NATIVE = "clickDigitalNative"
        const val CLICK_DIGITAL_EVENT = "clickDigitalEvent"
    }

    object Category {
        const val DIGITAL_NATIVE = "digital - native"
        const val DIGITAL_HOMEPAGE = "digital - homepage"
        const val DIGITAL_UANG_ELEKTRONIK = "digital - Uang Elektronik"
        const val EMONEY_PDP_SCREEN_NAME = "/digital/Uang Elektronik"
    }

    object Action {
        const val CLICK_UPDATE_SALDO = "click update saldo"
        const val CLICK_LIHAT_DETAIL = "click lihat detail"
        const val CLICK_X_BUTTON_CARD_NUMBER = "click x button card number"
        const val INPUT_CARD_NUMBER = "input card number"
        const val CLICK_ORDER_LIST_SECTION = "click order list section"
        const val CLICK_PROMO_SECTION = "click promo section"
    }
}
