package com.tokopedia.product.detail.data.model.navbar

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NavBar(
    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("items")
    @Expose
    val items: List<NavBarComponent> = emptyList()
)

data class NavBarComponent(
    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("componentName")
    @Expose
    val componentName: String = ""
) {
    fun getComponentViewId(): Int {
//        return when (componentName) {
//            ProductDetailConstant.MINI_SOCIAL_PROOF -> R.id.pdp_layout_social_proof
//            ProductDetailConstant.MINI_SOCIAL_PROOF_STOCK -> R.id.pdp_layout_social_proof_stock
//            ProductDetailConstant.PRODUCT_CONTENT -> R.id.pdp_layout_product_content
//            ProductDetailConstant.DISCUSSION_FAQ -> R.id.pdp_layout_discussion_faq
//            ProductDetailConstant.REVIEW -> R.id.pdp_layout_review
//            ProductDetailConstant.FINTECH_WIDGET_NAME -> R.id.pdpBasicFintechWidget
//            ProductDetailConstant.TRADE_IN -> R.id.general_info_container
//            ProductDetailConstant.PRODUCT_WHOLESALE_INFO -> R.id.general_info_container
//            ProductDetailConstant.PRODUCT_INSTALLMENT_INFO -> R.id.general_info_container
//            ProductDetailConstant.PRODUCT_INSTALLMENT_PAYLATER_INFO -> R.id.pdp_layout_product_installment_paylater_info
//            ProductDetailConstant.ORDER_PRIORITY -> R.id.pdp_layout_order_priority
//            ProductDetailConstant.PRODUCT_PROTECTION -> R.id.pdp_layout_product_protection
//            ProductDetailConstant.PRODUCT_FULLFILMENT -> R.id.pdp_layout_product_fullfilment
//            ProductDetailConstant.VARIANT_OPTIONS -> R.id.pdp_layout_variant_options
//            ProductDetailConstant.MINI_VARIANT_OPTIONS -> R.id.pdp_layout_mini_variant_options
//            ProductDetailConstant.UPCOMING_DEALS -> R.id.pdp_layout_upcoming_deals
//            ProductDetailConstant.MEDIA -> R.id.pdp_layout_media
//            ProductDetailConstant.TICKER_INFO -> R.id.pdp_layout_ticker_info
//            ProductDetailConstant.PRODUCT_SHOP_CREDIBILITY -> R.id.pdp_layout_product_shop_credibility
//            ProductDetailConstant.MINI_SHOP_WIDGET -> R.id.pdp_layout_mini_shop_widget
//            ProductDetailConstant.KEY_TOP_ADS -> R.id.pdp_layout_key_top_ads
//            ProductDetailConstant.SHIPMENT -> R.id.pdp_layout_shipment
//            ProductDetailConstant.SHIPMENT_V2 -> R.id.pdp_layout_shipment_v2
//            ProductDetailConstant.MVC -> R.id.pdp_layout_mvc
//            ProductDetailConstant.BEST_SELLER -> R.id.pdp_layout_best_seller
//            ProductDetailConstant.STOCK_ASSURANCE -> R.id.pdp_layout_stock_assurance
//            ProductDetailConstant.PRODUCT_DETAIL -> R.id.pdp_layout_product_detail
//            ProductDetailConstant.PRODUCT_BUNDLING -> R.id.pdp_layout_product_bundling
//            ProductDetailConstant.SHOPADS_CAROUSEL -> R.id.pdp_layout_shopads_carousel
//            ProductDetailConstant.PLAY_CAROUSEL -> R.id.pdp_layout_play_carousel
//            else -> -1
//        }
        return 0
    }
}