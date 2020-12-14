package com.tokopedia.digital.home.analytics

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.ALL_BANNERS_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.BACK_BUTTON_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.DYNAMIC_ICON_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.SEARCH_BOX_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.SEARCH_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.SEARCH_RESULT_PAGE_ICON_CLICK
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingActionConstant.SEARCH_RESULT_PAGE_ICON_IMPRESSION
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingAdditionalConstant.BUSINESS_UNIT
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingAdditionalConstant.BUSINESS_UNIT_RECHARGE
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingAdditionalConstant.CURRENT_SITE
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingAdditionalConstant.CURRENT_SITE_RECHARGE
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingAdditionalConstant.SCREEN_NAME
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingAdditionalConstant.SCREEN_NAME_TOPUP_BILLS
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingAdditionalConstant.USER_ID
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingCategoryConstant.DIGITAL_HOMEPAGE_CATEGORY
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingEEConstant.CREATIVE_NAME
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingEEConstant.CREATIVE_SLOT
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingEEConstant.ITEM_ID
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingEEConstant.ITEM_NAME
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingEEConstant.PROMOTIONS
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingEventNameConstant.CLICK_TOPUP_BILLS
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingEventNameConstant.SELECT_CONTENT
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingEventNameConstant.VIEW_ITEM
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingLabelConstant.ORDER_LIST
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingLabelConstant.TOPUP_BILLS
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class RechargeHomepageAnalytics {

    fun eventHomepageLaunched(userId: String) {
        val linkerData = LinkerData().apply {
            this.userId = userId
        }
        LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(
                LinkerConstants.EVENT_DIGITAL_HOMEPAGE, linkerData
        ))
    }

    fun eventClickOrderList() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, ORDER_LIST)
    }

    fun eventClickBackButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, BACK_BUTTON_CLICK, "")
    }

    fun eventClickSearchBox(userId: String) {
        val data = mutableMapOf<String, Any>(
                TrackAppUtils.EVENT to CLICK_TOPUP_BILLS,
                TrackAppUtils.EVENT_CATEGORY to DIGITAL_HOMEPAGE_CATEGORY,
                TrackAppUtils.EVENT_ACTION to SEARCH_BOX_CLICK,
                TrackAppUtils.EVENT_LABEL to TOPUP_BILLS
        )
        data.putAll(getDefaultFields(userId))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(CLICK_TOPUP_BILLS, convertToBundle(data))
    }

    fun eventClickSearch(searchQuery: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, SEARCH_CLICK, searchQuery)
    }

    fun eventClickAllBanners() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_TOPUP_BILLS, DIGITAL_HOMEPAGE_CATEGORY, ALL_BANNERS_CLICK, "")
    }

    fun eventSearchResultPageImpression(items: List<DigitalHomePageSearchCategoryModel>, userId: String) {
        val promotions = items.mapIndexed { index, item ->
            mapOf(
                    ITEM_ID to item.id,
                    ITEM_NAME to item.label,
                    CREATIVE_NAME to item.icon,
                    CREATIVE_SLOT to index.toString()
            )
        }

        val data = mutableMapOf(
                TrackAppUtils.EVENT to VIEW_ITEM,
                TrackAppUtils.EVENT_CATEGORY to DIGITAL_HOMEPAGE_CATEGORY,
                TrackAppUtils.EVENT_ACTION to SEARCH_RESULT_PAGE_ICON_IMPRESSION,
                TrackAppUtils.EVENT_LABEL to "",
                PROMOTIONS to promotions
        )
        data.putAll(getDefaultFields(userId))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, convertToBundle(data))
    }

    fun eventSearchResultPageClick(item: DigitalHomePageSearchCategoryModel, position: Int, userId: String) {
        val promotions = listOf(mapOf(
                ITEM_ID to item.id,
                ITEM_NAME to item.label,
                CREATIVE_NAME to item.icon,
                CREATIVE_SLOT to position.toString()
        ))

        val data = mutableMapOf(
                TrackAppUtils.EVENT to SELECT_CONTENT,
                TrackAppUtils.EVENT_CATEGORY to DIGITAL_HOMEPAGE_CATEGORY,
                TrackAppUtils.EVENT_ACTION to SEARCH_RESULT_PAGE_ICON_CLICK,
                TrackAppUtils.EVENT_LABEL to "${item.label} - $position",
                PROMOTIONS to promotions
        )
        data.putAll(getDefaultFields(userId))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, convertToBundle(data))
    }

    fun rechargeEnhanceEcommerceEvent(trackingDataString: String) {
        val trackingData = Gson().fromJson<Map<String, Any>>(trackingDataString, object : TypeToken<HashMap<String, Any>>() {}.type)
        val event = (trackingData[TrackAppUtils.EVENT] as? String) ?: ""
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(event, convertToBundle(trackingData))
    }

    private fun convertToBundle(data: Map<String, Any>): Bundle {
        val bundle = Bundle()
        for (entry in data.entries) {
            when (val value = entry.value) {
                is String -> bundle.putString(entry.key, value)
                is Boolean -> bundle.putBoolean(entry.key, value)
                is Int -> bundle.putInt(entry.key, value)
                is Long -> bundle.putLong(entry.key, value)
                is Double -> bundle.putDouble(entry.key, value)
                is List<*> -> {
                    val list = ArrayList<Bundle>(
                            value.map {
                                (it as? Map<String, Any>)?.let { map ->
                                    return@map convertToBundle(map)
                                }
                                null
                            }.filterNotNull()
                    )
                    bundle.putParcelableArrayList(entry.key, list)
                }
            }
        }
        return bundle
    }

    private fun getDefaultFields(userId: String): Map<String, Any> {
        return mapOf(
                CURRENT_SITE to CURRENT_SITE_RECHARGE,
                BUSINESS_UNIT to BUSINESS_UNIT_RECHARGE,
                SCREEN_NAME to SCREEN_NAME_TOPUP_BILLS,
                USER_ID to userId
        )
    }

    fun sliceOpenApp(userId: String){
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, "clickGAMain",
                TrackAppUtils.EVENT_CATEGORY, "ga main app",
                TrackAppUtils.EVENT_ACTION, "click open app button",
                TrackAppUtils.EVENT_LABEL, "",
                BUSINESS_UNIT, "recharge",
                CURRENT_SITE, "tokopediadigital",
                USER_ID, userId
        ))
    }

    fun onOpenPageFromSlice() {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                RechargeAnalytics.EVENT_KEY, "openScreen",
                RechargeAnalytics.EVENT_SCREEN_NAME, "recharge homepage - from voice search - mainapp"
        ))
    }

    companion object {
        const val ACTION_IMPRESSION = "impression"
        const val ACTION_CLICK = "click"
    }

}