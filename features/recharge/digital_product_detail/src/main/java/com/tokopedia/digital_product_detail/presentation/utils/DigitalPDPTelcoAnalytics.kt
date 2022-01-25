package com.tokopedia.digital_product_detail.presentation.utils

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class DigitalPDPTelcoAnalytics {

    fun eventInputNumberManual(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalPDPEventTracking.Event.CLICK_DIGITAL,
                TrackAppUtils.EVENT_CATEGORY, DigitalPDPEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.INPUT_MANUAL_NUMBER,
                TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}",
                DigitalPDPEventTracking.Additional.BUSINESS_UNIT, DigitalPDPEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalPDPEventTracking.Additional.CURRENT_SITE, DigitalPDPEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalPDPEventTracking.Additional.USER_ID, userId,
            )
        )
    }

    fun eventInputNumberContact(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalPDPEventTracking.Event.CLICK_DIGITAL,
                TrackAppUtils.EVENT_CATEGORY, DigitalPDPEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.INPUT_FROM_CONTACT,
                TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}",
                DigitalPDPEventTracking.Additional.BUSINESS_UNIT, DigitalPDPEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalPDPEventTracking.Additional.CURRENT_SITE, DigitalPDPEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalPDPEventTracking.Additional.USER_ID, userId,
            )
        )
    }

    fun eventInputNumberFavorite(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalPDPEventTracking.Event.CLICK_DIGITAL,
                TrackAppUtils.EVENT_CATEGORY, DigitalPDPEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.INPUT_FROM_FAVORITE_NUMBER,
                TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}",
                DigitalPDPEventTracking.Additional.BUSINESS_UNIT, DigitalPDPEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalPDPEventTracking.Additional.CURRENT_SITE, DigitalPDPEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalPDPEventTracking.Additional.USER_ID, userId,
            )
        )
    }

    fun impressionFavoriteNumberChips(categoryName: String, loyaltyStatus: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalPDPEventTracking.Event.VIEW_DIGITAL_IRIS,
                TrackAppUtils.EVENT_CATEGORY, DigitalPDPEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.VIEW_FAVORITE_NUMBER_CHIP,
                TrackAppUtils.EVENT_LABEL, "${categoryName}_${loyaltyStatus}",
                DigitalPDPEventTracking.Additional.BUSINESS_UNIT, DigitalPDPEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalPDPEventTracking.Additional.CURRENT_SITE, DigitalPDPEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalPDPEventTracking.Additional.USER_ID, userId,
            )
        )
    }

    fun clickFavoriteNumberChips(categoryName: String, operatorName: String, loyaltyStatus: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalPDPEventTracking.Event.CLICK_DIGITAL,
                TrackAppUtils.EVENT_CATEGORY, DigitalPDPEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.CLICK_FAVORITE_NUMBER_CHIP,
                TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}_${loyaltyStatus}",
                DigitalPDPEventTracking.Additional.BUSINESS_UNIT, DigitalPDPEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalPDPEventTracking.Additional.CURRENT_SITE, DigitalPDPEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalPDPEventTracking.Additional.USER_ID, userId,
            )
        )
    }

    fun impressionFavoriteContactChips(categoryName: String, loyaltyStatus: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalPDPEventTracking.Event.VIEW_DIGITAL_IRIS,
                TrackAppUtils.EVENT_CATEGORY, DigitalPDPEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.VIEW_FAVORITE_CONTACT_CHIP,
                TrackAppUtils.EVENT_LABEL, "${categoryName}_${loyaltyStatus}",
                DigitalPDPEventTracking.Additional.BUSINESS_UNIT, DigitalPDPEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalPDPEventTracking.Additional.CURRENT_SITE, DigitalPDPEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalPDPEventTracking.Additional.USER_ID, userId,
            )
        )
    }

    fun clickFavoriteContactChips(categoryName: String, operatorName: String, loyaltyStatus: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalPDPEventTracking.Event.CLICK_DIGITAL,
                TrackAppUtils.EVENT_CATEGORY, DigitalPDPEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.CLICK_FAVORITE_CONTACT_CHIP,
                TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}_${loyaltyStatus}",
                DigitalPDPEventTracking.Additional.BUSINESS_UNIT, DigitalPDPEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalPDPEventTracking.Additional.CURRENT_SITE, DigitalPDPEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalPDPEventTracking.Additional.USER_ID, userId,
            )
        )
    }

    fun clickFavoriteNumberAutoComplete(categoryName: String, operatorName: String, loyaltyStatus: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalPDPEventTracking.Event.CLICK_DIGITAL,
                TrackAppUtils.EVENT_CATEGORY, DigitalPDPEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.CLICK_AUTOCOMPLETE_FAVORITE_NUMBER,
                TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}_${loyaltyStatus}",
                DigitalPDPEventTracking.Additional.BUSINESS_UNIT, DigitalPDPEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalPDPEventTracking.Additional.CURRENT_SITE, DigitalPDPEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalPDPEventTracking.Additional.USER_ID, userId,
            )
        )
    }

    fun clickFavoriteContactAutoComplete(categoryName: String, operatorName: String, loyaltyStatus: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalPDPEventTracking.Event.CLICK_DIGITAL,
                TrackAppUtils.EVENT_CATEGORY, DigitalPDPEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.CLICK_AUTOCOMPLETE_FAVORITE_CONTACT,
                TrackAppUtils.EVENT_LABEL, "${categoryName}_${operatorName}_${loyaltyStatus}",
                DigitalPDPEventTracking.Additional.BUSINESS_UNIT, DigitalPDPEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalPDPEventTracking.Additional.CURRENT_SITE, DigitalPDPEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalPDPEventTracking.Additional.USER_ID, userId,
            )
        )
    }

    fun eventClearInputNumber(categoryName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalPDPEventTracking.Event.CLICK_DIGITAL,
                TrackAppUtils.EVENT_CATEGORY, DigitalPDPEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.CLEAR_INPUT_NUMBER,
                TrackAppUtils.EVENT_LABEL, categoryName,
                DigitalPDPEventTracking.Additional.BUSINESS_UNIT, DigitalPDPEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalPDPEventTracking.Additional.CURRENT_SITE, DigitalPDPEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalPDPEventTracking.Additional.USER_ID, userId,
            )
        )
    }

    fun clickOnContactIcon(categoryName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalPDPEventTracking.Event.CLICK_DIGITAL,
                TrackAppUtils.EVENT_CATEGORY, DigitalPDPEventTracking.Category.DIGITAL_HOMEPAGE,
                TrackAppUtils.EVENT_ACTION, DigitalPDPEventTracking.Action.CLICK_ON_CONTACT_ICON,
                TrackAppUtils.EVENT_LABEL, categoryName,
                DigitalPDPEventTracking.Additional.BUSINESS_UNIT, DigitalPDPEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                DigitalPDPEventTracking.Additional.CURRENT_SITE, DigitalPDPEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                DigitalPDPEventTracking.Additional.USER_ID, userId,
            )
        )
    }
}