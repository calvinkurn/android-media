package com.tokopedia.gm.common.constant;

/**
 * Created by nathan on 10/24/17.
 */

public class GMCommonUrl {

    public static String BASE_URL = "https://goldmerchant.tokopedia.com";

    public static final String SET_CASHBACK_PRODUCTS = "/v1/cashback/set";
    public static final String FEATURED_PRODUCT_URL = "/v1/mobile/featured_product/{shop_id}";
    public static final String GET_CASHBACK_PRODUCTS = "/v1/tx/cashback";
    public static final String GET_SHOP_STATUS = "/v2/shop/{shop_id}/subscription?include_os=true";
    public static final String SHOPS_SUBSCRIPTION = "/v2/shops/subscription";
    public static final String SHOPS_SUBSCRIPTIONS_AUTO_EXTEND = "/v2/shops/subscriptions/auto_extend";
    public static final String SHOPS_SCORE_STATUS = "/v1/shopstats/shopscore/sum/";

}
