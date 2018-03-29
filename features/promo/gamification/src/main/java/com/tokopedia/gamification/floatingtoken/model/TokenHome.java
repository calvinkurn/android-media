package com.tokopedia.gamification.floatingtoken.model;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenHome {

    private String buttonApplink;
    private String buttonURL;
    private TokenUser tokensUser;

    public String getButtonApplink() {
        return buttonApplink;
    }

    public String getButtonURL() {
        return buttonURL;
    }

    public TokenUser getTokensUser() {
        return tokensUser;
    }

    public void setButtonApplink(String buttonApplink) {
        this.buttonApplink = buttonApplink;
    }

    public void setButtonURL(String buttonURL) {
        this.buttonURL = buttonURL;
    }

    public void setTokensUser(TokenUser tokensUser) {
        this.tokensUser = tokensUser;
    }
}
