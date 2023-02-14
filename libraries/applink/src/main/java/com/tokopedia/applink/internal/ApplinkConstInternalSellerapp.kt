package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://sellerapp".
 * Order by name
 * Only create "tokopedia-android-internal://" if this deeplink is used only for android app, and not shared to iOs and web.
 * If the deeplink is shared between iOS and web, it should use "tokopedia://" scheme.
 */
object ApplinkConstInternalSellerapp {

    const val HOST_SELLERAPP = "sellerapp"


    const val INTERNAL_SELLERAPP = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_SELLERAPP}"

    const val EXTERNAL_SELLERAPP = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://${HOST_SELLERAPP}"

    //SellerHomeActivity
    const val SELLER_HOME = "$INTERNAL_SELLERAPP/sellerhome"

    //InitialSearchActivity
    const val SELLER_SEARCH = "$INTERNAL_SELLERAPP/seller-search"

    const val SELLER_HOME_PRODUCT_MANAGE_LIST = "$INTERNAL_SELLERAPP/sellerhome-product-list"

    const val SELLER_HOME_CHAT = "$INTERNAL_SELLERAPP/sellerhome-chat"

    const val SELLER_HOME_SOM_ALL = "$INTERNAL_SELLERAPP/sellerhome-som-allorder"

    const val SELLER_HOME_SOM_NEW_ORDER = "$INTERNAL_SELLERAPP/sellerhome-som-neworder"

    const val SELLER_HOME_SOM_READY_TO_SHIP = "$INTERNAL_SELLERAPP/sellerhome-som-readytoship"

    const val SELLER_HOME_SOM_SHIPPED = "$INTERNAL_SELLERAPP/sellerhome-som-inshipping"

    const val SELLER_HOME_SOM_DONE = "$INTERNAL_SELLERAPP/sellerhome-som-done"

    const val SELLER_HOME_SOM_CANCELLED = "$INTERNAL_SELLERAPP/sellerhome-som-cancelled"

    const val SELLER_HOME_SOM_CANCELLATION_REQUEST = "$INTERNAL_SELLERAPP/sellerhome-som-cancellation-request"

    //SellerOnboardingActivity
    const val WELCOME = "$INTERNAL_SELLERAPP/welcome"

    //CentralizedPromoActivity
    const val CENTRALIZED_PROMO = "$INTERNAL_SELLERAPP/centralized-promo"

    //FirstVoucherActivity
    const val CENTRALIZED_PROMO_FIRST_TIME = "$INTERNAL_SELLERAPP/first-time-centralized-promo"

    //MenuSettingActivity
    const val MENU_SETTING = "$INTERNAL_SELLERAPP/menu-setting"

    //SellerSeamlessLoginFragment
    const val SEAMLESS_CHOOSE_ACCOUNT = "$INTERNAL_SELLERAPP/login-seamless-choose-account"

    //CreateMerchantVoucherStepsActivity
    const val CREATE_VOUCHER = "$INTERNAL_SELLERAPP/create-voucher"
    const val CREATE_VOUCHER_PRODUCT = "$INTERNAL_SELLERAPP/create-voucher-product/{product_id}/"

    //VoucherListActivity
    const val VOUCHER_LIST = "$INTERNAL_SELLERAPP/voucher-list"
    const val VOUCHER_ACTIVE = "$INTERNAL_SELLERAPP/voucher-list/active"
    const val VOUCHER_HISTORY = "$INTERNAL_SELLERAPP/voucher-list/history"
    const val VOUCHER_PRODUCT_LIST = "$INTERNAL_SELLERAPP/voucher-product-list/{mode}/"

    //VoucherDetailActivity
    const val VOUCHER_DETAIL = "$INTERNAL_SELLERAPP/voucher-detail"
    const val VOUCHER_PRODUCT_DETAIL = "$INTERNAL_SELLERAPP/voucher-product-detail/{voucher_id}/"

    // Seller Shop Flash Sale - CampaignListActivity
    const val SELLER_SHOP_FLASH_SALE = "$INTERNAL_SELLERAPP/shop-flash-sale/{filter_mode}/"

    // Seller Tokopedia Flash Sale - CampaignListActivity
    const val SELLER_TOKOPEDIA_FLASH_SALE = "$INTERNAL_SELLERAPP/tokopedia-flash-sale/{tab_menu}/"

    // Seller Tokopedia Flash Sale - CampaignDetailActivity
    const val SELLER_TOKOPEDIA_FLASH_SALE_CAMPAIGN_DETAIL = "$INTERNAL_SELLERAPP/tokopedia-flash-sale/campaign-detail/{campaign_id}/"

    // Seller MVC Creation Intro - MvcIntroActivity
    const val SELLER_MVC_INTRO = "$INTERNAL_SELLERAPP/seller-mvc/intro"
    /**
     * Seller MVC Creation - MvcListActivity
     * This applink will redirect to voucher list page with following filter status at the suffix
     * here is the suffix definition:
     * - active: will display active vouchers, which is upcoming and ongoing vouchers
     * - history: will display inactive vouchers, which is canceled and ended vouchers
     * - ongoing: will display ongoing voucher only
     * - upcoming: will display upcoming voucher only
     */
    const val SELLER_MVC_LIST = "$INTERNAL_SELLERAPP/seller-mvc/list/{voucher_status}/"
    const val SELLER_MVC_LIST_ACTIVE = "$INTERNAL_SELLERAPP/seller-mvc/list/active/"
    const val SELLER_MVC_LIST_HISTORY = "$INTERNAL_SELLERAPP/seller-mvc/list/history/"
    const val SELLER_MVC_LIST_ONGOING = "$INTERNAL_SELLERAPP/seller-mvc/list/ongoing/"
    const val SELLER_MVC_LIST_UPCOMING = "$INTERNAL_SELLERAPP/seller-mvc/list/upcoming/"
    //{voucher_type} value -> shop, product
    const val SELLER_MVC_CREATE = "$INTERNAL_SELLERAPP/seller-mvc/create/{voucher_type}/"

    const val SELLER_MVC_DETAIL = "$INTERNAL_SELLERAPP/seller-mvc/detail/{voucher_id}/"

    // CampaignListActivity
    const val CAMPAIGN_LIST = "$INTERNAL_SELLERAPP/campaign-list"

    //SellerMenuActivity
    const val SELLER_MENU = "$INTERNAL_SELLERAPP/seller-menu"

    // SellerSettingsActivity
    const val SELLER_SETTINGS = "$INTERNAL_SELLERAPP/seller-settings"

    //AdminRoleAuthorizeActivity
    const val ADMIN_AUTHORIZE = "$INTERNAL_SELLERAPP/admin-authorize/{feature}/"

    //SellerFeedbackActivity
    const val SELLER_FEEDBACK = "$INTERNAL_SELLERAPP/seller-feedback"

    const val REVIEW_REMINDER = "$INTERNAL_SELLERAPP/review-reminder"
    //AdminRestrictionActivity
    const val ADMIN_RESTRICTION = "$INTERNAL_SELLERAPP/admin-restriction"
    const val PARAM_ARTICLE_URL = "article_url"

    //ShopDiscount
    const val SHOP_DISCOUNT = "$INTERNAL_SELLERAPP/shop-discount"
    const val SHOP_DISCOUNT_MANAGE_DISCOUNT = "$INTERNAL_SELLERAPP/shop-discount/manage-discount"
    const val SHOP_DISCOUNT_MANAGE_PRODUCT_DISCOUNT = "$INTERNAL_SELLERAPP/shop-discount/manage-discount/product"
    const val SHOP_DISCOUNT_MANAGE_PRODUCT_VARIANT_DISCOUNT = "$INTERNAL_SELLERAPP/shop-discount/manage-discount/product/variant"
    const val SHOP_DISCOUNT_MANAGE_PRODUCT_MULTI_LOC_DISCOUNT = "$INTERNAL_SELLERAPP/shop-discount/manage-discount/product/multi-loc"

    //TokomemberMainActivity
    const val TOKOMEMBER = "$INTERNAL_SELLERAPP/tokomember"
    const val TOKOMEMBER_PROGRAM_LIST = "$TOKOMEMBER/program-list"
    const val TOKOMEMBER_COUPON_LIST = "$TOKOMEMBER/coupon-list"
    const val TOKOMEMBER_COUPON_DETAIL = "$TOKOMEMBER/coupon-detail/{coupon_id}"

    //TmDashCreateActivity if registered
    const val TOKOMEMBER_PROGRAM_CREATION = "$TOKOMEMBER/program-creation"
    const val TOKOMEMBER_COUPON_CREATION = "$INTERNAL_SELLERAPP/tokomember/coupon-creation"
    const val TOKOMEMBER_PROGRAM_EXTENSION = "$TOKOMEMBER/program-extension/{program_id}"

    //TokomemberMemberListActivity
    const val INTERNAL_MEMBER_LIST = "$TOKOMEMBER/member-list"
}
