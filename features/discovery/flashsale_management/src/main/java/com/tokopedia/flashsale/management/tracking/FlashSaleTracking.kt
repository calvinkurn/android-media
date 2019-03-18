package com.tokopedia.flashsale.management.tracking

import android.app.Activity
import android.text.TextUtils
import java.sql.RowId
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

class FlashSaleTracking() {

    private fun sendScreenName(activity: Activity, screenName: String) {
        TrackApp.getInstance()?.getGTM()?.sendScreenAuthenticated(screenName)
    }

    companion object {
        val EVENT = "event"
        val EVENT_CATEGORY = "eventCategory"
        val EVENT_ACTION = "eventAction"
        val EVENT_LABEL = "eventLabel"
        val SHOP_ID = "eventLabel"

        val CLICK_SELLER_PARTICIPATION = "clickSellerParticipation"
        val SELLER_PARTICIPATION_PAGE = "seller participation page"
        val CLICK_INFO_TO_PRODUCT = "click ajukan produk"
        val CLICK_TAB_PRODUCT = "click daftar produk"
        val CLICK_TAB_INFO = "click deskripsi & kriteria"
        val CLICK_PRODUCT_QUICK_FILTER = "click filter icon"
        val CLICK_PRODUCT_TNC = "click syarat & ketentuan"
        val CLICK_PRODUCT_CAMPAIGN_LIST = "click daftar campaign"
        val CLICK_PRODUCT_UPDATE_CAMPAIGN = "click perbarui pengajuan"
        val CLICK_PRODUCT_SEARCH = "click search"
    }

    protected fun joinDash(vararg s: String): String {
        return TextUtils.join(" - ", s)
    }

    fun sendFlashSaleEvent(action: String, label: String, shopId: String? = null) {
        val eventMap = createFlashSaleMap(action, label, shopId)
        TrackApp.getInstance()?.getGTM()?.sendEnhanceECommerceEvent(eventMap)
    }

    fun createFlashSaleMap(action: String, label: String, shopId: String? = null): MutableMap<String, Any> {
        val eventMap = mutableMapOf<String, Any>()
        eventMap.put(EVENT, CLICK_SELLER_PARTICIPATION)
        eventMap.put(EVENT_CATEGORY, SELLER_PARTICIPATION_PAGE)
        eventMap.put(EVENT_ACTION, action)
        eventMap.put(EVENT_LABEL, label)
        if (!shopId.isNullOrEmpty()) {
            eventMap.put(SHOP_ID, shopId!!);
        }
        return eventMap
    }

    fun clickInfoToProduct(campaignId: String) {
        sendFlashSaleEvent(CLICK_INFO_TO_PRODUCT,
                campaignId)
    }

    fun clickTabProduct(campaignId: String) {
        sendFlashSaleEvent(CLICK_TAB_PRODUCT,
                campaignId)
    }

    fun clickTabInfo(campaignId: String) {
        sendFlashSaleEvent(CLICK_TAB_INFO,
                campaignId)
    }

    fun clickProductQuickFilter(campaignId: String, filterValue: String) {
        sendFlashSaleEvent(CLICK_PRODUCT_QUICK_FILTER,
                joinDash(campaignId, filterValue))
    }

    fun clickProductTnc(campaignId: String) {
        sendFlashSaleEvent(CLICK_PRODUCT_TNC,
                campaignId)
    }

    fun clickProductCampaignList(campaignId: String) {
        sendFlashSaleEvent(CLICK_PRODUCT_CAMPAIGN_LIST,
                campaignId)
    }

    fun clickProductUpdateCampaign(campaignId: String) {
        sendFlashSaleEvent(CLICK_PRODUCT_UPDATE_CAMPAIGN,
                campaignId)
    }

    fun clickProductSearch(campaignId: String, keyword: String) {
        sendFlashSaleEvent(CLICK_PRODUCT_SEARCH, joinDash(campaignId, keyword))
    }

}

