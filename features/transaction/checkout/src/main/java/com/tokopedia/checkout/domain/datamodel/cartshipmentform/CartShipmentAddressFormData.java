package com.tokopedia.checkout.domain.datamodel.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public class CartShipmentAddressFormData implements Parcelable {
    private boolean isError;
    private String errorMessage;

    private int errorCode;
    private boolean isMultiple;
    private List<GroupAddress> groupAddress = new ArrayList<>();
    private String keroToken;
    private String keroDiscomToken;
    private int keroUnixTime;
    private Donation donation;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }

    public List<GroupAddress> getGroupAddress() {
        return groupAddress;
    }

    public void setGroupAddress(List<GroupAddress> groupAddress) {
        this.groupAddress = groupAddress;
    }

    public String getKeroToken() {
        return keroToken;
    }

    public void setKeroToken(String keroToken) {
        this.keroToken = keroToken;
    }

    public String getKeroDiscomToken() {
        return keroDiscomToken;
    }

    public void setKeroDiscomToken(String keroDiscomToken) {
        this.keroDiscomToken = keroDiscomToken;
    }

    public int getKeroUnixTime() {
        return keroUnixTime;
    }

    public void setKeroUnixTime(int keroUnixTime) {
        this.keroUnixTime = keroUnixTime;
    }

    public Donation getDonation() {
        return donation;
    }

    public void setDonation(Donation donation) {
        this.donation = donation;
    }

    public CartShipmentAddressFormData() {
    }

    protected CartShipmentAddressFormData(Parcel in) {
        isError = in.readByte() != 0;
        errorMessage = in.readString();
        errorCode = in.readInt();
        isMultiple = in.readByte() != 0;
        groupAddress = in.createTypedArrayList(GroupAddress.CREATOR);
        keroToken = in.readString();
        keroDiscomToken = in.readString();
        keroUnixTime = in.readInt();
        donation = in.readParcelable(Donation.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isError ? 1 : 0));
        dest.writeString(errorMessage);
        dest.writeInt(errorCode);
        dest.writeByte((byte) (isMultiple ? 1 : 0));
        dest.writeTypedList(groupAddress);
        dest.writeString(keroToken);
        dest.writeString(keroDiscomToken);
        dest.writeInt(keroUnixTime);
        dest.writeParcelable(donation, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartShipmentAddressFormData> CREATOR = new Creator<CartShipmentAddressFormData>() {
        @Override
        public CartShipmentAddressFormData createFromParcel(Parcel in) {
            return new CartShipmentAddressFormData(in);
        }

        @Override
        public CartShipmentAddressFormData[] newArray(int size) {
            return new CartShipmentAddressFormData[size];
        }
    };
}
