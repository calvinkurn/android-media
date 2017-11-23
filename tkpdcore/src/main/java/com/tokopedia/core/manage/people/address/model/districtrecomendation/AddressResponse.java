package com.tokopedia.core.manage.people.address.model.districtrecomendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 31/10/17.
 */

public class AddressResponse implements Parcelable {

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
    @SerializedName("next_available")
    @Expose
    private boolean nextAvailable;
    @SerializedName("data")
    @Expose
    private ArrayList<Address> addresses;

    protected AddressResponse(Parcel in) {
        nextAvailable = in.readByte() != 0;
    }

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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (nextAvailable ? 1 : 0));
    }
}
