package com.tokopedia.vouchercreation.shop.create.view.uimodel.validation

import com.google.gson.annotations.SerializedName

data class PromoCodeValidation(
        @SerializedName("code")
        val promoCodeError: String
) : Validation