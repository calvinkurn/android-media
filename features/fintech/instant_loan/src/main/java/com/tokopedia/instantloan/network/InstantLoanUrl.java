package com.tokopedia.instantloan.network;

import static com.tokopedia.instantloan.network.InstantLoanUrl.BaseUrl.WEB_DOMAIN;

public interface InstantLoanUrl {

    public class BaseUrl {
        public static String WEB_DOMAIN = "https://www.tokpedia.com/";
    }

    public static String PATH_USER_STATUS = "dana-instant/api/user/ismobiledevice";
    String PATH_USER_PROFILE_STATUS = WEB_DOMAIN + "dana-instant/api/user/mobile/status";
    String PATH_POST_PHONEDATA = WEB_DOMAIN + "dana-instant/api/user/mobiledevice";
    String PATH_BANNER_OFFER = WEB_DOMAIN + "microfinance/banner/personal-loan";
    String WEB_LINK_NO_COLLATERAL = WEB_DOMAIN + "pinjaman-online/criteria";
    String WEB_LINK_COLLATERAL_FUND = WEB_DOMAIN + "pinjaman-online/criteria";
    String WEB_LINK_DASHBOARD = WEB_DOMAIN + "pinjaman-online/dashboard";
    String WEB_LINK_OTP = WEB_DOMAIN + "pinjaman-online/dana-instant/verify-phone";
    String WEB_LINK_LEARN_MORE = WEB_DOMAIN + "pinjaman-online/profil-kredit/";
    String WEB_LINK_TNC = WEB_DOMAIN + "bantuan/syarat-dan-ketentuan-pinjaman-dana-online/";
    String LOAN_AMOUNT_QUERY_PARAM = "?loan=";

    String SUBMISSION_HISTORY_URL = WEB_DOMAIN + "pinjaman-online/dana-instant/dashboard";
    String PAYMENT_METHODS_URL = WEB_DOMAIN + "pinjaman-online/dana-instant/payment-method/%s";
    String HELP_URL = WEB_DOMAIN + "contact-us?pid=162#step1";

}
