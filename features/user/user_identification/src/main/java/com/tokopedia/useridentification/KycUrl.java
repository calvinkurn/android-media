package com.tokopedia.useridentification;

import com.tokopedia.applink.ApplinkConst;

/**
 * @author by nisie on 16/11/18.
 */
public class KycUrl {

    public final static String ICON_NOT_VERIFIED = "https://ecs7.tokopedia.net/img/android/others/kyc_ilus_intro.png";
    public final static String ICON_WAITING = "https://ecs7.tokopedia.net/img/android/others/kyc_ilus_waiting.png";
    public final static String ICON_SUCCESS_VERIFY = "https://ecs7.tokopedia.net/img/android/others/kyc_ilus_berhasil_email.png";
    public final static String ICON_FAIL_VERIFY = "https://ecs7.tokopedia.net/img/android/others/kyc_ilus_gagal_email_copy.png";
    public final static String KTP_OK = "https://ecs7.tokopedia.net/img/android/others/ktp_ok.png";
    public final static String KTP_FAIL = "https://ecs7.tokopedia.net/img/android/others/ktp_fail.png";
    public final static String SELFIE_OK = "https://ecs7.tokopedia.net/img/android/others/selfie_ok.png";
    public final static String SELFIE_FAIL = "https://ecs7.tokopedia.net/img/android/others/selfie_fail.png";

    public static String BASE_URL = "https://m.tokopedia.com/";
    public final static String URL_TERMS_AND_CONDITION = BASE_URL + "terms/merchantkyc";
    public final static String APPLINK_TERMS_AND_CONDITION = String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TERMS_AND_CONDITION);
}
