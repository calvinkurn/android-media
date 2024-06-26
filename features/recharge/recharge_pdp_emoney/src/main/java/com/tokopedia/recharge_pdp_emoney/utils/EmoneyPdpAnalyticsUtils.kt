package com.tokopedia.recharge_pdp_emoney.utils

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Label.BUSINESS_UNIT
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Label.CURRENTSITE
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Label.USER_ID
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Value.RECHARGE_BU
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Value.RECHARGE_SITE
import com.tokopedia.recharge_pdp_emoney.utils.EmoneyPdpAnalyticsConst.Category.EMONEY_PDP_SCREEN_NAME
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import java.util.HashMap

/**
 * @author by jessica on 06/05/21
 */
object EmoneyPdpAnalyticsUtils {

    fun openEmoneyPdpScreen() {
        val customDimension: MutableMap<String, String> = HashMap()
        customDimension[BUSINESS_UNIT] = RECHARGE_BU
        customDimension[CURRENTSITE] = RECHARGE_SITE
        TrackApp.getInstance().gtm.sendScreenAuthenticated(EMONEY_PDP_SCREEN_NAME, customDimension)
    }

    fun clickCheckSaldoButton(userId: String, issuerName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, EmoneyPdpAnalyticsConst.Event.CLICK_DIGITAL_EVENT,
                TrackAppUtils.EVENT_CATEGORY, EmoneyPdpAnalyticsConst.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, EmoneyPdpAnalyticsConst.Action.CLICK_UPDATE_SALDO,
                TrackAppUtils.EVENT_LABEL, issuerName,
                BUSINESS_UNIT, RECHARGE_BU,
                CURRENTSITE, RECHARGE_SITE,
                USER_ID, userId
        ))
    }

    fun clickSeeProductDetail(issuerName: String, price: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, EmoneyPdpAnalyticsConst.Event.CLICK_EMONEY,
                TrackAppUtils.EVENT_CATEGORY, EmoneyPdpAnalyticsConst.Category.DIGITAL_UANG_ELEKTRONIK,
                TrackAppUtils.EVENT_ACTION, EmoneyPdpAnalyticsConst.Action.CLICK_LIHAT_DETAIL,
                TrackAppUtils.EVENT_LABEL, "$issuerName - $price",
                BUSINESS_UNIT, RECHARGE_BU,
                CURRENTSITE, RECHARGE_SITE,
                USER_ID, userId
        ))
    }

    fun clickClearCardNumber(userId: String, issuerName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, EmoneyPdpAnalyticsConst.Event.CLICK_EMONEY,
                TrackAppUtils.EVENT_CATEGORY, EmoneyPdpAnalyticsConst.Category.DIGITAL_UANG_ELEKTRONIK,
                TrackAppUtils.EVENT_ACTION, EmoneyPdpAnalyticsConst.Action.CLICK_X_BUTTON_CARD_NUMBER,
                TrackAppUtils.EVENT_LABEL, issuerName,
                BUSINESS_UNIT, RECHARGE_BU,
                CURRENTSITE, RECHARGE_SITE,
                USER_ID, userId
        ))
    }

    fun clickChangeCardNumber(cardNumber: String, userId: String, issuerName: String) {
        if (cardNumber.isNotEmpty()) {
            TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                    TrackAppUtils.EVENT, EmoneyPdpAnalyticsConst.Event.CLICK_EMONEY,
                    TrackAppUtils.EVENT_CATEGORY, EmoneyPdpAnalyticsConst.Category.DIGITAL_UANG_ELEKTRONIK,
                    TrackAppUtils.EVENT_ACTION, EmoneyPdpAnalyticsConst.Action.INPUT_CARD_NUMBER,
                    TrackAppUtils.EVENT_LABEL, "$issuerName - $cardNumber",
                    BUSINESS_UNIT, RECHARGE_BU,
                    CURRENTSITE, RECHARGE_SITE,
                    USER_ID, userId
            ))
        }
    }

    fun clickRecentTransactionTab(userId: String, issuerName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, EmoneyPdpAnalyticsConst.Event.CLICK_DIGITAL_NATIVE,
                TrackAppUtils.EVENT_CATEGORY, EmoneyPdpAnalyticsConst.Category.DIGITAL_NATIVE,
                TrackAppUtils.EVENT_ACTION, EmoneyPdpAnalyticsConst.Action.CLICK_ORDER_LIST_SECTION,
                TrackAppUtils.EVENT_LABEL, issuerName,
                BUSINESS_UNIT, RECHARGE_BU,
                CURRENTSITE, RECHARGE_SITE,
                USER_ID, userId
        ))
    }

    fun clickPromoTab(userId: String, issuerName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, EmoneyPdpAnalyticsConst.Event.CLICK_DIGITAL_NATIVE,
                TrackAppUtils.EVENT_CATEGORY, EmoneyPdpAnalyticsConst.Category.DIGITAL_NATIVE,
                TrackAppUtils.EVENT_ACTION, EmoneyPdpAnalyticsConst.Action.CLICK_PROMO_SECTION,
                TrackAppUtils.EVENT_LABEL, issuerName,
                BUSINESS_UNIT, RECHARGE_BU,
                CURRENTSITE, RECHARGE_SITE,
                USER_ID, userId
        ))
    }
}
