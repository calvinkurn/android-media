package com.tokopedia.digital.newcart.data;

import com.tokopedia.config.url.TokopediaUrl;

public class DigitalDealsUrl {
    public static String BASE_URL = TokopediaUrl.Companion.getInstance().getBOOKING();
    static final String PATH_CATEGORY = "v1/api/c/deal/children";
}
