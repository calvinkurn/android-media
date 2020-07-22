package com.tokopedia.power_merchant.subscribe.domain.model

import com.google.gson.annotations.SerializedName

class GoldValidateShopBeforePMResponse(
    @SerializedName("goldValidateShopBeforePM")
    val response: ValidatePowerMerchantResponse
)