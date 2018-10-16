
package com.tokopedia.challenges.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthProvider implements Parcelable {

    @SerializedName("Network")
    @Expose
    private String network;
    @SerializedName("Id")
    @Expose
    private String id;

    protected AuthProvider(Parcel in) {
        network = in.readString();
        id = in.readString();
    }

    public static final Creator<AuthProvider> CREATOR = new Creator<AuthProvider>() {
        @Override
        public AuthProvider createFromParcel(Parcel in) {
            return new AuthProvider(in);
        }

        @Override
        public AuthProvider[] newArray(int size) {
            return new AuthProvider[size];
        }
    };

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(network);
        dest.writeString(id);
    }
}
