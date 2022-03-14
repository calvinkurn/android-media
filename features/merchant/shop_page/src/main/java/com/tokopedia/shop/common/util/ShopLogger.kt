package com.tokopedia.shop.common.util

import com.google.gson.Gson
import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.DATA_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.FUNCTION_NAME_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.LIVE_DATA_NAME_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.REASON_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.SHOP_ID_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.SHOP_NAME_KEY
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.TYPE
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.EXTRA_PARAM_KEY.USER_ID_KEY
import com.tokopedia.shop.common.util.model.ShopPageHomeTabJourneyEmbraceBreadCrumbJsonData
import org.json.JSONObject
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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
        "shop_page_home_tab_journey"

    fun logTimberWarning(priority: Priority, tag: String, extraMessage: Map<String, String>) {
        ServerLogger.log(priority, tag, extraMessage)
    }

    fun logShopPageP2BuyerFlowAlerting(
        tag: String,
        functionName: String,
        liveDataName: String = "",
        userId: String = "",
        shopId: String,
        shopName: String = "",
        errorMessage: String,
        stackTrace: String,
        errType: String
    ) {
        val extraParam = mapOf(
            TYPE to errType,
            FUNCTION_NAME_KEY to functionName,
            LIVE_DATA_NAME_KEY to liveDataName,
            SHOP_ID_KEY to shopId,
            USER_ID_KEY to userId,
            SHOP_NAME_KEY to shopName,
            REASON_KEY to errorMessage,
            DATA_KEY to stackTrace
        )
        logTimberWarning(Priority.P2, tag, extraParam)
    }

    fun isExceptionIgnored(throwable: Throwable) =
        throwable is UnknownHostException || throwable is SocketTimeoutException

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
        shopName: String,
        errorMessage: String,
        stackTrace: String
    ): ShopPageHomeTabJourneyEmbraceBreadCrumbJsonData {
        return ShopPageHomeTabJourneyEmbraceBreadCrumbJsonData(
            shopId,
            shopName,
            errorMessage,
            stackTrace
        )
    }

    fun sendEmbraceLogError(
        message: String,
        properties: Map<String, Any>
    ) {
        EmbraceMonitoring.logError(message, properties, true)
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