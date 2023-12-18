package com.tokopedia.minicart.common.data.response.minicartlist

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.cartcommon.data.response.common.ProductTagInfo

data class Product(
        @SerializedName("cart_id")
        val cartId: String = "",
        @SerializedName("product_id")
        val productId: String = "",
        @SerializedName("product_weight")
        val productWeight: Int = 0,
        @SerializedName("product_quantity")
        val productQuantity: Int = 0,
        @SerializedName("product_name")
        val productName: String = "",
        @SerializedName("product_image")
        val productImage: ProductImage = ProductImage(),
        @SerializedName("variant_description_detail")
        val variantDescriptionDetail: VariantDescriptionDetail = VariantDescriptionDetail(),
        @SerializedName("product_warning_message")
        val productWarningMessage: String = "",
        @SerializedName("slash_price_label")
        val slashPriceLabel: String = "",
        @SerializedName("product_original_price")
        val productOriginalPrice: Double = 0.0,
        @SerializedName("initial_price")
        val initialPrice: Double = 0.0,
        @SerializedName("product_price")
        val productPrice: Double = 0.0,
        @SerializedName("product_tag_info")
        val productTagInfo: List<ProductTagInfo> = emptyList(),
        @SerializedName("product_information")
        val productInformation: List<String> = emptyList(),
        @SerializedName("product_notes")
        val productNotes: String = "",
        @SerializedName("product_min_order")
        val productMinOrder: Int = 0,
        @SerializedName("product_max_order")
        val productMaxOrder: Int = 0,
        @SerializedName("product_invenage_value")
        val productInvenageValue: Int = 0,
        @SerializedName("product_switch_invenage")
        val productSwitchInvenage: Int = 0,
        @SerializedName("parent_id")
        val parentId: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("wholesale_price")
        val wholesalePrice: List<WholesalePrice> = emptyList(),
        @SerializedName("campaign_id")
        val campaignId: String = "",
        @SerializedName("warehouse_id")
        val warehouseId: String = "",
        @SerializedName("category_id")
        val categoryId: String = "",
        @SerializedName("category")
        val category: String = "",
        @SerializedName("product_tracker_data")
        val productTrackerData: ProductTrackerData = ProductTrackerData(),
        @SerializedName("product_cashback")
        val productCashback: String = "",
        @SerializedName("selected_unavailable_action_link")
        val selectedUnavailableActionLink: String = "",
        @SerializedName("free_shipping_general")
        val freeShippingGeneral: FreeShippingGeneral = FreeShippingGeneral()
)
