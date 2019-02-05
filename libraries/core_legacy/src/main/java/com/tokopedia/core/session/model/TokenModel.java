package com.tokopedia.core.session.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by stevenfredian on 5/26/16.
 */
public class TokenModel implements Parcelable {

    /**
     * access_token : 2YotnFZFEjr1zCsicMWpAA
     * token_type : example
     * expires_in : 3600
     * refresh_token : tGzv3JOkF0XG5Qx2TlKWIA
     * error : access_denied
     * error_description : The resource owner or authorization server denied the request.
     */

    @SerializedName("access_token")
    @Expose
    private String accessToken;

    @SerializedName("token_type")
    @Expose
    private String tokenType;

    @SerializedName("expires_in")
    @Expose
    private int expiresIn;

    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;

    @SerializedName("scope")
    @Expose
    private String scope;


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

    public int getExpires_in() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accessToken);
        dest.writeString(this.tokenType);
        dest.writeInt(this.expiresIn);
        dest.writeString(this.refreshToken);
        dest.writeString(this.scope);
    }

    public TokenModel() {
    }

    protected TokenModel(Parcel in) {
        this.accessToken = in.readString();
        this.tokenType = in.readString();
        this.expiresIn = in.readInt();
        this.refreshToken = in.readString();
        this.scope = in.readString();
    }

    public static final Creator<TokenModel> CREATOR = new Creator<TokenModel>() {
        @Override
        public TokenModel createFromParcel(Parcel source) {
            return new TokenModel(source);
        }

        @Override
        public TokenModel[] newArray(int size) {
            return new TokenModel[size];
        }
    };
}
