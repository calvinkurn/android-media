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
    SellerMigrationFeatureName.FEATURE_INSTAGRAM_IMPORT,
    SellerMigrationFeatureName.FEATURE_ADS,
    SellerMigrationFeatureName.FEATURE_ADS_DETAIL,
    SellerMigrationFeatureName.FEATURE_REVIEW_TEMPLATE_AND_STATISTICS,
    SellerMigrationFeatureName.FEATURE_SHOP_INSIGHT,
    SellerMigrationFeatureName.FEATURE_MARKET_INSIGHT
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
    }
}