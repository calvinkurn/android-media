package com.tokopedia.posapp.product.common;

/**
 * @author okasurya on 4/2/18.
 */

public class ProductConstant {
    public static class Key {
        public static final String OFFSET = "offset";
        public static final String LIMIT = "limit";
        public static final String ETALASE_ID = "etalase_id";
        public static final String PRODUCT_ID = "product_id";
        public static final String SHOP_ID = "shop_id";
        public static final String ETALASE = "etalase";
        public static final String KEYWORD = "keyword";
        public static final String PER_PAGE = "perpage";
        public static final String PAGE = "page";
        public static final String OUTLET_ID = "outlet_id";
        public static final String EDIT_PRODUCT_REQUEST = "edit_product_request";
        public static final String OUTLET_NAME = "outlet_name";
    }

    public static class Status {
        public static final int ONLINE_PRICE_HIDDEN = 0;
        public static final int ONLINE_PRICE_SHOW = 1;
        public static final int LOCAL_PRICE_HIDDEN = 2;
        public static final int LOCAL_PRICE_SHOW = 3;
    }
}
