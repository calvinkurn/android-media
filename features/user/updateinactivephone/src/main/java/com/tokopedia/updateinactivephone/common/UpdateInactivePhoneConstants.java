package com.tokopedia.updateinactivephone.common;

public interface UpdateInactivePhoneConstants {


    interface OPERATION_NAME {
        String CHECK_USER_STATUS = "validateInactivePhoneQuery";
        String VALIDATE_USER_DATA = "validateInactiveNewPhoneQuery";
    }

    public interface Constants {
        String IS_DUPLICATE_REQUEST = "is_duplicate_request";
        String FILE_TO_UPLOAD = "fileToUpload";
        String USERID = "user_id";
        String ID = "id";
        String TOKEN = "token";
        String SERVER_ID = "server_id";
        String IMAGE_UPLOAD_URL = "image_upload_url";
        String RESOLUTION = "resolution";
        String PARAM_KTP_IMAGE_PATH = "ktp_image_path";
        String PARAM_BANK_BOOK_IMAGE_PATH = "account_image_path";
        String PARAM_DEVICE_ID = "device_id";
        String PARAM_FILE_TO_UPLOAD = "fileToUpload";

        String PARAM_KTP_IMAGE_ID = "ktp";
        String PARAM_BANKBOOK_IMAGE_ID = "tabungan";

        String USER_EMAIL = "user_mail";
        String USER_PHONE = "user_phone";

    }

    interface QUERY_CONSTANTS {
        String PHONE = "phone";
        String EMAIL = "email";
        String USER_ID = "userId";
        String OLD_PHONE = "oldPhone";
        String FILE_UPLOADED = "fileUploaded";
    }

    public interface RESPONSE_CONSTANTS {

        String INVALID_PHONE = "invalid_msisdn";
        String PHONE_TOO_SHORT = "too_short_msisdn";
        String PHONE_TOO_LONG = "too_long_msisdn";
        String INVALID_FILE_UPLOADED = "invalid_uploaded_file";
        String PHONE_BLACKLISTED = "blacklisted_msisdn";
        String PHONE_NOT_REGISTERED = "unregistered_msisdn";
        String PHONE_WITH_REGISTERED_EMAIL = "registered_email";
        String PHONE_WITH_PENDING_REQUEST = "has_pending_request";
        String UNREGISTERED_USER = "unregistered_user";
        String SERVER_ERROR = "server_error";
        String WRONG_USER_ID = "wrong_userid";
        String SAME_MSISDN = "same_msisdn";
        String REGISTERED_MSISDN = "registered_msisdn";
        String EMPTY_MSISDN = "empty_msisdn";
        String MAX_REACHED_MSISDN = "max_reached_msisdn";
        String INVALID_EMAIL = "invalid_email";
        String REQUEST_FAILED = "request_failed";
    }
}
