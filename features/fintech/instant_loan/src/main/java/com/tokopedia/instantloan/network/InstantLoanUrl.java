package com.tokopedia.instantloan.network;

import static com.tokopedia.core.network.constants.TkpdBaseURL.WEB_DOMAIN;

public interface InstantLoanUrl {

    public class BaseUrl {
        public static String WEB_DOMAIN = "https://www.tokpedia.com/";
    }

    public static String PATH_USER_STATUS = "dana-instant/api/user/ismobiledevice";
    String PATH_POST_PHONEDATA = "dana-instant/api/user/mobiledevice";
    String PATH_BANNER_OFFER = "microfinance/banner/personal-loan";
    String WEB_LINK_NO_COLLATERAL = WEB_DOMAIN + "pinjaman-online/criteria";
    String WEB_LINK_COLLATERAL_FUND = WEB_DOMAIN + "pinjaman-online/criteria";
    String WEB_LINK_DASHBOARD = WEB_DOMAIN + "pinjaman-online/dashboard";
    String WEB_LINK_OTP = WEB_DOMAIN + "pinjaman-online/dana-instant/verify-phone";
}
