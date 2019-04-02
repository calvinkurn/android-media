package com.tokopedia.loginphone.common.data;

/**
 * @author by nisie on 29/10/18.
 */
public class LoginRegisterPhoneUrl {

    public static String BASE_DOMAIN = "https://www.tokocash.com/";

    public static final String REQUEST_OTP_LOGIN = "oauth/otp";
    public static final String VERIFY_OTP_LOGIN = "oauth/verify_native";
    public static final String AUTHORIZE = "oauth/authorize_native";
    public static final String CHECK_MSISDN = "oauth/check/msisdn";

    public static final String URL_IMAGE_TOKOCASH_NO_ACCOUNT = "https://ecs7.tokopedia" +
            ".net/img/android/others/ic_tokocash_no_account.png";
    public static final String URL_IMAGE_TOKOCASH_PHONE_NOT_CONNECTED = "https://ecs7.tokopedia" +
            ".net/img/android/others/ic_tokocash_phone_not_connected.png";
}
