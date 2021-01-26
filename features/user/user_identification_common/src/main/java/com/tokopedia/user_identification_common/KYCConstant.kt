package com.tokopedia.user_identification_common

/**
 * @author by nisie on 12/11/18.
 */
interface KYCConstant {
    companion object {
        const val ERROR_STATUS_UNKNOWN = "1001"
        const val ERROR_MESSAGE_EMPTY = "1002"
        const val ERROR_UPLOAD_IMAGE = "1003"
        const val ERROR_UPLOAD_IDENTIFICATION = "1003.1"
        const val ERROR_REGISTER = "1004"
        const val UNHANDLED_RESPONSE = "1005"
        const val KYC_PROJECT_ID = 1
        const val MERCHANT_KYC_PROJECT_ID = 10
        const val EXTRA_STRING_IMAGE_RESULT = "image_result"
        const val REQUEST_CODE_CAMERA_KTP = 1010
        const val REQUEST_CODE_CAMERA_FACE = 2020
        const val STATUS_DEFAULT = -3
        const val STATUS_ERROR = -2
        const val STATUS_REJECTED = -1
        const val STATUS_PENDING = 0
        const val STATUS_VERIFIED = 1
        const val STATUS_EXPIRED = 2
        const val STATUS_NOT_VERIFIED = 3
        const val STATUS_APPROVED = 4
        const val STATUS_BLACKLISTED = 5
        const val IS_SUCCESS_GET_STATUS = 1
        const val IS_ALL_MUTATION_SUCCESS = 2
        const val IS_FILE_IMAGE_TOO_BIG = -5
        const val IS_FILE_IMAGE_NOT_EXIST = -7
        const val NOT_SUPPORT_LIVENESS = -9
        const val IS_FILE_LIVENESS_IMAGE_NOT_EXIST = -11
        const val PARAM_KYC_SRC = "source"
        const val VALUE_KYC_SRC_SELLER = "seller"
        const val EXTRA_IS_SOURCE_SELLER = "source_seller"
        const val PARAM_PROJECT_ID = "projectId"
        const val KTP_RETAKE = 1
        const val FACE_RETAKE = 2
        const val USER_EXIT = -9
        const val KYC_OVERLAY_COLOR = "#ae000000"
        const val KYC_AB_KEYWORD = "Liveness Detection 2"
        const val QUERY_GET_KYC_PROJECT_INFO = "get_kyc_project_info"
    }
}