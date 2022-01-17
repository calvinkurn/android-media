package com.tokopedia.pdp.fintech.domain.datamodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDetailClass(
    @SerializedName("getProductV3") val getProductV3: GetProductV3?
) : Parcelable

@Parcelize
data class GetProductV3(
    @SerializedName("productName") val productName: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("price") val price: Double?,
    @SerializedName("category") val categoryDetail: CategoryDetail?,
    @SerializedName("variant") val variant: Variant?,
) : Parcelable

@Parcelize
data class Variant(
    @SerializedName("products") val products: List<Products>
) : Parcelable

@Parcelize
data class CategoryDetail(
    @SerializedName("id")
    val categoryId: String?
) : Parcelable

@Parcelize
data class Options(
    @SerializedName("value") val value: String?
) : Parcelable

@Parcelize
data class Products(
    @SerializedName("productID") val productID: String?,
    @SerializedName("price") val price: Double
) : Parcelable









