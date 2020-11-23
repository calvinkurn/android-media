package com.tokopedia.logisticCommon.data.entity.address.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by m.normansyah on 2/2/16.
 * modified by m.normansyah on 6/10/2016
 */
public class District implements Parcelable {


    public District() {
    }

    public long Id;
    private City districtCity;
    public String districtId;
    public String districtName;
    public String districtJneCode;

    public void setDistrictCity(City districtCity) {
        this.districtCity = districtCity;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictJneCode() {
        return districtJneCode;
    }

    public void setDistrictJneCode(String districtJneCode) {
        this.districtJneCode = districtJneCode;
    }

    protected District(Parcel in) {
        districtCity = in.readParcelable(City.class.getClassLoader());
        districtId = in.readString();
        districtName = in.readString();
        districtJneCode = in.readString();
        Id = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(districtCity, flags);
        dest.writeString(districtId);
        dest.writeString(districtName);
        dest.writeString(districtJneCode);
        dest.writeLong(Id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<District> CREATOR = new Creator<District>() {
        @Override
        public District createFromParcel(Parcel in) {
            return new District(in);
        }

        @Override
        public District[] newArray(int size) {
            return new District[size];
        }
    };
}
