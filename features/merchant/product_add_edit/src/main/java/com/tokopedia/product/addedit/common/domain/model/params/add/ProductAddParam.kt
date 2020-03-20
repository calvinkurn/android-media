package com.tokopedia.product.addedit.common.domain.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductAddParam (
        @SerializedName("productName")
        @Expose
        var productName: String? = null,
        @SerializedName("price")
        @Expose
        var price: Int? = null,
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
        @SerializedName("catalog")
        @Expose
        var catalog: Catalog = Catalog(),
        @SerializedName("category")
        @Expose
        var category: Category? = null,
        @SerializedName("menu")
        @Expose
        var menu: Menu? = null,
        @SerializedName("picture")
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
        var videos: Videos = Videos(),
        @SerializedName("variant")
        @Expose
        var variant: Variant? = null
) : Parcelable

