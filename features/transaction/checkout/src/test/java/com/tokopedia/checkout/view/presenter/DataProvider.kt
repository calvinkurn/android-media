package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.UnitTestFileUtils
import com.tokopedia.checkout.data.model.response.shipment_address_form.ShipmentAddressFormGqlResponse
import com.tokopedia.purchase_platform.common.feature.checkout.request.DataCheckoutRequest
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
}