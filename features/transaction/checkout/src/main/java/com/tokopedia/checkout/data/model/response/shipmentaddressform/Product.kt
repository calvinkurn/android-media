package com.tokopedia.checkout.data.model.response.shipmentaddressform

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.checkout.domain.model.cartshipmentform.ProductVariantsResponse
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.EthicalDrugResponse
import com.tokopedia.purchase_platform.common.feature.gifting.data.response.AddOnGiftingResponse
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.data.PurchaseProtectionPlanDataResponse

data class Product(
    @SerializedName("origin_warehouse_ids")
    val originWarehouseIds: List<Long> = emptyList(),
    @SerializedName("errors")
    val errors: List<String> = emptyList(),
    @SerializedName("product_id")
    val productId: Long = 0,
    @SerializedName("cart_id")
    val cartId: Long = 0,
    @SerializedName("product_name")
    val productName: String = "",
    @SerializedName("product_price")
    val productPrice: Double = 0.0,
    @SerializedName("product_original_price")
    val productOriginalPrice: Double = 0.0,
    @SerializedName("product_wholesale_price")
    val productWholesalePrice: Double = 0.0,
    @SerializedName("product_weight_fmt")
    val productWeightFmt: String = "",
    @SerializedName("product_weight")
    val productWeight: Int = 0,
    @SerializedName("product_weight_actual")
    val productWeightActual: Int = 0,
    @SerializedName("product_is_free_returns")
    val productIsFreeReturns: Int = 0,
    @SerializedName("product_is_preorder")
    val productIsPreorder: Int = 0,
    @SerializedName("product_cashback")
    val productCashback: String = "",
    @SerializedName("product_price_currency")
    val productPriceCurrency: Int = 0,
    @SerializedName("product_image_src_200_square")
    val productImageSrc200Square: String = "",
    @SerializedName("product_notes")
    val productNotes: String = "",
    @SerializedName("product_quantity")
    val productQuantity: Int = 0,
    @SerializedName("product_finsurance")
    val productFinsurance: Int = 0,
    @SerializedName("product_fcancel_partial")
    val productFcancelPartial: Int = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("product_cat_id")
    val productCatId: Int = 0,
    @SerializedName("product_category")
    val productCategory: String = "",
    @SerializedName("last_level_category")
    val lastLevelCategory: String = "",
    @SerializedName("category_identifier")
    val categoryIdentifier: String = "",
    @SerializedName("product_alert_message")
    val productAlertMessage: String = "",
    @SerializedName("product_information")
    val productInformation: List<String> = emptyList(),
    @SuppressLint("Invalid Data Type")
    @SerializedName("campaign_id")
    val campaignId: Int = 0,
    @SerializedName("purchase_protection_plan_data")
    val purchaseProtectionPlanDataResponse: PurchaseProtectionPlanDataResponse = PurchaseProtectionPlanDataResponse(),
    @SerializedName("product_variants")
    val productVariantsResponse: ProductVariantsResponse = ProductVariantsResponse(),
    @SerializedName("product_tracker_data")
    val productTrackerData: ProductTrackerData = ProductTrackerData(),
    @SerializedName("product_preorder")
    val productPreorder: ProductPreorder = ProductPreorder(),
    @SerializedName("trade_in_info")
    val tradeInInfo: TradeInInfo = TradeInInfo(),
    @SerializedName("free_shipping")
    val freeShipping: FreeShipping = FreeShipping(),
    @SerializedName("free_shipping_extra")
    val freeShippingExtra: FreeShipping = FreeShipping(),
    @SerializedName("free_shipping_general")
    val freeShippingGeneral: FreeShippingGeneral = FreeShippingGeneral(),
    @SerializedName("product_ticker")
    val productTicker: ProductTicker = ProductTicker(),
    @SerializedName("variant_description_detail")
    val variantDescriptionDetail: VariantDescriptionDetail = VariantDescriptionDetail(),
    @SerializedName("add_ons")
    val addOns: AddOnGiftingResponse = AddOnGiftingResponse(),
    @SerializedName("ethical_drug")
    val ethicalDrugResponse: EthicalDrugResponse = EthicalDrugResponse(),
    @SerializedName("add_ons_product")
    val addOnsProduct: AddOnsProduct = AddOnsProduct(),
    @SerializedName("product_min_order")
    val productMinOrder: Int = 0,
    @SerializedName("product_max_order")
    val productMaxOrder: Int = 0,
    @SerializedName("product_invenage_value")
    val productInvenageValue: Int = 0,
    @SerializedName("product_switch_invenage")
    val productSwitchInvenage: Int = 0
)
