package com.tokopedia.feedplus.data.api;

import com.tokopedia.url.TokopediaUrl;

/**
 * @author by nisie on 10/3/18.
 */
public class FeedUrl {
    public static String GRAPHQL_DOMAIN = TokopediaUrl.Companion.getInstance().getGQL();
    public static String BASE_DOMAIN = TokopediaUrl.Companion.getInstance().getWS();
    public static String TOME_DOMAIN = TokopediaUrl.Companion.getInstance().getTOME();
    public static String MOBILE_DOMAIN = TokopediaUrl.Companion.getInstance().getMOBILEWEB();
    public static String URL_WEBVIEW_OS = MOBILE_DOMAIN + "official-store/mobile";
}
