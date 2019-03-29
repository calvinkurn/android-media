package com.tokopedia.user_identification_common;

/**
 * @author by nisie on 12/11/18.
 */
public interface KYCConstant {

    String ERROR_STATUS_UNKNOWN = "1001";
    String ERROR_MESSAGE_EMPTY = "1002";
    String ERROR_UPLOAD_IMAGE = "1003";
    String ERROR_REGISTER = "1004";
    String UNHANDLED_RESPONSE = "1005";

    int KYC_PROJECT_ID = 1;

    String EXTRA_STRING_IMAGE_RESULT = "image_result";

    int REQUEST_CODE_CAMERA_KTP = 1010;
    int REQUEST_CODE_CAMERA_FACE = 2020;

    int STATUS_ERROR = -2;
    int STATUS_REJECTED = -1;
    int STATUS_PENDING = 0;
    int STATUS_VERIFIED = 1;
    int STATUS_EXPIRED = 2;
    int STATUS_NOT_VERIFIED = 3;

    int IS_SUCCESS_GET_STATUS = 1;

    int IS_ALL_MUTATION_SUCCESS = 2;

    int IS_FILE_IMAGE_TOO_BIG = -5;

    String PARAM_KYC_SRC = "source";
    String VALUE_KYC_SRC_SELLER = "seller";
    String EXTRA_IS_SOURCE_SELLER = "source_seller";

}
