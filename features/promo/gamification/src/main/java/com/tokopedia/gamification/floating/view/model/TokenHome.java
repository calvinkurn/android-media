package com.tokopedia.gamification.floating.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenHome implements Parcelable{

    private String buttonApplink;
    private String buttonURL;
    private TokenUser tokensUser;
    private String[] countingMessage;
    private TokenEmptyState tokenEmptyState;

    public TokenHome() {
    }

    protected TokenHome(Parcel in) {
        buttonApplink = in.readString();
        buttonURL = in.readString();
        tokensUser = in.readParcelable(TokenUser.class.getClassLoader());
        countingMessage = in.createStringArray();
        tokenEmptyState = in.readParcelable(TokenEmptyState.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(buttonApplink);
        dest.writeString(buttonURL);
        dest.writeParcelable(tokensUser, flags);
        dest.writeStringArray(countingMessage);
        dest.writeParcelable(tokenEmptyState, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TokenHome> CREATOR = new Creator<TokenHome>() {
        @Override
        public TokenHome createFromParcel(Parcel in) {
            return new TokenHome(in);
        }

        @Override
        public TokenHome[] newArray(int size) {
            return new TokenHome[size];
        }
    };

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

    public String[] getCountingMessage() {
        return countingMessage;
    }

    public void setCountingMessage(String[] countingMessage) {
        this.countingMessage = countingMessage;
    }

    public TokenEmptyState getTokenEmptyState() {
        return tokenEmptyState;
    }

    public void setTokenEmptyState(TokenEmptyState tokenEmptyState) {
        this.tokenEmptyState = tokenEmptyState;
    }
}
