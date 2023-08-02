package com.tokopedia.cart.data.model.response.shopgroupsimplified

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import java.util.*

data class Product(
    @SerializedName("origin_warehouse_ids")
    val originWarehouseIds: List<Int> = emptyList(),
    @SerializedName("checkbox_state")
    val isCheckboxState: Boolean = false,
    @SerializedName("cart_id")
    val cartId: String = "",
    @SerializedName("variant_description_detail")
    val variantDescriptionDetail: VariantDescriptionDetail = VariantDescriptionDetail(),
    @SerializedName("product_information")
    val productInformation: List<String> = emptyList(),
    @SerializedName("product_information_with_icon")
    val productInformationWithIcon: List<ProductInformationWithIcon> = emptyList(),
    @SerializedName("parent_id")
    val parentId: String = "",
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("product_name")
    val productName: String = "",
    @SerializedName("campaign_id")
    val campaignId: String = "",
    @SerializedName("initial_price")
    val initialPrice: Double = 0.0,
    @SerializedName("initial_price_fmt")
    val initialPriceFmt: String = "",
    @SerializedName("product_price")
    val productPrice: Double = 0.0,
    @SerializedName("product_original_price")
    val productOriginalPrice: Double = 0.0,
    @SerializedName("slash_price_label")
    val slashPriceLabel: String = "",
    @SerializedName("category_id")
    val categoryId: String = "",
    @SerializedName("category")
    val category: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("wholesale_price")
    val wholesalePrice: List<WholesalePrice> = ArrayList(),
    @SerializedName("is_preorder")
    val isPreorder: Int = 0,
    @SerializedName("is_cod")
    var isCod: Boolean = false,
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
    @SerializedName("product_warning_message")
    val productWarningMessage: String = "",
    @SerializedName("product_alert_message")
    val productAlertMessage: String = "",
    @SerializedName("product_image")
    val productImage: ProductImage = ProductImage(),
    @SerializedName("product_notes")
    val productNotes: String = "",
    @SerializedName("product_quantity")
    val productQuantity: Int = 0,
    @SerializedName("product_weight")
    val productWeight: Int = 0,
    @SerializedName("product_preorder")
    val productPreorder: ProductPreorder = ProductPreorder(),
    @SerializedName("product_shop_id")
    val productShopId: String = "",
    @SerializedName("is_wishlisted")
    val isWishlisted: Boolean = false,
    @SerializedName("product_tracker_data")
    val productTrackerData: ProductTrackerData = ProductTrackerData(),
    @SerializedName("free_shipping")
    val freeShipping: FreeShipping = FreeShipping(),
    @SerializedName("free_shipping_extra")
    val freeShippingExtra: FreeShipping = FreeShipping(),
    @SerializedName("free_shipping_general")
    val freeShippingGeneral: FreeShippingGeneral = FreeShippingGeneral(),
    @SerializedName("selected_unavailable_action_link")
    val selectedUnavailableActionLink: String = "",
    @SerializedName("warehouse_id")
    val warehouseId: String = "",
    @SerializedName("ethical_drug")
    val ethicalDrug: EthicalDrug = EthicalDrug(),
    @SerializedName("bundle_ids")
    val bundleIds: List<String> = emptyList()
)
