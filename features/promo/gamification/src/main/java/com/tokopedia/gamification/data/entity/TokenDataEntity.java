package com.tokopedia.gamification.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenDataEntity implements Parcelable {

    @SerializedName("resultStatus")
    @Expose
    private ResultStatusEntity resultStatus;

    @SerializedName("offFlag")
    @Expose
    private Boolean offFlag;

    @SerializedName("sumToken")
    @Expose
    private Integer sumToken;

    @SerializedName("sumTokenStr")
    @Expose
    private String sumTokenStr;

    @SerializedName("tokenUnit")
    @Expose
    private String tokenUnit;

    @SerializedName("floating")
    @Expose
    private TokenFloatingEntity floating;

    @SerializedName("home")
    @Expose
    private TokenHomeEntity home;

    protected TokenDataEntity(Parcel in) {
        resultStatus = in.readParcelable(ResultStatusEntity.class.getClassLoader());
        byte tmpOffFlag = in.readByte();
        offFlag = tmpOffFlag == 0 ? null : tmpOffFlag == 1;
        if (in.readByte() == 0) {
            sumToken = null;
        } else {
            sumToken = in.readInt();
        }
        sumTokenStr = in.readString();
        tokenUnit = in.readString();
        floating = in.readParcelable(TokenFloatingEntity.class.getClassLoader());
        home = in.readParcelable(TokenHomeEntity.class.getClassLoader());
    }

    public static final Creator<TokenDataEntity> CREATOR = new Creator<TokenDataEntity>() {
        @Override
        public TokenDataEntity createFromParcel(Parcel in) {
            return new TokenDataEntity(in);
        }

        @Override
        public TokenDataEntity[] newArray(int size) {
            return new TokenDataEntity[size];
        }
    };

    public ResultStatusEntity getResultStatus() {
        return resultStatus;
    }

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

    public TokenFloatingEntity getFloating() {
        return floating;
    }

    public TokenHomeEntity getHome() {
        return home;
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
        dest.writeParcelable(resultStatus, flags);
        dest.writeByte((byte) (offFlag == null ? 0 : offFlag ? 1 : 2));
        if (sumToken == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(sumToken);
        }
        dest.writeString(sumTokenStr);
        dest.writeString(tokenUnit);
        dest.writeParcelable(floating, flags);
        dest.writeParcelable(home, flags);
    }
}
