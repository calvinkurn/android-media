package com.tokopedia.transactiondata.entity.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 11/02/19.
 */
public class TokopediaCornerData implements Parcelable {

    @SerializedName("is_tokopedia_corner")
    @Expose
    private boolean isTokopediaCorner;
    @SerializedName("user_corner_id")
    @Expose
    private String userCornerId;
    @SerializedName("corner_id")
    @Expose
    private int cornerId;

    public TokopediaCornerData() {
    }

    public TokopediaCornerData(String userCornerId, int cornerId) {
        this.isTokopediaCorner = true;
        this.userCornerId = userCornerId;
        this.cornerId = cornerId;
    }

    protected TokopediaCornerData(Parcel in) {
        isTokopediaCorner = in.readByte() != 0;
        userCornerId = in.readString();
        cornerId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isTokopediaCorner ? 1 : 0));
        dest.writeString(userCornerId);
        dest.writeInt(cornerId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TokopediaCornerData> CREATOR = new Creator<TokopediaCornerData>() {
        @Override
        public TokopediaCornerData createFromParcel(Parcel in) {
            return new TokopediaCornerData(in);
        }

        @Override
        public TokopediaCornerData[] newArray(int size) {
            return new TokopediaCornerData[size];
        }
    };

    public boolean isTokopediaCorner() {
        return isTokopediaCorner;
    }

    public void setTokopediaCorner(boolean tokopediaCorner) {
        isTokopediaCorner = tokopediaCorner;
    }

    public String getUserCornerId() {
        return userCornerId;
    }

    public void setUserCornerId(String userCornerId) {
        this.userCornerId = userCornerId;
    }

    public int getCornerId() {
        return cornerId;
    }

    public void setCornerId(int cornerId) {
        this.cornerId = cornerId;
    }
}
