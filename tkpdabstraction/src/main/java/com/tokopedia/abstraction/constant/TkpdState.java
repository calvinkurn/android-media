package com.tokopedia.abstraction.constant;

public class TkpdState {

    public static final String LOYALTY_GROUP_CHAT = "LOYALTY_GROUP_CHAT";
    public static final String TOPCHAT = "TOPCHAT";

    public class UpdateState {
        public static final int NO_UPDATE = 0;
        public static final int MUST_UPDATE = 1;
        public static final int OPTIONAL_UPDATE = 2;
    }


    public class ProductService {
        /* BROADCAST INTENT FILTER */
        public static final String BROADCAST_ADD_PRODUCT = "BROADCAST_ADD_PRODUCT";

        /* TYPE */
        public static final String SERVICE_TYPE = "SERVICE_TYPE";
        public static final int ADD_PRODUCT = 10;
        public static final int ADD_PRODUCT_WITHOUT_IMAGE = 11;
        public static final int EDIT_PRODUCT = 12;
        public static final int DELETE_PRODUCT = 13;
        public static final int INVALID_TYPE = -1;


        /* STATUS */
        public static final String STATUS_FLAG = "STATUS_FLAG";
        public static final int STATUS_RUNNING = 1;
        public static final int STATUS_DONE = 2;
        public static final int STATUS_ERROR = 3;

        /* DATA */
        public static final String PRODUCT_DB_ID = "PRODUCT_DB_ID";
        public static final long NO_PRODUCT_DB = -1;
        public static final String PRODUCT_POS = "PRODUCT_POS";
        public static final int NO_PRODUCT_POS = -1;
        public static final String PRODUCT_ID = "PRODUCT_ID";
        public static final int NO_PRODUCT_ID = -1;
        public static final String MESSAGE_ERROR_FLAG = "MESSAGE_ERROR_FLAG";
        public static final String INVALID_MESSAGE_ERROR = "default";


        public static final String PRODUCT_NAME = "PRODUCT_NAME";
        public static final String IMAGE_URI = "IMAGE_URI";
        public static final String PRODUCT_DESCRIPTION = "PRODUCT_DESCRIPTION";
        public static final String PRODUCT_URI = "PRODUCT_URI";
    }

    public class DrawerPosition {
        public static final int MANAGE_PRODUCT = 8;
    }
}