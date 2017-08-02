package com.tokopedia.core.manage.people.bank.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 7/24/17. Tokopedia
 */

public class BcaOneClickData {

    @SerializedName("token")
    private BcaOneClickToken token;

    public BcaOneClickToken getToken() {
        return token;
    }

    public void setToken(BcaOneClickToken token) {
        this.token = token;
    }
}
