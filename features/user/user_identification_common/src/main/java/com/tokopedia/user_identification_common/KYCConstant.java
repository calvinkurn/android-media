package com.tokopedia.user_identification_common;

/**
 * @author by nisie on 12/11/18.
 */
public class KYCConstant {

    public static final String ERROR_STATUS_UNKNOWN = "1001";
    public static final String ERROR_MESSAGE_EMPTY = "1002";

    public static final int KYC_PROJECT_ID = 1;

    public final static String EXTRA_STRING_IMAGE_RESULT = "image_result";

    public static final int REQUEST_CODE_CAMERA_KTP = 1010;
    public static final int REQUEST_CODE_CAMERA_FACE = 2020;

    public static final int STATUS_ERROR = -2;
    public static final int STATUS_REJECTED = -1;
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_VERIFIED = 1;
    public static final int STATUS_EXPIRED = 2;
    public static final int STATUS_NOT_VERIFIED = 3;

    public static final int IS_SUCCESS_GET_STATUS = 1;
}
