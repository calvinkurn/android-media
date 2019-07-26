package com.tokopedia.checkout.domain.datamodel.addressoptions;

import com.tokopedia.logisticcart.domain.shipping.RecipientAddressModel;

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
