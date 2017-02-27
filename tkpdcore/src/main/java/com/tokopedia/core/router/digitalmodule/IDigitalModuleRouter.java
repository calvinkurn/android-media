package com.tokopedia.core.router.digitalmodule;

import android.content.Intent;

import com.tokopedia.core.router.digitalmodule.passdata.ExampPassData;

/**
 * @author anggaprasetiyo on 2/23/17.
 */

public interface IDigitalModuleRouter {

    Intent instanceIntentCartDigitalProduct(ExampPassData passData);
}
