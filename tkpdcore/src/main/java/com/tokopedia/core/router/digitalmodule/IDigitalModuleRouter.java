package com.tokopedia.core.router.digitalmodule;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;

/**
 * @author anggaprasetiyo on 2/23/17.
 */

public interface IDigitalModuleRouter {

    int REQUEST_CODE_CART_DIGITAL = 216;
    String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    int PAYMENT_SUCCESS = 5;
    int PAYMENT_CANCELLED = 6;
    int PAYMENT_FAILED = 7;

    Intent instanceIntentCartDigitalProduct(DigitalCheckoutPassData passData);

    Intent instanceIntentCartDigitalProductWithBundle(Bundle bundle);

}
