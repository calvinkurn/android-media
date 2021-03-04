package com.tokopedia.seller.action.common.analytics

/**
 * Data layer: https://mynakama.tokopedia.com/datatracker/product/requestdetail/114
 */
object SellerActionTracking {

    object Key {
        internal const val EVENT = "event"
        internal const val EVENT_CATEGORY = "eventCategory"
        internal const val EVENT_ACTION = "eventAction"
        internal const val EVENT_LABEL = "eventLabel"
        internal const val BUSINESS_UNIT = "businessUnit"
        internal const val CURRENT_SITE = "currentSite"
        internal const val USER_ID = "userId"
    }

    object EventName {
        internal const val VIEW = "viewGASellerIris"
        internal const val CLICK = "clickGASeller"
    }

    object EventAction {
        object Impression {
            private const val IMPRESSION = "impression"

            internal const val EMPTY_STATE = "$IMPRESSION empty state"
            internal const val ERROR_STATE = "$IMPRESSION error state"
            internal const val LOADING_STATE = "$IMPRESSION loading state"
            internal const val NON_LOGIN_STATE = "$IMPRESSION non login state"
            internal const val SUCCESS_CERTAIN_STATE = "$IMPRESSION success state - certain date"
            internal const val SUCCESS_TODAY_STATE = "$IMPRESSION success state - today"
        }

        object Click {
            private const val CLICK = "click"

            internal const val OPEN_APP_BUTTON = "$CLICK open app button"
            internal const val ORDER_LINE = "$CLICK order line"
        }
    }

    internal const val EVENT_CATEGORY = "ga seller app"

    internal const val PHYSICAL_GOODS = "physical goods"
    internal const val TOKOPEDIA_SELLER = "tokopediaseller"

}