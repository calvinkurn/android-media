package com.tokopedia.tkpd.payment.model.responsecartstep1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * CartDestination
 * Created by Angga.Prasetiyo on 05/07/2016.
 */
public class CartDestination implements Parcelable {

    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;
    @SerializedName("address_country")
    @Expose
    private String addressCountry;
    @SerializedName("address_name")
    @Expose
    private String addressName;
    @SerializedName("address_id")
    @Expose
    private String addressId;
    @SerializedName("address_postal")
    @Expose
    private String addressPostal;
    @SerializedName("receiver_phone")
    @Expose
    private String receiverPhone;
    @SerializedName("address_district")
    @Expose
    private String addressDistrict;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("address_street")
    @Expose
    private String addressStreet;
    @SerializedName("address_city")
    @Expose
    private String addressCity;
    @SerializedName("address_province")
    @Expose
    private String addressProvince;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressPostal() {
        return addressPostal;
    }

    public void setAddressPostal(String addressPostal) {
        this.addressPostal = addressPostal;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getAddressDistrict() {
        return addressDistrict;
    }

    public void setAddressDistrict(String addressDistrict) {
        this.addressDistrict = addressDistrict;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
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

    protected CartDestination(Parcel in) {
        longitude = in.readString();
        receiverName = in.readString();
        addressCountry = in.readString();
        addressName = in.readString();
        addressId = in.readString();
        addressPostal = in.readString();
        receiverPhone = in.readString();
        addressDistrict = in.readString();
        latitude = in.readString();
        addressStreet = in.readString();
        addressCity = in.readString();
        addressProvince = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(longitude);
        dest.writeString(receiverName);
        dest.writeString(addressCountry);
        dest.writeString(addressName);
        dest.writeString(addressId);
        dest.writeString(addressPostal);
        dest.writeString(receiverPhone);
        dest.writeString(addressDistrict);
        dest.writeString(latitude);
        dest.writeString(addressStreet);
        dest.writeString(addressCity);
        dest.writeString(addressProvince);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CartDestination> CREATOR = new Parcelable.Creator<CartDestination>() {
        @Override
        public CartDestination createFromParcel(Parcel in) {
            return new CartDestination(in);
        }

        @Override
        public CartDestination[] newArray(int size) {
            return new CartDestination[size];
        }
    };
}
