package com.tokopedia.common.topupbills.analytics

import com.tokopedia.track.TrackAppUtils

object CommonSmartBillsConstant {
    fun MutableMap<String, Any>.addGeneralClick(): MutableMap<String, Any>? {
        this[TrackAppUtils.EVENT_CATEGORY] = CATEGORY_SMART_BILLS
        this[TrackAppUtils.EVENT] = CLICK_SMART_BILLS
        this[CURRENT_SITE] = CURRENT_SITE_VALUE
        this[BUSINESS_UNIT] = BUSINESS_UNIT_VALUE
        return this
    }

    fun MutableMap<String, Any>.addGeneralClickAddBills(): MutableMap<String, Any>? {
        this[TrackAppUtils.EVENT_CATEGORY] = CATEGORY_SMART_BILLS_ADD_BILLS
        this[TrackAppUtils.EVENT] = CLICK_SMART_BILLS
        this[CURRENT_SITE] = CURRENT_SITE_VALUE
        this[BUSINESS_UNIT] = BUSINESS_UNIT_VALUE
        return this
    }

    fun MutableMap<String, Any>.addGeneralView(): MutableMap<String, Any>? {
        this[TrackAppUtils.EVENT_CATEGORY] = CATEGORY_SMART_BILLS
        this[TrackAppUtils.EVENT] = VIEW_SMART_BILLS_IRIS
        this[CURRENT_SITE] = CURRENT_SITE_VALUE
        this[BUSINESS_UNIT] = BUSINESS_UNIT_VALUE
        return this
    }

    fun MutableMap<String, Any>.addGeneralViewAddBills(): MutableMap<String, Any>? {
        this[TrackAppUtils.EVENT_CATEGORY] = CATEGORY_SMART_BILLS_ADD_BILLS
        this[TrackAppUtils.EVENT] = VIEW_SMART_BILLS_IRIS
        this[CURRENT_SITE] = CURRENT_SITE_VALUE
        this[BUSINESS_UNIT] = BUSINESS_UNIT_VALUE
        return this
    }

    const val CURRENT_SITE = "currentSite"
    const val BUSINESS_UNIT = "businessUnit"

    const val CLICK_SMART_BILLS = "clickSmartBill"
    const val VIEW_SMART_BILLS_IRIS = "viewSmartBillIris"
    const val CATEGORY_SMART_BILLS = "digital - smart bill management"
    const val CATEGORY_SMART_BILLS_ADD_BILLS = "digital - smart bill management - add bills"

    const val CURRENT_SITE_VALUE = "tokopediaDigitalRecharge"
    const val BUSINESS_UNIT_VALUE = "top up and tagihan"
    const val BUSINESS_UNIT_RECHARGE_VALUE = "recharge"
}