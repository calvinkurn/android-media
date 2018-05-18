package com.tokopedia.topads.sourcetagging.constant;

import java.util.concurrent.TimeUnit;

/**
 * Created by hadi.putra on 16/04/18.
 */

public class TopAdsSourceTaggingConstant {
    public static final String KEY_SOURCE_PREFERENCE = "topads_source";
    public static final String KEY_TAGGING_SOURCE = "source";

    public static final String PARAM_EXTRA_SHOP_ID = "shop_id";
    public static final String PARAM_EXTRA_ITEM_ID = "product_id";
    public static final String PARAM_EXTRA_USER_ID = "user_id";

    public static final String PARAM_KEY_TIMESTAMP = "timestamp";
    public static final String PARAM_KEY_SOURCE = "source";

    public static final String SEPARATOR = "#separator#";

    public static final long EXPIRING_TIME_IN_SECOND = TimeUnit.SECONDS.toMillis(2);
}
