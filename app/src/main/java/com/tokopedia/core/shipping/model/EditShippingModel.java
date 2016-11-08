package com.tokopedia.core.shipping.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kris on 11/6/2015.
 */
public class EditShippingModel {

    public static class ParamEditShop implements Parcelable{
        public String okeMinWeightValue = "0";
        public String feeTikiValue = "0";
        public String jneFeeValue = "0";
        public String jneAWB = "0";
        public String posMinWeightValue = "0";
        public String posFeeValue = "0";
        public String isAllow;
        public boolean jneOkeActivated = false;
        public String DiffDistrict = "0";
        public String latitude = "";
        public String longitude = "";
        public String fullAdress = "";
        public String isWhiteList = "0";
        public String isAllowGojek = "0";
        public String phoneNumber = "";
        public String msisdnVerified = "0";
        public String zipCode = "";
        public ParamEditShop(){

        }

        protected ParamEditShop(Parcel in) {
            okeMinWeightValue = in.readString();
            feeTikiValue = in.readString();
            jneFeeValue = in.readString();
            jneAWB = in.readString();
            posMinWeightValue = in.readString();
            posFeeValue = in.readString();
            isAllow = in.readString();
            DiffDistrict = in.readString();
            latitude = in.readString();
            longitude = in.readString();
            fullAdress = in.readString();
            isWhiteList = in.readString();
            isAllowGojek = in.readString();
            phoneNumber = in.readString();
            msisdnVerified = in.readString();
            zipCode = in.readString();
        }

        public static final Creator<ParamEditShop> CREATOR = new Creator<ParamEditShop>() {
            @Override
            public ParamEditShop createFromParcel(Parcel in) {
                return new ParamEditShop(in);
            }

            @Override
            public ParamEditShop[] newArray(int size) {
                return new ParamEditShop[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(okeMinWeightValue);
            parcel.writeString(feeTikiValue);
            parcel.writeString(jneFeeValue);
            parcel.writeString(jneAWB);
            parcel.writeString(posMinWeightValue);
            parcel.writeString(posFeeValue);
            parcel.writeString(isAllow);
            parcel.writeString(DiffDistrict);
            parcel.writeString(latitude);
            parcel.writeString(longitude);
            parcel.writeString(fullAdress);
            parcel.writeString(isWhiteList);
            parcel.writeString(isAllowGojek);
            parcel.writeString(phoneNumber);
            parcel.writeString(msisdnVerified);
            parcel.writeString(zipCode);
        }
    }

    public static class CourierAttribute {
        public String ShippingImageUri;
        public String ShippingName;
        public String ExtraError;
        public Boolean isAllowShipping;
        public Boolean courierChecked;
        public String ShippingID;
        public String ShippingWeightNotice;
        public int ShippingMaxAddFee;
        public ArrayList<Integer> checkedPackage;
        public List<PackageAttribute> packageAttributes = new ArrayList<>();
    }

    public static class PackageAttribute{
        public String ShippingPackage;
        public int ShippingPackageID;
        public Boolean ShippingChecked;
        public String ShippingDescription;
        public int isInstantCourier = 0;
    }

    public static class ProvinceAttribute{
        public String provinceID = "N/A";
        public ArrayList<String> cityNames = new ArrayList<>();
        public ArrayList<CityAttribute> cityList = new ArrayList<>();
    }

    public static class CityAttribute{
        public String cityID = "N/A";
        public ArrayList<String> districtNames = new ArrayList<>();
        public ArrayList<DistrictAttribute> districtList = new ArrayList<>();
    }

    public static class DistrictAttribute{
        public String districtID = "N/A";
        public ArrayList<String> supportedCourierID = new ArrayList<>();
    }

}