package com.tokopedia.phoneverification;

import com.tokopedia.url.TokopediaUrl;

/**
 * @author by alvinatin on 12/10/18.
 */

public class PhoneVerificationConst {

    public static final String EXTRA_INIT_FRAGMENT = "EXTRA_INIT_FRAGMENT";
    public static final int INIT_STATE_FRAGMENT_HOME = 0;

    public static final String SCREEN_CHANGE_PHONE_NUMBER = "Change Phone Number Page";
    public static final String SCREEN_PHONE_VERIFICATION = "Phone Verification Screen";

    public static String BASE_URL = TokopediaUrl.Companion.getInstance().getACCOUNTS();

    public static final String CHANGE_PHONE_NUMBER = "/api/msisdn/change-msisdn";
    public static final String VERIFY_PHONE_NUMBER = "/api/msisdn/verify-msisdn";

    public static final String URL_TOKOCASH_SHARE = "https://ecs7.tokopedia.net/img/android/others/ic_referral_tokocash.png";

}
