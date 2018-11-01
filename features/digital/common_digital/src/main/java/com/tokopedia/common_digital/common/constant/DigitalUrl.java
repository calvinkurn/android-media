package com.tokopedia.common_digital.common.constant;

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

    public static String WEB_DOMAIN = "https://www.tokopedia.com/";
    public static String DIGITAL_BANTUAN = WEB_DOMAIN + "bantuan/produk-digital/";

    public static class HelpUrl {
        public static String PULSA = DIGITAL_BANTUAN + "pulsa/";
        public static String PAKET_DATA = DIGITAL_BANTUAN + "pulsa/";
        public static String PLN = DIGITAL_BANTUAN + "listrik-pln/";
        public static String BPJS = DIGITAL_BANTUAN + "bpjs/";
        public static String PDAM = DIGITAL_BANTUAN + "air-pdam/";
        public static String GAME = DIGITAL_BANTUAN + "voucher-game/";
        public static String CREDIT = DIGITAL_BANTUAN + "angsuran-kredit/";
        public static String TV = DIGITAL_BANTUAN + "tv-kabel/";
        public static String POSTPAID = DIGITAL_BANTUAN + "seluler-pascabayar/";
        public static String TELKOM = DIGITAL_BANTUAN + "telepon/";
        public static String STREAMING = DIGITAL_BANTUAN + "voucher-streaming/";
        public static String PGN = DIGITAL_BANTUAN + "gas/";
        public static String ROAMING = DIGITAL_BANTUAN + "roaming/";
        public static String TAX = DIGITAL_BANTUAN + "pajak/";
        public static String GIFT_CARD = DIGITAL_BANTUAN + "voucher-gift-card/";
        public static String RETRIBUTION = DIGITAL_BANTUAN + "retribusi/";
        public static String MTIX = DIGITAL_BANTUAN + "m-tix-xxi/";
        public static String CREDIT_CARD = DIGITAL_BANTUAN + "tagihan-kartu-kredit/";
        public static String ETOLL = DIGITAL_BANTUAN + "e-money/#cara-update-saldo-kartu";
    }


}
