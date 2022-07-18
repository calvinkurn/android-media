package com.tokopedia.digital_deals.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Catalog implements Parcelable {

    @SerializedName("digital_category_id")
    @Expose
    private String digitalCategoryId;
    @SerializedName("digital_product_id")
    @Expose
    private String digitalProductId;
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
        this.digitalCategoryId = in.readString();
        this.digitalProductId = in.readString();
        this.digitalProductCode = in.readString();
    }

    public Catalog() {
    }

    public String getDigitalCategoryId() {
        return digitalCategoryId;
    }

    public String getDigitalProductId() {
        return digitalProductId;
    }

    public String getDigitalProductCode() {
        return digitalProductCode;
    }

    public void setDigitalProductCode(String digitalProductCode) {
        this.digitalProductCode = digitalProductCode;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(digitalCategoryId);
        dest.writeString(digitalProductId);
        dest.writeString(digitalProductCode);
    }

    public int describeContents() {
        return 0;
    }

}