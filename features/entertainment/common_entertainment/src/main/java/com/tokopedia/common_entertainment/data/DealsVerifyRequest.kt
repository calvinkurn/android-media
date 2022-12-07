package com.tokopedia.common_entertainment.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class DealsVerifyRequest(
    @SerializedName("book")
    var book: Boolean = true,
    @SerializedName("checkout")
    var checkout: Boolean = true,
    @SerializedName("cartdata")
    var cartdata: CartData = CartData()
)

data class CartData(
    @SerializedName("metadata")
    var metadata: MetaData = MetaData(),
    @SerializedName("error")
    var error: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("error_description")
    var errorDescription: String = ""
)

data class MetaData(
    @SerializedName("product_ids")
    var productIds: List<String> = emptyList(),
    @SerializedName("product_names")
    var productNames: List<String> = emptyList(),
    @SerializedName("provider_ids")
    var providerIds: List<String> = emptyList(),
    @SerializedName("item_ids")
    var itemIds: List<String> = emptyList(),
    @SerializedName("category_name")
    var categoryName: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("total_price")
    var totalPrice: Long = 0,
    @SerializedName("quantity")
    var quantity: Int = 0,
    @SerializedName("item_map")
    var itemMaps: List<ItemMap> = emptyList()
)

data class ItemMap(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("product_id")
    var productId: String = "",
    @SerializedName("product_name")
    var productName: String = "",
    @SerializedName("package_id")
    var packageId: String = "",
    @SerializedName("package_name")
    var packageName: String = "",
    @SerializedName("provider_id")
    var providerId: String = "",
    @SerializedName("category_id")
    var categoryId: String = "",
    @SerializedName("start_time")
    var startTime: String = "",
    @SerializedName("end_time")
    var endTime: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    var price: Double = 0.0,
    @SerializedName("quantity")
    var quantity: Int = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("total_price")
    var totalPrice: Long= 0,
    @SerializedName("location_name")
    var locationName: String = "",
    @SerializedName("location_desc")
    var locationDesc: String = "",
    @SerializedName("product_app_url")
    var productAppUrl: String = "",
    @SerializedName("web_app_url")
    var webAppUrl: String = "",
    @SerializedName("product_image")
    var productImage: String = "",
    @SerializedName("flag_id")
    var flagID: String = "",
    @SerializedName("schedule_timestamp")
    var scheduleTimestamp: String = ""
)
