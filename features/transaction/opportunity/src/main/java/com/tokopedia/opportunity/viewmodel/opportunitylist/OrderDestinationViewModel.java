package com.tokopedia.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/7/17.
 */
public class OrderDestinationViewModel implements Parcelable{

    private int receiverPhoneIsTokopedia;
    private String receiverName;
    private String addressCountry;
    private String addressPostal;
    private String addressDistrict;
    private String receiverPhone;
    private String addressStreet;
    private String addressCity;
    private String addressProvince;

    public OrderDestinationViewModel() {
    }

    protected OrderDestinationViewModel(Parcel in) {
        receiverPhoneIsTokopedia = in.readInt();
        receiverName = in.readString();
        addressCountry = in.readString();
        addressPostal = in.readString();
        addressDistrict = in.readString();
        receiverPhone = in.readString();
        addressStreet = in.readString();
        addressCity = in.readString();
        addressProvince = in.readString();
    }

    public static final Creator<OrderDestinationViewModel> CREATOR = new Creator<OrderDestinationViewModel>() {
        @Override
        public OrderDestinationViewModel createFromParcel(Parcel in) {
            return new OrderDestinationViewModel(in);
        }

        @Override
        public OrderDestinationViewModel[] newArray(int size) {
            return new OrderDestinationViewModel[size];
        }
    };

    public int getReceiverPhoneIsTokopedia() {
        return receiverPhoneIsTokopedia;
    }

    public void setReceiverPhoneIsTokopedia(int receiverPhoneIsTokopedia) {
        this.receiverPhoneIsTokopedia = receiverPhoneIsTokopedia;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

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

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
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
        dest.writeInt(receiverPhoneIsTokopedia);
        dest.writeString(receiverName);
        dest.writeString(addressCountry);
        dest.writeString(addressPostal);
        dest.writeString(addressDistrict);
        dest.writeString(receiverPhone);
        dest.writeString(addressStreet);
        dest.writeString(addressCity);
        dest.writeString(addressProvince);
    }
}
