package com.tokopedia.tkpdreactnative.react.fingerprint.utils;

import com.tokopedia.config.url.TokopediaUrl;

/**
 * Created by zulfikarrahman on 3/27/18.
 */

public class FingerprintConstantRegister {
    public static final String OTP_FINGERPRINT_ADD = "/otp/fingerprint/add";
    public static final String V2_FINGERPRINT_PUBLICKEY_SAVE = "/v2/fingerprint/publickey/save";
    public static String ACCOUNTS_DOMAIN = TokopediaUrl.Companion.getInstance().getACCOUNTS();
    public static String TOP_PAY_DOMAIN =TokopediaUrl.Companion.getInstance().getPAY_ID();
}
