package com.tokopedia.checkout.domain.datamodel.addressoptions;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

/**
 * Created by fajarnuha on 09/02/19.
 */
public class CornerAddressModel {

    private RecipientAddressModel cornerModel;
    private boolean isSelected;

    public CornerAddressModel() {
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public RecipientAddressModel getCornerModel() {
        return cornerModel;
    }

    public void setCornerModel(RecipientAddressModel cornerModel) {
        this.cornerModel = cornerModel;
    }

}
