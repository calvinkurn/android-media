package com.tokopedia.checkout.view

import com.google.gson.Gson
import com.tokopedia.checkout.UnitTestFileUtils
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipmentAddressFormGqlResponse
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesApiGqlResponse
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesGqlResponse
import com.tokopedia.logisticcart.scheduledelivery.domain.entity.response.ScheduleDeliveryRatesResponse
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUseResponse

object DataProvider {

    private val GSON = Gson()
    private val FILE_UTIL = UnitTestFileUtils()

    fun provideValidateUseResponse(): ValidateUseResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/apply_promo_global_and_merchant_response_success.json"), ValidateUseResponse::class.java)
    }

//    fun provideSingleDataCheckoutRequest(): DataCheckoutRequest {
//        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/single_data_checkout_request.json"), DataCheckoutRequest::class.java)
//    }

    fun provideShipmentAddressFormResponse(): ShipmentAddressFormGqlResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/bundling_saf_with_all_feature.json"), ShipmentAddressFormGqlResponse::class.java)
    }

    fun provideRatesV3Response(): RatesGqlResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/ratesv3.json"), RatesGqlResponse::class.java)
    }

    fun provideRatesV3EnabledBoPromoResponse(): RatesGqlResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/rates_v3_enabled_bo_promo.json"), RatesGqlResponse::class.java)
    }

    fun provideRatesV3apiResponse(): RatesApiGqlResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/ratesv3api.json"), RatesApiGqlResponse::class.java)
    }

    fun provideRatesV3ApiEnabledBoPromoResponse(): RatesApiGqlResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/rates_v3_api_enabled_bo_promo.json"), RatesApiGqlResponse::class.java)
    }

    fun provideRatesV3EmptyServicesResponse(): RatesApiGqlResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/rates_v3_empty_services.json"), RatesApiGqlResponse::class.java)
    }

    fun provideScheduleDeliveryRatesResponse(): ScheduleDeliveryRatesResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/schedule_delivery_rates.json"), ScheduleDeliveryRatesResponse::class.java)
    }

    fun provideScheduleDeliveryRecommendedRatesResponse(): ScheduleDeliveryRatesResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/schedule_delivery_recommended_rates.json"), ScheduleDeliveryRatesResponse::class.java)
    }

    fun provideShipmentAddressFormWithPlatformFeeEnabledResponse(): ShipmentAddressFormGqlResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/saf_with_platform_fee_enabled.json"), ShipmentAddressFormGqlResponse::class.java)
    }

    fun provideShipmentAddressFormWithPlatformFeeDisabledResponse(): ShipmentAddressFormGqlResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/saf_with_platform_fee_disabled.json"), ShipmentAddressFormGqlResponse::class.java)
    }

    fun provideShipmentAddressFormWithAddOnsProductEnabledResponse(): ShipmentAddressFormGqlResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/saf_with_add_ons_product.json"), ShipmentAddressFormGqlResponse::class.java)
    }
}
