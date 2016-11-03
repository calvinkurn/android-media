package com.tokopedia.tkpd.purchase.model.response.txlist;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 21/04/2016.
 */
public class OrderDestination implements Parcelable {
    private static final String TAG = OrderDestination.class.getSimpleName();

    @SerializedName("receiver_phone_is_tokopedia")
    @Expose
    private Integer receiverPhoneIsTokopedia;
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;
    @SerializedName("address_country")
    @Expose
    private String addressCountry;
    @SerializedName("address_postal")
    @Expose
    private String addressPostal;
    @SerializedName("address_district")
    @Expose
    private String addressDistrict;
    @SerializedName("receiver_phone")
    @Expose
    private String receiverPhone;
    @SerializedName("address_street")
    @Expose
    private String addressStreet;
    @SerializedName("address_city")
    @Expose
    private String addressCity;
    @SerializedName("address_province")
    @Expose
    private String addressProvince;

    public Integer getReceiverPhoneIsTokopedia() {
        return receiverPhoneIsTokopedia;
    }

    public void setReceiverPhoneIsTokopedia(Integer receiverPhoneIsTokopedia) {
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

    public String getDetailDestination() {
        return Html.fromHtml(receiverName)
                + "\n"
                + Html.fromHtml(addressStreet)
                + "\n"
                + Html.fromHtml(addressDistrict)
                + ", "
                + addressCity
                + ", "
                + addressPostal
                + "\n"
                + addressProvince
                + "\nTelepon : "
                + receiverPhone;
    }

    protected OrderDestination(Parcel in) {
        receiverPhoneIsTokopedia = in.readByte() == 0x00 ? null : in.readInt();
        receiverName = in.readString();
        addressCountry = in.readString();
        addressPostal = in.readString();
        addressDistrict = in.readString();
        receiverPhone = in.readString();
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
        if (receiverPhoneIsTokopedia == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(receiverPhoneIsTokopedia);
        }
        dest.writeString(receiverName);
        dest.writeString(addressCountry);
        dest.writeString(addressPostal);
        dest.writeString(addressDistrict);
        dest.writeString(receiverPhone);
        dest.writeString(addressStreet);
        dest.writeString(addressCity);
        dest.writeString(addressProvince);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OrderDestination> CREATOR = new Parcelable.Creator<OrderDestination>() {
        @Override
        public OrderDestination createFromParcel(Parcel in) {
            return new OrderDestination(in);
        }

        @Override
        public OrderDestination[] newArray(int size) {
            return new OrderDestination[size];
        }
    };
}
