package com.tokopedia.checkout.view

import com.google.gson.Gson
import com.tokopedia.checkout.UnitTestFileUtils
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipmentAddressFormGqlResponse
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesApiGqlResponse
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesGqlResponse
import com.tokopedia.checkout.data.model.request.checkout.old.DataCheckoutRequest
import com.tokopedia.logisticcart.scheduledelivery.domain.entity.response.ScheduleDeliveryRatesResponse
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
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/bundling_saf_with_all_feature.json"), ShipmentAddressFormGqlResponse::class.java)
    }

    fun provideRatesV3Response(): RatesGqlResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/ratesv3.json"), RatesGqlResponse::class.java)
    }

    fun provideRatesV3EnabledBoPromoResponse(): RatesGqlResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/rates_v3_enabled_bo_promo.json"), RatesGqlResponse::class.java)
    }

    fun provideRatesV3apiResponse(): RatesApiGqlResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/ratesv3api.json"), RatesApiGqlResponse::class.java)
    }

    fun provideRatesV3ApiEnabledBoPromoResponse(): RatesApiGqlResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/rates_v3_api_enabled_bo_promo.json"), RatesApiGqlResponse::class.java)
    }

    fun provideRatesV3EmptyServicesResponse(): RatesApiGqlResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/rates_v3_empty_services.json"), RatesApiGqlResponse::class.java)
    }

    fun provideScheduleDeliveryRatesResponse(): ScheduleDeliveryRatesResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/schedule_delivery_rates.json"), ScheduleDeliveryRatesResponse::class.java)
    }
}
