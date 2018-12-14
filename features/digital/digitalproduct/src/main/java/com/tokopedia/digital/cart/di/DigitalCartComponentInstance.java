package com.tokopedia.digital.cart.di;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.common_digital.common.di.DaggerDigitalComponent;
import com.tokopedia.common_digital.common.di.DigitalComponent;

/**
 * Created by Rizky on 29/08/18.
 */
public class DigitalCartComponentInstance {

    private static DigitalCartComponent digitalCartComponent;

    public static DigitalCartComponent getDigitalCartComponent(Application application) {
        if (digitalCartComponent == null) {
            DigitalComponent digitalComponent = DaggerDigitalComponent.builder().baseAppComponent(
                    ((BaseMainApplication) application).getBaseAppComponent()).build();
            digitalCartComponent = DaggerDigitalCartComponent.builder().digitalComponent(
                    digitalComponent).build();
        }
        return digitalCartComponent;
    }

}
