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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.buttonApplink);
        dest.writeString(this.buttonURL);
        dest.writeParcelable(this.tokensUser, flags);
    }

    public TokenHome() {
    }

    protected TokenHome(Parcel in) {
        this.buttonApplink = in.readString();
        this.buttonURL = in.readString();
        this.tokensUser = in.readParcelable(TokenUser.class.getClassLoader());
    }

    public static final Creator<TokenHome> CREATOR = new Creator<TokenHome>() {
        @Override
        public TokenHome createFromParcel(Parcel source) {
            return new TokenHome(source);
        }

        @Override
        public TokenHome[] newArray(int size) {
            return new TokenHome[size];
        }
    };
}
