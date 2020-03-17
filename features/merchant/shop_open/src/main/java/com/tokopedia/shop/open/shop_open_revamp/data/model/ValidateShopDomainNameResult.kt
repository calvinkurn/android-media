package com.tokopedia.shop.open.shop_open_revamp.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.open.data.model.response.ValidateShopDomainNameError

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