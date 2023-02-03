package com.tokopedia.internal_review.common

import com.tokopedia.imageassets.ImageUrl

/**
 * Created By @ilhamsuaib on 21/01/21
 */

object Const {

    const val OS_TYPE = "android"

    const val LOTTIE_URL_RATING_1 = ImageUrl.LOTTIE_URL_RATING_1
    const val LOTTIE_URL_RATING_2 = ImageUrl.LOTTIE_URL_RATING_2
    const val LOTTIE_URL_RATING_3 = ImageUrl.LOTTIE_URL_RATING_3
    const val LOTTIE_URL_RATING_4 = ImageUrl.LOTTIE_URL_RATING_4
    const val LOTTIE_URL_RATING_5 = ImageUrl.LOTTIE_URL_RATING_5

    const val IMG_REQUEST_FEEDBACK = ImageUrl.IMG_REQUEST_FEEDBACK
    const val IMG_THANK_YOU = ImageUrl.IMG_THANK_YOU

    object SharedPrefKey {
        const val PREFERENCE_NAME = "CACHE_SELLER_IN_APP_REVIEW"
        const val KEY_HAS_ADDED_PRODUCT = "KEY_SIR_HAS_ADDED_PRODUCT"
        const val KEY_HAS_POSTED_FEED = "KEY_SIR_HAS_POSTED_FEED"
        const val KEY_CHATS_REPLIED_TO = "KEY_SIR_CHATS_REPLIED_TO"
        const val KEY_HAS_OPENED_REVIEW = "KEY_SIR_HAS_OPENED_REVIEW"
        const val KEY_LAST_REVIEW_ASKED = "KEY_SIR_LAST_REVIEW_ASKED"
        const val KEY_IS_ALLOW_APP_REVIEW_DEBUGGING = "KEY_SIR_IS_ALLOW_APP_REVIEW_DEBUGGING"
    }

    object RemoteConfigKey {
        const val SELLERAPP_IN_APP_REVIEW = "android_sellerapp_seller_inapp_review"
        const val CUSTOMERAPP_INTERNAL_REVIEW = "android_customerapp_internal_review"
    }
}
