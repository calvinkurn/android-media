package com.tokopedia.core.payment.model.responsecartstep1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * CreditCardData
 * Created by Angga.Prasetiyo on 05/07/2016.
 */
public class CreditCardData implements Parcelable {
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("postal_code")
    @Expose
    private String postalCode;
    @SerializedName("card_num")
    @Expose
    private String cardNum;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("first_name")
    @Expose
    private String firstName;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    protected CreditCardData(Parcel in) {
        city = in.readString();
        postalCode = in.readString();
        cardNum = in.readString();
        address = in.readString();
        phone = in.readString();
        state = in.readString();
        lastName = in.readString();
        firstName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(postalCode);
        dest.writeString(cardNum);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(state);
        dest.writeString(lastName);
        dest.writeString(firstName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CreditCardData> CREATOR = new Parcelable.Creator<CreditCardData>() {
        @Override
        public CreditCardData createFromParcel(Parcel in) {
            return new CreditCardData(in);
        }

        @Override
        public CreditCardData[] newArray(int size) {
            return new CreditCardData[size];
        }
    };
}
