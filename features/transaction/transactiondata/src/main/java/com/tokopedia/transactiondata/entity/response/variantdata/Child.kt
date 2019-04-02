package com.tokopedia.transactiondata.entity.response.variantdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class Child(

        @SerializedName("product_id")
        @Expose
        val productId: Int,

        @SerializedName("price")
        @Expose
        val price: Int,

        @SerializedName("stock")
        @Expose
        val stock: Int,

        @SerializedName("min_order")
        @Expose
        val minOrder: Int,

        @SerializedName("max_order")
        @Expose
        val maxOrder: Int,

        @SerializedName("sku")
        @Expose
        val sku: String,

        @SerializedName("option_ids")
        @Expose
        val optionIds: ArrayList<Int>,

        @SerializedName("is_enabled")
        @Expose
        val isEnabled: Boolean,

        @SerializedName("name")
        @Expose
        val name: String,

        @SerializedName("url")
        @Expose
        val url: String,

        @SerializedName("is_buyable")
        @Expose
        val isBuyable: Boolean,

        @SerializedName("stock_wording")
        @Expose
        val stockWording: String,

        @SerializedName("stock_wording_html")
        @Expose
        val stockWordingHtml: String

)