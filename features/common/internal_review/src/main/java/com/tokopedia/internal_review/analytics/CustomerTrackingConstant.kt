package com.tokopedia.internal_review.analytics

/**
 * Created By @ilhamsuaib on 02/02/21
 */

object CustomerTrackingConstant {

    object Key {
        const val BUSINESS_UNIT = "businessUnit"
        const val CURRENT_SITE = "currentSite"
        const val USER_ID = "userId"
    }

    object Event {
        const val CLICK_CUSTOMER_REVIEW = "clickCustomerReview"
        const val VIEW_CUSTOMER_REVIEW_IRIS = "viewCustomerReviewIris"
    }

    object Category {
        const val CUSTOMER_APP_REVIEW = "customer app review"
    }

    object Action {
        const val CLICK_CLOSE_POP_UP = "click close pop up"
        const val IMPRESSION_NO_NETWORK = "impression no network"
        const val IMPRESSION_ERROR_STATE = "impression error state"
        const val IMPRESSION_LOADING_STATE = "impression loading state"
    }

    object Value {
        const val PHYSICAL_GOODS = "physicalgoods"
        const val TOKOPEDIACUSTOMER = "tokopediacustomer"
    }
}