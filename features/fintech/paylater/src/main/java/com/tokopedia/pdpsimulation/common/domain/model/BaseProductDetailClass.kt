package com.tokopedia.pdpsimulation.common.domain.model

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
    @SerializedName("pictures") val pictures: List<Pictures?>?,
    @SerializedName("variant") val variant: Variant?,
) : Parcelable

@Parcelize
data class Variant(

    @SerializedName("selections") val selections: List<Selections>,
    @SerializedName("products") val products: List<Products>
) : Parcelable

@Parcelize
data class Selections(
    @SerializedName("options") val options: List<Options?>
) : Parcelable

@Parcelize
data class Options(
    @SerializedName("value") val value: String?
) : Parcelable

@Parcelize
data class Products(
    @SerializedName("productID") val productID: String?,
    @SerializedName("combination") val combination: List<Int?>
) : Parcelable

@Parcelize
data class Pictures(
    @SerializedName("urlThumbnail") val urlThumbnail: String?
) : Parcelable








