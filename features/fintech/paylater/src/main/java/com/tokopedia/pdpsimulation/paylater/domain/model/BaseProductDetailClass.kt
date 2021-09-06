package com.tokopedia.pdpsimulation.paylater.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BaseProductDetailClass(
    @SerializedName("getProductV3") val getProductV3: GetProductV3?
) : Parcelable

@Parcelize
data class GetProductV3(
    @SerializedName("productName") val productName: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("price") val price: Double?,
    @SerializedName("pictures") val pictures: List<Pictures?>?
) : Parcelable

@Parcelize
data class Pictures(
    @SerializedName("urlThumbnail") val urlThumbnail: String?
) : Parcelable








