package com.tokopedia.checkout.data.model.response.shipmentaddressform

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class BoMetadata(
        @SerializedName("bo_type")
        val boType: Int = 0,
        @SerializedName("bo_eligibilities")
        val boEligibilities: List<BoEligibility> = emptyList(),
        @SerializedName("additional_attributes")
        val additionalAttributes: List<BoAdditionalAttribute> = emptyList()
): Parcelable

@Parcelize
class BoEligibility(
        @SerializedName("key")
        val key: String = "",
        @SerializedName("value")
        val value: String = ""
): Parcelable

@Parcelize
class BoAdditionalAttribute(
        @SerializedName("key")
        val key: String = "",
        @SerializedName("value")
        val value: String = ""
): Parcelable