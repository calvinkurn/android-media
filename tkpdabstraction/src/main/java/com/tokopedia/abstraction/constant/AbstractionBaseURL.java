package com.tokopedia.abstraction.constant;

import com.tokopedia.url.TokopediaUrl;

/**
 * Created by hendry on 17/01/18.
 */

public class AbstractionBaseURL {
    // will be overwritten in respective MainApplication (SellerMainApplication or ConsumerMainApplication)
    public static String JS_DOMAIN = TokopediaUrl.Companion.getInstance().getJS();
}
