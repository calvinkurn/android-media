package com.tokopedia.loginphone.checkregisterphone.data;

import com.tokopedia.config.url.TokopediaUrl;

/**
 * @author by nisie on 31/10/18.
 */
public class CheckMsisdnUrl {

    public static String BASE_DOMAIN = TokopediaUrl.Companion.getInstance().getACCOUNTS();

    public static final String PATH_REGISTER_MSISDN_CHECK = "/api/v1/account/register/msisdn/check";
}
