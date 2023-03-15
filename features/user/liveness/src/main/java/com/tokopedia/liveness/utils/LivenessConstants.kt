package com.tokopedia.liveness.utils

import com.tokopedia.imageassets.TokopediaImageUrl

object LivenessConstants {
    const val KYC_BASE_URL = "https://accounts.tokopedia.com/"
    const val KYC_PARAMS = "[{\"kyc_type\": 1,\"param\": \"ktp_image\"},{\"kyc_type\": 2,\"param\": \"face_image\"}]\n"

    const val SCAN_FACE_FAIL_TIME = TokopediaImageUrl.SCAN_FACE_FAIL_TIME
    const val SCAN_FACE_FAIL_NETWORK = TokopediaImageUrl.SCAN_FACE_FAIL_NETWORK

    const val ANIMATION_YAW = TokopediaImageUrl.ANIMATION_YAW
    const val ANIMATION_MOUTH = TokopediaImageUrl.ANIMATION_MOUTH
    const val ANIMATION_BLINK = TokopediaImageUrl.ANIMATION_BLINK

    const val FAILED_BADNETWORK = 1
    const val FAILED_TIMEOUT = 2

    const val NOT_SUPPORT_LIVENESS = -9
    const val KYC_LIVENESS_FILE_NOT_FOUND = -11

    const val ARG_FAILED_TYPE = "failed_type"

    const val REMOTE_CONFIG_KEY_LIVENESS_RANDOM_DETECTION = "android_user_random_detection_liveness"
}
