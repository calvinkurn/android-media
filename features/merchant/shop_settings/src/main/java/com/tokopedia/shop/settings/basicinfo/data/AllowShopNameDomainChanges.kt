package com.tokopedia.shop.settings.basicinfo.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class AllowShopNameDomainChanges(
        @Expose
        @SerializedName("allowShopNameDomainChanges")
        val data: AllowShopNameDomainChangesData = AllowShopNameDomainChangesData()
)

data class AllowShopNameDomainChangesData(
        @Expose
        @SerializedName("isDomainAllowed")
        val isDomainAllowed: Boolean = false,
        @Expose
        @SerializedName("reasonDomainNotAllowed")
        val reasonDomainNotAllowed: String = "",
        @Expose
        @SerializedName("isNameAllowed")
        val isNameAllowed: Boolean = false,
        @Expose
        @SerializedName("reasonNameNotAllowed")
        val reasonNameNotAllowed: String = "",
        @Expose
        @SerializedName("error")
        val error: AllowShopNameDomainChangesError = AllowShopNameDomainChangesError()
)

data class AllowShopNameDomainChangesError(
        @Expose
        @SerializedName("message")
        val message: String = ""
)