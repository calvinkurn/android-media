package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class ProductTextData implements Parcelable {

    @SerializedName("text_etd")
    @Expose
    private String textEtd;

    @SerializedName("text_price")
    @Expose
    private String textPrice;

    public ProductTextData() {
    }

    protected ProductTextData(Parcel in) {
        textEtd = in.readString();
        textPrice = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(textEtd);
        dest.writeString(textPrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductTextData> CREATOR = new Creator<ProductTextData>() {
        @Override
        public ProductTextData createFromParcel(Parcel in) {
            return new ProductTextData(in);
        }

        @Override
        public ProductTextData[] newArray(int size) {
            return new ProductTextData[size];
        }
    };

    public String getTextEtd() {
        return textEtd;
    }

    public void setTextEtd(String textEtd) {
        this.textEtd = textEtd;
    }

    public String getTextPrice() {
        return textPrice;
    }

    public void setTextPrice(String textPrice) {
        this.textPrice = textPrice;
    }
}
