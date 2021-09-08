package com.tokopedia.checkout.bundle.view

import com.google.gson.Gson
import com.tokopedia.checkout.bundle.UnitTestFileUtils
import com.tokopedia.checkout.bundle.data.model.response.shipmentaddressform.ShipmentAddressFormGqlResponse
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesApiGqlResponse
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesGqlResponse
import com.tokopedia.checkout.bundle.data.model.request.checkout.old.DataCheckoutRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUseResponse

object DataProvider {

    private val gson = Gson()
    private val fileUtil = UnitTestFileUtils()

    fun provideValidateUseResponse(): ValidateUseResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/apply_promo_global_and_merchant_response_success.json"), ValidateUseResponse::class.java)
    }

    fun provideSingleDataCheckoutRequest(): DataCheckoutRequest {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/single_data_checkout_request.json"), DataCheckoutRequest::class.java)
    }

    fun provideShipmentAddressFormResponse(): ShipmentAddressFormGqlResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/saf_with_all_feature.json"), ShipmentAddressFormGqlResponse::class.java)
    }

    fun provideRatesV3Response(): RatesGqlResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/ratesv3.json"), RatesGqlResponse::class.java)
    }

    fun provideRatesV3apiResponse(): RatesApiGqlResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/ratesv3api.json"), RatesApiGqlResponse::class.java)
    }

}