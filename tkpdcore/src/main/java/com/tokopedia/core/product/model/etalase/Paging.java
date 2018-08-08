package com.tokopedia.core.product.model.etalase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 13/11/2015.
 */
public class Paging implements Parcelable{
    private static final String TAG = Paging.class.getSimpleName();

    @SerializedName("uri_next")
    @Expose
    private String uriNext;
    @SerializedName("uri_previous")
    @Expose
    private String uriPrevious;

    public String getUriNext() {
        return uriNext;
    }

    public void setUriNext(String uriNext) {
        this.uriNext = uriNext;
    }

    public String getUriPrevious() {
        return uriPrevious;
    }

    public void setUriPrevious(String uriPrevious) {
        this.uriPrevious = uriPrevious;
    }

    protected Paging(Parcel in) {
        uriNext = in.readByte() == 0x00 ? null : in.readString();
        uriPrevious = in.readByte() == 0x00 ? null : in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (uriNext == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeString(uriNext);
        }
        if (uriPrevious == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeString(uriPrevious);
        }
    }

    @SuppressWarnings("unused")
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
}
