package com.tokopedia.topads.common.constant;

import android.text.format.DateUtils;

import com.tokopedia.url.TokopediaUrl;

/**
 * Created by hadi.putra on 23/04/18.
 */

public class TopAdsCommonConstant {
    public static String BASE_DOMAIN_URL = TokopediaUrl.getInstance().getTA();

    public static final String PATH_TOPADS_SHOP_DEPOSIT = "/v1.1/dashboard/deposit";
    public static final String PATH_TOPADS_TOTAL_ADS = "/v1.1/dashboard/total_ad";
    public static final String PATH_CHECK_PROMO = "v1/promo/check";
    public static final String TOPADS_FREE_CLAIM_URL = "https://ta.tokopedia.com/v1/credits/claim";
    public static final String TOPADS_SELLER_CENTER = "https://seller.tokopedia.com/about-topads/";

    public static final String PARAM_SHOP_ID = "shop_id";

    public static final String REQUEST_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final int CACHE_EXPIRED_TIME = (int) (DateUtils.HOUR_IN_MILLIS / DateUtils.SECOND_IN_MILLIS);

    public static final int MAX_DATE_RANGE = 60;

    public static final String URL_ONECLICKPROMO = "https://ta.tokopedia.com/v2/manage/oneclickpromo";
}
