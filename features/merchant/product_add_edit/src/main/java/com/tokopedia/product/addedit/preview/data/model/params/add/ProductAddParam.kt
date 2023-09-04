package com.tokopedia.product.addedit.preview.data.model.params.add

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.math.BigInteger

@Parcelize
@SuppressLint("Invalid Data Type")
data class ProductAddParam (
        @SerializedName("productName")
        var productName: String? = null,
        @SuppressLint("Invalid Data Type") // price currently using Integer at server
        @SerializedName("price")
        var price: BigInteger? = null,
        @SerializedName("priceCurrency")
        var priceCurrency: String? = null,
        @SerializedName("stock")
        var stock: Int? = null,
        @SerializedName("status")
        var status: String? = null,
        @SerializedName("description")
        var description: String? = null,
        @SerializedName("minOrder")
        var minOrder: Int? = null,
        @SerializedName("weightUnit")
        var weightUnit: String? = null,
        @SerializedName("weight")
        var weight: Int? = null,
        @SerializedName("condition")
        var condition: String? = null,
        @SerializedName("mustInsurance")
        var mustInsurance: Boolean? = null,
        @SerializedName("sku")
        var sku: String? = null,
        @SerializedName("shop")
        var shop: ShopParam = ShopParam(),
        @SerializedName("category")
        var category: Category? = null,
        @SerializedName("menus")
        var productShowCases: List<ProductEtalase>? = null,
        @SerializedName("pictures")
        var picture: Pictures = Pictures(),
        @SerializedName("preorder")
        var preorder: Preorder? = null,
        @SerializedName("wholesale")
        var wholesale: Wholesales? = null,
        @SerializedName("video")
        var videos: Videos = Videos(),
        @SerializedName("variant")
        var variant: Variant? = null,
        @SerializedName("cpl")
        var cpl: CPLData? = null,
        @SerializedName("annotations")
        var annotations: List<String>? = null,
        @SerializedName("metadata")
        var categoryMetadata: Metadata = Metadata()
) : Parcelable

@Parcelize
data class Metadata (
    @SerializedName("category")
    var category: Category = Category(),
): Parcelable {
    @Parcelize
    data class Category (
        @SerializedName("isFromRecommendation")
        var isFromRecommendation: Boolean = false,
        @SerializedName("recommendationRank")
        var recommendationRank: Int = 0,
        @SerializedName("recommendationList")
        var recommendationList: List<Recommendation> = emptyList()
    ): Parcelable

    @Parcelize
    data class Recommendation (
        @SerializedName("categoryID")
        var categoryId: Long = 0,
        @SerializedName("confidenceScore")
        var confidenceScore: Double = 0.0,
        @SerializedName("precision")
        var precision: Double = 0.0
    ): Parcelable
}

@Parcelize
data class ShopParam(
        @SerializedName("id")
        var shopId: String = ""
) : Parcelable
