package com.tokopedia.home.constant;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.url.TokopediaUrl;

public class BerandaUrl {

    public static String DOMAIN_URL = TokopediaUrl.Companion.getInstance().getWEB();
    public static String GRAPHQL_URL = TokopediaUrl.Companion.getInstance().getGQL();
    public static String ACE_URL = "https://ace.tokopedia.com/";

    public static final String PROMO_URL = DOMAIN_URL + "promo/";
    public static final String FLAG_APP = "?flag_app=1";

    public static final String PLAY_CHANNEL_LIST = String.format("%s?url=%s",
            ApplinkConst.WEBVIEW, DOMAIN_URL + "play/channels/?is_app=1");
}
