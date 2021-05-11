package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class Product(
        @SerializedName("campaign_id")
        val campaignId: String = "",
        @SerializedName("campaign_type_name")
        val campaignTypeName: String = "",
        @SerializedName("catalog_id")
        val catalogId: String = "",
        @SerializedName("categories")
        val categories: List<Category> = emptyList(),
        @SerializedName("category")
        val category: String = "",
        @SerializedName("category_id")
        val categoryId: String = "",
        @SerializedName("currency_rate")
        val currencyRate: Int = 0,
        @SerializedName("free_shipping")
        val freeShipping: FreeShipping = FreeShipping(),
        @SerializedName("hide_gimmick")
        val hideGimmick: Boolean = false,
        @SerializedName("initial_price")
        val initialPrice: Long = 0L,
        @SerializedName("initial_price_fmt")
        val initialPriceFmt: String = "",
        @SerializedName("is_big_campaign")
        val isBigCampaign: Boolean = false,
        @SerializedName("is_blacklisted")
        val isBlacklisted: Boolean = false,
        @SerializedName("is_campaign_error")
        val isCampaignError: Boolean = false,
        @SerializedName("is_cod")
        val isCod: Boolean = false,
        @SerializedName("is_parent")
        val isParent: Boolean = false,
        @SerializedName("is_ppp")
        val isPpp: Boolean = false,
        @SerializedName("is_preorder")
        val isPreorder: Int = 0,
        @SerializedName("is_slash_price")
        val isSlashPrice: Boolean = false,
        @SerializedName("is_update_price")
        val isUpdatePrice: Boolean = false,
        @SerializedName("is_wishlisted")
        val isWishlisted: Boolean = false,
        @SerializedName("last_update_price")
        val lastUpdatePrice: Long = 0,
        @SerializedName("parent_id")
        val parentId: Int = 0,
        @SerializedName("price_changes")
        val priceChanges: PriceChanges = PriceChanges(),
        @SerializedName("product_alert_message")
        val productAlertMessage: String = "",
        @SerializedName("product_alias")
        val productAlias: String = "",
        @SerializedName("product_all_images")
        val productAllImages: String = "",
        @SerializedName("product_cashback")
        val productCashback: String = "",
        @SerializedName("product_cashback_value")
        val productCashbackValue: Long = 0,
        @SerializedName("product_condition")
        val productCondition: Int = 0,
        @SerializedName("product_finsurance")
        val productFinsurance: Int = 0,
        @SerializedName("product_id")
        val productId: String = "",
        @SerializedName("product_image")
        val productImage: ProductImage = ProductImage(),
        @SerializedName("product_information")
        val productInformation: List<String> = emptyList(),
        @SerializedName("product_invenage_value")
        val productInvenageValue: Int = 0,
        @SerializedName("product_max_order")
        val productMaxOrder: Int = 0,
        @SerializedName("product_min_order")
        val productMinOrder: Int = 0,
        @SerializedName("product_name")
        val productName: String = "",
        @SerializedName("product_notes")
        val productNotes: String = "",
        @SerializedName("product_original_price")
        val productOriginalPrice: Long = 0,
        @SerializedName("product_price")
        val productPrice: Long = 0,
        @SerializedName("product_price_currency")
        val productPriceCurrency: Int = 0,
        @SerializedName("product_price_fmt")
        val productPriceFmt: String = "",
        @SerializedName("product_price_original_fmt")
        val productPriceOriginalFmt: String = "",
        @SerializedName("product_quantity")
        val productQuantity: Int = 0,
        @SerializedName("product_rating")
        val productRating: Int = 0,
        @SerializedName("product_returnable")
        val productReturnable: Int = 0,
        @SerializedName("product_shop_id")
        val productShopId: String = "",
        @SerializedName("product_showcase")
        val productShowcase: ProductShowcase = ProductShowcase(),
        @SerializedName("product_status")
        val productStatus: Int = 0,
        @SerializedName("product_switch_invenage")
        val productSwitchInvenage: Int = 0,
        @SerializedName("product_tracker_data")
        val productTrackerData: ProductTrackerData = ProductTrackerData(),
        @SerializedName("product_url")
        val productUrl: String = "",
        @SerializedName("product_warning_message")
        val productWarningMessage: String = "",
        @SerializedName("product_weight")
        val productWeight: Int = 0,
        @SerializedName("product_weight_fmt")
        val productWeightFmt: String = "",
        @SerializedName("product_weight_unit_code")
        val productWeightUnitCode: Int = 0,
        @SerializedName("product_weight_unit_text")
        val productWeightUnitText: String = "",
        @SerializedName("sku")
        val sku: String = "",
        @SerializedName("slash_price_label")
        val slashPriceLabel: String = "",
        @SerializedName("variant")
        val variant: Variant = Variant(),
        @SerializedName("variant_description_detail")
        val variantDescriptionDetail: VariantDescriptionDetail = VariantDescriptionDetail(),
        @SerializedName("warehouse_id")
        val warehouseId: String = "",
        @SerializedName("wholesale_price")
        val wholesalePrice: List<WholesalePrice> = emptyList()
)