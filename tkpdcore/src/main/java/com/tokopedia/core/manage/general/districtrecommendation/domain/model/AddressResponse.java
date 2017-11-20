package com.tokopedia.core.manage.general.districtrecommendation.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 17/11/17.
 */

public class AddressResponse implements Parcelable {
    private boolean nextAvailable;
    private ArrayList<Address> addresses;

    public AddressResponse() {
    }

    protected AddressResponse(Parcel in) {
        nextAvailable = in.readByte() != 0;
    }

    public static final Creator<AddressResponse> CREATOR = new Creator<AddressResponse>() {
        @Override
        public AddressResponse createFromParcel(Parcel in) {
            return new AddressResponse(in);
        }

        @Override
        public AddressResponse[] newArray(int size) {
            return new AddressResponse[size];
        }
    };

    public boolean isNextAvailable() {
        return nextAvailable;
    }

    public void setNextAvailable(boolean nextAvailable) {
        this.nextAvailable = nextAvailable;
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (nextAvailable ? 1 : 0));
        dest.writeList(addresses);
    }
}
