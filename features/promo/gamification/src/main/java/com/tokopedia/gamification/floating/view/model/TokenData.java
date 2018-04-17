package com.tokopedia.gamification.floating.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenData implements Parcelable{

    private Boolean offFlag;
    private Integer sumToken;
    private String sumTokenStr;
    private String tokenUnit;
    private TokenFloating floating;
    private TokenHome home;

    public Boolean getOffFlag() {
        return offFlag;
    }

    public Integer getSumToken() {
        return sumToken;
    }

    public String getSumTokenStr() {
        return sumTokenStr;
    }

    public String getTokenUnit() {
        return tokenUnit;
    }

    public TokenFloating getFloating() {
        return floating;
    }

    public TokenHome getHome() {
        return home;
    }

    public void setOffFlag(Boolean offFlag) {
        this.offFlag = offFlag;
    }

    public void setSumToken(Integer sumToken) {
        this.sumToken = sumToken;
    }

    public void setSumTokenStr(String sumTokenStr) {
        this.sumTokenStr = sumTokenStr;
    }

    public void setTokenUnit(String tokenUnit) {
        this.tokenUnit = tokenUnit;
    }

    public void setFloating(TokenFloating floating) {
        this.floating = floating;
    }

    public void setHome(TokenHome home) {
        this.home = home;
    }

    public boolean isShowCountDown(){
        return home.getTokensUser().getShowTime();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.offFlag);
        dest.writeValue(this.sumToken);
        dest.writeString(this.sumTokenStr);
        dest.writeString(this.tokenUnit);
        dest.writeParcelable(this.floating, flags);
        dest.writeParcelable(this.home, flags);
    }

    public TokenData() {
    }

    protected TokenData(Parcel in) {
        this.offFlag = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.sumToken = (Integer) in.readValue(Integer.class.getClassLoader());
        this.sumTokenStr = in.readString();
        this.tokenUnit = in.readString();
        this.floating = in.readParcelable(TokenFloating.class.getClassLoader());
        this.home = in.readParcelable(TokenHome.class.getClassLoader());
    }

    public static final Creator<TokenData> CREATOR = new Creator<TokenData>() {
        @Override
        public TokenData createFromParcel(Parcel source) {
            return new TokenData(source);
        }

        @Override
        public TokenData[] newArray(int size) {
            return new TokenData[size];
        }
    };
}
