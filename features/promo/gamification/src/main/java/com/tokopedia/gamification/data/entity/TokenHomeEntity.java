package com.tokopedia.gamification.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenHomeEntity implements Parcelable {

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

    @SerializedName("tokenSourceMessage")
    @Expose
    private String[] tokenSourceMessage;

    protected TokenHomeEntity(Parcel in) {
        buttonApplink = in.readString();
        buttonURL = in.readString();
        countingMessage = in.createStringArray();
        tokenSourceMessage = in.createStringArray();
        tokensUser = in.readParcelable(TokenUserEntity.class.getClassLoader());
        emptyState = in.readParcelable(TokenEmptyStateEntity.class.getClassLoader());
    }

    public static final Creator<TokenHomeEntity> CREATOR = new Creator<TokenHomeEntity>() {
        @Override
        public TokenHomeEntity createFromParcel(Parcel in) {
            return new TokenHomeEntity(in);
        }

        @Override
        public TokenHomeEntity[] newArray(int size) {
            return new TokenHomeEntity[size];
        }
    };

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

    public String[] getTokenSourceMessage() {
        return tokenSourceMessage;
    }

    public void setTokenSourceMessage(String[] tokenSourceMessage) {
        this.tokenSourceMessage = tokenSourceMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(buttonApplink);
        dest.writeString(buttonURL);
        dest.writeStringArray(countingMessage);
        dest.writeStringArray(tokenSourceMessage);
        dest.writeParcelable(tokensUser, flags);
        dest.writeParcelable(emptyState, flags);
    }
}
