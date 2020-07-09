package com.tokopedia.liveness.utils

object LivenessConstants {
    const val KYC_BASE_URL = "https://accounts.tokopedia.com/"
    const val KYC_PARAMS = "[{\"kyc_type\": 1,\"param\": \"ktp_image\"},{\"kyc_type\": 2,\"param\": \"face_image\"}]\n"

    const val SCAN_FACE_FAIL_TIME = "https://ecs7.tokopedia.net/img/android/others/account_verification_failed_time.png"
    const val SCAN_FACE_FAIL_NETWORK = "https://ecs7.tokopedia.net/img/android/others/account_verification_failed_badnetwork.png"
    const val SCAN_FACE_FAIL_GENERAL = "https://ecs7.tokopedia.net/img/android/others/account_verification_failed_general.png"

    const val ANIMATION_YAW = "https://ecs7.tokopedia.net/lottie/turning_the_head_to_right_and_left.json"
    const val ANIMATION_MOUTH = "https://ecs7.tokopedia.net/lottie/mouth_open_and_close.json"
    const val ANIMATION_BLINK = "https://ecs7.tokopedia.net/lottie/blink_interaction.json"

    const val FAILED_GENERAL = 0
    const val FAILED_BADNETWORK = 1
    const val FAILED_TIMEOUT = 2

    const val KYC_FILE_NOT_FOUND = -7
    const val NOT_SUPPORT_LIVENESS = -9
    const val KYC_LIVENESS_FILE_NOT_FOUND = -11

    const val ARG_FAILED_TYPE = "failed_type"
    const val ARG_ERROR_CODE = "error_code"

    const val LIVENESS_400 = -1
    const val LIVENESS_401 = -2
    const val LIVENESS_403 = -3
    const val LIVENESS_408 = -4
    const val LIVENESS_500 = -5
    const val LIVENESS_502 = -6
    const val LIVENESS_504 = -7
    const val LIVENESS_UNKNOWNHOST = -8
    const val LIVENESS_SOCKET_TIMEOUT = -9
    const val LIVENESS_MESSAGE_ERROR = -10
    const val LIVENESS_IO_EXCEPTION = -11
    const val LIVENESS_ERROR_CODE_DEFAULT = -12
}