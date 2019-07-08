package com.tokopedia.abstraction.constant;

import com.tokopedia.config.url.TokopediaUrl;

/**
 * Created by hendry on 17/01/18.
 */

public class AbstractionBaseURL {
    // will be overwritten in respective MainApplication (SellerMainApplication or ConsumerMainApplication)
    public static String JS_DOMAIN = TokopediaUrl.Companion.getInstance().getJS();
    public static String MOBILE_DOMAIN = "https://m.tokopedia.com/";
}
