package com.tokopedia.digital_deals.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Catalog implements Parcelable {

    @SerializedName("digital_category_id")
    @Expose
    private int digitalCategoryId;
    @SerializedName("digital_product_id")
    @Expose
    private int digitalProductId;
    @SerializedName("digital_product_code")
    @Expose
    private String digitalProductCode;
    public final static Parcelable.Creator<Catalog> CREATOR = new Creator<Catalog>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Catalog createFromParcel(Parcel in) {
            return new Catalog(in);
        }

        public Catalog[] newArray(int size) {
            return (new Catalog[size]);
        }

    };

    protected Catalog(Parcel in) {
        this.digitalCategoryId = in.readInt();
        this.digitalProductId = in.readInt();
        this.digitalProductCode = in.readString();
    }

    public Catalog() {
    }

    public int getDigitalCategoryId() {
        return digitalCategoryId;
    }

    public void setDigitalCategoryId(int digitalCategoryId) {
        this.digitalCategoryId = digitalCategoryId;
    }

    public int getDigitalProductId() {
        return digitalProductId;
    }

    public void setDigitalProductId(int digitalProductId) {
        this.digitalProductId = digitalProductId;
    }

    public String getDigitalProductCode() {
        return digitalProductCode;
    }

    public void setDigitalProductCode(String digitalProductCode) {
        this.digitalProductCode = digitalProductCode;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(digitalCategoryId);
        dest.writeInt(digitalProductId);
        dest.writeString(digitalProductCode);
    }

    public int describeContents() {
        return 0;
    }

}