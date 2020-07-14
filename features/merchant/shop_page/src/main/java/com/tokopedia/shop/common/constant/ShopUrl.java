package com.tokopedia.shop.common.constant;

import com.tokopedia.url.TokopediaUrl;

/**
 * Created by nathan on 10/24/17.
 */

public class ShopUrl extends ShopCommonUrl {

    public static String BASE_ACE_URL = TokopediaUrl.Companion.getInstance().getACE();
    public static String BASE_OFFICIAL_STORE_URL = TokopediaUrl.Companion.getInstance().getMOJITO();

    public static final String SHOP_PRODUCT_PATH = "v1/web-service/shop/get_shop_product";
    public static final String SHOP_DYNAMIC_FILTER = "v1/dynamic_attributes";
    public static final String SHOP_FAVOURITE_USER = "/v4/shop/get_people_who_favorite_myshop.pl";

    public static final String SHOP_PRODUCT_OS_DISCOUNT = "/os/v1/campaign/product/info";

    public static final String SHOP_HELP_CENTER = "https://www.tokopedia.com/bantuan/status-toko-tidak-aktif/";

}