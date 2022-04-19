package com.tokopedia.shop.common.graphql.data.shopopen

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidateShopDomainNameResult(
        @SerializedName("validateDomainShopName")
        @Expose
        val validateDomainShopName: ValidateDomainShopName = ValidateDomainShopName()
): Parcelable

@Parcelize
data class ValidateDomainShopName(
        @SuppressLint("Invalid Data Type")
        @SerializedName("isValid")
        @Expose
        val isValid: Boolean = false,
        @SerializedName("error")
        @Expose
        val error: ValidateShopDomainNameError = ValidateShopDomainNameError()
): Parcelable

@Parcelize
data class ValidateShopDomainNameError(
        @SerializedName("message")
        @Expose
        val message:String = ""
): Parcelable