package com.tokopedia.digital.product;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.digital.product.di.DaggerDigitalProductComponent;
import com.tokopedia.digital.product.di.DigitalProductComponent;

/**
 * @author by furqan on 25/06/18.
 */

public class DigitalProductComponentInstance {

    private static DigitalProductComponent digitalProductComponent;

    public static DigitalProductComponent getDigitalProductComponent(Application application) {
        if (digitalProductComponent == null) {
            digitalProductComponent = DaggerDigitalProductComponent.builder().baseAppComponent(
                    ((BaseMainApplication)application).getBaseAppComponent()).build();
        }
        return digitalProductComponent;
    }
}
