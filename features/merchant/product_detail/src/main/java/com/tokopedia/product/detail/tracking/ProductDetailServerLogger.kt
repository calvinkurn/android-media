package com.tokopedia.product.detail.tracking

import android.content.Context
import android.util.Log
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.product.detail.common.data.model.pdplayout.CacheState
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import org.json.JSONObject

object ProductDetailServerLogger {
    private const val PDP_MODULE_NAME = "product_detail"
    private const val PDP_FIRST_OPEN_PAGE_STATE = "first open page"
    private const val PDP_SUCCESS_GET_P1_STATE = "success get p1"
    private const val PDP_SUCCESS_GET_P2_STATE = "success get p2"
    private const val PDP_SUCCESS_GET_TOPADS_IS_ADS_STATE = "success get topads is ads"
    private const val PDP_SUCCESS_ATC_STATE = "success atc"
    private const val PDP_AFFILIATE_COOKIE_HIT = "hit affiliate cookie"
    private const val PDP_ADDRESS_CHANGED_STATE = "address changed refresh pdp"

    private const val PRODUCT_ID_KEY = "productId"
    private const val SHOP_NAME_KEY = "shopName"
    private const val PRODUCT_NAME_KEY = "productName"
    private const val LOCALIZATION_STRING_KEY = "localizationString"

    private const val IS_SUCCESS_KEY = "isSuccess"
    private const val ERROR_MESSAGE_KEY = "errorMessage"
    private const val ERROR_CODE_KEY = "errorCode"
    private const val IS_TOPADS_KEY = "isTopAds"
    private const val ATC_TYPE_KEY = "atcType"
    private const val IS_FROM_CACHE = "isFromCache"
    private const val CACHE_FIRST_THEN_CLOUD = "cacheFirstThenCloud"
    private const val IS_CAMPAIGN = "isCampaign"
    private const val REMOTE_CACHEABLE_ACTIVE = "remoteCacheableActive"
    private const val IS_PREFETCH = "isPreFetch"

    private const val PDP_OPEN_FAIL = "PDP_OPEN_FAIL"

    private const val TYPE_KEY = "type"
    private const val DESC_KEY = "desc"
    private const val ERR_KEY = "err"
    private const val URI_KEY = "uri"

    fun logNewRelicProductCannotOpen(uri: String, throwable: Throwable) {
        ServerLogger.log(
            Priority.P2,
            PDP_OPEN_FAIL,
            mapOf(
                TYPE_KEY to PDP_OPEN_FAIL,
                DESC_KEY to throwable.message.orEmpty(),
                URI_KEY to uri,
                ERR_KEY to Log.getStackTraceString(throwable)
                    .take(ProductDetailConstant.LOG_MAX_LENGTH)
                    .trim()
            )
        )
    }

    fun logNewRelicP1Error(error: Throwable) {
        ServerLogger.log(
            Priority.P2,
            "PDP_LOAD_PAGE_FAILED",
            mapOf(
                "type" to "pdp",
                "desc" to error.message.orEmpty(),
                "err" to Log.getStackTraceString(error)
                    .take(ProductDetailConstant.LOG_MAX_LENGTH).trim()
            )
        )
    }

    fun logNewRelicP1Success(productId: String?, cacheState: CacheState?, isCampaign: Boolean?) {
        ServerLogger.log(
            Priority.P2,
            "PDP_LOAD_PAGE_SUCCESS",
            mapOf(
                "type" to "pdp",
                PRODUCT_ID_KEY to productId.orEmpty(),
                IS_FROM_CACHE to cacheState?.isFromCache.toString(),
                CACHE_FIRST_THEN_CLOUD to cacheState?.cacheFirstThenCloud.toString(),
                IS_CAMPAIGN to isCampaign.toString(),
                REMOTE_CACHEABLE_ACTIVE to cacheState?.remoteCacheableActive.toString(),
                IS_PREFETCH to cacheState?.isPrefetch.toString()
            )
        )
    }
}
