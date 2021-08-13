package com.tokopedia.applink.sellermigration

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@StringDef(value = [
    SellerMigrationFeatureName.FEATURE_TEMPLATE_CHAT,
    SellerMigrationFeatureName.FEATURE_MULTI_EDIT,
    SellerMigrationFeatureName.FEATURE_TOPADS,
    SellerMigrationFeatureName.FEATURE_SET_CASHBACK,
    SellerMigrationFeatureName.FEATURE_FEATURED_PRODUCT,
    SellerMigrationFeatureName.FEATURE_STOCK_REMINDER,
    SellerMigrationFeatureName.FEATURE_SHOP_CASHBACK_VOUCHER,
    SellerMigrationFeatureName.FEATURE_SET_VARIANT,
    SellerMigrationFeatureName.FEATURE_EDIT_PRODUCT_CASHBACK,
    SellerMigrationFeatureName.FEATURE_INSTAGRAM_IMPORT,
    SellerMigrationFeatureName.FEATURE_ADS,
    SellerMigrationFeatureName.FEATURE_ADS_DETAIL,
    SellerMigrationFeatureName.FEATURE_REVIEW_TEMPLATE_AND_STATISTICS,
    SellerMigrationFeatureName.FEATURE_SHOP_INSIGHT,
    SellerMigrationFeatureName.FEATURE_MARKET_INSIGHT,
    SellerMigrationFeatureName.FEATURE_SELLER_CHAT,
    SellerMigrationFeatureName.FEATURE_POST_FEED,
    SellerMigrationFeatureName.FEATURE_BALANCE,
    SellerMigrationFeatureName.FEATURE_CHAT_SETTING,
    SellerMigrationFeatureName.FEATURE_PLAY_FEED,
    SellerMigrationFeatureName.FEATURE_FINANCIAL_SERVICES,
    SellerMigrationFeatureName.FEATURE_BROADCAST_CHAT,
    SellerMigrationFeatureName.FEATURE_DISCUSSION
])
annotation class SellerMigrationFeatureName {
    companion object {
        const val FEATURE_TEMPLATE_CHAT = "template_chat"
        const val FEATURE_MULTI_EDIT = "multi_edit"
        const val FEATURE_TOPADS = "topads"
        const val FEATURE_SET_CASHBACK = "set_cashback"
        const val FEATURE_FEATURED_PRODUCT = "featured_product"
        const val FEATURE_STOCK_REMINDER = "stock_reminder"
        const val FEATURE_SHOP_CASHBACK_VOUCHER = "shop_cashback_voucher"
        const val FEATURE_SET_VARIANT = "set_variant"
        const val FEATURE_EDIT_PRODUCT_CASHBACK = "edit_product_cashback"
        const val FEATURE_INSTAGRAM_IMPORT = "instagram_import"
        const val FEATURE_ADS = "ads"
        const val FEATURE_ADS_DETAIL = "ads_detail"
        const val FEATURE_REVIEW_TEMPLATE_AND_STATISTICS = "review_template_and_statistics"
        const val FEATURE_SHOP_INSIGHT = "shop_insight"
        const val FEATURE_MARKET_INSIGHT = "market_insight"
        const val FEATURE_SELLER_CHAT = "seller_chat"
        const val FEATURE_POST_FEED = "post_feed"
        const val FEATURE_BALANCE = "balance"
        const val FEATURE_CHAT_SETTING = "chat_setting"
        const val FEATURE_PLAY_FEED = "play_feed"
        const val FEATURE_FINANCIAL_SERVICES = "financial_services"
        const val FEATURE_BROADCAST_CHAT = "broadcast_chat"
        const val FEATURE_CENTRALIZED_PROMO = "centralized_promo"
        const val FEATURE_DISCUSSION = "discussion"
    }
}