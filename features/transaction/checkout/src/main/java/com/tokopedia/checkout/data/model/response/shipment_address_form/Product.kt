package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.data.PurchaseProtectionPlanDataResponse

data class Product(
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("product_id")
        val productId: Long = 0,
        @SerializedName("cart_id")
        val cartId: Long = 0,
        @SerializedName("product_name")
        val productName: String = "",
        @SerializedName("product_price_fmt")
        val productPriceFmt: String = "",
        @SerializedName("product_price")
        val productPrice: Long = 0,
        @SerializedName("product_original_price")
        val productOriginalPrice: Long = 0,
        @SerializedName("product_wholesale_price")
        val productWholesalePrice: Long = 0,
        @SerializedName("product_wholesale_price_fmt")
        val productWholesalePriceFmt: String = "",
        @SerializedName("product_weight_fmt")
        val productWeightFmt: String = "",
        @SerializedName("product_weight")
        val productWeight: Int = 0,
        @SerializedName("product_condition")
        val productCondition: Int = 0,
        @SerializedName("product_url")
        val productUrl: String = "",
        @SerializedName("product_returnable")
        val productReturnable: Int = 0,
        @SerializedName("product_is_free_returns")
        val productIsFreeReturns: Int = 0,
        @SerializedName("product_is_preorder")
        val productIsPreorder: Int = 0,
        @SerializedName("product_cashback")
        val productCashback: String = "",
        @SerializedName("product_min_order")
        val productMinOrder: Int = 0,
        @SerializedName("product_invenage_value")
        val productInvenageValue: Int = 0,
        @SerializedName("product_switch_invenage")
        val productSwitchInvenage: Int = 0,
        @SerializedName("product_price_currency")
        val productPriceCurrency: Int = 0,
        @SerializedName("product_image_src_200_square")
        val productImageSrc200Square: String = "",
        @SerializedName("product_notes")
        val productNotes: String = "",
        @SerializedName("product_quantity")
        val productQuantity: Int = 0,
        @SerializedName("product_menu_id")
        val productMenuId: Int = 0,
        @SerializedName("product_finsurance")
        val productFinsurance: Int = 0,
        @SerializedName("product_fcancel_partial")
        val productFcancelPartial: Int = 0,
        @SerializedName("product_cat_id")
        val productCatId: Int = 0,
        @SerializedName("product_catalog_id")
        val productCatalogId: Int = 0,
        @SerializedName("product_category")
        val productCategory: String = "",
        @SerializedName("product_alert_message")
        val productAlertMessage: String = "",
        @SerializedName("product_information")
        val productInformation: List<String> = emptyList(),
        @SerializedName("campaign_id")
        val campaignId: Int = 0,
        @SerializedName("purchase_protection_plan_data")
        val purchaseProtectionPlanDataResponse: PurchaseProtectionPlanDataResponse = PurchaseProtectionPlanDataResponse(),

        @SerializedName("free_returns")
        val freeReturns: FreeReturns? = null,

        @SerializedName("product_tracker_data")
        val productTrackerData: ProductTrackerData? = null,

        @SerializedName("product_preorder")
        val productPreorder: ProductPreorder? = null,

        @SerializedName("trade_in_info")
        val tradeInInfo: TradeInInfo? = null,

        @SerializedName("free_shipping")
        val freeShipping: FreeShipping? = null,

        @SerializedName("free_shipping_extra")
        val freeShippingExtra: FreeShipping? = null,

        @SerializedName("product_ticker")
        val productTicker: ProductTicker? = null,

        @SerializedName("variant_description_detail")
        val variantDescriptionDetail: VariantDescriptionDetail? = null,

)