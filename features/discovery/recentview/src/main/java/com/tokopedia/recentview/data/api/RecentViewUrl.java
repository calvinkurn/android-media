package com.tokopedia.recentview.data.api;

import com.tokopedia.config.url.TokopediaUrl;

public class RecentViewUrl {
    public static String MOJITO_DOMAIN = TokopediaUrl.Companion.getInstance().getMOJITO();
    public static final String GET_RECENT_VIEW_URL = "users/{userId}/recentview/products/v1";

}
