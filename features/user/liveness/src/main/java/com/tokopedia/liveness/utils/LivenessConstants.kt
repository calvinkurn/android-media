package com.tokopedia.liveness.utils

object LivenessConstants {
    const val KYC_BASE_URL = "https://accounts.tokopedia.com/"
    const val KYC_PARAMS = "[{\"kyc_type\": 1,\"param\": \"ktp_image\"},{\"kyc_type\": 2,\"param\": \"face_image\"}]\n"

    const val SCAN_FACE_FAIL_TIME = "https://ecs7.tokopedia.net/img/android/others/account_verification_failed_time.png"
    const val SCAN_FACE_FAIL_NETWORK = "https://ecs7.tokopedia.net/img/android/others/account_verification_failed_badnetwork.png"

    const val ANIMATION_YAW = "https://ecs7.tokopedia.net/lottie/turning_the_head_to_right_and_left.json"
    const val ANIMATION_MOUTH = "https://ecs7.tokopedia.net/lottie/mouth_open_and_close.json"
    const val ANIMATION_BLINK = "https://ecs7.tokopedia.net/lottie/blink_interaction.json"

    const val FAILED_BADNETWORK = 1
    const val FAILED_TIMEOUT = 2

    const val NOT_SUPPORT_LIVENESS = -9
    const val KYC_LIVENESS_FILE_NOT_FOUND = -11

    const val ARG_FAILED_TYPE = "failed_type"
}