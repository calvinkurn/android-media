package com.tokopedia.otp.cotp.data;

import com.tokopedia.config.url.TokopediaUrl;

/**
 * @author by nisie on 4/25/18.
 */

public class CotpUrl {

    public static String BASE_URL = TokopediaUrl.Companion.getInstance().getACCOUNTS();

    private static final String BASE_OTP = "/otp";
    static final String REQUEST_OTP = BASE_OTP + "/request";
    static final String VALIDATE_OTP = BASE_OTP + "/validate";
    static final String REQUEST_OTP_EMAIL = BASE_OTP + "/email/request";
    static final String PATH_GET_METHOD_LIST = BASE_OTP + "/ws/mode-list";

}
