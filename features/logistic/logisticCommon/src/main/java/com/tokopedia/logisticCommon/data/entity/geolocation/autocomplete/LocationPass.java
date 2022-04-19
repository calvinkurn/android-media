package com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 1/29/16.
 */
public class LocationPass implements Parcelable {

    private String latitude = "";
    private String longitude = "";
    private String manualAddress = "";
    private String generatedAddress = "";
    private String cityName = "";
    private String districtName = "";

    protected LocationPass(Parcel in) {
        latitude = in.readString();
        longitude = in.readString();
        manualAddress = in.readString();
        generatedAddress = in.readString();
        cityName = in.readString();
        districtName = in.readString();
    }

    public static final Creator<LocationPass> CREATOR = new Creator<LocationPass>() {
        @Override
        public LocationPass createFromParcel(Parcel in) {
            return new LocationPass(in);
        }

        @Override
        public LocationPass[] newArray(int size) {
            return new LocationPass[size];
        }
    };

    public LocationPass() {

    }

    public LocationPass(String latitude, String longitude, String manualAddress, String generatedAddress, String cityName, String districtName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.manualAddress = manualAddress;
        this.generatedAddress = generatedAddress;
        this.cityName = cityName;
        this.districtName = districtName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(manualAddress);
        parcel.writeString(generatedAddress);
        parcel.writeString(cityName);
        parcel.writeString(districtName);
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getManualAddress() {
        return manualAddress;
    }

    public void setManualAddress(String manualAddress) {
        this.manualAddress = manualAddress;
    }

    public void setGeneratedAddress(String generatedAddress) {
        this.generatedAddress = generatedAddress;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getGeneratedAddress() {
        return generatedAddress;
    }

    public static class Builder {
        private String latitude;
        private String longitude;
        private String manualAddress;
        private String generatedAddress;
        private String cityName;
        private String districtName;

        public Builder() {
        }

        public static Builder aLocationPass() {
            return new Builder();
        }

        public Builder setLatitude(String latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder setLongitude(String longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder setManualAddress(String manualAddress) {
            this.manualAddress = manualAddress;
            return this;
        }

        public Builder setGeneratedAddress(String generatedAddress) {
            this.generatedAddress = generatedAddress;
            return this;
        }

        public Builder setCityName(String generateCityName) {
            this.cityName = generateCityName;
            return this;
        }

        public Builder setDistrictName(String districtName) {
            this.districtName = districtName;
            return this;
        }

        public Builder but() {
            return aLocationPass()
                    .setLatitude(latitude)
                    .setLongitude(longitude)
                    .setManualAddress(manualAddress)
                    .setGeneratedAddress(generatedAddress)
                    .setCityName(cityName)
                    .setDistrictName(districtName);
        }

        public LocationPass build() {
            LocationPass locationPass = new LocationPass();
            locationPass.setLatitude(latitude);
            locationPass.setLongitude(longitude);
            locationPass.setManualAddress(manualAddress);
            locationPass.setGeneratedAddress(generatedAddress);
            locationPass.setCityName(cityName);
            locationPass.setDistrictName(districtName);
            return locationPass;
        }
    }
}
