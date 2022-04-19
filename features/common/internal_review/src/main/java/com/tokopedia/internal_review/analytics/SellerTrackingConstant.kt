package com.tokopedia.internal_review.analytics

/**
 * Created By @ilhamsuaib on 02/02/21
 */

object SellerTrackingConstant {

    object Key {
        const val BUSINESS_UNIT = "businessUnit"
        const val CURRENT_SITE = "currentSite"
        const val USER_ID = "userId"
    }

    object Event {
        const val CLICK_SELLER_REVIEW = "clickSellerReview"
        const val VIEW_SELLER_REVIEW_IRIS = "viewSellerReviewIris"
    }

    object Category {
        const val SELLER_APP_REVIEW = "seller app review"
    }

    object Action {
        const val CLICK_CLOSE_POP_UP = "click close pop up"
        const val IMPRESSION_NO_NETWORK = "impression no network"
        const val IMPRESSION_ERROR_STATE = "impression error state"
        const val IMPRESSION_LOADING_STATE = "impression loading state"
    }

    object Value {
        const val PHYSICAL_GOODS = "physicalgoods"
        const val TOKOPEDIASELLER = "tokopediaseller"
    }
}