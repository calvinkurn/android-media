package com.tokopedia.promocheckout.common.domain.model.event

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CartItem(
        @SerializedName("address")
        @Expose
        val address: Any = Any(),
        @SerializedName("app_link")
        @Expose
        val appLink: String = "",
        @SerializedName("category_id")
        @Expose
        val categoryId: String = "0",
        @SerializedName("configuration")
        @Expose
        val configuration: Configuration= Configuration(),
        @SerializedName("discount")
        @Expose
        val discount: Int = 0,
        @SerializedName("discounted_price")
        @Expose
        val discountedPrice: Double = 0.0,
        @SerializedName("display_sequence")
        @Expose
        val displaySequence: Int = 0,
        @SerializedName("fulfillment_service_id")
        @Expose
        val fulfillmentServiceId: String = "0",
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",
        @SerializedName("item_id")
        @Expose
        val itemId: String = "",
        @SerializedName("meta_data")
        @Expose
        val metaData: MetaData = MetaData(),
        @SerializedName("mrp")
        @Expose
        val mrp: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        @Expose
        val price: Int = 0,
        @SerializedName("product")
        @Expose
        val product: Product = Product(),
        @SerializedName("product_id")
        @Expose
        val productId: String = "0",
        @SerializedName("product_name")
        @Expose
        val productName: String = "",
        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0,
        @SerializedName("subscription_info")
        @Expose
        val subscriptionInfo: Any = Any(),
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("total_price")
        @Expose
        val totalPrice: Double = 0.0,
        @SerializedName("url")
        @Expose
        val url: String = "",
        @SerializedName("vertical_name")
        @Expose
        val verticalName: String = ""
)