package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * @author anggaprasetiyo on 31/01/18.
 */

data class Product(
        @SerializedName("variant_description_detail")
        val variantDescriptionDetail: VariantDescriptionDetail = VariantDescriptionDetail(),
        @SerializedName("product_information")
        val productInformation: List<String> = emptyList(),
        @SerializedName("parent_id")
        val parentId: Long = 0,
        @SerializedName("product_id")
        val productId: Long = 0,
        @SerializedName("product_name")
        val productName: String = "",
        @SerializedName("sku")
        val sku: String = "",
        @SerializedName("campaign_id")
        val campaignId: Int = 0,
        @SerializedName("free_returns")
        val freeReturns: FreeReturns = FreeReturns(),
        @SerializedName("initial_price")
        val initialPrice: Long = 0,
        @SerializedName("initial_price_fmt")
        val initialPriceFmt: String = "",
        @SerializedName("product_price_fmt")
        val productPriceFmt: String = "",
        @SerializedName("product_price")
        val productPrice: Long = 0,
        @SerializedName("product_original_price")
        val productOriginalPrice: Long = 0,
        @SerializedName("is_slash_price")
        val isSlashPrice: Boolean = false,
        @SerializedName("slash_price_label")
        val slashPriceLabel: String = "",
        @SerializedName("category_id")
        val categoryId: Int = 0,
        @SerializedName("category")
        val category: String = "",
        @SerializedName("catalog_id")
        val catalogId: Int = 0,
        @SerializedName("wholesale_price")
        val wholesalePrice: List<WholesalePrice> = ArrayList(),
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
        @SerializedName("is_cod")
        var isCod: Boolean = false,
        @SerializedName("product_cashback")
        val productCashback: String = "",
        @SerializedName("product_min_order")
        val productMinOrder: Int = 0,
        @SerializedName("product_max_order")
        val productMaxOrder: Int = 0,
        @SerializedName("product_rating")
        val productRating: Double = 0.toDouble(),
        @SerializedName("product_invenage_value")
        val productInvenageValue: Int = 0,
        @SerializedName("product_switch_invenage")
        val productSwitchInvenage: Int = 0,
        @SerializedName("product_warning_message")
        val productWarningMessage: String = "",
        @SerializedName("product_alert_message")
        val productAlertMessage: String = "",
        @SerializedName("price_changes")
        val priceChanges: PriceChanges = PriceChanges(),
        @SerializedName("product_invenage_total")
        val productInvenageTotal: ProductInvenageTotal = ProductInvenageTotal(),
        @SerializedName("currency_rate")
        val currencyRate: Int = 0,
        @SerializedName("product_price_currency")
        val productPriceCurrency: Int = 0,
        @SerializedName("product_image")
        val productImage: ProductImage = ProductImage(),
        @SerializedName("product_all_images")
        val productAllImages: String = "",
        @SerializedName("product_notes")
        val productNotes: String = "",
        @SerializedName("product_quantity")
        val productQuantity: Int = 0,
        @SerializedName("product_weight")
        val productWeight: Int = 0,
        @SerializedName("product_weight_unit_code")
        val productWeightUnitCode: Int = 0,
        @SerializedName("product_weight_unit_text")
        val productWeightUnitText: String = "",
        @SerializedName("last_update_price")
        val lastUpdatePrice: Long = 0,
        @SerializedName("is_update_price")
        val isUpdatePrice: Boolean = false,
        @SerializedName("product_preorder")
        val productPreorder: ProductPreorder = ProductPreorder(),
        @SerializedName("product_showcase")
        val productShowcase: ProductShowCase = ProductShowCase(),
        @SerializedName("product_finsurance")
        val productFinsurance: Int = 0,
        @SerializedName("product_shop_id")
        val productShopId: Int = 0,
        @SerializedName("is_wishlisted")
        val isWishlisted: Boolean = false,
        @SerializedName("product_tracker_data")
        val productTrackerData: ProductTrackerData = ProductTrackerData(),
        @SerializedName("free_shipping")
        val freeShipping: FreeShipping = FreeShipping(),
        @SerializedName("free_shipping_extra")
        val freeShippingExtra: FreeShipping = FreeShipping()
)
