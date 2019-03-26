package com.tokopedia.instantdebitbca.data.view.model;

/**
 * Created by nabillasabbaha on 25/03/19.
 */
public class TokenInstantDebitBca {

    private String accessToken;
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
