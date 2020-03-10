package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.variant.Child
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.purchase_platform.features.express_checkout.data.entity.response.variant.Option

data class ProductVariantResponse(
        @SerializedName("parentID")
        @Expose
        var parentId: Int = 0,

        @SerializedName("defaultChild")
        @Expose
        var defaultChild: Int = 0,

        @SerializedName("variant")
        @Expose
        var variant: List<VariantResponse> = listOf(),

        @SerializedName("children")
        @Expose
        var children: List<ChildResponse> = listOf()
)

data class VariantResponse(
        @SerializedName("product_variant_id")
        @Expose
        val productVariantId: Int = 0,

        @SerializedName("name")
        @Expose
        val variantName: String = "",

        @SerializedName("identifier")
        @Expose
        val identifier: String = "",

        @SerializedName("position")
        @Expose
        val position: Int = 0,

        @SerializedName("option")
        @Expose
        val options: ArrayList<OptionResponse> = ArrayList()
)

data class OptionResponse(

        @SerializedName("product_variant_option_id")
        @Expose
        val id: Int = 0,

        @SerializedName("value")
        @Expose
        val value: String = "",

        @SerializedName("hex")
        @Expose
        val hex: String = ""

)

data class ChildResponse(

        @SerializedName("product_id")
        @Expose
        val productId: Int,

        @SerializedName("price")
        @Expose
        val price: Int,

        @SerializedName("stock")
        @Expose
        val stock: Int,

        @SerializedName("minimum_order")
        @Expose
        val minOrder: Int,

//        @SerializedName("max_order")
//        @Expose
//        val maxOrder: Int,

        @SerializedName("sku")
        @Expose
        val sku: String,

        @SerializedName("option_id")
        @Expose
        val optionIds: ArrayList<Int>,

        @SerializedName("enabled")
        @Expose
        val isEnabled: Boolean,

        @SerializedName("product_name")
        @Expose
        val name: String,

        @SerializedName("product_url")
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