package com.tokopedia.core.router.digitalmodule;

import android.content.Intent;

import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;

/**
 * @author anggaprasetiyo on 2/23/17.
 */

public interface IDigitalModuleRouter {

    Intent instanceIntentCartDigitalProduct(DigitalCheckoutPassData passData);
}
