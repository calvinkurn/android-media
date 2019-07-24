package com.tokopedia.train.common.constant;

import com.tokopedia.url.TokopediaUrl;

/**
 * Created by alvarisi on 2/19/18.
 */

public class TrainUrl {

    public static final String QUERY_GQL = "query";
    public static final String VARIABLE_GQL = "variables";
    public static final String INPUT_GQL = "input";


    public static String BASE_URL = TokopediaUrl.Companion.getInstance().getGQL();
    public static String WEB_DOMAIN = TokopediaUrl.Companion.getInstance().getTIKET();
    public static String BASE_WEB_DOMAIN = TokopediaUrl.Companion.getInstance().getWEB();
    public static String KAI_WEBVIEW = WEB_DOMAIN + "kereta-api";

    private static String PATH_USER_BOOKING_LIST = "/user/bookings";
    private static String PARAM_DIGITAL_ISPULSA = "?ispulsa=1";

    public static String TRAIN_ORDER_LIST = KAI_WEBVIEW + PATH_USER_BOOKING_LIST + PARAM_DIGITAL_ISPULSA;
    public static String HELP_PAGE = BASE_WEB_DOMAIN + "contact-us?flag_app=1";

    public static String PARAM_TRAIN_MENU_ID = "4";
    public static String PARAM_TRAIN_SUBMENU_ID = "378";

}