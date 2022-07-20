package com.tokopedia.digital_deals.data

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DealsVerifyRequest(
        @SerializedName("book")
        @Expose
        var book : Boolean = true,
        @SerializedName("checkout")
        @Expose
        var checkout : Boolean = true,
        @SerializedName("cartdata")
        @Expose
        var cartdata : CartData = CartData()
)

data class CartData(
        @SerializedName("metadata")
        @Expose
        var metadata : MetaData = MetaData(),
        @SerializedName("error")
        @Expose
        var error : String = "",
        @SerializedName("status")
        @Expose
        var status : String = "",
        @SerializedName("error_description")
        @Expose
        var errorDescription : String = ""

)

data class MetaData(
        @SerializedName("product_ids")
        @Expose
        var productIds : List<String> = emptyList(),
        @SerializedName("product_names")
        @Expose
        var productNames : List<String> = emptyList(),
        @SerializedName("provider_ids")
        @Expose
        var providerIds : List<String> = emptyList(),
        @SerializedName("item_ids")
        @Expose
        var itemIds : List<String> = emptyList(),
        @SerializedName("category_name")
        @Expose
        var categoryName : String = "",
        @SerializedName("total_price")
        @Expose
        var totalPrice : Int = 0,
        @SerializedName("quantity")
        @Expose
        var quantity : Int = 0,
        @SerializedName("item_map")
        @Expose
        var itemMaps : List<ItemMap> = emptyList()
)

data class ItemMap(
        @SerializedName("id")
        @Expose
        var id : String = "",
        @SerializedName("name")
        @Expose
        var name : String = "",
        @SerializedName("product_id")
        @Expose
        var productId : String = "",
        @SerializedName("product_name")
        @Expose
        var productName : String = "",
        @SerializedName("package_id")
        @Expose
        var packageId : String = "",
        @SerializedName("package_name")
        @Expose
        var packageName : String = "",
        @SerializedName("provider_id")
        @Expose
        var providerId : String = "",
        @SerializedName("category_id")
        @Expose
        var categoryId : String = "",
        @SerializedName("start_time")
        @Expose
        var startTime : String = "",
        @SerializedName("end_time")
        @Expose
        var endTime : String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        @Expose
        var price : Double = 0.0,
        @SerializedName("quantity")
        @Expose
        var quantity : Int = 0,
        @SerializedName("total_price")
        @Expose
        var totalPrice: Int= 0,
        @SerializedName("location_name")
        @Expose
        var locationName : String = "",
        @SerializedName("location_desc")
        @Expose
        var locationDesc : String = "",
        @SerializedName("product_app_url")
        @Expose
        var productAppUrl : String = "",
        @SerializedName("web_app_url")
        @Expose
        var webAppUrl : String = "",
        @SerializedName("product_image")
        @Expose
        var productImage : String = "",
        @SerializedName("flag_id")
        @Expose
        var flagID : String = "",
        @SerializedName("schedule_timestamp")
        @Expose
        var scheduleTimestamp : String = ""

)