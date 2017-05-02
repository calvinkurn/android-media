package com.tokopedia.core.router.digitalmodule;

import android.content.Intent;

import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;

/**
 * @author anggaprasetiyo on 2/23/17.
 */

public interface IDigitalModuleRouter {

    int REQUEST_CODE_CART_DIGITAL = 216;
    String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    Intent instanceIntentCartDigitalProduct(DigitalCheckoutPassData passData);

}
