package com.tokopedia.kyc_centralized.common

/**
 * @author by nisie on 12/11/18.
 */
object KYCConstant {
    const val KEY_KNOW_ERROR_CODE = "KYC-KNOW-ERROR-CODE"
    const val ERROR_STATUS_UNKNOWN = "1001"
    const val KYC_PROJECT_ID = 1
    const val EXTRA_STRING_IMAGE_RESULT = "image_result"
    const val REQUEST_CODE_CAMERA_KTP = 1010
    const val REQUEST_CODE_CAMERA_FACE = 2020
    const val IS_FILE_IMAGE_TOO_BIG = -5
    const val IS_FILE_IMAGE_NOT_EXIST = -7
    const val NOT_SUPPORT_LIVENESS = -9
    const val IS_FILE_LIVENESS_IMAGE_NOT_EXIST = -11
    const val EXTRA_IS_SOURCE_SELLER = "source_seller"
//    const val PARAM_PROJECT_ID = "projectId"
    const val KTP_RETAKE = 1
    const val FACE_RETAKE = 2
    const val USER_EXIT = -9

    const val BUSINESS_UNIT = "businessUnit"
    const val USER_PLATFORM = "user platform"
    const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
    const val CURRENT_SITE = "currentSite"
    const val TRACKER_ID = "trackerId"

    const val LIVENESS_TAG = "LIVENESS_TAG"

    const val EXTRA_USE_CROPPING = "useCropping"
    const val EXTRA_USE_COMPRESSION = "useCompression"

    const val PADDING_16 = 16
    const val PADDING_ZERO = 0
    const val PADDING_0_5F = 0.5f

    const val QUALITY_100 = 100F
    const val QUALITY_70 = 70F
    const val QUALITY_50 = 50F
    const val QUALITY_40 = 40F
    const val QUALITY_30 = 30F
    const val QUALITY_20 = 20F

    const val MB_2 = 2
    const val MB_3 = 3
    const val MB_6 = 6
    const val MB_10 = 10
    const val MB_15 = 15

    const val consentCollectionIdProduction = "bf7c9ba1-a4a9-447e-bbee-974c905a95ac"
    const val consentCollectionIdStaging = "8db1d162-15ea-45c1-9b69-20b256e39e44"

    const val collectionIdGotoKycProgressiveStaging = "b41f13da-f6ef-425a-b65e-f07bbb42821c"
    const val collectionIdGotoKycProgressiveProduction = "172a03bf-1385-425f-9ffe-94ea7119e77d"
    const val collectionIdGotoKycNonProgressiveStaging = "6c4e768e-a059-4e90-a011-f11f587d8739"
    const val collectionIdGotoKycNonProgressiveProduction = "23d6102a-a625-4036-964a-aa039198605a"

    const val PROJECT_ID_ACCOUNT = "7"

    object SharedPreference {
        /*
        * WARNING!!!
        * the value of this variable [KEY_KYC_FLOW_TYPE] must be the same as the value of the
        * [KEY_SHARED_PREFERENCE_KYC_FLOW_TYPE] variable in [com.tokopedia.liveness.analytics.LivenessDetectionAnalytics]
        * */
        const val KEY_KYC_FLOW_TYPE = "kyc_type"

        const val VALUE_KYC_FLOW_TYPE_ALA_CARTE = "ala carte"
        const val VALUE_KYC_FLOW_TYPE_CKYC = "ckyc"
    }

    object GotoDataSource {
        const val TOKO_KYC = "0"
        const val GOTO_NON_PROGRESSIVE = "1"
        const val GOTO_PROGRESSIVE = "2"
    }

    object GotoKycFlow {
        const val NON_PROGRESSIVE = "NON_PROGRESSIVE_ELIGIBLE"
        const val PROGRESSIVE = "PROGRESSIVE_ELIGIBLE"
        const val AWAITING_APPROVAL_GOPAY = "KYC_AWAITING_APPROVAL"
        const val ONEKYC_BLOCKED = "ONEKYC_BLOCKED"
    }

    object ActivityResult {
        const val RESULT_FINISH = 99
        const val RELOAD = 98
    }
}
