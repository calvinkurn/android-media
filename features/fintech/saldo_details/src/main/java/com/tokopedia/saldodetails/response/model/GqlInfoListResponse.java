package com.tokopedia.saldodetails.response.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GqlInfoListResponse implements Parcelable {


    @SerializedName("label")
    private String label;

    @SerializedName("value")
    private String value;

    public GqlInfoListResponse(Parcel in) {
        this.label = in.readString();
        this.value = in.readString();
    }

    public static final Creator<GqlInfoListResponse> CREATOR = new Creator<GqlInfoListResponse>() {
        @Override
        public GqlInfoListResponse createFromParcel(Parcel in) {
            return new GqlInfoListResponse(in);
        }

        @Override
        public GqlInfoListResponse[] newArray(int size) {
            return new GqlInfoListResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(value);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
