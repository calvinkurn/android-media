package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Basic(
        @SerializedName("alias")
        @Expose
        val alias: String = "",

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

        @SerializedName("is_eligible_cod")
        @Expose
        val isEligibleCod: Boolean = false,

        @SerializedName("is_free_return")
        @Expose
        val isFreeReturn: Boolean = false,

        @SerializedName("is_kreasi_lokal")
        @Expose
        val isKreasiLokal: Boolean = false,

        @SerializedName("is_must_insurance")
        @Expose
        val isMustInsurance: Boolean = false,

        @SerializedName("last_update_price")
        @Expose
        val lastUpdatePrice: String = "",

        @SerializedName("max_order")
        @Expose
        val maxOrder: Int = 0,

        @SerializedName("min_order")
        @Expose
        val minOrder: Int = 0,

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("price")
        @Expose
        val price: Int = 0,

        @SerializedName("price_currency")
        @Expose
        val priceCurrency: String = "",

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

        @SerializedName("weight_unit")
        @Expose
        val weightUnit: Int = 0
)