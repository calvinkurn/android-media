package com.tokopedia.home.constant;

import com.tokopedia.config.url.TokopediaUrl;

public class BerandaUrl {

    public static String DOMAIN_URL = TokopediaUrl.Companion.getInstance().getWEB();
    public static String GRAPHQL_URL = TokopediaUrl.Companion.getInstance().getGQL();
    public static String ACE_URL = "https://ace.tokopedia.com/";
    public static final String PROMO_URL = DOMAIN_URL + "promo/";
    public static final String FLAG_APP = "?flag_app=1";
}
