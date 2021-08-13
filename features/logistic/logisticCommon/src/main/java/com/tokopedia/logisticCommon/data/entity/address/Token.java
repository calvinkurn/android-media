package com.tokopedia.logisticCommon.data.entity.address;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Token implements Parcelable {

    @SerializedName("ut")
    @Expose
    private int ut;

    @SerializedName("district_recommendation")
    @Expose
    private String districtRecommendation;

    public int getUt() {
        return ut;
    }

    public void setUt(int ut) {
        this.ut = ut;
    }

    public String getDistrictRecommendation() {
        return districtRecommendation;
    }

    public void setDistrictRecommendation(String districtRecommendation) {
        this.districtRecommendation = districtRecommendation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ut);
        dest.writeString(this.districtRecommendation);
    }

    public Token() {
    }

    protected Token(Parcel in) {
        this.ut = in.readInt();
        this.districtRecommendation = in.readString();
    }

    public static final Creator<Token> CREATOR = new Creator<Token>() {
        @Override
        public Token createFromParcel(Parcel source) {
            return new Token(source);
        }

        @Override
        public Token[] newArray(int size) {
            return new Token[size];
        }
    };
}
