package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Basic(
        @SerializedName("alias")
        @Expose
        val alias: String = "",

        @SerializedName("catalogID")
        @Expose
        val catalogID: Int = 0,

        @SerializedName("condition")
        @Expose
        val condition: Int = 0,

        @SerializedName("description")
        @Expose
        val description: String = "",

        @SerializedName("gtin")
        @Expose
        val gtin: String = "",

        @SerializedName("id")
        @Expose
        val id: Int = 0,

        @SerializedName("isEligibleCOD")
        @Expose
        val isEligibleCod: Boolean = false,

        @SerializedName("isFreeReturn")
        @Expose
        val isFreeReturn: Boolean = false,

        @SerializedName("isKreasiLokal")
        @Expose
        val isKreasiLokal: Boolean = false,

        @SerializedName("isMustInsurance")
        @Expose
        val isMustInsurance: Boolean = false,

        @SerializedName("lastUpdatePrice")
        @Expose
        val lastUpdatePrice: String = "",

        @SerializedName("maxOrder")
        @Expose
        val maxOrder: Int = 0,

        @SerializedName("minOrder")
        @Expose
        val minOrder: Int = 0,

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("price")
        @Expose
        val price: Int = 0,

        @SerializedName("priceCurrency")
        @Expose
        val priceCurrency: String = "",

        @SerializedName("shopID")
        @Expose
        val shopID: Int = 0,

        @SerializedName("sku")
        @Expose
        val sku: String = "",

        @SerializedName("status")
        @Expose
        val status: Int = 0,

        @SerializedName("stock")
        @Expose
        val stock: Int = 0,

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("weight")
        @Expose
        val weight: Int = 0,

        @SerializedName("weightUnit")
        @Expose
        val weightUnit: Int = 0
)