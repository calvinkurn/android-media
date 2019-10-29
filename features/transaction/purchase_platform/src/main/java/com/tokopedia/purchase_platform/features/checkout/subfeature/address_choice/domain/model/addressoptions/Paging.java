package com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.model.addressoptions;

import android.os.Parcel;
import android.os.Parcelable;

public class Paging implements Parcelable {

    private String uriNext;

    public Paging() {
    }

    protected Paging(Parcel in) {
        uriNext = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uriNext);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Paging> CREATOR = new Creator<Paging>() {
        @Override
        public Paging createFromParcel(Parcel in) {
            return new Paging(in);
        }

        @Override
        public Paging[] newArray(int size) {
            return new Paging[size];
        }
    };

    public String getUriNext() {
        return uriNext;
    }

    public void setUriNext(String uriNext) {
        this.uriNext = uriNext;
    }
}
