package com.tokopedia.shop.common.constant;

/**
 * Created by hendry on 18/07/18.
 */

public class ShopPageConstant {
    public static final int ETALASE_TO_SHOW = 5;
    public static final int MAXIMUM_SELECTED_ETALASE_LIST = 4;

    public static final int DEFAULT_PER_PAGE = 10;
    public static final int ETALASE_HIGHLIGHT_COUNT = 5;

    public static final int DEFAULT_MEMBERSHIP_POSITION = 0;
    public static final int DEFAULT_MERCHANT_VOUCHER_POSITION = 1;
    public static final int DEFAULT_FEATURED_POSITION = 2;
    public static final int DEFAULT_ETALASE_HIGHLIGHT_POSITION = 3;
    public static final int DEFAULT_ETALASE_POSITION = 4;
    public static final int DEFAULT_ETALASE_TITLE_POSITION = 5;
    public static final int ITEM_OFFSET = 6;

    // if the count data <= SMALL_DATA_LIMIT, the data become vertical list
    public static final int SMALL_DATA_LIMIT = 2;

    public static final String MODERATE_STATUS_QUERY = "moderate_status_query";
    public static final String MODERATE_REQUEST_QUERY = "moderate_request_query";
    public static final String SHOP_FAVORITE_QUERY = "shop_favorite_query";

    public static final String GO_TO_MEMBERSHIP_DETAIL = "membership detail";
    public static final String GO_TO_MEMBERSHIP_REGISTER = "membership register";
    public static final String EMPTY_PRODUCT_SEARCH_IMAGE_URL =
            "https://ecs7.tokopedia.net/img/android/others/product_search_not_found.png";

    public static final String KEY_MEMBERSHIP_DATA_MODEL = "KEY_MEMBERSHIP_DATA_MODEL_POSITION";
    public static final String KEY_MERCHANT_VOUCHER_DATA_MODEL = "KEY_MERCHANT_VOUCHER_DATA_MODEL_POSITION";
    public static final String KEY_FEATURED_PRODUCT_DATA_MODEL = "KEY_FEATURED_PRODUCT_DATA_MODEL_POSITION";
    public static final String KEY_ETALASE_HIGHLIGHT_DATA_MODEL = "KEY_ETALASE_HIGHLIGHT_DATA_MODEL_POSITION";
    public static final String KEY_ETALASE_DATA_MODEL = "KEY_ETALASE_DATA_MODEL";
    public static final String KEY_ETALASE_TITLE_DATA_MODEL = "KEY_ETALASE_TITLE_DATA_MODEL_POSITION";
    public static final String KEY_SHOP_PRODUCT_FIRST_DATA_MODEL = "KEY_SHOP_PRODUCT_FIRST_DATA_MODEL";
    public static final String KEY_SHOP_PRODUCT_ADD_DATA_MODEL = "KEY_SHOP_PRODUCT_ADD_DATA_MODEL";
    public static final String KEY_SHOP_SELLER_EMPTY_PRODUCT_DATA_MODEL = "KEY_SHOP_SELLER_EMPTY_PRODUCT_DATA_MODEL";
    public static final String KEY_SHOP_BUYER_EMPTY_PRODUCT_DATA_MODEL = "KEY_SHOP_BUYER_EMPTY_PRODUCT_DATA_MODEL";
    public static final String URL_IMAGE_SELLER_EMPTY_STATE_BACKGROUND_PATTERN = "https://ecs7.tokopedia.net/android/shop_page/seller_empty_product_card_background.png";
    public static final String URL_IMAGE_SELLER_EMPTY_STATE_STAR_ICON = "https://ecs7.tokopedia.net/android/shop_page/seller_empty_product_card_star.png";
    public static final String URL_IMAGE_SELLER_EMPTY_STATE_TOKOPEDIA_ICON = "https://ecs7.tokopedia.net/android/shop_page/seller_empty_product_card_icon.png";
    public static final String URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE = "https://ecs7.tokopedia.net/android/shop_page/image_product_empty_state_buyer.png";
}
