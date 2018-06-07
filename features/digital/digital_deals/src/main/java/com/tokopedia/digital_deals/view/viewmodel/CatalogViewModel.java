package com.tokopedia.digital_deals.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class CatalogViewModel implements Parcelable {

    private int digitalCategoryId;
    private int digitalProductId;
    private String digitalProductCode;
    public final static Parcelable.Creator<CatalogViewModel> CREATOR = new Creator<CatalogViewModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CatalogViewModel createFromParcel(Parcel in) {
            return new CatalogViewModel(in);
        }

        public CatalogViewModel[] newArray(int size) {
            return (new CatalogViewModel[size]);
        }

    };

    protected CatalogViewModel(Parcel in) {
        this.digitalCategoryId = in.readInt();
        this.digitalProductId = in.readInt();
        this.digitalProductCode = in.readString();
    }

    public CatalogViewModel() {
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