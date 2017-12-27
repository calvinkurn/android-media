package com.tokopedia.core.manage.general.districtrecommendation.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 17/11/17.
 */

public class Token implements Parcelable {
    @SerializedName("district_recommendation")
    @Expose
    private String districtRecommendation;
    @SerializedName("ut")
    @Expose
    private int unixTime;

    public Token() {
    }

    protected Token(Parcel in) {
        districtRecommendation = in.readString();
        unixTime = in.readInt();
    }

    public static final Creator<Token> CREATOR = new Creator<Token>() {
        @Override
        public Token createFromParcel(Parcel in) {
            return new Token(in);
        }

        @Override
        public Token[] newArray(int size) {
            return new Token[size];
        }
    };

    public String getDistrictRecommendation() {
        return districtRecommendation;
    }

    public void setDistrictRecommendation(String districtRecommendation) {
        this.districtRecommendation = districtRecommendation;
    }

    public int getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(int unixTime) {
        this.unixTime = unixTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(districtRecommendation);
        dest.writeInt(unixTime);
    }
}
