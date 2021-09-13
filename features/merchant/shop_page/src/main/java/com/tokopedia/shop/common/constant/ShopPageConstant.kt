package com.tokopedia.shop.common.constant

/**
 * Created by hendry on 18/07/18.
 */
object ShopPageConstant {
    const val ETALASE_TO_SHOW = 5
    const val MAXIMUM_SELECTED_ETALASE_LIST = 4
    const val START_PAGE = 1
    const val DEFAULT_PER_PAGE = 10
    const val SHOP_PRODUCT_EMPTY_STATE_LIMIT = 20
    const val ETALASE_HIGHLIGHT_COUNT = 5
    const val SHOP_SHARE_DEFAULT_CHANNEL = "default"
    const val SHOP_SHARE_OTHERS_CHANNEL = "others"
    const val SHOP_SHARE_GQL_TRACKER_ACTION = "ShopSharing"
    const val SHOP_SHARE_GQL_TRACKER_SOURCE = "shop-page"
    const val DEFAULT_VALUE_ETALASE_TYPE = 0 // It's only for apps or frontend identifier

    // if the count data <= SMALL_DATA_LIMIT, the data become vertical list
    const val GO_TO_MEMBERSHIP_DETAIL = "membership detail"
    const val EMPTY_PRODUCT_SEARCH_IMAGE_URL = "https://ecs7.tokopedia.net/android/others/illustration_product_empty.png"
    const val KEY_MEMBERSHIP_DATA_MODEL = "KEY_MEMBERSHIP_DATA_MODEL_POSITION"
    const val KEY_MERCHANT_VOUCHER_DATA_MODEL = "KEY_MERCHANT_VOUCHER_DATA_MODEL_POSITION"
    const val KEY_FEATURED_PRODUCT_DATA_MODEL = "KEY_FEATURED_PRODUCT_DATA_MODEL_POSITION"
    const val KEY_ETALASE_HIGHLIGHT_DATA_MODEL = "KEY_ETALASE_HIGHLIGHT_DATA_MODEL_POSITION"
    const val KEY_SORT_FILTER_DATA_MODEL = "KEY_ETALASE_DATA_MODEL"
    const val KEY_ETALASE_TITLE_DATA_MODEL = "KEY_ETALASE_TITLE_DATA_MODEL_POSITION"
    const val KEY_SHOP_PRODUCT_FIRST_DATA_MODEL = "KEY_SHOP_PRODUCT_FIRST_DATA_MODEL"
    const val KEY_SHOP_PRODUCT_ADD_DATA_MODEL = "KEY_SHOP_PRODUCT_ADD_DATA_MODEL"
    const val KEY_SHOP_SELLER_EMPTY_PRODUCT_ALL_ETALASE_DATA_MODEL = "KEY_SHOP_SELLER_EMPTY_PRODUCT_ALL_ETALASE_DATA_MODEL"
    const val KEY_SHOP_BUYER_EMPTY_PRODUCT_DATA_MODEL = "KEY_SHOP_BUYER_EMPTY_PRODUCT_DATA_MODEL"
    const val URL_IMAGE_SELLER_PRODUCT_ALL_ETALASE_EMPTY_STATE_BACKGROUND = "https://ecs7.tokopedia.net/android/shop_page/seller_shop_product_empty_background.png"
    const val URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE = "https://ecs7.tokopedia.net/android/shop_page/image_product_empty_state_buyer.png"
    const val URL_IMAGE_BUYER_SHOP_SEARCH_EMPTY_STATE = "https://ecs7.tokopedia.net/android/empty_state_search_filter.png"
    const val ENABLE_SHOP_PAGE_HEADER_CHOOSE_ADDRESS_WIDGET = "android_shop_page_enable_choose_address_widget_on_shop_page_header"
    const val ENABLE_SHOP_PAGE_UNIVERSAL_BOTTOM_SHEET = "android_shop_page_enable_universal_bottom_sheet"
}