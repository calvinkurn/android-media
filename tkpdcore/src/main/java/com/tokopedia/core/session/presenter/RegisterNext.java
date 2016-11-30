package com.tokopedia.core.session.presenter;

import android.content.Context;
import android.os.Bundle;

import org.json.JSONObject;

/**
 * Created by m.normansyah on 13/11/2015.
 */
public interface RegisterNext {

    String messageTAG = "RegisterNext : ";

    String BIRTHDAY = "birth_day";// 1
    String BIRTHMONTH = "birth_month";// 2
    String BIRTHYEAR = "birth_year";// 3
    String CONFIRM_PASSWORD = "confirm_password";// 4
    String EMAIL = "email";// 5
    String FACEBOOK_USERID = "fb_user_id";// 6
    String FULLNAME = "full_name";// 7
    String GENDER = "gender";// 8
    String PASSWORD = "password";// 9
    String PHONE = "phone";// 10
    String IS_AUTO_VERIFY = "is_auto_verify";// 11

    String CHECK_T_AND_COND_STRING = "CHECK_T_AND_COND_STRING";

    String DATA = "DATA";
}
