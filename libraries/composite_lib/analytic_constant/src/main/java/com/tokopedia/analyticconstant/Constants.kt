package com.tokopedia.analytic_constant

import com.google.firebase.analytics.FirebaseAnalytics

class Param protected constructor() {
    companion object {
        const val ACHIEVEMENT_ID = FirebaseAnalytics.Param.ACHIEVEMENT_ID
        const val CHARACTER =  FirebaseAnalytics.Param.CHARACTER
        const val TRAVEL_CLASS =  FirebaseAnalytics.Param.TRAVEL_CLASS
        const val CONTENT_TYPE =  FirebaseAnalytics.Param.CONTENT_TYPE
        const val CURRENCY =  FirebaseAnalytics.Param.CURRENCY
        const val COUPON = FirebaseAnalytics.Param.COUPON
        const val START_DATE = FirebaseAnalytics.Param.START_DATE
        const val END_DATE = FirebaseAnalytics.Param.END_DATE
        const val FLIGHT_NUMBER = FirebaseAnalytics.Param.FLIGHT_NUMBER
        const val GROUP_ID = FirebaseAnalytics.Param.GROUP_ID
        const val ITEM_CATEGORY = FirebaseAnalytics.Param.ITEM_CATEGORY
        const val ITEM_ID = FirebaseAnalytics.Param.ITEM_ID
        const val ITEM_LOCATION_ID = FirebaseAnalytics.Param.ITEM_LOCATION_ID
        const val ITEM_NAME = FirebaseAnalytics.Param.ITEM_NAME
        const val LOCATION = FirebaseAnalytics.Param.LOCATION
        const val LEVEL = FirebaseAnalytics.Param.LEVEL
        const val LEVEL_NAME = FirebaseAnalytics.Param.LEVEL_NAME

        @Deprecated("")
        const val SIGN_UP_METHOD = FirebaseAnalytics.Param.SIGN_UP_METHOD
        const val METHOD = FirebaseAnalytics.Param.METHOD
        const val NUMBER_OF_NIGHTS = FirebaseAnalytics.Param.NUMBER_OF_NIGHTS
        const val NUMBER_OF_PASSENGERS = FirebaseAnalytics.Param.NUMBER_OF_PASSENGERS
        const val NUMBER_OF_ROOMS = FirebaseAnalytics.Param.NUMBER_OF_ROOMS
        const val DESTINATION = FirebaseAnalytics.Param.DESTINATION
        const val ORIGIN = FirebaseAnalytics.Param.ORIGIN
        const val PRICE = FirebaseAnalytics.Param.PRICE
        const val QUANTITY = FirebaseAnalytics.Param.QUANTITY
        const val SCORE = FirebaseAnalytics.Param.SCORE
        const val SHIPPING = FirebaseAnalytics.Param.SHIPPING
        const val TRANSACTION_ID = FirebaseAnalytics.Param.TRANSACTION_ID
        const val SEARCH_TERM = FirebaseAnalytics.Param.SEARCH_TERM
        const val SUCCESS = FirebaseAnalytics.Param.SUCCESS
        const val TAX = FirebaseAnalytics.Param.TAX
        const val VALUE = FirebaseAnalytics.Param.VALUE
        const val VIRTUAL_CURRENCY_NAME = FirebaseAnalytics.Param.VIRTUAL_CURRENCY_NAME
        const val CAMPAIGN = FirebaseAnalytics.Param.CAMPAIGN
        const val SOURCE = FirebaseAnalytics.Param.SOURCE
        const val MEDIUM = FirebaseAnalytics.Param.MEDIUM
        const val TERM = FirebaseAnalytics.Param.TERM
        const val CONTENT = FirebaseAnalytics.Param.CONTENT
        const val ACLID = FirebaseAnalytics.Param.ACLID
        const val CP1 = FirebaseAnalytics.Param.CP1
        const val ITEM_BRAND = FirebaseAnalytics.Param.ITEM_BRAND
        const val ITEM_VARIANT = FirebaseAnalytics.Param.ITEM_VARIANT
        const val ITEM_LIST = FirebaseAnalytics.Param.ITEM_LIST
        const val CHECKOUT_STEP = FirebaseAnalytics.Param.CHECKOUT_STEP
        const val CHECKOUT_OPTION = FirebaseAnalytics.Param.CHECKOUT_OPTION
        const val CREATIVE_NAME = FirebaseAnalytics.Param.CREATIVE_NAME
        const val CREATIVE_SLOT = FirebaseAnalytics.Param.CREATIVE_SLOT
        const val AFFILIATION = FirebaseAnalytics.Param.AFFILIATION
        const val INDEX = FirebaseAnalytics.Param.INDEX
    }
}

class Event protected constructor() {
    companion object {
        const val ADD_PAYMENT_INFO = FirebaseAnalytics.Event.ADD_PAYMENT_INFO
        const val ADD_TO_CART = FirebaseAnalytics.Event.ADD_TO_CART
        const val ADD_TO_WISHLIST = FirebaseAnalytics.Event.ADD_TO_WISHLIST
        const val APP_OPEN = FirebaseAnalytics.Event.APP_OPEN
        const val BEGIN_CHECKOUT = FirebaseAnalytics.Event.BEGIN_CHECKOUT
        const val CAMPAIGN_DETAILS = FirebaseAnalytics.Event.CAMPAIGN_DETAILS
        const val ECOMMERCE_PURCHASE = FirebaseAnalytics.Event.ECOMMERCE_PURCHASE
        const val GENERATE_LEAD = FirebaseAnalytics.Event.GENERATE_LEAD
        const val JOIN_GROUP = FirebaseAnalytics.Event.JOIN_GROUP
        const val LEVEL_END = FirebaseAnalytics.Event.LEVEL_END
        const val LEVEL_START = FirebaseAnalytics.Event.LEVEL_START
        const val LEVEL_UP = FirebaseAnalytics.Event.LEVEL_UP
        const val LOGIN = FirebaseAnalytics.Event.LOGIN
        const val POST_SCORE = FirebaseAnalytics.Event.POST_SCORE
        const val PRESENT_OFFER = FirebaseAnalytics.Event.PRESENT_OFFER
        const val PURCHASE_REFUND = FirebaseAnalytics.Event.PURCHASE_REFUND
        const val SEARCH = FirebaseAnalytics.Event.SEARCH
        const val SELECT_CONTENT = FirebaseAnalytics.Event.SELECT_CONTENT
        const val SHARE = FirebaseAnalytics.Event.SHARE
        const val SIGN_UP = FirebaseAnalytics.Event.SIGN_UP
        const val SPEND_VIRTUAL_CURRENCY = FirebaseAnalytics.Event.SPEND_VIRTUAL_CURRENCY
        const val TUTORIAL_BEGIN = FirebaseAnalytics.Event.TUTORIAL_BEGIN
        const val TUTORIAL_COMPLETE = FirebaseAnalytics.Event.TUTORIAL_COMPLETE
        const val UNLOCK_ACHIEVEMENT = FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT
        const val VIEW_ITEM = FirebaseAnalytics.Event.VIEW_ITEM
        const val VIEW_ITEM_LIST = FirebaseAnalytics.Event.VIEW_ITEM_LIST
        const val VIEW_SEARCH_RESULTS = FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS
        const val EARN_VIRTUAL_CURRENCY = FirebaseAnalytics.Event.EARN_VIRTUAL_CURRENCY
        const val REMOVE_FROM_CART = FirebaseAnalytics.Event.REMOVE_FROM_CART
        const val CHECKOUT_PROGRESS = FirebaseAnalytics.Event.CHECKOUT_PROGRESS
        const val SET_CHECKOUT_OPTION = FirebaseAnalytics.Event.SET_CHECKOUT_OPTION
        const val PRODUCTVIEW = "productview"
        const val PRODUCTCLICK = "productclick"
        const val VIEWPRODUCT = "viewproduct"
        const val ADDTOCART = "addtocart"
        const val BEGINCHECKOUT = "begin_checkout"
        const val CHECKOUT_PROGRESS_CUSTOM = "checkout_progress"
    }
}