package com.tokopedia.shop.open.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateShopDomainNameResult(

        @SerializedName("validateDomainShopName")
        @Expose
        val validateDomainShopName: ValidateDomainShopName = ValidateDomainShopName()
)

data class ValidateDomainShopName(
        @SerializedName("isValid")
        @Expose
        val isValid: Boolean = false,
        @SerializedName("error")
        @Expose
        val error: ValidateShopDomainNameError = ValidateShopDomainNameError()
)