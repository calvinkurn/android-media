package com.tokopedia.purchase_platform.features.cart.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.data.model.response.WholesalePrice
import com.tokopedia.transactiondata.entity.response.cartlist.shopgroup.FreeShipping
import java.util.*

/**
 * @author anggaprasetiyo on 31/01/18.
 */

data class Product(
        @SerializedName("parent_id")
        @Expose
        val parentId: Int = 0,
        @SerializedName("product_id")
        @Expose
        val productId: Int = 0,
        @SerializedName("product_name")
        @Expose
        val productName: String = "",
        @SerializedName("sku")
        @Expose
        val sku: String = "",
        @SerializedName("campaign_id")
        @Expose
        val campaignId: Int = 0,
        @SerializedName("free_returns")
        @Expose
        val freeReturns: FreeReturns = FreeReturns(),
        @SerializedName("product_price_fmt")
        @Expose
        val productPriceFmt: String = "",
        @SerializedName("product_price")
        @Expose
        val productPrice: Int = 0,
        @SerializedName("product_original_price")
        @Expose
        val productOriginalPrice: Int = 0,
        @SerializedName("is_slash_price")
        @Expose
        val isSlashPrice: Boolean = false,
        @SerializedName("category_id")
        @Expose
        val categoryId: Int = 0,
        @SerializedName("category")
        @Expose
        val category: String = "",
        @SerializedName("catalog_id")
        @Expose
        val catalogId: Int = 0,
        @SerializedName("wholesale_price")
        @Expose
        val wholesalePrice: List<WholesalePrice> = ArrayList(),
        @SerializedName("product_weight_fmt")
        @Expose
        val productWeightFmt: String = "",
        @SerializedName("product_condition")
        @Expose
        val productCondition: Int = 0,
        @SerializedName("product_status")
        @Expose
        val productStatus: Int = 0,
        @SerializedName("product_url")
        @Expose
        val productUrl: String = "",
        @SerializedName("product_returnable")
        @Expose
        val productReturnable: Int = 0,
        @SerializedName("is_freereturns")
        @Expose
        val isFreereturns: Int = 0,
        @SerializedName("is_preorder")
        @Expose
        val isPreorder: Int = 0,
        @SerializedName("is_cod")
        @Expose
        var isCod: Boolean = false,
        @SerializedName("product_cashback")
        @Expose
        val productCashback: String = "",
        @SerializedName("product_min_order")
        @Expose
        val productMinOrder: Int = 0,
        @SerializedName("product_max_order")
        @Expose
        val productMaxOrder: Int = 0,
        @SerializedName("product_rating")
        @Expose
        val productRating: Double = 0.toDouble(),
        @SerializedName("product_invenage_value")
        @Expose
        val productInvenageValue: Int = 0,
        @SerializedName("product_switch_invenage")
        @Expose
        val productSwitchInvenage: Int = 0,
        @SerializedName("price_changes")
        @Expose
        val priceChanges: PriceChanges = PriceChanges(),
        @SerializedName("product_invenage_total")
        @Expose
        val productInvenageTotal: ProductInvenageTotal = ProductInvenageTotal(),
        @SerializedName("currency_rate")
        @Expose
        val currencyRate: Int = 0,
        @SerializedName("product_price_currency")
        @Expose
        val productPriceCurrency: Int = 0,
        @SerializedName("product_image")
        @Expose
        val productImage: ProductImage = ProductImage(),
        @SerializedName("product_all_images")
        @Expose
        val productAllImages: String = "",
        @SerializedName("product_notes")
        @Expose
        val productNotes: String = "",
        @SerializedName("product_quantity")
        @Expose
        val productQuantity: Int = 0,
        @SerializedName("product_weight")
        @Expose
        val productWeight: Int = 0,
        @SerializedName("product_weight_unit_code")
        @Expose
        val productWeightUnitCode: Int = 0,
        @SerializedName("product_weight_unit_text")
        @Expose
        val productWeightUnitText: String = "",
        @SerializedName("last_update_price")
        @Expose
        val lastUpdatePrice: Long = 0,
        @SerializedName("is_update_price")
        @Expose
        val isUpdatePrice: Boolean = false,
        @SerializedName("product_preorder")
        @Expose
        val productPreorder: ProductPreorder = ProductPreorder(),
        @SerializedName("product_showcase")
        @Expose
        val productShowcase: ProductShowCase = ProductShowCase(),
        @SerializedName("product_finsurance")
        @Expose
        val productFinsurance: Int = 0,
        @SerializedName("product_shop_id")
        @Expose
        val productShopId: Int = 0,
        @SerializedName("is_wishlisted")
        @Expose
        val isWishlisted: Boolean = false,
        @SerializedName("product_tracker_data")
        @Expose
        val productTrackerData: ProductTrackerData = ProductTrackerData(),
        @SerializedName("free_shipping")
        val freeShipping: FreeShipping = FreeShipping()
)
