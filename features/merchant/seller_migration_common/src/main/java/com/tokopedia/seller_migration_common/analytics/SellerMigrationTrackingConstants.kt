package com.tokopedia.seller_migration_common.analytics

import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName

object SellerMigrationTrackingConstants {
    private const val EVENT_CLICK_GO_TO_SELLER_APP = "click go to seller app"
    private const val EVENT_CLICK_LEARN_MORE = "click learn more"
    const val USER_ID_VALUE = "%s"
    const val TRACKING_EVENT = "event"
    const val TRACKING_EVENT_CATEGORY = "eventCategory"
    const val TRACKING_EVENT_ACTION = "eventAction"
    const val TRACKING_EVENT_LABEL = "eventLabel"
    const val TRACKING_USER_ID = "userId"
    const val EVENT_CLICK_SELLER_MIGRATION = "clickSellerMigration"
    const val EVENT_CATEGORY_SELLER_MIGRATION = "seller app migration"
    const val EVENT_CATEGORY_MIGRATION_PAGE = "migration page"
    const val EVENT_CLICK_LEARN_MORE_VOUCHER = "$EVENT_CLICK_LEARN_MORE - mvc sheet"
    const val EVENT_CLICK_VOUCHER_BOTTOM_SHEET = "click voucher toko cashback"
    const val EVENT_CLICK_GO_TO_SELLER_APP_ACCOUNT = "$EVENT_CLICK_GO_TO_SELLER_APP - store tab"
    const val EVENT_CLICK_GO_TO_SELLER_APP_CHAT = "$EVENT_CLICK_GO_TO_SELLER_APP - chat page"
    const val EVENT_CLICK_GO_TO_SELLER_APP_PRODUCT = "$EVENT_CLICK_GO_TO_SELLER_APP - product page"
    const val EVENT_CLICK_GO_TO_SELLER_APP_REVIEW = "$EVENT_CLICK_GO_TO_SELLER_APP - review page"
    const val EVENT_CLICK_GO_TO_SELLER_APP_VOUCHER = "$EVENT_CLICK_GO_TO_SELLER_APP - mvc sheet"
    const val EVENT_CLICK_GO_TO_SELLER_APP_POST_FEED = "$EVENT_CLICK_GO_TO_SELLER_APP - post feed page"
    const val EVENT_CLICK_GO_TO_SELLER_APP_TOPADS = "$EVENT_CLICK_GO_TO_SELLER_APP - topads page"
    const val EVENT_CLICK_GO_TO_SELLER_APP_BALANCE = "$EVENT_CLICK_GO_TO_SELLER_APP - balance page"
    const val EVENT_LABEL_TO_APP_STORE = "playstore"
    const val EVENT_LABEL_TO_SELLER_APP = "seller app"
    const val KEY_CUSTOM_DIMENSION_CURRENT_SITE = "currentSite"
    const val VALUE_CUSTOM_DIMENSION_CURRENT_SITE = "tokopediaseller"
    const val KEY_CUSTOM_DIMENSION_BUSINESS_UNIT = "businessUnit"
    const val VALUE_CUSTOM_DIMENSION_BUSINESS_UNIT_PG = "physical goods"

    const val EVENT_CLICK_SHOP_ACCOUNT = "clickShopAccount"
    const val EVENT_CONTENT_FEED_SHOP_PAGE = "content feed - shop page"
    const val EVENT_CLICK_TOKOPEDIA_SELLER = "click tokopedia seller"
    const val EVENT_TOKOPEDIA_MARKET_PLACE = "tokopediamarketplace"
    const val EVENT_CLICK_SELLER_NOTIFICATION = "click seller notifications"
    const val VALUE_SETTINGS = "settings"

    val USER_REDIRECTION_EVENT_NAME = mapOf(
            SellerMigrationFeatureName.FEATURE_TEMPLATE_CHAT to "clickInboxChat",
            SellerMigrationFeatureName.FEATURE_MULTI_EDIT to "clickManageProduct",
            SellerMigrationFeatureName.FEATURE_TOPADS to "clickManageProduct",
            SellerMigrationFeatureName.FEATURE_SET_CASHBACK to "clickManageProduct",
            SellerMigrationFeatureName.FEATURE_FEATURED_PRODUCT to "clickManageProduct",
            SellerMigrationFeatureName.FEATURE_STOCK_REMINDER to "clickManageProduct",
            SellerMigrationFeatureName.FEATURE_SET_VARIANT to "clickAddProduct",
            SellerMigrationFeatureName.FEATURE_INSTAGRAM_IMPORT to "clickAddProduct",
            SellerMigrationFeatureName.FEATURE_ADS to "clickPDP",
            SellerMigrationFeatureName.FEATURE_ADS_DETAIL to "clickPDP",
            SellerMigrationFeatureName.FEATURE_REVIEW_TEMPLATE_AND_STATISTICS to "clickReview"
    )

    val USER_REDIRECTION_EVENT_CATEGORY = mapOf(
            SellerMigrationFeatureName.FEATURE_TEMPLATE_CHAT to "inbox-chat",
            SellerMigrationFeatureName.FEATURE_MULTI_EDIT to "product list page",
            SellerMigrationFeatureName.FEATURE_TOPADS to "product list page",
            SellerMigrationFeatureName.FEATURE_SET_CASHBACK to "product list page",
            SellerMigrationFeatureName.FEATURE_FEATURED_PRODUCT to "product list page",
            SellerMigrationFeatureName.FEATURE_STOCK_REMINDER to "product list page",
            SellerMigrationFeatureName.FEATURE_SET_VARIANT to "add product page",
            SellerMigrationFeatureName.FEATURE_INSTAGRAM_IMPORT to "add product page",
            SellerMigrationFeatureName.FEATURE_ADS to "product detail page - seller side",
            SellerMigrationFeatureName.FEATURE_ADS_DETAIL to "product detail page - seller side",
            SellerMigrationFeatureName.FEATURE_REVIEW_TEMPLATE_AND_STATISTICS to "ulasan page"
    )

    val USER_REDIRECTION_EVENT_ACTION = mapOf(
            SellerMigrationFeatureName.FEATURE_TEMPLATE_CHAT to "click on gear icon setting",
            SellerMigrationFeatureName.FEATURE_MULTI_EDIT to "click edit sekaligus - seller migration",
            SellerMigrationFeatureName.FEATURE_TOPADS to "click topads - seller migration",
            SellerMigrationFeatureName.FEATURE_SET_CASHBACK to "click atur cashback - seller migration",
            SellerMigrationFeatureName.FEATURE_FEATURED_PRODUCT to "click produk unggulan - seller migration",
            SellerMigrationFeatureName.FEATURE_STOCK_REMINDER to "click pengingat stok - seller migration",
            SellerMigrationFeatureName.FEATURE_SET_VARIANT to "click atur varian - seller migration",
            SellerMigrationFeatureName.FEATURE_INSTAGRAM_IMPORT to "click instoped - seller migration",
            SellerMigrationFeatureName.FEATURE_ADS to "click ads button",
            SellerMigrationFeatureName.FEATURE_ADS_DETAIL to "click ads button",
            SellerMigrationFeatureName.FEATURE_REVIEW_TEMPLATE_AND_STATISTICS to "click ulasan produk dan template - seller migration"
    )

    val USER_REDIRECTION_EVENT_LABEL = mapOf(
            SellerMigrationFeatureName.FEATURE_TEMPLATE_CHAT to "Template Chat - ",
            SellerMigrationFeatureName.FEATURE_MULTI_EDIT to "",
            SellerMigrationFeatureName.FEATURE_TOPADS to "",
            SellerMigrationFeatureName.FEATURE_SET_CASHBACK to "",
            SellerMigrationFeatureName.FEATURE_FEATURED_PRODUCT to "",
            SellerMigrationFeatureName.FEATURE_STOCK_REMINDER to "",
            SellerMigrationFeatureName.FEATURE_SET_VARIANT to "",
            SellerMigrationFeatureName.FEATURE_INSTAGRAM_IMPORT to "",
            SellerMigrationFeatureName.FEATURE_ADS to "new ads - ",
            SellerMigrationFeatureName.FEATURE_ADS_DETAIL to "detail ads - ",
            SellerMigrationFeatureName.FEATURE_REVIEW_TEMPLATE_AND_STATISTICS to ""
    )

    val USER_REDIRECTION_BUSINESS_UNIT = mapOf(
            SellerMigrationFeatureName.FEATURE_TEMPLATE_CHAT to "physical goods",
            SellerMigrationFeatureName.FEATURE_MULTI_EDIT to "physical goods",
            SellerMigrationFeatureName.FEATURE_TOPADS to "physical goods",
            SellerMigrationFeatureName.FEATURE_SET_CASHBACK to "physical goods",
            SellerMigrationFeatureName.FEATURE_FEATURED_PRODUCT to "physical goods",
            SellerMigrationFeatureName.FEATURE_STOCK_REMINDER to "physical goods",
            SellerMigrationFeatureName.FEATURE_SET_VARIANT to "physical goods",
            SellerMigrationFeatureName.FEATURE_INSTAGRAM_IMPORT to "physical goods",
            SellerMigrationFeatureName.FEATURE_ADS to "",
            SellerMigrationFeatureName.FEATURE_ADS_DETAIL to "",
            SellerMigrationFeatureName.FEATURE_REVIEW_TEMPLATE_AND_STATISTICS to "physical goods"
    )
}