package com.tokopedia.shop.common.util

import com.google.gson.Gson
import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.shop.common.util.model.ShopPageHomeTabJourneyEmbraceBreadCrumbJsonData
import org.json.JSONObject

object ShopLogger {
    const val SHOP_EMBRACE_BREADCRUMB_ACTION_FAIL_GET_P1 = "fail get P1"
    const val SHOP_EMBRACE_LOG_SHOP_ID = "shop_id"
    const val SHOP_EMBRACE_LOG_SHOP_NAME = "shop_name"
    const val SHOP_EMBRACE_LOG_ERROR_MESSAGE = "error_message"
    const val SHOP_EMBRACE_BREADCRUMB_ACTION_FAIL_GET_SHOP_HEADER_WIDGET =
        "fail get shop header widget"
    const val SHOP_EMBRACE_BREADCRUMB_ACTION_FAIL_GET_INITIAL_PRODUCT_LIST =
        "fail get initial product list"
    const val SHOP_EMBRACE_BREADCRUMB_ACTION_FAIL_GET_SHOP_PAGE_GET_LAYOUT_V2 =
        "fail get shopPageGetLayoutV2"
    private const val SHOP_EMBRACE_BREADCRUMB_FORMAT = "%s, %s, %s"
    private const val SHOP_EMBRACE_BREADCRUMB_SHOP_PAGE_HOME_TAB_JOURNEY =
        "shop_home_journey"

    fun logBreadCrumbShopPageHomeTabJourney(
        actionName: String,
        breadCrumbData: ShopPageHomeTabJourneyEmbraceBreadCrumbJsonData
    ) {
        val jsonObject = JSONObject(Gson().toJson(breadCrumbData))
        sendEmbraceBreadCrumb(
            SHOP_EMBRACE_BREADCRUMB_SHOP_PAGE_HOME_TAB_JOURNEY,
            actionName,
            jsonObject
        )
    }

    fun mapToShopPageHomeTabJourneyEmbraceBreadCrumbJsonData(
        shopId: String,
        stackTrace: String
    ): ShopPageHomeTabJourneyEmbraceBreadCrumbJsonData {
        return ShopPageHomeTabJourneyEmbraceBreadCrumbJsonData(
            shopId,
            stackTrace
        )
    }

    private fun sendEmbraceBreadCrumb(eventName: String, action: String, jsonObject: JSONObject) {
        EmbraceMonitoring.logBreadcrumb(
            String.format(
                SHOP_EMBRACE_BREADCRUMB_FORMAT,
                eventName,
                action,
                jsonObject
            )
        )
    }
}
