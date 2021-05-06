package com.tokopedia.oneclickcheckout.order.data.get

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import java.util.*

data class ProductDataResponse(
        @SerializedName("product_tracker_data")
        val productTrackerData: ProductTrackerData = ProductTrackerData(),
        @SerializedName("product_id")
        @SuppressLint("Invalid Data Type")
        val productId: Long = 0,
        @SerializedName("product_name")
        val productName: String = "",
        @SerializedName("product_price")
        @SuppressLint("Invalid Data Type")
        val productPrice: Long = 0,
        @SerializedName("category_id")
        val categoryId: String = "",
        @SerializedName("category")
        val category: String = "",
        @SerializedName("wholesale_price")
        @SuppressLint("Invalid Data Type")
        val wholesalePrice: List<WholesalePrice> = ArrayList(),
        @SerializedName("product_weight")
        val productWeight: Int = 0,
        @SerializedName("is_preorder")
        val isPreOrder: Int = 0,
        @SerializedName("product_cashback")
        val productCashback: String = "",
        @SerializedName("product_min_order")
        val productMinOrder: Int = 0,
        @SerializedName("product_max_order")
        val productMaxOrder: Int = 0,
        @SerializedName("product_invenage_value")
        val productInvenageValue: Int = 0,
        @SerializedName("product_switch_invenage")
        val productSwitchInvenage: Int = 0,
        @SerializedName("product_image")
        val productImage: ProductImage = ProductImage(),
        @SerializedName("product_notes")
        val productNotes: String = "",
        @SerializedName("product_quantity")
        val productQuantity: Int = 0,
        @SerializedName("campaign_id")
        @SuppressLint("Invalid Data Type")
        val campaignId: String = "",
        @SerializedName("product_original_price")
        @SuppressLint("Invalid Data Type")
        val productOriginalPrice: Long = 0,
        @SerializedName("product_price_original_fmt")
        @SuppressLint("Invalid Data Type")
        val productPriceOriginalFmt: String = "",
        @SerializedName("is_slash_price")
        @SuppressLint("Invalid Data Type")
        val isSlashPrice: Boolean = false,
        @SerializedName("product_finsurance")
        val productFinsurance: Int = 0,
        @SerializedName("warehouse_id")
        @SuppressLint("Invalid Data Type")
        val wareHouseId: Long = 0,
        @SerializedName("free_shipping")
        val freeShipping: FreeShipping = FreeShipping(),
        @SerializedName("free_shipping_extra")
        val freeShippingExtra: FreeShipping = FreeShipping(),
        @SerializedName("product_preorder")
        val productPreorder: ProductPreorderResponse = ProductPreorderResponse()
)

data class ProductImage(
        @SerializedName("image_src_200_square")
        val imageSrc200Square: String = ""
)

class ProductPreorderResponse(
        @SerializedName("duration_day")
        val durationDay: String = ""
)