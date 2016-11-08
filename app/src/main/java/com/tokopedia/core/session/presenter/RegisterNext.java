package com.tokopedia.core.session.presenter;

import android.content.Context;
import android.os.Bundle;

import org.json.JSONObject;

/**
 * Created by m.normansyah on 13/11/2015.
 */
@Deprecated
public interface RegisterNext {
    String TAG = "MNORMANSYAH";
    String messageTAG = "RegisterNext : ";
//
//    String BIRTHDAY = "bday_dd";// 1
//    String BIRTHMONTH = "bday_mm";// 2
//    String BIRTHYEAR = "bday_yy";// 3
//    String CONFIRM_PASSWORD = "confirm_password";// 4
//    String EMAIL = "email";// 5
//    String FACEBOOK_USERID = "fb_user_id";// 6
//    String FULLNAME = "full_name";// 7
//    String GENDER = "gender";// 8
//    String PASSWORD = "password";// 9
//    String PHONE = "phone";// 10
//    String IS_AUTO_VERIFY = "is_auto_verify";// 11
//
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

    int EMAIL_POS = 0;
    int PASSWORD_POS = 1;
    int CONFIRM_PASSWORD_POS = 2;
    int CHECKED_T_AND_COND = 3;
    int IS_LOADING = 4;
    String DATA = "DATA";
    String REGISTER_SUCCESS_KEY = "REGISTER_SUCCESS_KEY";
    void initData(Context context);
    String formatTermAndCondition(String termAndPrivacy, String linkTermCondition, String linkPrivacyPolicy);
    void fetchArguments(Bundle argument);
    void fetchFromPreference(Context context);
    void fetchRotationData(Bundle argument);
    void saveDataBeforeRotation(Bundle argument);
    void initDataInstance(Context context);
    boolean isAfterRotate();

    void registerToServer(Context context);
    Object parseJSON(JSONObject jsonObject);

    void updateData(int type, Object... data);

    void setData(int type, Bundle data);
}
