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


    const val CURRENT_SITE = "currentSite"
    const val BUSINESS_UNIT = "businessUnit"
    const val USER_ID = "userId"

    const val CLICK_SMART_BILLS = "clickSmartBill"
    const val CLICK_DIGITAl = "clickDigital"
    const val VIEW_SMART_BILLS_IRIS = "viewSmartBillIris"
    const val VIEW_DIGITAL_IRIS = "viewDigitalIris"
    const val CATEGORY_SMART_BILLS = "digital - smart bill management"
    const val CATEGORY_SMART_BILLS_ADD_BILLS = "digital - smart bill management - add bills"

    const val SELECT_CONTENT = "select_content"

    const val ITEMS = "items"
    const val ITEM_LIST = "item_list"
    const val ITEM_ID = "item_id"
    const val ITEM_NAME = "item_name"
    const val ITEM_BRAND = "item_brand"
    const val ITEM_CATEGORY = "item_category"
    const val ITEM_VARIANT = "item_variant"
    const val INDEX = "index"
    const val PRICE = "price"

    const val CURRENT_SITE_VALUE = "tokopediaDigitalRecharge"
    const val BUSINESS_UNIT_VALUE = "top up and tagihan"
    const val BUSINESS_UNIT_RECHARGE_VALUE = "recharge"
}
