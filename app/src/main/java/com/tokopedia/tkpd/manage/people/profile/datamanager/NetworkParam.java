package com.tokopedia.tkpd.manage.people.profile.datamanager;

import android.content.Context;

import com.tokopedia.tkpd.manage.people.profile.model.PeopleProfilePass;
import com.tokopedia.tkpd.util.SessionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 6/27/16.
 */
public class NetworkParam {

    private static final int STATIC_GOLANG_VALUE = 2;
    
    private static final String PARAM_SERVER_LANGUAGE = "new_add";
    private static final String PARAM_PROFILE_USER_ID = "profile_user_id";
    private static final String PARAM_BIRTH_DAY = "birth_day";
    private static final String PARAM_BIRTH_MONTH = "birth_month";
    private static final String PARAM_BIRTH_YEAR = "birth_year";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_FULLNAME = "full_name";
    private static final String PARAM_GENDER = "gender";
    private static final String PARAM_HOBBY = "hobby";
    private static final String PARAM_MESSENGER = "messenger";
    private static final String PARAM_MSISDN = "msisdn";
    private static final String PARAM_VERIFIED = "msisdn_verified";
    private static final String PARAM_USER_PASSWORD = "user_password";
    private static final String PARAM_FILE_UPLOADED = "file_uploaded";

    public static Map<String, String> getProfile(Context context) {
        Map<String , String> param = new HashMap<>();
        param.put(PARAM_PROFILE_USER_ID, SessionHandler.getLoginID(context));
        return param;
    }

    public static Map<String, String> generateHost() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_SERVER_LANGUAGE, String.valueOf(STATIC_GOLANG_VALUE));
        return params;
    }

    public static Map<String, String> editProfile(PeopleProfilePass mPass) {
        Map<String, String> param = new HashMap<>();
        param.put(PARAM_BIRTH_DAY, mPass.getDay());
        param.put(PARAM_BIRTH_MONTH, mPass.getMonth());
        param.put(PARAM_BIRTH_YEAR, mPass.getYear());
        param.put(PARAM_EMAIL, mPass.getEmail());
        param.put(PARAM_FULLNAME, mPass.getFullName());
        param.put(PARAM_GENDER, mPass.getGender());
        param.put(PARAM_HOBBY, mPass.getHobby());
        param.put(PARAM_MESSENGER, mPass.getMessenger());
        param.put(PARAM_MSISDN, mPass.getMsisdn());
        param.put(PARAM_VERIFIED, mPass.getVerifiedPhone());
        param.put(PARAM_USER_PASSWORD, mPass.getPassword());
        return param;
    }

    public static Map<String, String> uploadProfilePic(PeopleProfilePass mPass) {
        Map<String, String> param = new HashMap<>();
        param.put(PARAM_FILE_UPLOADED, mPass.getFileUploaded());
        return param;
    }
}
