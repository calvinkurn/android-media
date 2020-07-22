package com.tokopedia.updateinactivephone.common

interface UpdateInactivePhoneConstants {

    interface OperationName {
        companion object {
            const val CHECK_USER_STATUS = "validateInactivePhoneQuery"
            const val VALIDATE_USER_DATA = "validateInactiveNewPhoneQuery"
        }
    }

    interface Constants {
        companion object {
            const val IS_DUPLICATE_REQUEST = "is_duplicate_request"
            const val FILE_TO_UPLOAD = "fileToUpload"
            const val USERID = "user_id"
            const val ID = "id"
            const val TOKEN = "token"
            const val SERVER_ID = "server_id"
            const val IMAGE_UPLOAD_URL = "image_upload_url"
            const val RESOLUTION = "resolution"
            const val PARAM_KTP_IMAGE_PATH = "ktp_image_path"
            const val PARAM_BANK_BOOK_IMAGE_PATH = "account_image_path"
            const val PARAM_DEVICE_ID = "device_id"
            const val PARAM_FILE_TO_UPLOAD = "fileToUpload"

            const val PARAM_KTP_IMAGE_ID = "ktp"
            const val PARAM_BANKBOOK_IMAGE_ID = "tabungan"

            const val USER_EMAIL = "user_mail"
            const val USER_PHONE = "user_phone"
        }

    }

    interface QueryConstants {
        companion object {
            val PHONE = "phone"
            val EMAIL = "email"
            val USER_ID = "userId"
            val OLD_PHONE = "oldPhone"
            val FILE_UPLOADED = "fileUploaded"
            val ID_CARD_IMAGE = "idCardImage"
            val SAVING_BOOK_IMAGE = "savingBookImage"
        }
    }

    interface ResponseConstants {
        companion object {
            const val INVALID_PHONE = "invalid_msisdn"
            const val PHONE_TOO_SHORT = "too_short_msisdn"
            const val PHONE_TOO_LONG = "too_long_msisdn"
            const val INVALID_FILE_UPLOADED = "invalid_uploaded_file"
            const val PHONE_BLACKLISTED = "blacklisted_msisdn"
            const val PHONE_NOT_REGISTERED = "unregistered_msisdn"
            const val PHONE_WITH_REGISTERED_EMAIL = "registered_email"
            const val PHONE_WITH_PENDING_REQUEST = "has_pending_request"
            const val UNREGISTERED_USER = "unregistered_user"
            const val SERVER_ERROR = "server_error"
            const val WRONG_USER_ID = "wrong_userid"
            const val SAME_MSISDN = "same_msisdn"
            const val REGISTERED_MSISDN = "registered_msisdn"
            const val EMPTY_MSISDN = "empty_msisdn"
            const val MAX_REACHED_MSISDN = "max_reached_msisdn"
            const val INVALID_EMAIL = "invalid_email"
            const val REQUEST_FAILED = "request_failed"
        }
    }

    interface UpdateInactivePhoneQueryConstant {
        companion object {
            const val QUERY_CHECK_PHONE_NUMBER_STATUS = "check_phone_number_status"
            const val QUERY_UPDATE_PHONE_EMAIL = "update_phone_email"
            const val QUERY_VALIDATE_USER_DATA = "validate_user_data"
        }
    }
}
