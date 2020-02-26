package com.tokopedia.sellerhomedrawer.data.constant

import com.tokopedia.abstraction.constant.TkpdState

class SellerHomeState : TkpdState() {

    object DrawerPosition {
        val NO_ACCESS = 0
        val INBOX_MESSAGE = 1
        val INBOX_REVIEW = 2
        val INBOX_TALK = 3
        val INBOX_TICKET = 4
        val INDEX_HOME = 5
        val LOGIN = 6
        val MANAGE_PEOPLE = 7
        val MANAGE_PRODUCT = 8
        val ADD_PRODUCT = 1001
        val MANAGE_PAYMENT_AND_TOPUP = 55
        val MANAGE_TRANSACTION_DIGITAL = 56
        val MANAGE_PRICE_PRODUCT_DIGITAL = 57
        val DRAFT_PRODUCT = 22
        val MANAGE_SHOP = 9
        val PEOPLE = 10
        val PEOPLE_DEPOSIT = 11
        val PEOPLE_TRANSACTION = 12
        val SHOP_TRANSACTION = 13
        val CREATE_SHOP = 14
        val SHOP_INFO = 15
        val GENERAL_SETTING = 16
        val RESOLUTION_CENTER = 18
        val SELLER_INFO = 193
        val REGISTER = 19
        val DEVELOPER_OPTIONS = 20
        val MANAGE_ETALASE = 21
        val PEOPLE_PAYMENT_STATUS = 201
        val PEOPLE_ORDER_STATUS = 202
        val PEOPLE_CONFIRM_SHIPPING = 203
        val PEOPLE_TRANSACTION_LIST = 204
        val PEOPLE_TRANSACTION_CANCELED = 205
        val PEOPLE_SHOPPING_LIST = 206
        val PEOPLE_DIGITAL_TRANSACTION_LIST = 207
        val PEOPLE_FLIGHT_TRANSACTION_LIST = 208
        val PEOPLE_TRAIN_TRANSACTION_LIST = 209
        val PEOPLE_PAYMENT_LIST = 212
        val PEOPLE_DEALS_TRANSACTION_LIST = 210
        val PEOPLE_EVENTS_TRANSACTION_LIST = 211

        val SHOP_NEW_ORDER = 301
        val SHOP_CONFIRM_SHIPPING = 302
        val SHOP_SHIPPING_STATUS = 303
        val SHOP_TRANSACTION_LIST = 304
        val SHOP = 300

        val INBOX = 31
        val WISHLIST = 32
        val SECURITY_QUESTION = 33
        val REGISTER_NEXT = 34
        val ACTIVATION_RESENT = 35
        val FORGOT_PASSWORD = 36
        val HOTLIST = 37
        val HOME = 38
        val SETTINGS = 39
        val REGISTER_THIRD = 40
        val CONTACT_US = 41
        val REPORT = 42
        val LOGOUT = 43
        val SELLER_INDEX_HOME = INDEX_HOME
        val REGISTER_INITIAL = 45
        val SELLER_GM_SUBSCRIBE = 46
        val SELLER_GM_SUBSCRIBE_EXTEND = 47
        val SELLER_PRODUCT_EXTEND = 53
        val SELLER_PRODUCT_DIGITAL_EXTEND = 54
        val SELLER_TOP_ADS = 48
        val SELLER_FLASH_SALE = 59
        val SELLER_GM_STAT = 49
        val SELLER_MITRA_TOPPERS = 101
        val GOLD_MERCHANT = 50
        val HELP = 51
        val SHOP_OPPORTUNITY_LIST = 52
        val CATEGORY_NAVIGATION = 54
        val RESOLUTION_CENTER_BUYER = 55
        val RESOLUTION_CENTER_SELLER = 56
        val MYBILLS = 57

        val FEATURED_PRODUCT = 99

        val POS_TRANSACTION_HISTORY = 401
        val POS_OUTLET = 402
        val POS_PRODUCT_MANAGEMENET = 403

        val APPSHARE = 58

    }

    object SellingTransaction {

        val FROM_WIDGET_TAG = "from widget"
        val EXTRA_STATE_TAB_POSITION = "tab"
        val EXTRA_QUERY = "query"

        val TAB_POSITION_SELLING_OPPORTUNITY = "1"
        val TAB_POSITION_SELLING_NEW_ORDER = "2"
        val TAB_POSITION_SELLING_CONFIRM_SHIPPING = "3"
        val TAB_POSITION_SELLING_SHIPPING_STATUS = "4"
        val TAB_POSITION_SELLING_TRANSACTION_LIST = "5"
    }
}