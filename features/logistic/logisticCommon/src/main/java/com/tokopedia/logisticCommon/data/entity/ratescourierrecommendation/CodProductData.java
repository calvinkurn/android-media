package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 18/12/18.
 */
public class CodProductData implements Parcelable {

    @SerializedName("is_cod_available")
    @Expose
    private Integer isCodAvailable;
    @SerializedName("cod_text")
    @Expose
    private String codText;
    @SerializedName("cod_price")
    @Expose
    private Integer codPrice;
    @SerializedName("formatted_price")
    @Expose
    private String formattedPrice;
    @SerializedName("tnc_text")
    @Expose
    private String tncText;
    @SerializedName("tnc_link")
    @Expose
    private String tncLink;

    protected CodProductData(Parcel in) {
        isCodAvailable = in.readInt();
        codText = in.readString();
        codPrice = in.readInt();
        formattedPrice = in.readString();
        tncText = in.readString();
        tncLink = in.readString();
    }

    public static final Creator<CodProductData> CREATOR = new Creator<CodProductData>() {
        @Override
        public CodProductData createFromParcel(Parcel in) {
            return new CodProductData(in);
        }

        @Override
        public CodProductData[] newArray(int size) {
            return new CodProductData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(isCodAvailable);
        parcel.writeString(codText);
        parcel.writeInt(codPrice);
        parcel.writeString(formattedPrice);
        parcel.writeString(tncText);
        parcel.writeString(tncLink);
    }


    public Integer getIsCodAvailable() {
        return isCodAvailable;
    }

    public void setIsCodAvailable(Integer isCodAvailable) {
        this.isCodAvailable = isCodAvailable;
    }

    public String getCodText() {
        return codText;
    }

    public void setCodText(String codText) {
        this.codText = codText;
    }

    public Integer getCodPrice() {
        return codPrice;
    }

    public void setCodPrice(Integer codPrice) {
        this.codPrice = codPrice;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public String getTncText() {
        return tncText;
    }

    public void setTncText(String tncText) {
        this.tncText = tncText;
    }

    public String getTncLink() {
        return tncLink;
    }

    public void setTncLink(String tncLink) {
        this.tncLink = tncLink;
    }
}
