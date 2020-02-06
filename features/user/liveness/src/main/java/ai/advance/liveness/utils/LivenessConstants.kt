package ai.advance.liveness.utils

object LivenessConstants {
    @JvmStatic
    val SCAN_FACE_FAIL_TIME = "https://ecs7.tokopedia.net/img/android/others/account_verification_failed_time.png"
    @JvmStatic
    val SCAN_FACE_FAIL_NETWORK = "https://ecs7.tokopedia.net/img/android/others/account_verification_failed_badnetwork.png"
    @JvmStatic
    val SCAN_FACE_FAIL_GENERAL = "https://ecs7.tokopedia.net/img/android/others/account_verification_failed_general.png"
//    val SCAN_FACE_FAIL_NETWORK = "https://ecs7.tokopedia.net/img/android/others/account_verification_failed_bad_network.png"


    const val FAILED_GENERAL = 0
    const val FAILED_BADNETWORK = 1
    const val FAILED_TIMEOUT = 2

    const val KYC_FILE_NOT_FOUND = -7
}