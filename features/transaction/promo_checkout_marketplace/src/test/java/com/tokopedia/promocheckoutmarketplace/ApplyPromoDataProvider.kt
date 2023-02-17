package com.tokopedia.promocheckoutmarketplace

import com.google.gson.Gson
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUseResponse

object ApplyPromoDataProvider {

    private val gson = Gson()
    private val fileUtil = UnitTestFileUtils()

    fun provideApplyPromoEmptyRequest(): ValidateUsePromoRequest {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/apply_promo_empty_request.json"), ValidateUsePromoRequest::class.java)
    }

    fun provideApplyPromoGlobalRequest(): ValidateUsePromoRequest {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/apply_promo_global_request.json"), ValidateUsePromoRequest::class.java)
    }

    fun provideApplyPromoMerchantRequest(): ValidateUsePromoRequest {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/apply_promo_merchant_request.json"), ValidateUsePromoRequest::class.java)
    }

    fun provideApplyPromoGlobalAndMerchantRequest(): ValidateUsePromoRequest {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/apply_promo_global_and_merchant_request.json"), ValidateUsePromoRequest::class.java)
    }

    fun provideApplyPromoGlobalResponseSuccess(): ValidateUseResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/apply_promo_global_response_success.json"), ValidateUseResponse::class.java)
    }

    fun provideApplyPromoMerchantResponseSuccess(): ValidateUseResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/apply_promo_merchant_response_success.json"), ValidateUseResponse::class.java)
    }

    fun provideApplyPromoGlobalAndMerchantResponseSuccess(): ValidateUseResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/apply_promo_global_and_merchant_response_success.json"), ValidateUseResponse::class.java)
    }

    fun provideApplyPromoResponseError(): ValidateUseResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/apply_promo_response_error.json"), ValidateUseResponse::class.java)
    }

    fun provideApplyPromoResponseFailed(): ValidateUseResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/apply_promo_response_failed.json"), ValidateUseResponse::class.java)
    }

    fun provideApplyPromoGlobalResponseFailed(): ValidateUseResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/apply_promo_global_response_failed.json"), ValidateUseResponse::class.java)
    }

    fun provideApplyPromoMerchantResponseFailed(): ValidateUseResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/apply_promo_merchant_response_failed.json"), ValidateUseResponse::class.java)
    }

    fun provideApplyPromoBoResponseFailed(): ValidateUseResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/apply_promo_bo_response_failed.json"), ValidateUseResponse::class.java)
    }

    fun provideApplyPromoResponseClashing(): ValidateUseResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/apply_promo_response_clashing.json"), ValidateUseResponse::class.java)
    }

    fun provideApplyPromoMerchantSuccessButGetRedState(): ValidateUseResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/apply_promo_merchant_response_success_but_get_red_state.json"), ValidateUseResponse::class.java)
    }

    fun provideApplyPromoGlobalAndMerchantRequestInvalid(): ValidateUsePromoRequest {
        val request = provideApplyPromoGlobalAndMerchantRequest()
        request.codes = mutableListOf("GLOBAL_CODE_INVALID")
        request.orders.forEach {
            it?.codes = mutableListOf("MERCHANT_CODE_INVALID")
        }

        return request
    }
}
