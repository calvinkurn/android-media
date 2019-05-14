package com.tokopedia.shop.common.constant;

import com.tokopedia.config.url.TokopediaUrl;

/**
 * Created by nathan on 10/24/17.
 */

public class ShopCommonUrl {

    public static String BASE_URL = TokopediaUrl.Companion.getInstance().getTOME();
    public static String BASE_WS_URL = TokopediaUrl.Companion.getInstance().getWS();

    public static final String SHOP_INFO_PATH = "v1/web-service/shop/get_shop_info";

    public static final String TOGGLE_FAVOURITE_SHOP = "v4/action/favorite-shop/fav_shop.pl";

}
