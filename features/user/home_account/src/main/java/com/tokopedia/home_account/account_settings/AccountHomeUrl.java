package com.tokopedia.home_account.account_settings;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.url.TokopediaUrl;

/**
 * @author okasurya on 9/14/18.
 */
public class AccountHomeUrl {
    public static String BASE_MOBILE_URL = TokopediaUrl.Companion.getInstance().getMOBILEWEB();
    public final static String URL_TOKOPEDIA_CORNER = BASE_MOBILE_URL + "tokopedia-corner";
    public final static String APPLINK_TOKOPEDIA_CORNER = String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TOKOPEDIA_CORNER);
}