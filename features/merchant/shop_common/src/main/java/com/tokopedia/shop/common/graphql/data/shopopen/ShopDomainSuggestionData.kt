package com.tokopedia.shop.common.graphql.data.shopopen

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShopDomainSuggestionData(
        @SerializedName("shopDomainSuggestion")
        @Expose
        val shopDomainSuggestion: ShopDomainSuggestion = ShopDomainSuggestion()
): Parcelable

@Parcelize
data class ShopDomainSuggestion(
        @SerializedName("result")
        @Expose
        val result: ValidateShopDomainSuggestionResult = ValidateShopDomainSuggestionResult(),
        @SerializedName("error")
        @Expose
        val error: ValidateShopDomainSuggestionError = ValidateShopDomainSuggestionError()
): Parcelable

@Parcelize
data class ValidateShopDomainSuggestionResult(
        @SerializedName("shopDomain")
        @Expose
        val shopDomain: String = "",
        @SerializedName("shopDomains")
        @Expose
        val shopDomains: List<String> = listOf()
): Parcelable

@Parcelize
data class ValidateShopDomainSuggestionError(
        @SerializedName("message")
        @Expose
        val message: String = ""
): Parcelable
