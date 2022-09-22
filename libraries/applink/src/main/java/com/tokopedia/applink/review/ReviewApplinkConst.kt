package com.tokopedia.applink.review

object ReviewApplinkConst {
    const val PATH_REVIEW = "review"
    const val AUTHORITY_PRODUCT = "product"
    const val PATH_PRODUCT_REVIEW = "product-review"
    const val PATH_CREATE = "create"
    const val REVIEW_DETAIL_EXPECTED_PATH_SIZE = 1
    const val REVIEW_EXPECTED_PATH_SIZE = 0
    const val PARAM_TAB = "tab"
    const val PARAM_PRODUCT_ID = "productId"

    // inbox review tabs
    const val PENDING_TAB = "waiting-review" // 1st tab
    const val HISTORY_TAB = "history" // 2nd tab
    const val SELLER_TAB = "seller" // 3rd tab

    // inbox reputation tabs
    const val RATING_PRODUCT_TAB = "rating-product" // 1st tab
    const val SELLER_INBOX_REVIEW_TAB = "inbox-ulasan" // 2nd tab
    const val BUYER_REVIEW_TAB = "history" // 3rd tab
    const val PENALTY_AND_REWARD_TAB = "penalty-and-reward" // 4th tab

    // review credibility
    const val REVIEW_CREDIBILITY_SOURCE_REVIEW_INBOX = "inbox"
    const val REVIEW_CREDIBILITY_SOURCE_REVIEW_READING = "review-list"
    const val REVIEW_CREDIBILITY_SOURCE_REVIEW_READING_IMAGE_PREVIEW = "reading image preview"
    const val REVIEW_CREDIBILITY_SOURCE_REVIEW_GALLERY = "gallery"
    const val REVIEW_CREDIBILITY_SOURCE_REVIEW_MOST_HELPFUL = "mosthelpful-review"
}