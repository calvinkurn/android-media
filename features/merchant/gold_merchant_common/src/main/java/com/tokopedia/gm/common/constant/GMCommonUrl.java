package com.tokopedia.gm.common.constant;

import com.tokopedia.url.TokopediaUrl;

/**
 * Created by nathan on 10/24/17.
 */

public class GMCommonUrl {

    public static String BASE_URL = TokopediaUrl.Companion.getInstance().getGOLDMERCHANT();

    public static final String SET_CASHBACK_PRODUCTS = "v1/cashback/set";
    public static final String FEATURED_PRODUCT_URL = "v1/mobile/featured_product/{shop_id}";
    public static final String GET_CASHBACK_PRODUCTS = "v1/tx/cashback";
    public static final String SHOPS_SCORE_STATUS = "v1/shopstats/shopscore/sum/";

    public static final String SHOP_INTERRUPT_PAGE = "https://www.tokopedia.com/shop-interrupt";
}
