package com.tokopedia.product.manage.common.feature.list.constant

object ProductManageDataLayer {
    private const val CLICK_SETTINGS_OPTION = "click settings option"
    private const val CLICK_ON_TOGGLE_STOCK_REMINDER = "click on toggle stock reminder"
    private const val CLICK_ON_FEATURED_PRODUCT_POP_UP = "click on featured product pop up"
    private const val CLICK_ON_MULTIPLE_SELECT_BULK_SETTINGS = "click on multiple select - bulk settings"
    private const val CLICK_ON_EDIT_STOCK = "click on edit stock"
    private const val CLICK_ON_SORTING_FILTER = "click on sorting filter"
    private const val CLICK_ON_OTHERS_FILTER = "click on others filter"
    private const val CLICK_ON_MORE_OTHERS_FILTER = "click on more others filter"
    private const val VARIANTS = "variants"

    const val EVENT_NAME = "clickManageProduct"
    const val EVENT_CATEGORY = "product list page"
    const val EVENT_NAME_EDIT_PRODUCT = "clickEditProduct"
    const val EVENT_CATEGORY_EDIT_PRODUCT = "edit product page"

    const val EVENT_ACTION_CLICK_ADD = "click add product"
    const val EVENT_ACTION_CLICK_MULTIPLE = "click multiple select"
    const val EVENT_ACTION_CLICK_EDIT_PRICE = "click edit price"
    const val EVENT_ACTION_CLICK_EDIT_STOCK = "click edit stock"
    const val EVENT_ACTION_CLICK_COMPLETE = "click complete product draft"
    const val EVENT_ACTION_CLICK_INVENTORY = "click inventory list tab"
    const val EVENT_ACTION_CLICK_CONTACT_CS = "click contact cs"

    const val EVENT_ACTION_CLICK_SETTINGS_PREVIEW = "$CLICK_SETTINGS_OPTION - see product preview"
    const val EVENT_ACTION_CLICK_SETTINGS_DUPLICATE = "$CLICK_SETTINGS_OPTION - duplicate product"
    const val EVENT_ACTION_CLICK_SETTINGS_REMINDER = "$CLICK_SETTINGS_OPTION - stock reminder"
    const val EVENT_ACTION_CLICK_SETTINGS_DELETE = "$CLICK_SETTINGS_OPTION - delete product"
    const val EVENT_ACTION_CLICK_SETTINGS_TOPADS = "click advertise the product"
    const val EVENT_ACTION_CLICK_SETTINGS_TOPADS_DETAIL = "click ads detail"
    const val EVENT_ACTION_CLICK_SETTINGS_CASHBACK = "$CLICK_SETTINGS_OPTION - cashback settings"
    const val EVENT_ACTION_CLICK_SETTINGS_FEATURED = "$CLICK_SETTINGS_OPTION - featured product"

    const val EVENT_ACTION_CLICK_TOGGLE_REMINDER = CLICK_ON_TOGGLE_STOCK_REMINDER
    const val EVENT_ACTION_CLICK_TOGGLE_REMINDER_SAVE = "$CLICK_ON_TOGGLE_STOCK_REMINDER - save on"

    const val EVENT_ACTION_CLICK_FEATURED_PRODUCT_POP_UP_SAVE = "$CLICK_ON_FEATURED_PRODUCT_POP_UP - save"
    const val EVENT_ACTION_CLICK_FEATURED_PRODUCT_POP_UP_MORE = "$CLICK_ON_FEATURED_PRODUCT_POP_UP - learn more"

    const val EVENT_ACTION_CLICK_ON_PRODUCT = "click on product"
    const val EVENT_ACTION_CLICK_ON_CASHBACK_SETTINGS = "click on cashback settings - save"
    const val EVENT_ACTION_CLICK_ON_CASHBACK_SETTINGS_POP_UP = "click on cashback settings pop up - save"
    const val EVENT_ACTION_CLICK_ON_DELETE_PRODUCT = "click on delete product - yes"
    const val EVENT_ACTION_CLICK_ON_ETALASE_FILTER = "click on etalase filter"
    const val EVENT_ACTION_CLICK_ON_CATEGORY_FILTER = "click on category filter - see all"
    const val EVENT_ACTION_CLICK_ON_FILTER = "click on filter - show all products"
    const val EVENT_ACTION_CLICK_ON_MORE_SORTING = "click on more sorting filter"
    const val EVENT_ACTION_CLICK_ON_EDIT_PRICE_SAVE = "click on edit price - save"

    const val EVENT_ACTION_CLICK_ON_BULK_SETTINGS = CLICK_ON_MULTIPLE_SELECT_BULK_SETTINGS
    const val EVENT_ACTION_CLICK_ON_BULK_SETTINGS_MOVE_ETALASE = "$EVENT_ACTION_CLICK_ON_BULK_SETTINGS - move etalase"
    const val EVENT_ACTION_CLICK_ON_BULK_SETTINGS_DEACTIVE = "$CLICK_ON_MULTIPLE_SELECT_BULK_SETTINGS - deactive product"
    const val EVENT_ACTION_CLICK_ON_BULK_SETTINGS_DELETE_BULK = "$CLICK_ON_MULTIPLE_SELECT_BULK_SETTINGS - delete bulk product"

    const val EVENT_ACTION_CLICK_ON_EDIT_STOCK_TOGGLE = "$CLICK_ON_EDIT_STOCK - toggle active product"
    const val EVENT_ACTION_CLICK_ON_EDIT_STOCK_SAVE = "$CLICK_ON_EDIT_STOCK - save"

    const val EVENT_ACTION_CLICK_ON_SORTING_FILTER = "$CLICK_ON_SORTING_FILTER - see all"
    const val EVENT_ACTION_CLICK_ON_SORTING_FILTER_NAME = CLICK_ON_SORTING_FILTER

    const val EVENT_ACTION_CLICK_ON_OTHERS_FILTER = "$CLICK_ON_OTHERS_FILTER - see all"
    const val EVENT_ACTION_CLICK_ON_OTHERS_FILTER_NAME = CLICK_ON_OTHERS_FILTER

    const val EVENT_ACTION_CLICK_ON_MORE_OTHERS_FILTER = CLICK_ON_MORE_OTHERS_FILTER
    const val EVENT_ACTION_CLICK_ON_MORE_OTHERS_FILTER_SAVE = "$CLICK_ON_MORE_OTHERS_FILTER - save"

    const val EVENT_ACTION_CLICK_ON_PROMOTION_PAGE = "click back on promotion page"
    const val EVENT_ACTION_CLICK_CASHBACK_VALUE = "click cashback "
    const val EVENT_ACTION_CLICK_NO_CASHBACK_VALUE = "click no cashback"
    const val EVENT_ACTION_CLICK_SAVE_PROMOTION = "click save promotion"

    const val EVENT_ACTION_CLICK_ON_EDIT_PRICE_VARIANT = "$EVENT_ACTION_CLICK_EDIT_PRICE - $VARIANTS"
    const val EVENT_ACTION_CLICK_ON_EDIT_PRICE_VARIANT_SAVE = "$EVENT_ACTION_CLICK_ON_EDIT_PRICE_VARIANT - save"
    const val EVENT_ACTION_CLICK_ON_EDIT_STOCK_VARIANT = "$EVENT_ACTION_CLICK_EDIT_STOCK - $VARIANTS"
    const val EVENT_ACTION_CLICK_ON_STATUS_TOGGLE_VARIANT = "click status toogle - $VARIANTS"
    const val EVENT_ACTION_CLICK_ON_CHANGE_AMOUNT_VARIANT = "click change amount - $VARIANTS"
    const val EVENT_ACTION_CLICK_ON_EDIT_STOCK_VARIANT_SAVE = "$EVENT_ACTION_CLICK_ON_EDIT_STOCK_VARIANT - save"
    const val EVENT_ACTION_CLICK_MENU_MORE_ELLIPSES = "click elipses"
    const val EVENT_ACTION_CLICK_MENU_MORE_SHOP_SHOWCASE = "click etalase toko"
    const val STATUS_TOGGLE_ON = "{on}"
    const val STATUS_TOGGLE_OFF = "{off}"

    const val EVENT_ACTION_CLICK_ALLOCATION_CLOSE = "click close stock allocation - "
    const val EVENT_ACTION_CLICK_ALLOCATION_ON_MAIN_STOCK = "click on stock utama - "
    const val EVENT_ACTION_CLICK_ALLOCATION_PRODUCT_STATUS = "click product status - "
    const val EVENT_ACTION_CLICK_ALLOCATION_DECREASE_STOCK = "click decrease stock - "
    const val EVENT_ACTION_CLICK_ALLOCATION_INPUT_STOCK = "click input stock - "
    const val EVENT_ACTION_CLICK_ALLOCATION_INCREASE_STOCK = "click increase stock - "
    const val EVENT_ACTION_CLICK_ALLOCATION_ON_STOCK_CAMPAIGN = "click on stock campaign - "
    const val EVENT_ACTION_CLICK_ALLOCATION_SAVE_STOCK = "click save stock - "
    const val EVENT_ACTION_CLICK_ALLOCATION_PREVIEW_VARIANT_PRODUCT = "click preview variant product"

    const val ALLOCATION_SINGLE_PRODUCT = "single product"
    const val ALLOCATION_VARIANT_PRODUCT = "variant product"

    const val EVENT_LABEL_ALLOCATION_ON = "on"
    const val EVENT_LABEL_ALLOCATION_OFF = "off"
    const val EVENT_LABEL_ALLOCATION_MAIN = "main"
    const val EVENT_LABEL_ALLOCATION_CAMPAIGN = "campaign"

    const val CUSTOM_DIMENSION_PAGE_SOURCE = "pageSource"
    const val CUSTOM_DIMENSION_PAGE_SOURCE_ADD_PRODUCT = "/add-product"
    const val CUSTOM_DIMENSION_PAGE_SOURCE_EDIT_PRODUCT = "/edit-product"

    const val SCREEN_NAME_STOCK_ALLOCATION_SINGLE = "/stock allocation - single product"
    const val SCREEN_NAME_STOCK_ALLOCATION_VARIANT = "/stock allocation - variant product"

    //MA
    const val BUSINESS_UNIT = "businessUnit"
    const val CURRENT_SITE = "currentSite"
    const val USER_ID = "userId"
    const val BUSINESS_UNIT_BROADCAST_CHAT = "communication, value"
    const val CURRENT_SITE_BROADCAST_CHAT = "tokopediamarketplace"
    const val EVENT_ACTION_CLICK_ON_CAROUSEL = "click on carousel"
    const val EVENT_LABEL_BROADCAST_CHAT = "broadcast chat"
    const val EVENT_CATEGORY_PRODUCT_MANAGE_PAGE = "manage product page"

    //SA
    const val EVENT_ACTION_CLICK_BROADCAST_CHAT = "click broadcast chat"
    const val CURRENT_SITE_BROADCAST_CHAT_SA = "tokopediaseller"
    const val BUSINESS_UNIT_BROADCAST_CHAT_SA = "communication"

}