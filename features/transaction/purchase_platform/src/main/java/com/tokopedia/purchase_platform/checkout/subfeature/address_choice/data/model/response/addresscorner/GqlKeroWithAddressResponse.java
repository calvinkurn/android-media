package com.tokopedia.purchase_platform.checkout.subfeature.address_choice.data.model.response.addresscorner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 11/02/19.
 */
public class GqlKeroWithAddressResponse {

    @SerializedName("keroAddressWithCorner")
    @Expose
    AddressCornerResponse keroAddressWithCorner;

    public GqlKeroWithAddressResponse() {
    }

    public AddressCornerResponse getKeroAddressWithCorner() {
        return keroAddressWithCorner;
    }

    public void setKeroAddressWithCorner(AddressCornerResponse keroAddressWithCorner) {
        this.keroAddressWithCorner = keroAddressWithCorner;
    }
}
