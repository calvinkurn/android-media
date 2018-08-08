package com.tokopedia.core.talk.talkproduct.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Paging implements Parcelable {

    @SerializedName("uri_next")
    @Expose
    private String uriNext;
    @SerializedName("uri_previous")
    @Expose
    private String uriPrevious;

    /**
     *
     * @return
     *     The uriNext
     */
    public String getUriNext() {
        return uriNext;
    }

    /**
     *
     * @param uriNext
     *     The uri_next
     */
    public void setUriNext(String uriNext) {
        this.uriNext = uriNext;
    }

    /**
     *
     * @return
     *     The uriPrevious
     */
    public String getUriPrevious() {
        return uriPrevious;
    }

    /**
     *
     * @param uriPrevious
     *     The uri_previous
     */
    public void setUriPrevious(String uriPrevious) {
        this.uriPrevious = uriPrevious;
    }


    protected Paging(Parcel in) {
        uriNext = in.readString();
        uriPrevious = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uriNext);
        dest.writeString(uriPrevious);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Paging> CREATOR = new Parcelable.Creator<Paging>() {
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