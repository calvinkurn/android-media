package com.tokopedia.instantloan.network;

import static com.tokopedia.core.network.constants.TkpdBaseURL.WEB_DOMAIN;

public interface InstantLoanUrl {

    public class BaseUrl {
        public static String WEB_DOMAIN = "https://www.tokpedia.com/";
    }

    public static String PATH_USER_STATUS = "dana-instant/api/user/ismobiledevice";
    String PATH_USER_PROFILE_STATUS = "https://www.tokopedia.com/dana-instant/api/user/mobile/status";
    String PATH_POST_PHONEDATA = "dana-instant/api/user/mobiledevice";
    String PATH_BANNER_OFFER = "https://www.tokopedia.com/microfinance/banner/personal-loan";
    String WEB_LINK_NO_COLLATERAL = WEB_DOMAIN + "pinjaman-online/criteria";
    String WEB_LINK_COLLATERAL_FUND = WEB_DOMAIN + "pinjaman-online/criteria";
    String WEB_LINK_DASHBOARD = WEB_DOMAIN + "pinjaman-online/dashboard";
    String WEB_LINK_OTP = WEB_DOMAIN + "pinjaman-online/dana-instant/verify-phone";
    String WEB_LINK_LEARN_MORE = WEB_DOMAIN + "pinjaman-online/profil-kredit/";
    String WEB_LINK_TNC = WEB_DOMAIN + "bantuan/syarat-dan-ketentuan-pinjaman-dana-online/";
    String LOAN_AMOUNT_QUERY_PARAM =  "?loan=";

}
