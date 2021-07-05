package com.tokopedia.internal_review.common

/**
 * Created By @ilhamsuaib on 21/01/21
 */

object Const {

    const val OS_TYPE = "android"

    const val LOTTIE_URL_RATING_1 = "https://ecs7.tokopedia.net/img/android/lottie/lottie_anim_sir_pedi_cry.json"
    const val LOTTIE_URL_RATING_2 = "https://ecs7.tokopedia.net/img/android/lottie/lottie_anim_sir_pedi_sad.json"
    const val LOTTIE_URL_RATING_3 = "https://ecs7.tokopedia.net/img/android/lottie/lottie_anim_sir_pedi_standard.json"
    const val LOTTIE_URL_RATING_4 = "https://ecs7.tokopedia.net/img/android/lottie/lottie_anim_sir_pedi_happy.json"
    const val LOTTIE_URL_RATING_5 = "https://ecs7.tokopedia.net/img/android/lottie/lottie_anim_sir_pedi_very_happy.json"

    const val IMG_REQUEST_FEEDBACK = "https://images.tokopedia.net/img/android/merchant/seller_review/img_sir_req_feedback.png"
    const val IMG_THANK_YOU = "https://images.tokopedia.net/img/android/merchant/seller_review/img_sir_thank_you.png"

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