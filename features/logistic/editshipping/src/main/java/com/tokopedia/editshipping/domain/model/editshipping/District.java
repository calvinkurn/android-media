package com.tokopedia.editshipping.domain.model.editshipping;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kris on 3/2/2016.
 */
public class District implements Parcelable {
    @SerializedName("district_id")
    @Expose
    public long districtId;
    @SerializedName("district_name")
    @Expose
    public String districtName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.districtId);
        dest.writeString(this.districtName);
    }

    public District() {
        districtId = 0;
    }

    protected District(Parcel in) {
        this.districtId = (Long) in.readValue(Long.class.getClassLoader());
        this.districtName = in.readString();
    }

    public static final Parcelable.Creator<District> CREATOR = new Parcelable.Creator<District>() {
        @Override
        public District createFromParcel(Parcel source) {
            return new District(source);
        }

        @Override
        public District[] newArray(int size) {
            return new District[size];
        }
    };
}
