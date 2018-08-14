package com.tokopedia.common_digital;

/**
 * Created by Rizky on 13/08/18.
 */
public class DigitalUrl {

    public static String DIGITAL_API_DOMAIN = "https://pulsa-api.tokopedia.com/";

    public static final String VERSION = "v1.4/";
    public static final String HMAC_KEY = "web_service_v4";

    public static final String PATH_STATUS = "status";
    public static final String PATH_CATEGORY_LIST = "category/list";
    public static final String PATH_CATEGORY = "category";
    public static final String PATH_OPERATOR = "operator/list";
    public static final String PATH_PRODUCT = "product/list";
    public static final String PATH_FAVORITE_LIST = "favorite/list";
    public static final String PATH_SALDO = "saldo";
    public static final String PATH_GET_CART = "cart";
    public static final String PATH_PATCH_OTP_SUCCESS = "cart/otp-success";
    public static final String PATH_ORDER = "order";
    public static final String PATH_ADD_TO_CART = "cart";
    public static final String PATH_CHECKOUT = "checkout";
    public static final String PATH_CHECK_VOUCHER = "voucher/check";
    public static final String PATH_CANCEL_VOUCHER = "voucher/cancel";
    public static final String PATH_USSD = "ussd/balance";
    public static final String PATH_SMARTCARD_INQUIRY = "smartcard/inquiry";
    public static final String PATH_SMARTCARD_COMMAND = "smartcard/command";

    public static final String BASE_URL = DIGITAL_API_DOMAIN + VERSION;

}
