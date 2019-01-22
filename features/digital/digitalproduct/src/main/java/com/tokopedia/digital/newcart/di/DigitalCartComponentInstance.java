package com.tokopedia.digital.newcart.di;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.common_digital.common.di.DaggerDigitalCommonComponent;
import com.tokopedia.common_digital.common.di.DigitalCommonComponent;
import com.tokopedia.digital.cart.di.DaggerDigitalCartComponent;
import com.tokopedia.digital.common.di.DaggerDigitalComponent;
import com.tokopedia.digital.common.di.DigitalComponent;

/**
 * Created by Rizky on 29/08/18.
 */
public class DigitalCartComponentInstance {

    private static DigitalCartComponent digitalCartComponent;

    public static DigitalCartComponent getDigitalCartComponent(Application application) {
        if (digitalCartComponent == null) {
            DigitalCommonComponent digitalCommonComponent = DaggerDigitalCommonComponent.builder().baseAppComponent(
                    ((BaseMainApplication) application).getBaseAppComponent()).build();
            DigitalComponent digitalComponent = DaggerDigitalComponent.builder()
                    .digitalCommonComponent(digitalCommonComponent)
                    .build();
            digitalCartComponent = DaggerDigitalCartComponent.builder().digitalComponent(
                    digitalComponent).build();
        }
        return digitalCartComponent;
    }

}
