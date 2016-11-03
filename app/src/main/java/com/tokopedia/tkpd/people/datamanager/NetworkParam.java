package com.tokopedia.tkpd.people.datamanager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 5/31/16.
 */
public class NetworkParam {

    private static final String PARAM_PROFILE_ID = "profile_user_id";

    public static Map<String, String> paramGetPeopleInfo(String userID) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PROFILE_ID, userID);
        return params;
    }
}
