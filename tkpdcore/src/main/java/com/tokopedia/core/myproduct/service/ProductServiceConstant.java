package com.tokopedia.core.myproduct.service;

/**
 * Created by m.normansyah on 29/12/2015.
 */
public interface ProductServiceConstant {
    String TAG = "MNORMANSYAH";
    String messageTAG = "ProductServiceConstant : ";

    // common
    String RECEIVER = "receiver";
    int NUMBER_OF_TRY = 1;
    int TIMEOUT_TIME = 10000;
    String IS_NEED_LOGIN = "IS_NEED_LOGIN";

    // error
    String NETWORK_ERROR_FLAG ="NETWORK_ERROR_FLAG";
    int INVALID_NETWORK_ERROR_FLAG = -1;

    String PRODUCT_EDIT_PHOTOS = "PRODUCT_EDIT_PHOTOS";

    // ADD PRODUCT VALIDATION
    String CLICK_NAME = "click_name";
    String DUPLICATE = "duplicate";
    String PRODUCT_CATALOG_ID = "product_catalog_id";
    String PRODUCT_CONDITION = "product_condition";
    String PRODUCT_DEPARTMENT_ID = "product_department_id";
    String PRODUCT_DESCRIPTION = "product_description";
    String PRODUCT_ETALASE_ID = "product_etalase_id";
    String PRODUCT_ETALASE_NAME = "product_etalase_name";
    String PRODUCT_MIN_ORDER = "product_min_order";
    String PRODUCT_MUST_INSURANCE = "product_must_insurance";
    String PRODUCT_NAME = "product_name";
    String PRODUCT_PHOTO = "product_photo";
    String PRODUCT_PHOTO_DEFAULT = "product_photo_default";
    String PRODUCT_PHOTO_DESCRIPTION = "product_photo_desc";
    String PRODUCT_PRICE ="product_price";
    String PRODUCT_PRICE_CURRENCY ="product_price_currency";
    String PRODUCT_RETURNABLE ="product_returnable";
    String PRODUCT_UPLOAD_TO ="product_upload_to";
    String PRODUCT_WEIGHT ="product_weight";
    String PRODUCT_WEIGHT_UNIT ="product_weight_unit";
    String PRODUCT_PRC_1 ="prd_prc_1";
    String PRODUCT_PRC_2 ="prd_prc_2";
    String PRODUCT_PRC_3 ="prd_prc_3";
    String PRODUCT_PRC_4 ="prd_prc_4";
    String PRODUCT_PRC_5 ="prd_prc_5";
    String QTY_MAX_1 ="qty_max_1";
    String QTY_MAX_2 ="qty_max_2";
    String QTY_MAX_3 ="qty_max_3";
    String QTY_MAX_4 ="qty_max_4";
    String QTY_MAX_5 ="qty_max_5";
    String QTY_MIN_1 ="qty_min_1";
    String QTY_MIN_2 ="qty_min_2";
    String QTY_MIN_3 ="qty_min_3";
    String QTY_MIN_4 ="qty_min_4";
    String QTY_MIN_5 ="qty_min_5";
    String PO_PROCESS_TYPE ="po_process_type";
    String PO_PROCESS_VALUE ="po_process_value";
    String SERVER_ID ="server_id";
    int DUPLICATE_COPY_FROM_OTHER_PRODUCT = 1;
    int DUPLICATE_NOT_COPY_FROM_OTHER_PRODUCT = 0;
    int PRODUCT_UPLOAD_TO_GUDANG = 2;
    int PRODUCT_UPLOAD_TO_ETALASE = 1;
    int PO_PROCESS_TYPE_DAY = 1;
    int PO_PROCESS_TYPE_EMPTY = 0;


    // UPLOAD IMAGE - ALREADY AT VALIDATION
    String POST_KEY = "post_key";
    // PRODUCT SUBMIT
    String FILE_UPLOAD_TO = "file_uploaded";

    String STOCK_STATUS = "STOCK_STATUS";
    String SHOP_ID = "SHOP_ID";
    String SHOP_ID_PARAM = "shop_id";

    //local broadcast manage
    String ACTION_COMPLETED_ADD_PRODUCT = "action_completed_add_product";

}
