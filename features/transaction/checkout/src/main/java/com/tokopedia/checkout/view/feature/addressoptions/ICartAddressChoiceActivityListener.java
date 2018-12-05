package com.tokopedia.checkout.view.feature.addressoptions;

import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.RecipientAddressModel;

/**
 * @author anggaprasetiyo on 26/02/18.
 */

public interface ICartAddressChoiceActivityListener {

    void finishSendResultActionSelectedAddress(RecipientAddressModel selectedAddressResult);

    void finishSendResultActionToMultipleAddressForm();

    void setToolbarTitle(String title);
}
