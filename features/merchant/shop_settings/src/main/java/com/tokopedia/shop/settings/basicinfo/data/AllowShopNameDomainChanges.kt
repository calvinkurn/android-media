package com.tokopedia.shop.settings.basicinfo.data

import com.google.gson.annotations.SerializedName

data class AllowShopNameDomainChanges(
    @SerializedName("allowShopNameDomainChanges")
    val data: AllowShopNameDomainChangesData
) {

    data class AllowShopNameDomainChangesData(
        @SerializedName("isDomainAllowed")
        val isDomainAllowed: Boolean,
        @SerializedName("reasonDomainNotAllowed")
        val reasonDomainNotAllowed: String,
        @SerializedName("isNameAllowed")
        val isNameAllowed: Boolean,
        @SerializedName("reasonNameNotAllowed")
        val reasonNameNotAllowed: String,
        @SerializedName("error")
        val error: AllowShopNameDomainChangesError
    )

    data class AllowShopNameDomainChangesError(
        @SerializedName("message")
        val message: String
    )
}