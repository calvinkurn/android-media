package com.tokopedia.changephonenumber;

import com.tokopedia.url.TokopediaUrl;

/**
 * @author by alvinatin on 26/09/18.
 */

public class ChangePhoneNumberUrl {

    public static String BASE_URL = TokopediaUrl.Companion.getInstance().getACCOUNTS();

    public class MSISDN {
        public static final String VERIFY_PHONE_NUMBER = "/api/msisdn/verify-msisdn";
        public static final String CHANGE_PHONE_NUMBER = "/api/msisdn/change-msisdn";
    }

    public class Ktp {
        public static final String CHECK_STATUS = "/api/ktp/check-status";
    }

    public class Image {
        public static final String VALIDATE_SIZE = "/api/image/validate-size";
        public static final String GET_UPLOAD_HOST = "/api/image/upload-host";
        public static final String SUBMIT_DETAIL = "/api/image/submit-detail";
    }

    public class ChangeMSISDN {
        public static final String GET_WARNING = "/api/v1/change-msisdn/get-warning";
        public static final String SEND_EMAIL = "/api/v1/change-msisdn/update";
        public static final String VALIDATE = "/api/v1/change-msisdn/validate";
        public static final String VALIDATE_EMAIL_CODE = "/api/v1/change-msisdn/validate-code";
    }

    public class OTP {
        private static final String BASE_OTP = "/otp";
        public static final String VALIDATE_OTP_STATUS = BASE_OTP + "/get-validation-status";
    }
}
