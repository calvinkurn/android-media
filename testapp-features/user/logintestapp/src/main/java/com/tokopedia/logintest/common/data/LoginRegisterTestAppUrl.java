package com.tokopedia.logintest.common.data;

import com.tokopedia.url.TokopediaUrl;

/**
 * @author by nisie on 10/11/18.
 */
public class LoginRegisterTestAppUrl {
    public static String BASE_DOMAIN = TokopediaUrl.Companion.getInstance().getACCOUNTS();
    public static final String PATH_DISCOVER_LOGIN = "api/discover";

    //* ACTIVATION URL
    public static final String RESEND_ACTIVATION = "/api/resend";
    public static final String ACTIVATE_UNICODE = "/token";
    public static final String CHANGE_EMAIL = "/api/v1/activation/change-email";
    public static final String PATH_IMAGE_ACTIVATION = "https://ecs7.tokopedia.net/img/android/others/icon_activation.png";

    //* REGISTER URL
    public static final String PATH_REGISTER_VALIDATION = "/api/v1/account/register/check";
    public static final String DO_REGISTER = "api/register";

    //*WELCOME PAGE
    public final static String URL_BACKGROUND
            = "https://ecs7.tokopedia.net/img/android/welcome_baloon/xhdpi/welcome_baloon.png";


}
