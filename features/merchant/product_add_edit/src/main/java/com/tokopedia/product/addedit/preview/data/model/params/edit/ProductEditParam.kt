package com.tokopedia.product.addedit.preview.data.model.params.edit

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.addedit.preview.data.model.params.add.*
import kotlinx.parcelize.Parcelize
import java.math.BigInteger

@Parcelize
@SuppressLint("Invalid Data Type")
data class ProductEditParam (
        @SerializedName("productID")
        var productId: String? = null,
        @SerializedName("productName")
        var productName: String? = null,
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
        var videos: Videos? = Videos(),
        @SerializedName("variant")
        var variant: Variant? = null,
        @SerializedName("cpl")
        var cpl: CPLData? = null,
        @SerializedName("annotations")
        var annotations: List<String>? = null
) : Parcelable
