package com.tokopedia.shop.settings.basicinfo.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//data class AllowShopNameDomainChanges(
//    @SerializedName("allowShopNameDomainChanges")
//    val data: AllowShopNameDomainChangesData
//) {
//
//    data class AllowShopNameDomainChangesData(
//        @SerializedName("isDomainAllowed")
//        val isDomainAllowed: Boolean,
//        @SerializedName("reasonDomainNotAllowed")
//        val reasonDomainNotAllowed: String,
//        @SerializedName("isNameAllowed")
//        val isNameAllowed: Boolean,
//        @SerializedName("reasonNameNotAllowed")
//        val reasonNameNotAllowed: String,
//        @SerializedName("error")
//        val error: AllowShopNameDomainChangesError
//    )
//
//    data class AllowShopNameDomainChangesError(
//        @SerializedName("message")
//        val message: String
//    )
//}





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