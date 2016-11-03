package com.tokopedia.tkpd.payment.model.responsecartstep1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Veritrans
 * Created by Angga.Prasetiyo on 11/07/2016.
 */
public class Veritrans implements Parcelable{
    @SerializedName("token_url")
    @Expose
    private String tokenUrl;
    @SerializedName("client_key")
    @Expose
    private String clientKey;

    protected Veritrans(Parcel in) {
        tokenUrl = in.readString();
        clientKey = in.readString();
    }

    public static final Creator<Veritrans> CREATOR = new Creator<Veritrans>() {
        @Override
        public Veritrans createFromParcel(Parcel in) {
            return new Veritrans(in);
        }

        @Override
        public Veritrans[] newArray(int size) {
            return new Veritrans[size];
        }
    };

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tokenUrl);
        dest.writeString(clientKey);
    }
}
