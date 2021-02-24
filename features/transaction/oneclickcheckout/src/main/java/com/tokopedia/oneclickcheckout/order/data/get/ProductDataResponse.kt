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
        @SerializedName("product_price_fmt")
        @SuppressLint("Invalid Data Type")
        val productPriceFmt: String = "",
        @SerializedName("product_price")
        @SuppressLint("Invalid Data Type")
        val productPrice: Long = 0,
        @SerializedName("parent_id")
        @SuppressLint("Invalid Data Type")
        val parentId: Long = 0,
        @SerializedName("category_id")
        @SuppressLint("Invalid Data Type")
        val categoryId: Int = 0,
        @SerializedName("category")
        val category: String = "",
        @SerializedName("catalog_id")
        @SuppressLint("Invalid Data Type")
        val catalogId: Int = 0,
        @SerializedName("wholesale_price")
        @SuppressLint("Invalid Data Type")
        val wholesalePrice: List<WholesalePrice> = ArrayList(),
        @SerializedName("product_weight")
        val productWeight: Int = 0,
        @SerializedName("product_weight_fmt")
        val productWeightFmt: String = "",
        @SerializedName("product_condition")
        val productCondition: Int = 0,
        @SerializedName("product_status")
        val productStatus: Int = 0,
        @SerializedName("product_url")
        val productUrl: String = "",
        @SerializedName("product_returnable")
        val productReturnable: Int = 0,
        @SerializedName("is_freereturns")
        val isFreereturns: Int = 0,
        @SerializedName("is_preorder")
        val isPreorder: Int = 0,
        @SerializedName("product_cashback")
        val productCashback: String = "",
        @SerializedName("product_min_order")
        val productMinOrder: Int = 0,
        @SerializedName("product_max_order")
        val productMaxOrder: Int = 0,
        @SerializedName("product_rating")
        val productRating: String = "",
        @SerializedName("product_invenage_value")
        val productInvenageValue: Int = 0,
        @SerializedName("product_switch_invenage")
        val productSwitchInvenage: Int = 0,
        @SerializedName("product_invenage_total")
        val productInvenageTotal: ProductInvenageTotal = ProductInvenageTotal(),
        @SerializedName("product_price_currency")
        @SuppressLint("Invalid Data Type")
        val productPriceCurrency: Int = 0,
        @SerializedName("product_image")
        val productImage: ProductImage = ProductImage(),
        @SerializedName("product_all_images")
        val productAllImages: String = "",
        @SerializedName("product_notes")
        val productNotes: String = "",
        @SerializedName("product_quantity")
        val productQuantity: Int = 0,
        @SerializedName("product_weight_unit_code")
        val productWeightUnitCode: Int = 0,
        @SerializedName("product_weight_unit_text")
        val productWeightUnitText: String = "",
        @SerializedName("last_update_price")
        @SuppressLint("Invalid Data Type")
        val lastUpdatePrice: Long = 0,
        @SerializedName("is_update_price")
        @SuppressLint("Invalid Data Type")
        val isUpdatePrice: Boolean = false,
        @SerializedName("product_alias")
        val productAlias: String = "",
        @SerializedName("sku")
        val sku: String = "",
        @SerializedName("campaign_id")
        @SuppressLint("Invalid Data Type")
        val campaignId: Int = 0,
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
        @SerializedName("is_wishlisted")
        val isWishlisted: Boolean = false,
        @SerializedName("is_ppp")
        val isPPP: Boolean = false,
        @SerializedName("is_cod")
        val isCod: Boolean = false,
        @SerializedName("warehouse_id")
        @SuppressLint("Invalid Data Type")
        val wareHouseId: Int = 0,
        @SerializedName("is_parent")
        val isParent: Boolean = false,
        @SerializedName("is_campaign_error")
        val isCampaignError: Boolean = false,
        @SerializedName("is_blacklisted")
        val isBlacklisted: Boolean = false,
        @SerializedName("free_shipping")
        val freeShipping: FreeShipping = FreeShipping(),
        @SerializedName("free_shipping_extra")
        val freeShippingExtra: FreeShipping = FreeShipping(),
        @SerializedName("booking_stock")
        val bookingStock: Int = 0,
        @SerializedName("product_preorder")
        val productPreorder: ProductPreorderResponse = ProductPreorderResponse()
)