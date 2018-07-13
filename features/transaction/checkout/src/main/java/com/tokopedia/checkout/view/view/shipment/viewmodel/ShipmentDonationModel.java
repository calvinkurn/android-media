package com.tokopedia.checkout.view.view.shipment.viewmodel;

import com.tokopedia.checkout.domain.datamodel.cartshipmentform.Donation;
import com.tokopedia.checkout.view.view.shipment.ShipmentData;

/**
 * @author Irfan Khoirul on 13/07/18.
 */

public class ShipmentDonationModel implements ShipmentData {

    private Donation donation;
    private boolean checked;

    public ShipmentDonationModel() {
    }

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
