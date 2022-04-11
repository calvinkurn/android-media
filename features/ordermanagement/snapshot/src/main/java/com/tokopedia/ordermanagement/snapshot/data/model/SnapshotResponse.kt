package com.tokopedia.ordermanagement.snapshot.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
    @Expose
    @field:SerializedName("get_order_snapshot")
    val getOrderSnapshot: GetOrderSnapshot = GetOrderSnapshot()
)

data class GetOrderSnapshot(
    @Expose
    @field:SerializedName("bundle_id")
    val bundleId: String = "",

    @Expose
    @field:SerializedName("bundle_variant_name")
    val bundleName: String = "",

    @Expose
    @field:SerializedName("product_bundling_icon")
    val bundleIcon: String = "",

    @Expose
    @field:SerializedName("is_have_bundle_product")
    val isBundleProduct: Boolean = false,

    @Expose
    @field:SerializedName("product_price_formatted")
    val productPriceFormatted: String = "",

    @Expose
    @field:SerializedName("shop_image_primary_url")
    val shopImagePrimaryUrl: String = "",

    @Expose
    @field:SerializedName("product_url")
    var productUrl: String = "",

    @Expose
    @field:SerializedName("product_total_weight_formatted")
    val productTotalWeightFormatted: String = "",

    @Expose
    @field:SerializedName("is_os")
    val isOs: Boolean = false,

    @Expose
    @field:SerializedName("shop_summary")
    val shopSummary: ShopSummary = ShopSummary(),

    @Expose
    @field:SerializedName("pre_order")
    val preOrder: Boolean = false,

    @Expose
    @field:SerializedName("product_additional_data")
    val productAdditionalData: ProductAdditionalData = ProductAdditionalData(),

    @Expose
    @field:SerializedName("order_detail")
    val orderDetail: OrderDetail = OrderDetail(),

    @Expose
    @field:SerializedName("is_pm")
    val isPm: Boolean = false,

    @Expose
    @field:SerializedName("product_weight_formatted")
    val productWeightFormatted: String = "",

    @Expose
    @field:SerializedName("pre_order_duration")
    val preOrderDuration: String = "",

    @Expose
    @field:SerializedName("product_image_secondary")
    val productImageSecondary: List<ProductImageSecondaryItem> = listOf(),

    @Expose
    @field:SerializedName("campaign_data")
    val campaignData: CampaignData = CampaignData()
)

data class ShopSummary(
    @Expose
    @field:SerializedName("shop_id")
    val shopId: String = "",

    @Expose
    @field:SerializedName("user_id")
    val userId: String = "",

    @Expose
    @field:SerializedName("shop_domain")
    val shopDomain: String = "",

    @Expose
    @field:SerializedName("logo")
    val logo: String = "",

    @Expose
    @field:SerializedName("shop_name")
    val shopName: String = "",

    @Expose
    @field:SerializedName("badge_url")
    val badgeUrl: String = ""
)

data class ProductAdditionalData(
    @Expose
    @field:SerializedName("create_time")
    val createTime: String = "",

    @Expose
    @field:SerializedName("product_price")
    val productPrice: String = ""
)

data class OrderDetail(
    @Expose
    @field:SerializedName("subtotal_price")
    val subtotalPrice: Double = 0.0,

    @Expose
    @field:SerializedName("quantity")
    val quantity: String = "",

    @Expose
    @field:SerializedName("notes")
    val notes: String = "",

    @Expose
    @field:SerializedName("create_time")
    val createTime: String = "",

    @Expose
    @field:SerializedName("product_weight")
    val productWeight: Double = 0.0,

    @Expose
    @field:SerializedName("quantity_reject")
    val quantityReject: String = "",

    @Expose
    @field:SerializedName("finsurance")
    val finsurance: String = "",

    @Expose
    @field:SerializedName("product_price")
    val productPrice: Double = 0.0,

    @Expose
    @field:SerializedName("product_name")
    val productName: String = "",

    @Expose
    @field:SerializedName("total_weight")
    val totalWeight: Double = 0.0,

    @Expose
    @field:SerializedName("product_desc")
    val productDesc: String = "",

    @Expose
    @field:SerializedName("order_dtl_id")
    val orderDtlId: String = "",

    @Expose
    @field:SerializedName("condition")
    val condition: String = "",

    @Expose
    @field:SerializedName("returnable")
    val returnable: String = "",

    @Expose
    @field:SerializedName("insurance_price")
    val insurancePrice: Double = 0.0,

    @Expose
    @field:SerializedName("normal_price")
    val normalPrice: Double = 0.0,

    @Expose
    @field:SerializedName("product_id")
    val productId: String = "",

    @Expose
    @field:SerializedName("quantity_deliver")
    val quantityDeliver: String = "",

    @Expose
    @field:SerializedName("child_cat_id")
    val childCatId: String = "",

    @Expose
    @field:SerializedName("min_order")
    val minOrder: String = "",

    @Expose
    @field:SerializedName("order_id")
    val orderId: String = "",

    @Expose
    @field:SerializedName("currency_id")
    val currencyId: String = "",

    @Expose
    @field:SerializedName("must_insurance")
    val mustInsurance: String = ""
)

data class ProductImageSecondaryItem(
    @Expose
    @field:SerializedName("file_path")
    val filePath: String = "",

    @Expose
    @field:SerializedName("image_url")
    val imageUrl: String = "",

    @Expose
    @field:SerializedName("file_name")
    val fileName: String = ""
)

data class CampaignData(
    @Expose
    @field:SerializedName("product_id")
    val productId: String = "",

    @Expose
    @field:SerializedName("campaign")
    val campaign: Campaign = Campaign()
)

data class Campaign(
    @Expose
    @field:SerializedName("discount_percentage_text_color")
    val discountPercentageTextColor: String = "",

    @Expose
    @field:SerializedName("discount_percentage_text")
    val discountPercentageText: String = "",

    @Expose
    @field:SerializedName("original_price")
    val originalPrice: String = "",

    @Expose
    @field:SerializedName("original_price_fmt")
    val originalPriceFmt: String = "",

    @Expose
    @field:SerializedName("discount_percentage_label_color")
    val discountPercentageLabelColor: String = "",

    @Expose
    @field:SerializedName("discounted_price")
    val discountedPrice: String = ""
)