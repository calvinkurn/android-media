package com.tokopedia.core.manage.people.bank.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nisie on 6/10/16.
 */
public class BankAccountPass {

    private static final String PARAM_PAGE = "page";
    private static final String PARAM_PROFILE_USER_ID = "profile_user_id";

    String page;
    String profileUserId;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getprofileUserId() {
        return profileUserId;
    }

    public void setprofileUserId(String profile_user_id) {
        this.profileUserId = profile_user_id;
    }

    public Map<String, String> getBankAccountParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_PAGE, getPage());
        param.put(PARAM_PROFILE_USER_ID, getprofileUserId());
        return param;
    }
}
