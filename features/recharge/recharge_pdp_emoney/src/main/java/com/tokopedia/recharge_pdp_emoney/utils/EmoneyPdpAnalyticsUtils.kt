package com.tokopedia.recharge_pdp_emoney.utils

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Label.BUSINESS_UNIT
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Label.CURRENTSITE
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Label.USER_ID
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Value.RECHARGE_BU
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Value.RECHARGE_SITE
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * @author by jessica on 06/05/21
 */
object EmoneyPdpAnalyticsUtils {

    fun clickCheckSaldoButton(userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, EmoneyPdpAnalyticsConst.Event.DIGITAL_GENERAL_EVENT,
                TrackAppUtils.EVENT_CATEGORY, EmoneyPdpAnalyticsConst.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, EmoneyPdpAnalyticsConst.Action.CLICK_UPDATE_SALDO,
                TrackAppUtils.EVENT_LABEL, "",
                BUSINESS_UNIT, RECHARGE_BU,
                CURRENTSITE, RECHARGE_SITE,
                USER_ID, userId
        ))
    }

    fun clickSeeProductDetail(price: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, EmoneyPdpAnalyticsConst.Event.CLICK_EMONEY,
                TrackAppUtils.EVENT_CATEGORY, EmoneyPdpAnalyticsConst.Category.DIGITAL_UANG_ELEKTRONIK,
                TrackAppUtils.EVENT_ACTION, EmoneyPdpAnalyticsConst.Action.CLICK_LIHAT_DETAIL,
                TrackAppUtils.EVENT_LABEL, price,
                BUSINESS_UNIT, RECHARGE_BU,
                CURRENTSITE, RECHARGE_SITE,
                USER_ID, userId
        ))
    }

    fun clickClearCardNumber(userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, EmoneyPdpAnalyticsConst.Event.CLICK_EMONEY,
                TrackAppUtils.EVENT_CATEGORY, EmoneyPdpAnalyticsConst.Category.DIGITAL_UANG_ELEKTRONIK,
                TrackAppUtils.EVENT_ACTION, EmoneyPdpAnalyticsConst.Action.CLICK_X_BUTTON_CARD_NUMBER,
                TrackAppUtils.EVENT_LABEL, "",
                BUSINESS_UNIT, RECHARGE_BU,
                CURRENTSITE, RECHARGE_SITE,
                USER_ID, userId
        ))
    }

    fun clickCameraIcon(userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, EmoneyPdpAnalyticsConst.Event.CLICK_DIGITAL_NATIVE,
                TrackAppUtils.EVENT_CATEGORY, EmoneyPdpAnalyticsConst.Category.DIGITAL_NATIVE,
                TrackAppUtils.EVENT_ACTION, EmoneyPdpAnalyticsConst.Action.CLICK_CAMERA_ICON,
                TrackAppUtils.EVENT_LABEL, "",
                BUSINESS_UNIT, RECHARGE_BU,
                CURRENTSITE, RECHARGE_SITE,
                USER_ID, userId
        ))
    }

    fun clickChangeCardNumber(cardNumber: String, userId: String) {
        if (cardNumber.isNotEmpty()) {
            TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                    TrackAppUtils.EVENT, EmoneyPdpAnalyticsConst.Event.CLICK_EMONEY,
                    TrackAppUtils.EVENT_CATEGORY, EmoneyPdpAnalyticsConst.Category.DIGITAL_UANG_ELEKTRONIK,
                    TrackAppUtils.EVENT_ACTION, EmoneyPdpAnalyticsConst.Action.INPUT_CARD_NUMBER,
                    TrackAppUtils.EVENT_LABEL, cardNumber,
                    BUSINESS_UNIT, RECHARGE_BU,
                    CURRENTSITE, RECHARGE_SITE,
                    USER_ID, userId
            ))
        }
    }

    fun clickRecentTransactionTab(userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, EmoneyPdpAnalyticsConst.Event.CLICK_DIGITAL_NATIVE,
                TrackAppUtils.EVENT_CATEGORY, EmoneyPdpAnalyticsConst.Category.DIGITAL_NATIVE,
                TrackAppUtils.EVENT_ACTION, EmoneyPdpAnalyticsConst.Action.CLICK_ORDER_LIST_SECTION,
                TrackAppUtils.EVENT_LABEL, "",
                BUSINESS_UNIT, RECHARGE_BU,
                CURRENTSITE, RECHARGE_SITE,
                USER_ID, userId
        ))
    }

    fun clickPromoTab(userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, EmoneyPdpAnalyticsConst.Event.CLICK_DIGITAL_NATIVE,
                TrackAppUtils.EVENT_CATEGORY, EmoneyPdpAnalyticsConst.Category.DIGITAL_NATIVE,
                TrackAppUtils.EVENT_ACTION, EmoneyPdpAnalyticsConst.Action.CLICK_PROMO_SECTION,
                TrackAppUtils.EVENT_LABEL, "",
                BUSINESS_UNIT, RECHARGE_BU,
                CURRENTSITE, RECHARGE_SITE,
                USER_ID, userId
        ))
    }
}