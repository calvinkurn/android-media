package com.tokopedia.updateinactivephone.common

interface UpdateInactivePhoneConstants {

    interface OPERATION_NAME {
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

    interface QUERY_CONSTANTS {
        companion object {
            val PHONE = "phone"
            val EMAIL = "email"
            val USER_ID = "userId"
            val OLD_PHONE = "oldPhone"
            val FILE_UPLOADED = "fileUploaded"
        }
    }

    interface RESPONSE_CONSTANTS {
        companion object {

            val INVALID_PHONE = "invalid_msisdn"
            val PHONE_TOO_SHORT = "too_short_msisdn"
            val PHONE_TOO_LONG = "too_long_msisdn"
            val INVALID_FILE_UPLOADED = "invalid_uploaded_file"
            val PHONE_BLACKLISTED = "blacklisted_msisdn"
            val PHONE_NOT_REGISTERED = "unregistered_msisdn"
            val PHONE_WITH_REGISTERED_EMAIL = "registered_email"
            val PHONE_WITH_PENDING_REQUEST = "has_pending_request"
            val UNREGISTERED_USER = "unregistered_user"
            val SERVER_ERROR = "server_error"
            val WRONG_USER_ID = "wrong_userid"
            val SAME_MSISDN = "same_msisdn"
            val REGISTERED_MSISDN = "registered_msisdn"
            val EMPTY_MSISDN = "empty_msisdn"
            val MAX_REACHED_MSISDN = "max_reached_msisdn"
            val INVALID_EMAIL = "invalid_email"
            val REQUEST_FAILED = "request_failed"
        }
    }
}
