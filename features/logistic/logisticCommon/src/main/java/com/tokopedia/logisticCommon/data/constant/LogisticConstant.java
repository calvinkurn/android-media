package com.tokopedia.logisticCommon.data.constant;

/**
 * Created by Irfan Khoirul on 21/11/18.
 */

public class LogisticConstant {

    // Private constructor to prevent instantiation
    private LogisticConstant() {

    }

    public static final int REQUEST_CODE_PARAM_CREATE = 101;
    public static final int REQUEST_CODE_PARAM_EDIT = 102;
    public static final int REQUEST_CODE_PICK_DROP_OFF_TRADE_IN = 6352;

    public static final int ADD_NEW_ADDRESS_CREATED = 3333;
    public static final int ADD_NEW_ADDRESS_CREATED_FROM_EMPTY = 3355;

    public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";
    public static final String EXTRA_EXISTING_LOCATION = "EXTRA_EXISTING_LOCATION";
    public static final String EXTRA_IS_FROM_MARKETPLACE_CART = "EXTRA_IS_FROM_MARKETPLACE_CART";
    public static final String EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW";

    public static final String INSTANCE_TYPE_ADD_ADDRESS_FROM_SINGLE_CHECKOUT = "11";
    public static final String INSTANCE_TYPE_ADD_ADDRESS_FROM_SINGLE_CHECKOUT_EMPTY_DEFAULT_ADDRESS = "13";
    public static final String INSTANCE_TYPE_EDIT_ADDRESS_FROM_SINGLE_CHECKOUT = "12";

    public static final String INSTANCE_TYPE_ADD_ADDRESS_FROM_MULTIPLE_CHECKOUT = "21";
    public static final String INSTANCE_TYPE_EDIT_ADDRESS_FROM_MULTIPLE_CHECKOUT = "22";

    public static final String RESULT_DATA_STORE_LOCATION = "RESULT_DATA_STORE_LOCATION";

}
