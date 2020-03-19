package com.tokopedia.product.addedit.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductInput (
    
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
    var catalog: Catalog? = null,
    @SerializedName("category")
    @Expose
    var category: Category? = null,
    @SerializedName("menu")
    @Expose
    var menu: Menu? = null,
    @SerializedName("pictures")
    @Expose
    var pictures: Pictures? = null,
    @SerializedName("preorder")
    @Expose
    var preorder: Preorder? = null,
    @SerializedName("wholesale")
    @Expose
    var wholesale: Wholesale? = null,
    @SerializedName("video")
    @Expose
    var video: Video? = null,
    @SerializedName("variant")
    @Expose
    var variant: Variant? = null

)
