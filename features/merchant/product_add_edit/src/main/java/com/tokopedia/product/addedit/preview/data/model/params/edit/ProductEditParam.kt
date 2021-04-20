package com.tokopedia.product.addedit.preview.data.model.params.edit

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.addedit.preview.data.model.params.add.*
import kotlinx.android.parcel.Parcelize
import java.math.BigInteger

@Parcelize
data class ProductEditParam (
        @SerializedName("productID")
        @Expose
        var productId: String? = null,
        @SerializedName("productName")
        @Expose
        var productName: String? = null,
        @SerializedName("price")
        @Expose
        var price: BigInteger? = null,
        @SerializedName("priceCurrency")
        @Expose
        var priceCurrency: String? = null,
        @SerializedName("stock")
        @Expose
        var stock: Int? = null,
        @SerializedName("status")
        @Expose
        var status: String? = null,
        @SerializedName("description")
        @Expose
        var description: String? = null,
        @SerializedName("minOrder")
        @Expose
        var minOrder: Int? = null,
        @SerializedName("weightUnit")
        @Expose
        var weightUnit: String? = null,
        @SerializedName("weight")
        @Expose
        var weight: Int? = null,
        @SerializedName("condition")
        @Expose
        var condition: String? = null,
        @SerializedName("mustInsurance")
        @Expose
        var mustInsurance: Boolean? = null,
        @SerializedName("sku")
        @Expose
        var sku: String? = null,
        @SerializedName("shop")
        @Expose
        var shop: ShopParam = ShopParam(),
        @SerializedName("catalog")
        @Expose
        var catalog: Catalog = Catalog(),
        @SerializedName("category")
        @Expose
        var category: Category? = null,
        @SerializedName("menus")
        @Expose
        var productShowCases: List<ProductEtalase>? = null,
        @SerializedName("pictures")
        @Expose
        var picture: Pictures = Pictures(),
        @SerializedName("preorder")
        @Expose
        var preorder: Preorder? = null,
        @SerializedName("wholesale")
        @Expose
        var wholesale: Wholesales? = null,
        @SerializedName("video")
        @Expose
        var videos: Videos? = Videos(),
        @SerializedName("variant")
        @Expose
        var variant: Variant? = null,
        @SerializedName("annotations")
        @Expose
        var annotations: List<String>? = null
) : Parcelable