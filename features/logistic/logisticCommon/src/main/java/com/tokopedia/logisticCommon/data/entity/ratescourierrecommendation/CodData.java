package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 18/12/18.
 */
public class CodData implements Parcelable {

    @SerializedName("is_cod")
    @Expose
    private int isCod;
    @SerializedName("cod_text")
    @Expose
    private String codText;

    public CodData() {
    }

    protected CodData(Parcel in) {
        isCod = in.readInt();
        codText = in.readString();
    }

    public static final Creator<CodData> CREATOR = new Creator<CodData>() {
        @Override
        public CodData createFromParcel(Parcel in) {
            return new CodData(in);
        }

        @Override
        public CodData[] newArray(int size) {
            return new CodData[size];
        }
    };

    public int getIsCod() {
        return isCod;
    }

    public void setIsCod(int isCod) {
        this.isCod = isCod;
    }

    public String getCodText() {
        return codText;
    }

    public void setCodText(String codText) {
        this.codText = codText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(isCod);
        parcel.writeString(codText);
    }
}
