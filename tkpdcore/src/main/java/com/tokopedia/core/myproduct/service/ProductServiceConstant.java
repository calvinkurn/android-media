package com.tokopedia.core.myproduct.service;

/**
 * Created by m.normansyah on 29/12/2015.
 */
public interface ProductServiceConstant {
    String TAG = "MNORMANSYAH";
    String messageTAG = "ProductServiceConstant : ";

    // common
    String TYPE = "type";
    int INVALID_TYPE = -1;
    String RETRY_FLAG = "RETRY_FLAG";
    String RECEIVER = "receiver";
    int NUMBER_OF_TRY = 1;
    int TIMEOUT_TIME = 10000;
    String IS_NEED_LOGIN = "IS_NEED_LOGIN";

    // error
    String MESSAGE_ERROR_FLAG ="MESSAGE_ERROR_FLAG";
    String INVALID_MESSAGE_ERROR = "default";
    String NETWORK_ERROR_FLAG ="NETWORK_ERROR_FLAG";
    int INVALID_NETWORK_ERROR_FLAG = -1;

    int STATUS_RUNNING = 0;
    int STATUS_FINISHED = 1;
    int STATUS_ERROR = 2;

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    String ACTION_FOO = "com.tokopedia.tkpd.revamp.service.action.FOO";
    String ACTION_BAZ = "com.tokopedia.tkpd.revamp.service.action.BAZ";

    String EXTRA_PARAM1 = "com.tokopedia.tkpd.revamp.service.extra.PARAM1";
    String EXTRA_PARAM2 = "com.tokopedia.tkpd.revamp.service.extra.PARAM2";

    // ADDPRODUCT
    int ADD_PRODUCT = 10;
    int ADD_PRODUCT_WITHOUT_IMAGE = 11;
    int EDIT_PRODUCT = 12;
    int DELETE_PRODUCT = 13;
    int UPDATE_RETURNABLE_NOTE_ADD_PRODUCT = 14;
    int ADD_RETURNABLE_NOTE_ADD_PRODUCT = 15;
    String PRODUCT_DATABASE_ID = "PRODUCT_DATABASE_ID";
    String PRODUCT_POSITION = "PRODUCT_POSITION";
    String ADD_PRODUCT_SHOW_DIALOG = "ADD_PRODUCT_SHOW_DIALOG";
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
    String PRODUCT_ID = "PRODUCT_ID";

    // EDIT SHOP NOTE
    String EDIT_SHOP_NOTE_PARAM = "EDIT_SHOP_NOTE_PARAM";
    String ADD_SHOP_NOTE_PARAM= "ADD_SHOP_NOTE_PARAM";
    String EDIT_SHOP_NOTE_DATA = "EDIT_SHOP_NOTE_DATA";
    String SHOP_DOMAIN = "SHOP_DOMAIN";
    String SHOP_DOMAIN_PARAM = "shop_domain";
    String NOTE_CONTENT = "note_content";
    String NOTE_ID = "note_id";
    String TERMS = "terms";
    String NOTE_TITLE = "note_title";
    String RETURNABLE_NOTE = "RETURNABLE_NOTE";
    String RETURNABLE_NOTE_DETAIL = "RETURNABLE_NOTE_DETAIL";
    String RETURNABLE_NOTE_CONTENT = "RETURNABLE_NOTE_CONTENT";

    //local broadcast manage
    String ACTION_COMPLETED_ADD_PRODUCT = "action_completed_add_product";

}
