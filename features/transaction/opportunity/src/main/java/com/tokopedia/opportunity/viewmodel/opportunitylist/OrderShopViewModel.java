package com.tokopedia.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/7/17.
 */
public class OrderShopViewModel implements Parcelable{

    private String addressPostal;
    private String addressDistrict;
    private String addressCity;
    private String addressStreet;
    private String shipperPhone;
    private String addressCountry;
    private String addressProvince;

    public OrderShopViewModel() {
    }

    protected OrderShopViewModel(Parcel in) {
        addressPostal = in.readString();
        addressDistrict = in.readString();
        addressCity = in.readString();
        addressStreet = in.readString();
        shipperPhone = in.readString();
        addressCountry = in.readString();
        addressProvince = in.readString();
    }

    public static final Creator<OrderShopViewModel> CREATOR = new Creator<OrderShopViewModel>() {
        @Override
        public OrderShopViewModel createFromParcel(Parcel in) {
            return new OrderShopViewModel(in);
        }

        @Override
        public OrderShopViewModel[] newArray(int size) {
            return new OrderShopViewModel[size];
        }
    };

    public String getAddressPostal() {
        return addressPostal;
    }

    public void setAddressPostal(String addressPostal) {
        this.addressPostal = addressPostal;
    }

    public String getAddressDistrict() {
        return addressDistrict;
    }

    public void setAddressDistrict(String addressDistrict) {
        this.addressDistrict = addressDistrict;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getShipperPhone() {
        return shipperPhone;
    }

    public void setShipperPhone(String shipperPhone) {
        this.shipperPhone = shipperPhone;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getAddressProvince() {
        return addressProvince;
    }

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(addressPostal);
        dest.writeString(addressDistrict);
        dest.writeString(addressCity);
        dest.writeString(addressStreet);
        dest.writeString(shipperPhone);
        dest.writeString(addressCountry);
        dest.writeString(addressProvince);
    }
}
