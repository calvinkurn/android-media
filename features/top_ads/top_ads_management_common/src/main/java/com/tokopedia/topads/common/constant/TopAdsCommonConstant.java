package com.tokopedia.topads.common.constant;

import android.text.format.DateUtils;

import com.tokopedia.url.TokopediaUrl;

/**
 * Created by hadi.putra on 23/04/18.
 */

public class TopAdsCommonConstant {
    public static String BASE_DOMAIN_URL = TokopediaUrl.getInstance().getTA();

    public static final String TOPADS_SELLER_CENTER = "https://seller.tokopedia.com/about-topads/";

    public static final String PARAM_SHOP_ID = "shop_id";
    public static final String DIRECTED_FROM_MANAGE_OR_PDP = "directed_from_manage_or_pdp";

    public static final String REQUEST_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final int CACHE_EXPIRED_TIME = (int) (DateUtils.HOUR_IN_MILLIS / DateUtils.SECOND_IN_MILLIS);

    public static final String TOPADS_GRAPHQL_TA_URL = "https://gql.tokopedia.com/graphql/ta";

}
