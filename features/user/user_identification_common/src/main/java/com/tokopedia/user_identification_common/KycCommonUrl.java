package com.tokopedia.user_identification_common;

import com.tokopedia.applink.ApplinkConst;

/**
 * @author by nisie on 16/11/18.
 */
public class KycCommonUrl {
    public static String TERMS_CONDITION = "https://m.tokopedia.com/myshop/verify/terms";

    public static String BASE_URL = "https://m.tokopedia.com/";
    public final static String URL_TERMS_AND_CONDITION = BASE_URL + "terms/merchantkyc";
    public final static String APPLINK_TERMS_AND_CONDITION = String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TERMS_AND_CONDITION);

}
