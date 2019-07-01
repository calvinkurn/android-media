package com.tokopedia.reputation.common.constant;

import com.tokopedia.config.url.TokopediaUrl;

/**
 * Created by nathan on 10/24/17.
 */

public class ReputationCommonUrl {

    public static String BASE_URL = TokopediaUrl.Companion.getInstance().getWS();

    public static final String STATISTIC_SPEED_URL = "/reputationapp/statistic/api/v1/shop/{shop_id}/speed";
    public static final String STATISTIC_SPEED_URL_V2 = "/reputationapp/statistic/api/v2/shop/{shop_id}/speed/daily";

}
