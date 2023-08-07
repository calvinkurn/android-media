package com.tokopedia.digital_product_detail.data.model.data

object DigitalPDPConstant {
    const val PARAM_MENU_ID = "menu_id"
    const val PARAM_PRODUCT_ID = "product_id"
    const val PARAM_CLIENT_NUMBER = "client_number"
    const val PARAM_CATEGORY_ID = "category_id"
    const val PARAM_OPERATOR_ID = "operator_id"
    const val EXTRA_PARAM = "extra_param"
    const val EXTRA_QR_PARAM = "scanResult"
    const val EXTRA_UPDATED_TITLE = "UPDATED_TITLE"
    const val PARAM_NEED_RESULT = "1"

    const val INPUT_ACTION_TRACKING_DELAY = 1000L

    const val MINIMUM_OPERATOR_PREFIX = 4
    const val MINIMUM_OPERATOR_PREFIX_LISTRIK = 1
    const val MINIMUM_VALID_NUMBER_LENGTH = 10
    const val MAXIMUM_VALID_NUMBER_LENGTH = 14
    const val DEFAULT_ICON_RES = 0

    const val TELCO_PREFERENCES_NAME = "telco_preferences"
    const val TOKEN_LISTRIK_PREFERENCES_NAME = "token_listrik_preferences"
    const val FAVNUM_PERMISSION_CHECKER_IS_DENIED = "favnum_permission_checker_is_denied"

    const val REQUEST_CODE_DIGITAL_SAVED_NUMBER = 77
    const val REQUEST_CODE_LOGIN = 1010
    const val REQUEST_CODE_LOGIN_ALT = 1011
    const val REQUEST_CODE_VERIFY_PHONE_NUMBER = 1012
    const val RESULT_CODE_QR_SCAN = 101

    const val DELAY_PREFIX_TIME = 200L
    const val VALIDATOR_DELAY_TIME = 2000L
    const val DELAY_MULTI_TAB = 1000L
    const val DELAY_CLIENT_NUMBER_TRANSITION = 200L

    const val CHECKOUT_NO_PROMO = "0"
    const val LOADER_DIALOG_TEXT = ""

    const val RECOMMENDATION_GQL_CHANNEL_NAME_PULSA = "pulsa_pdp_last_transaction"
    const val RECOMMENDATION_GQL_CHANNEL_NAME_DEFAULT = "recharge_pdp_last_trx_client_number"
    const val PERSO_CHANNEL_NAME_INDOSAT_CHECK_BALANCE = "indosat_check_balance"
    const val MCCM_CHANNEL_NAME = "recharge_pdp_mccm_new_layout"

    const val OTHER_COMPONENT_APPLINK_OMNI = "applink_omni"
    const val APPLINK_OMNI_DATA_CODE = "omni_applink_data_code"

    const val EXTRA_CHECK_BALANCE_ACCESS_TOKEN = "indosat_check_balance_access_token"
    const val INDOSAT_CHECK_BALANCE_TYPE_OTP = "otp"
    const val INDOSAT_CHECK_BALANCE_TYPE_WIDGET = "widget"
}
