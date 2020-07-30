package com.tokopedia.promocheckout.common.domain.model.event

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Product(
        @SerializedName("allowed_payment_methods")
        @Expose
        val allowedPaymentMethods: List<String> = emptyList(),
        @SerializedName("category_id")
        @Expose
        val categoryId: Int = 0,
        @SerializedName("category_name")
        @Expose
        val categoryName: String = "",
        @SerializedName("currency")
        @Expose
        val currency: String = "",
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("image")
        @Expose
        val image: String = "",
        @SerializedName("in_stock")
        @Expose
        val inStock: Int = 0,
        @SerializedName("max_order_qty")
        @Expose
        val maxOrderQty: Int = 0,
        @SerializedName("merchant")
        @Expose
        val merchant: Merchant = Merchant(),
        @SerializedName("min_order_qty")
        @Expose
        val minOrderQty: Int = 0,
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("pre_validate")
        @Expose
        val preValidate: Boolean = false,
        @SerializedName("price")
        @Expose
        val price: Int = 0,
        @SerializedName("product_link")
        @Expose
        val productLink: String = "",
        @SerializedName("seo_url")
        @Expose
        val seoUrl: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("url")
        @Expose
        val url: String = "",
        @SerializedName("vertical_id")
        @Expose
        val verticalId: Int = 0
)