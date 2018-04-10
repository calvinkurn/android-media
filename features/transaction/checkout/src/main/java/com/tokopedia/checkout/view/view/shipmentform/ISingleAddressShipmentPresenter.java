package com.tokopedia.checkout.view.view.shipmentform;

import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;

import java.util.HashMap;

/**
 * Created by Irfan Khoirul on 13/03/18.
 */

public interface ISingleAddressShipmentPresenter {

    void processCheckPromoCodeFromSuggestedPromo(String promoCode);

    HashMap<String, String> generatePickupPointParams(RecipientAddressModel addressAdapterData);

}
