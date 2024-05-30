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

    fun logBreadCrumbFirstOpenPage(
        productId: String?,
        shopName: String?,
        productName: String?,
        context: Context?
    ) {
        val localizationString = context?.let {
            generateLocalizationString(ChooseAddressUtils.getLocalizingAddressData(it))
        } ?: ""

        val jsonObject = JSONObject().apply {
            put(PRODUCT_ID_KEY, productId)
            put(SHOP_NAME_KEY, shopName)
            put(PRODUCT_NAME_KEY, productName)
            put(LOCALIZATION_STRING_KEY, localizationString)
        }
        logBreadCrumb(PDP_FIRST_OPEN_PAGE_STATE, jsonObject)
    }

    fun logBreadCrumbSuccessGetDataP1(
        isSuccess: Boolean = false,
        errorMessage: String = "",
        errorCode: String = "",
        cacheState: CacheState?,
        isCampaign: Boolean
    ) {
        val jsonObject = JSONObject().apply {
            put(IS_SUCCESS_KEY, isSuccess)
            put(ERROR_MESSAGE_KEY, errorMessage)
            put(ERROR_CODE_KEY, errorCode)
            put(IS_FROM_CACHE, cacheState?.isFromCache.toString())
            put(CACHE_FIRST_THEN_CLOUD, cacheState?.cacheFirstThenCloud.toString())
            put(IS_CAMPAIGN, isCampaign)
            put(REMOTE_CACHEABLE_ACTIVE, cacheState?.remoteCacheableActive.toString())
            put(IS_PREFETCH, cacheState?.isPrefetch.toString())
        }
        logBreadCrumb(PDP_SUCCESS_GET_P1_STATE, jsonObject)
    }

    fun logBreadCrumbSuccessGetDataP2(isSuccess: Boolean) {
        val jsonObject = JSONObject().apply {
            put(IS_SUCCESS_KEY, isSuccess)
        }
        logBreadCrumb(PDP_SUCCESS_GET_P2_STATE, jsonObject)
    }

    fun logBreadCrumbTopAdsIsAds(
        isSuccess: Boolean = false,
        errorMessage: String? = "",
        errorCode: Int = 0,
        isTopAds: Boolean = false
    ) {
        val jsonObject = JSONObject().apply {
            put(IS_SUCCESS_KEY, isSuccess)
            put(ERROR_MESSAGE_KEY, errorMessage)
            put(ERROR_CODE_KEY, errorCode)
            put(IS_TOPADS_KEY, isTopAds)
        }
        logBreadCrumb(PDP_SUCCESS_GET_TOPADS_IS_ADS_STATE, jsonObject)
    }

    fun logBreadCrumbAtc(
        isSuccess: Boolean,
        errorMessage: String = "",
        atcType: Int
    ) {
        val jsonObject = JSONObject().apply {
            put(IS_SUCCESS_KEY, isSuccess)
            put(ERROR_MESSAGE_KEY, errorMessage)
            put(ATC_TYPE_KEY, atcType)
        }
        logBreadCrumb(PDP_SUCCESS_ATC_STATE, jsonObject)
    }

    fun logBreadCrumbAffiliateCookie(
        isSuccess: Boolean,
        errorMessage: String = ""
    ) {
        val jsonObject = JSONObject().apply {
            put(IS_SUCCESS_KEY, isSuccess)
            put(ERROR_MESSAGE_KEY, errorMessage)
        }
        logBreadCrumb(PDP_AFFILIATE_COOKIE_HIT, jsonObject)
    }

    fun logBreadCrumbAddressChanged(context: Context?) {
        val localizationString = context?.let {
            generateLocalizationString(ChooseAddressUtils.getLocalizingAddressData(it))
        } ?: ""

        val jsonObject = JSONObject().apply {
            put(LOCALIZATION_STRING_KEY, localizationString)
        }
        logBreadCrumb(PDP_ADDRESS_CHANGED_STATE, jsonObject)
    }

    private fun generateLocalizationString(localizationChooseAddress: LocalCacheModel): String {
        return "addressId:${localizationChooseAddress.address_id}, " +
            "cityId: ${localizationChooseAddress.city_id}, " +
            "districtId: ${localizationChooseAddress.district_id}, " +
            "whId: ${localizationChooseAddress.warehouse_id}, " +
            "serviceType: ${localizationChooseAddress.service_type}"
    }

}
