package com.tokopedia.useridentification;

import com.tokopedia.applink.ApplinkConst;

/**
 * @author by nisie on 16/11/18.
 */
public interface KycUrl {

    String ICON_NOT_VERIFIED = "https://ecs7.tokopedia.net/img/android/others/kyc_ilus_intro.png";
    String ICON_WAITING = "https://ecs7.tokopedia.net/img/android/others/kyc_ilus_waiting.png";
    String ICON_SUCCESS_VERIFY = "https://ecs7.tokopedia.net/img/android/others/kyc_ilus_berhasil_email.png";
    String ICON_FAIL_VERIFY = "https://ecs7.tokopedia.net/img/android/others/kyc_ilus_gagal_email_copy.png";
    String KTP_OK = "https://ecs7.tokopedia.net/img/android/others/ktp_ok.png";
    String KTP_FAIL = "https://ecs7.tokopedia.net/img/android/others/ktp_fail.png";
    String SELFIE_OK = "https://ecs7.tokopedia.net/img/android/others/selfie_ok.png";
    String SELFIE_FAIL = "https://ecs7.tokopedia.net/img/android/others/selfie_fail.png";

    //TODO alvin change this url
    String URL_TERMS_AND_CONDITION = "https://31-feature-m-staging.tokopedia.com/terms/merchantkyc";
    String APPLINK_TERMS_AND_CONDITION = String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TERMS_AND_CONDITION);
}
