package com.tokopedia.checkout.domain.datamodel.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

public class CartTickerData implements Parcelable {

    private int id;
    private String message;
    private String page;

    public CartTickerData() {
    }

    public CartTickerData(int id, String message, String page) {
        this.id = id;
        this.message = message;
        this.page = page;
    }

    protected CartTickerData(Parcel in) {
        id = in.readInt();
        message = in.readString();
        page = in.readString();
    }

    public static final Creator<CartTickerData> CREATOR = new Creator<CartTickerData>() {
        @Override
        public CartTickerData createFromParcel(Parcel in) {
            return new CartTickerData(in);
        }

        @Override
        public CartTickerData[] newArray(int size) {
            return new CartTickerData[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(message);
        parcel.writeString(page);
    }
}
