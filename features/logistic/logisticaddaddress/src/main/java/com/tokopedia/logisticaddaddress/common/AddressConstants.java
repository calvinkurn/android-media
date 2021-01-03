package com.tokopedia.logisticaddaddress.common;

/**
 * Created by Fajar Ulin Nuha on 09/10/18.
 */
public class AddressConstants {

    public static final String SCREEN_ADD_ADDRESS_FORM = "Add Address Form";

    public static final String SCREEN_MANAGE_ADDRESS = "People Edit Address";

    public static final int REQUEST_CODE = 0x12;
    public static final String IS_DISTRICT_RECOMMENDATION = "district_recommendation";
    public static final String EXTRA_PLATFORM_PAGE = "EXTRA_PLATFORM_PAGE";
    public static final String EXTRA_INSTANCE_TYPE = "EXTRA_INSTANCE_TYPE";
    public static final String PLATFORM_MARKETPLACE_CART = "PLATFORM_MARKETPLACE_CART";
    public static final String IS_EDIT = "is_edit";
    public static final String EDIT_PARAM = "EDIT_PARAM";
    public static final int REQUEST_CODE_PARAM_CREATE = 101;
    public static final int REQUEST_CODE_PARAM_EDIT = 102;

    public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";

    public static final String KERO_TOKEN = "token";


    public static final int INSTANCE_TYPE_DEFAULT = 0;

    public static final int INSTANCE_TYPE_ADD_ADDRESS_FROM_MANAGE_ADDRESS = 1;
    public static final int INSTANCE_TYPE_ADD_ADDRESS_FROM_MANAGE_ADDRESS_EMPTY_DEFAULT_ADDRESS = 3;
    public static final int INSTANCE_TYPE_EDIT_ADDRESS_FROM_MANAGE_ADDRESS = 2;

    public static final int INSTANCE_TYPE_ADD_ADDRESS_FROM_SINGLE_CHECKOUT = 11;
    public static final int INSTANCE_TYPE_ADD_ADDRESS_FROM_SINGLE_CHECKOUT_EMPTY_DEFAULT_ADDRESS = 13;
    public static final int INSTANCE_TYPE_EDIT_ADDRESS_FROM_SINGLE_CHECKOUT = 12;

    public static final int INSTANCE_TYPE_ADD_ADDRESS_FROM_MULTIPLE_CHECKOUT = 21;
    public static final int INSTANCE_TYPE_EDIT_ADDRESS_FROM_MULTIPLE_CHECKOUT = 22;

    public static final String EXTRA_LAT = "EXTRA_LAT";
    public static final String EXTRA_LONG = "EXTRA_LONG";
    public static final String EXTRA_SHOW_AUTOCOMPLETE = "EXTRA_SHOW_AUTOCOMPLETE";
    public static final String EXTRA_REQUEST_LOCATION = "EXTRA_REQUEST_LOCATION";
    public static final String EXTRA_IS_MISMATCH = "EXTRA_IS_MISMATCH";
    public static final String EXTRA_IS_MISMATCH_SOLVED = "EXTRA_IS_ORIGIN_MISMATCH_SOLVED";
    public static final String EXTRA_IS_POLYGON = "EXTRA_IS_POLYGON";
    public static final String EXTRA_DISTRICT_ID = "EXTRA_DISTRICT_ID";
    public static final String EXTRA_SAVE_DATA_UI_MODEL = "EXTRA_SAVE_DATA_UI_MODEL";
    public static final String EXTRA_IS_CHANGES_REQUESTED = "EXTRA_IS_CHANGES_REQUESTED";
    public static final String EXTRA_IS_UNNAMED_ROAD = "EXTRA_UNNAMED_ROAD";
    public static final String EXTRA_IS_NULL_ZIPCODE = "EXTRA_IS_NULL_ZIPCODE";

    private static final Double MONAS_LAT = -6.175794;
    private static final Double MONAS_LONG = 106.826457;
    private static final Double INDONESIA_CENTER_LAT = -5.002085;
    private static final Double INDONESIA_CENTER_LONG = 111.865003;
    public static final String EXTRA_IS_FULL_FLOW = "EXTRA_IS_FULL_FLOW";
    public static final String EXTRA_ADDRESS_MODEL = "EXTRA_ADDRESS_MODEL";
    public static final String EXTRA_IS_LOGISTIC_LABEL = "EXTRA_IS_LOGISTIC_LABEL";
    public static final String EXTRA_IS_CIRCUIT_BREAKER = "EXTRA_IS_CIRCUIT_BREAKER";
    public static final String EXTRA_WAREHOUSE_DATA = "EXTRA_WAREHOUSE_DATA";
    public static final String EXTRA_IS_EDIT_WAREHOUSE = "EXTRA_IS_EDIT_WAREHOUSE";

    public static final int CIRCUIT_BREAKER_ON_CODE = 101;

    public static final Double DEFAULT_LAT = MONAS_LAT;
    public static final Double DEFAULT_LONG = MONAS_LONG;

    public static final Float ZOOM_LEVEL_THRESHOLD = 14f;

    public static final String ANA_POSITIVE = "positive";
    public static final String ANA_NEGATIVE = "negative";

    public static final String LOGISTIC_LABEL = "logistic";
    public static final String NON_LOGISTIC_LABEL = "non-logistic";

    public static final int GPS_REQUEST = 108;
    public static final String SCREEN_NAME_USER_NEW = "/user/address/create";
}
