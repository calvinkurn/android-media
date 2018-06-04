package com.tokopedia.checkout.view.view.addressoptions;

import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;

/**
 * @author anggaprasetiyo on 26/02/18.
 */

public interface ICartAddressChoiceActivityListener {

    void finishSendResultActionSelectedAddress(RecipientAddressModel selectedAddressResult);

    void finishSendResultActionToMultipleAddressForm();
}
