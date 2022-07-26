package com.tokopedia.oneclickcheckout.order.data.get

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.gifting.data.response.AddOnsResponse
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.data.PurchaseProtectionPlanDataResponse
import java.util.*

class ProductDataResponse(
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("cart_id")
        val cartId: String = "",
        @SerializedName("product_id")
        @SuppressLint("Invalid Data Type")
        val productId: Long = 0,
        @SerializedName("parent_id")
        val parentId: String = "",
        @SerializedName("product_name")
        val productName: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("product_price")
        val productPrice: Long = 0,
        @SerializedName("category_id")
        val categoryId: String = "",
        @SerializedName("category")
        val category: String = "",
        @SerializedName("last_level_category")
        val lastLevelCategory: String = "",
        @SerializedName("category_identifier")
        val categoryIdentifier: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("wholesale_price")
        val wholesalePrice: List<WholesalePrice> = ArrayList(),
        @SerializedName("product_weight")
        val productWeight: Int = 0,
        @SerializedName("product_weight_actual")
        val productWeightActual: Int = 0,
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
        val campaignId: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("product_original_price")
        val productOriginalPrice: Long = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("initial_price")
        val initialPrice: Long = 0,
        @SerializedName("slash_price_label")
        val slashPriceLabel: String = "",
        @SerializedName("product_finsurance")
        val productFinsurance: Int = 0,
        @SerializedName("warehouse_id")
        @SuppressLint("Invalid Data Type")
        val warehouseId: Long = 0,
        @SerializedName("free_shipping")
        val freeShipping: FreeShipping = FreeShipping(),
        @SerializedName("free_shipping_extra")
        val freeShippingExtra: FreeShipping = FreeShipping(),
        @SerializedName("product_preorder")
        val productPreorder: ProductPreorderResponse = ProductPreorderResponse(),
        @SerializedName("product_tracker_data")
        val productTrackerData: ProductTrackerData = ProductTrackerData(),
        @SerializedName("purchase_protection_plan_data")
        val purchaseProtectionPlanDataResponse: PurchaseProtectionPlanDataResponse = PurchaseProtectionPlanDataResponse(),
        @SerializedName("variant_description_detail")
        val variantDescriptionDetail: ProductVariantDescriptionDetail = ProductVariantDescriptionDetail(),
        @SerializedName("product_warning_message")
        val productWarningMessage: String = "",
        @SerializedName("product_alert_message")
        val productAlertMessage: String = "",
        @SerializedName("product_information")
        val productInformation: List<String> = emptyList(),
        @SerializedName("add_ons")
        val addOns: AddOnsResponse = AddOnsResponse()
)

class ProductImage(
        @SerializedName("image_src_200_square")
        val imageSrc200Square: String = ""
)

class ProductPreorderResponse(
        @SerializedName("duration_day")
        val durationDay: String = ""
)

class ProductVariantDescriptionDetail(
        @SerializedName("variant_name")
        val variantName: List<String> = emptyList(),
        @SerializedName("variant_description")
        val variantDescription: String = ""
)