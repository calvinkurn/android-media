package com.tokopedia.sessioncommon.data.model;

/**
 * @author by nisie on 12/19/17.
 */

public class LoginEmailDomain {
    protected TokenViewModel token;
    protected GetUserInfoData info;
    protected MakeLoginPojo loginResult;

    public void setToken(TokenViewModel token) {
        this.token = token;
    }

    public TokenViewModel getToken() {
        return token;
    }

    public void setInfo(GetUserInfoData info) {
        this.info = info;
    }

    public GetUserInfoData getInfo() {
        return info;
    }

    public void setLoginResult(MakeLoginPojo loginResult) {
        this.loginResult = loginResult;
    }

    public MakeLoginPojo getLoginResult() {
        return loginResult;
    }
}
