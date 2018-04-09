package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenHomeEntity {

    @SerializedName("buttonApplink")
    @Expose
    private String buttonApplink;

    @SerializedName("buttonURL")
    @Expose
    private String buttonURL;

    @SerializedName("countingMessage")
    @Expose
    private String[] countingMessage;

    @SerializedName("tokensUser")
    @Expose
    private TokenUserEntity tokensUser;

    @SerializedName("emptyState")
    @Expose
    private TokenEmptyStateEntity emptyState;

    public String getButtonApplink() {
        return buttonApplink;
    }

    public String getButtonURL() {
        return buttonURL;
    }

    public TokenUserEntity getTokensUser() {
        return tokensUser;
    }

    public String[] getCountingMessage() {
        return countingMessage;
    }

    public TokenEmptyStateEntity getEmptyState() {
        return emptyState;
    }
}
