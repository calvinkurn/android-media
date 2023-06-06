package com.tokopedia.logisticaddaddress.common;

/**
 * Created by Fajar Ulin Nuha on 09/10/18.
 */
public class AddressConstants {

    public static final int REQUEST_CODE = 0x12;
    public static final String EXTRA_INSTANCE_TYPE = "EXTRA_INSTANCE_TYPE";
    public static final String EDIT_PARAM = "EDIT_PARAM";

    public static final String KERO_TOKEN = "token";


    public static final int INSTANCE_TYPE_DEFAULT = 0;

    public static final int INSTANCE_TYPE_ADD_ADDRESS_FROM_MANAGE_ADDRESS_EMPTY_DEFAULT_ADDRESS = 3;
    public static final int INSTANCE_TYPE_EDIT_ADDRESS_FROM_MANAGE_ADDRESS = 2;

    public static final int INSTANCE_TYPE_ADD_ADDRESS_FROM_SINGLE_CHECKOUT_EMPTY_DEFAULT_ADDRESS = 13;
    public static final int INSTANCE_TYPE_EDIT_ADDRESS_FROM_SINGLE_CHECKOUT = 12;

    public static final int INSTANCE_TYPE_ADD_ADDRESS_FROM_MULTIPLE_CHECKOUT = 21;
    public static final int INSTANCE_TYPE_EDIT_ADDRESS_FROM_MULTIPLE_CHECKOUT = 22;

    public static final String EXTRA_LAT = "EXTRA_LAT";
    public static final String EXTRA_LONG = "EXTRA_LONG";
    public static final String EXTRA_SHOW_AUTOCOMPLETE = "EXTRA_SHOW_AUTOCOMPLETE";
    public static final String EXTRA_IS_MISMATCH = "EXTRA_IS_MISMATCH";
    public static final String EXTRA_IS_MISMATCH_SOLVED = "EXTRA_IS_ORIGIN_MISMATCH_SOLVED";
    public static final String EXTRA_IS_POLYGON = "EXTRA_IS_POLYGON";
    public static final String EXTRA_SAVE_DATA_UI_MODEL = "EXTRA_SAVE_DATA_UI_MODEL";
    public static final String EXTRA_IS_EDIT = "EXTRA_IS_EDIT";
    public static final String EXTRA_ADDRESS_ID = "EXTRA_ADDRESS_ID";
    public static final String EXTRA_IS_CHANGES_REQUESTED = "EXTRA_IS_CHANGES_REQUESTED";
    public static final String EXTRA_IS_UNNAMED_ROAD = "EXTRA_UNNAMED_ROAD";
    public static final String EXTRA_IS_NULL_ZIPCODE = "EXTRA_IS_NULL_ZIPCODE";
    public static final String EXTRA_GMS_AVAILABILITY = "EXTRA_GMS_AVAILABILITY";
    public static final String EXTRA_NEGATIVE_FULL_FLOW = "EXTRA_NEGATIVE_FULL_FLOW";
    public static final String EXTRA_FROM_ADDRESS_FORM = "EXTRA_FROM_ADDRESS_FORM";
    public static final String EXTRA_FROM_PINPOINT = "EXTRA_FROM_PINPOINT";
    public static final String EXTRA_RESET_TO_SEARCH_PAGE = "EXTRA_RESET_TO_SEARCH_PAGE";
    public static final String KEY_SAVE_INSTANCE_SAVE_ADDRESS_DATA_MODEL = "KEY_SAVE_INSTANCE_SAVE_ADDRESS_DATA_MODEL";

    private static final Double MONAS_LAT = -6.175794;
    private static final Double MONAS_LONG = 106.826457;
    public static final String EXTRA_IS_FULL_FLOW = "EXTRA_IS_FULL_FLOW";
    public static final String EXTRA_ADDRESS_MODEL = "EXTRA_ADDRESS_MODEL";
    public static final String EXTRA_IS_LOGISTIC_LABEL = "EXTRA_IS_LOGISTIC_LABEL";
    public static final String EXTRA_IS_CIRCUIT_BREAKER = "EXTRA_IS_CIRCUIT_BREAKER";
    public static final String EXTRA_WAREHOUSE_DATA = "EXTRA_WAREHOUSE_DATA";
    public static final String EXTRA_IS_EDIT_WAREHOUSE = "EXTRA_IS_EDIT_WAREHOUSE";
    public static final String EXTRA_IS_POSITIVE_FLOW = "EXTRA_IS_POSITIVE_FLOW";
    public static final String EXTRA_IS_ANA_REVAMP = "EXTRA_IS_ANA_REVAMP";
    public static final String EXTRA_IS_PINPOINT = "EXTRA_IS_PINPOINT";

    public static final int CIRCUIT_BREAKER_ON_CODE = 101;

    public static final Double DEFAULT_LAT = MONAS_LAT;
    public static final Double DEFAULT_LONG = MONAS_LONG;

    public static final String ANA_POSITIVE = "positive";
    public static final String ANA_NEGATIVE = "negative";

    public static final String LOGISTIC_LABEL = "logistic";
    public static final String NON_LOGISTIC_LABEL = "non-logistic";

    public static final int GPS_REQUEST = 108;

    // Lite Pinpoint
    public static final String KEY_DISTRICT_ID = "KEY_DISTRICT_ID";
    public static final String KEY_LAT_ID = "KEY_LAT_ID";
    public static final String KEY_LONG_ID = "KEY_LONG_ID";
    public static final String KEY_CURRENT_LOC = "KEY_CURRENT_LOC";
    public static final String KEY_LOCATION_PASS = "KEY_LOCATION_PASS";
    public static final String KEY_ADDRESS_DATA = "KEY_ADDRESS_DATA";
    public static final String KEY_SOURCE_PINPOINT = "KEY_SOURCE_PINPOINT";
    public static final String PARAM_LAT = "lat";
    public static final String PARAM_LONG = "lng";
    public static final String PARAM_TRACKER = "trackerId";
    public static final String PARAM_TRACKER_LABEL = "label";
    public static final String PARAM_DISTRICT_ID = "districtId";
    public static final String PARAM_CURRENT_LOC = "currentLoc";
    public static final String PARAM_SOURCE = "source" ;

    // User Consent
    public static final String ADD_ADDRESS_COLLECTION_ID_STAGING = "01203d44-15c8-46f5-aed2-77e92dd4e625";
    public static final String ADD_ADDRESS_COLLECTION_ID_PRODUCTION = "1dd907e3-3e1f-405d-90c5-3db42e19d59d";
    public static final String EDIT_ADDRESS_COLLECTION_ID_STAGING = "e5a06379-2564-4357-8380-d2bf9e694340";
    public static final String EDIT_ADDRESS_COLLECTION_ID_PRODUCTION = "011d3e50-a147-41ea-8ea4-eae1dda6701e";
}
