package com.tokopedia.product.manage.feature.list.constant

object ProductManageDataLayer {
    private const val CLICK_SETTINGS_OPTION = "click settings option"
    private const val CLICK_ON_TOGGLE_STOCK_REMINDER = "click on toggle stock reminder"
    private const val CLICK_ON_FEATURED_PRODUCT_POP_UP = "click on featured product pop up"
    private const val CLICK_ON_MULTIPLE_SELECT_BULK_SETTINGS = "click on multiple select - bulk settings"
    private const val CLICK_ON_EDIT_STOCK = "click on edit stock"
    private const val CLICK_ON_SORTING_FILTER = "click on sorting filter"
    private const val CLICK_ON_OTHERS_FILTER = "click on others filter"
    private const val CLICK_ON_MORE_OTHERS_FILTER = "click on more others filter"

    const val EVENT_NAME = "clickManageProduct"
    const val EVENT_CATEGORY = "product list page"
    const val EVENT_NAME_EDIT_PRODUCT = "clickEditProduct"
    const val EVENT_CATEGORY_EDIT_PRODUCT = "edit product page"

    const val EVENT_ACTION_CLICK_ADD = "click add product"
    const val EVENT_ACTION_CLICK_MULTIPLE = "click multiple select"
    const val EVENT_ACTION_CLICK_EDIT_VARIANTS = "click edit variants detail"
    const val EVENT_ACTION_CLICK_EDIT_PRICE = "click edit price"
    const val EVENT_ACTION_CLICK_EDIT_STOCK = "click edit stock"
    const val EVENT_ACTION_CLICK_COMPLETE = "click complete product draft"
    const val EVENT_ACTION_CLICK_INVENTORY = "click inventory list tab"
    const val EVENT_ACTION_CLICK_CONTACT_CS = "click contact cs"

    const val EVENT_ACTION_CLICK_SETTINGS_PREVIEW = "$CLICK_SETTINGS_OPTION - see product preview"
    const val EVENT_ACTION_CLICK_SETTINGS_DUPLICATE = "$CLICK_SETTINGS_OPTION - duplicate product"
    const val EVENT_ACTION_CLICK_SETTINGS_REMINDER = "$CLICK_SETTINGS_OPTION - stock reminder"
    const val EVENT_ACTION_CLICK_SETTINGS_DELETE = "$CLICK_SETTINGS_OPTION - delete product"
    const val EVENT_ACTION_CLICK_SETTINGS_TOPADS = "$CLICK_SETTINGS_OPTION - topads"
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
}