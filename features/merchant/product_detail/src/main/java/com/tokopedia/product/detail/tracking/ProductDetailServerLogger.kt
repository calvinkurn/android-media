package com.tokopedia.product.detail.tracking

import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import org.json.JSONObject

object ProductDetailServerLogger {
    private const val PDP_MODULE_NAME = "product_detail"
    private const val PDP_EMBRACE_BREADCRUMB_FORMAT = "%s, %s, %s"
    private const val PDP_FIRST_OPEN_PAGE_STATE = "first open page"
    private const val PDP_SUCCESS_GET_P1_STATE = "success get p1"
    private const val PDP_SUCCESS_GET_P2_STATE = "success get p2"
    private const val PDP_SUCCESS_GET_TOPADS_IS_ADS_STATE = "success get topads is ads"
    private const val PDP_SUCCESS_ATC_STATE = "success atc"
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

    fun logBreadCrumbFirstOpenPage(productId: String?,
                                   shopName: String?,
                                   productName: String?,
                                   localizationChooseAddress: LocalCacheModel) {
        val localizationString = "addressId:${localizationChooseAddress.address_id}, " +
                "cityId: ${localizationChooseAddress.city_id}, " +
                "districtId: ${localizationChooseAddress.district_id}, " +
                "whId: ${localizationChooseAddress.warehouse_id}, " +
                "serviceType: ${localizationChooseAddress.service_type}"

        val jsonObject = JSONObject().apply {
            put(PRODUCT_ID_KEY, productId)
            put(SHOP_NAME_KEY, shopName)
            put(PRODUCT_NAME_KEY, productName)
            put(LOCALIZATION_STRING_KEY, localizationString)
        }
        logBreadCrumb(PDP_FIRST_OPEN_PAGE_STATE, jsonObject)
    }

    fun logBreadCrumbSuccessGetDataP1(isSuccess: Boolean = false,
                                      errorMessage: String = "",
                                      errorCode: String = "") {
        val jsonObject = JSONObject().apply {
            put(IS_SUCCESS_KEY, isSuccess)
            put(ERROR_MESSAGE_KEY, errorMessage)
            put(ERROR_CODE_KEY, errorCode)
        }
        logBreadCrumb(PDP_SUCCESS_GET_P1_STATE, jsonObject)
    }

    fun logBreadCrumbSuccessGetDataP2(isSuccess: Boolean) {
        val jsonObject = JSONObject().apply {
            put(IS_SUCCESS_KEY, isSuccess)
        }
        logBreadCrumb(PDP_SUCCESS_GET_P2_STATE, jsonObject)
    }

    fun logBreadCrumbTopAdsIsAds(isSuccess: Boolean,
                                 errorMessage: String?,
                                 errorCode: String,
                                 isTopAds: Boolean) {
        val jsonObject = JSONObject().apply {
            put(IS_SUCCESS_KEY, isSuccess)
            put(ERROR_MESSAGE_KEY, errorMessage ?: "")
            put(ERROR_CODE_KEY, errorCode)
            put(IS_TOPADS_KEY, isTopAds)
        }
        logBreadCrumb(PDP_SUCCESS_GET_TOPADS_IS_ADS_STATE, jsonObject)
    }

    fun logBreadCrumbAtc(isSuccess: Boolean,
                         errorMessage: String,
                         atcType: Int) {
        val jsonObject = JSONObject().apply {
            put(IS_SUCCESS_KEY, isSuccess)
            put(ERROR_MESSAGE_KEY, errorMessage)
            put(ATC_TYPE_KEY, atcType)
        }
        logBreadCrumb(PDP_SUCCESS_ATC_STATE, jsonObject)
    }

    fun logBreadCrumbAddressChanged(localizationChooseAddress: LocalCacheModel) {
        val localizationString = generateLocalizationString(localizationChooseAddress)

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

    private fun logBreadCrumb(state: String, embraceJsonData: JSONObject) {
        EmbraceMonitoring.logBreadcrumb(String.format(
                PDP_EMBRACE_BREADCRUMB_FORMAT,
                PDP_MODULE_NAME,
                state,
                embraceJsonData
        )
        )
    }
}