package com.tokopedia.core.network.retrofit.interceptors;

/**
 * @author by nisie on 1/17/18.
 */

public class AccountsBasicInterceptor extends AccountsInterceptor {

    public AccountsBasicInterceptor() {
        super("", false, false);
    }

    @Override
    protected String getToken() {
        return "";
    }
}
