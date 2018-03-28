package com.tokopedia.payment.utils;

/**
 * @author anggaprasetiyo on 10/9/17.
 */

public interface Constant {

    @Deprecated
    interface TempRedirectPayment {
        String TOP_PAY_DOMAIN_URL_LIVE = "https://www.tokopedia.com";
        String TOP_PAY_DOMAIN_URL_STAGING = "https://staging.tokopedia.com";
        String TOP_PAY_PATH_HELP_URL_TEMPORARY = "/bantuan/sistem-refund-otomatis";
        String APP_LINK_SCHEME_WEB_VIEW = "tokopedia://webviewbackhome";
        String APP_LINK_SCHEME_HOME = "tokopedia://home";
        String APP_LINK_FINGERPRINT = "tokopedia://fingerprint/save";
        String TOP_PAY_DOMAIN_CREDIT_CARD = "https://pay.tokopedia.com";
        String TOP_PAY_PATH_CREDIT_CARD_VERITRANS = "/v2/3dsecure/cc/veritrans";
        String TOP_PAY_PATH_CREDIT_CARD_SPRINTASIA = "/v2/3dsecure/sprintasia";
    }
}
