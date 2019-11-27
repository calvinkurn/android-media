package com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Irfan Khoirul on 13/07/18.
 */

public class Donation implements Parcelable {
    private String title;
    private int nominal;
    private String description;

    public Donation() {
    }

    protected Donation(Parcel in) {
        title = in.readString();
        nominal = in.readInt();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(nominal);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Donation> CREATOR = new Creator<Donation>() {
        @Override
        public Donation createFromParcel(Parcel in) {
            return new Donation(in);
        }

        @Override
        public Donation[] newArray(int size) {
            return new Donation[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
