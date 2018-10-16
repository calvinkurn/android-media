package com.tokopedia.instantloan.network;

import com.tokopedia.network.constant.TkpdBaseURL;

import static com.tokopedia.instantloan.network.InstantLoanUrl.BaseUrl.WEB_DOMAIN;

public interface InstantLoanUrl {

    public class BaseUrl {
        public static String WEB_DOMAIN = "https://www.tokpedia.com/";
    }

    public static String PATH_USER_STATUS = "dana-instant/api/user/ismobiledevice";
    String PATH_USER_PROFILE_STATUS = TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL + "dana-instant/api/user/mobile/status";
    String PATH_POST_PHONEDATA = TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL + "dana-instant/api/user/mobiledevice";
    String PATH_BANNER_OFFER = TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL + "microfinance/banner/personal-loan";
    String WEB_LINK_NO_COLLATERAL = WEB_DOMAIN + "pinjaman-online/criteria";
    String WEB_LINK_COLLATERAL_FUND = WEB_DOMAIN + "pinjaman-online/criteria";
    String WEB_LINK_DASHBOARD = WEB_DOMAIN + "pinjaman-online/dashboard";
    String WEB_LINK_OTP = WEB_DOMAIN + "pinjaman-online/dana-instant/verify-phone";
    String WEB_LINK_LEARN_MORE = WEB_DOMAIN + "pinjaman-online/profil-kredit/";
    String WEB_LINK_TNC = WEB_DOMAIN + "bantuan/syarat-dan-ketentuan-pinjaman-dana-online/";
    String LOAN_AMOUNT_QUERY_PARAM = "?loan=";

    String SUBMISSION_HISTORY_URL = "http://www.tokopedia.com/pinjaman-online/dana-instant/dashboard";
    String PAYMENT_METHODS_URL = "http://www.tokopedia.com/pinjaman-online/dana-instant/payment-method/%s";
    String HELP_URL = "https://www.tokopedia.com/contact-us?pid=162#step1";

}
