package com.tokopedia.checkout.view.feature.shipment.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Donation;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentData;

/**
 * @author Irfan Khoirul on 13/07/18.
 */

public class ShipmentDonationModel implements ShipmentData, Parcelable {

    private Donation donation;
    private boolean checked;

    public ShipmentDonationModel() {
    }

    protected ShipmentDonationModel(Parcel in) {
        donation = in.readParcelable(Donation.class.getClassLoader());
        checked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(donation, flags);
        dest.writeByte((byte) (checked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShipmentDonationModel> CREATOR = new Creator<ShipmentDonationModel>() {
        @Override
        public ShipmentDonationModel createFromParcel(Parcel in) {
            return new ShipmentDonationModel(in);
        }

        @Override
        public ShipmentDonationModel[] newArray(int size) {
            return new ShipmentDonationModel[size];
        }
    };

    public Donation getDonation() {
        return donation;
    }

    public void setDonation(Donation donation) {
        this.donation = donation;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
