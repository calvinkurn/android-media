package com.tokopedia.shop.common.constant

/**
 * Created by hendry on 18/07/18.
 */
object ShopPageConstant {
    const val START_PAGE = 1
    const val DEFAULT_PER_PAGE_NON_TABLET = 10
    const val DEFAULT_PER_PAGE_TABLET = 20
    const val DEFAULT_PER_FOR_SHARE_PURPOSE = 6
    const val SHOP_PRODUCT_EMPTY_STATE_LIMIT = 20
    const val ETALASE_HIGHLIGHT_COUNT = 5
    const val SHOP_SHARE_DEFAULT_CHANNEL = "default"
    const val SHOP_SHARE_OTHERS_CHANNEL = "others"
    const val SHOP_SHARE_GQL_TRACKER_ACTION = "ShopSharing"
    const val SHOP_SHARE_GQL_TRACKER_SOURCE = "shop-page"
    const val DEFAULT_VALUE_ETALASE_TYPE = 0 // It's only for apps or frontend identifier
    const val ATC_SUCCESS_VALUE = 1
    const val VALUE_INT_ONE = 1
    const val SOURCE = "shop"

    // if the count data <= SMALL_DATA_LIMIT, the data become vertical list
    const val GO_TO_MEMBERSHIP_DETAIL = "membership detail"
    const val EMPTY_PRODUCT_SEARCH_IMAGE_URL =
        "https://images.tokopedia.net/android/others/illustration_product_empty.png"
    const val KEY_MEMBERSHIP_DATA_MODEL = "KEY_MEMBERSHIP_DATA_MODEL_POSITION"
    const val KEY_MERCHANT_VOUCHER_DATA_MODEL = "KEY_MERCHANT_VOUCHER_DATA_MODEL_POSITION"
    const val KEY_FEATURED_PRODUCT_DATA_MODEL = "KEY_FEATURED_PRODUCT_DATA_MODEL_POSITION"
    const val KEY_ETALASE_HIGHLIGHT_DATA_MODEL = "KEY_ETALASE_HIGHLIGHT_DATA_MODEL_POSITION"
    const val KEY_SORT_FILTER_DATA_MODEL = "KEY_ETALASE_DATA_MODEL"
    const val KEY_ETALASE_TITLE_DATA_MODEL = "KEY_ETALASE_TITLE_DATA_MODEL_POSITION"
    const val KEY_SHOP_PRODUCT_FIRST_DATA_MODEL = "KEY_SHOP_PRODUCT_FIRST_DATA_MODEL"
    const val KEY_SHOP_PRODUCT_ADD_DATA_MODEL = "KEY_SHOP_PRODUCT_ADD_DATA_MODEL"
    const val KEY_SHOP_SELLER_EMPTY_PRODUCT_ALL_ETALASE_DATA_MODEL =
        "KEY_SHOP_SELLER_EMPTY_PRODUCT_ALL_ETALASE_DATA_MODEL"
    const val KEY_SHOP_BUYER_EMPTY_PRODUCT_DATA_MODEL = "KEY_SHOP_BUYER_EMPTY_PRODUCT_DATA_MODEL"
    const val URL_IMAGE_SELLER_PRODUCT_ALL_ETALASE_EMPTY_STATE_BACKGROUND =
        "https://images.tokopedia.net/android/shop_page/seller_shop_product_empty_background.png"
    const val URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE =
        "https://images.tokopedia.net/android/shop_page/image_product_empty_state_buyer.png"
    const val URL_IMAGE_BUYER_SHOP_SEARCH_EMPTY_STATE =
        "https://images.tokopedia.net/android/empty_state_search_filter.png"
    const val ALL_SHOWCASE_ID = "etalase"
    const val CODE_STATUS_SUCCESS = "200"
    const val SHARED_PREF_AFFILIATE_CHANNEL = "SHARED_PREF_AFFILIATE_CHANNEL"
    const val SHOP_PAGE_SHARED_PREFERENCE = "SHOP_PAGE_PREFERENCE"
    const val SHOP_COMPARISON_WIDGET_PAGE_NAME = "shop_comparison"
    const val HACHIKO_VOUCHER_GRAPHQL_API_VERSION = "2.0.0"
    const val LABEL_TITLE_PRODUCT_SOLD_COUNT = "terjual"
    const val FORMAT_CONVERT_PERCENTAGE_TO_HEX = "%02X"
    const val SHARED_PREF_NAME = "SHARED_PREF_SHOP_PAGE_MOCK_WIDGET"
    const val SHARED_PREF_MOCK_LOTTIE_URL_DATA = "SHARED_PREF_MOCK_LOTTIE_URL_DATA"
    object ShopLayoutFeatures {
        const val DIRECT_PURCHASE = "direct_purchase"
    }

    object ShopProductCardAtc {
        const val CARD_HOME = "Card Home"
        const val CARD_PRODUCT = "Card Product"
        const val CARD_ETALASE = "Card Etalase"
    }

    object BundleType {
        const val SINGLE_BUNDLE = "Single Bundle"
        const val MULTIPLE_BUNDLE = "Multiple Bundle"
    }

    object ShopTickerType {
        const val WARNING = "warning"
        const val INFO = "info"
        const val DANGER = "danger"
    }

    object ShopTierType {
        const val NA = -1
        const val REGULAR_MERCHANT = 0
        const val POWER_MERCHANT = 1
        const val OFFICIAL_STORE = 2
        const val POWER_MERCHANT_PRO = 3
    }

    object RequestParamValue {
        const val PAGE_NAME_SHOP_COMPARISON_WIDGET = "shop_comparison"
    }

    object ShopTabActiveStatus {
        const val ACTIVE = 1
        const val INACTIVE = 0
    }

    /**
     * This is the constant for shop page performance improvement history
     */
    @Suppress("unused")
    object ShopPageFeatureImprovementType {
        const val V3_IMPROVEMENT = "V3" // Faster shop header using more slimmer P1 network call
        const val V4_REIMAGINED = "V4" // Shop Page Reimagined
        const val V4_WITH_OPTIMIZED_P1 = "V4.1" // Shop Page Reimagined with more optimized P1
        const val V4_2 = "V4.2" // Shop Page Reimagined with more optimized P1 by removeing getShopProduct on P1
        const val V4_3 = "V4.3" // Shop Page Reimagined with more optimized P1 by caching GetShopPageHeaderLayoutUseCase
        const val PREFETCH_V1_0 = "prefetch_V1.0" // Shop Page with prefetch data.
    }
}
