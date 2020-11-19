package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.UnitTestFileUtils
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUseResponse

object DataProvider {

    private val gson = Gson()
    private val fileUtil = UnitTestFileUtils()

    fun provideValidateUseResponse(): ValidateUseResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/apply_promo_global_and_merchant_response_success.json"), ValidateUseResponse::class.java)
    }

}