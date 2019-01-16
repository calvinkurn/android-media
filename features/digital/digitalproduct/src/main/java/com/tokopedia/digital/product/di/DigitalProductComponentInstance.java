package com.tokopedia.digital.product.di;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.common_digital.common.di.DaggerDigitalCommonComponent;
import com.tokopedia.common_digital.common.di.DigitalCommonComponent;
import com.tokopedia.digital.common.di.DaggerDigitalComponent;
import com.tokopedia.digital.common.di.DigitalComponent;

/**
 * @author by furqan on 25/06/18.
 */

public class DigitalProductComponentInstance {

    private static DigitalProductComponent digitalProductComponent;

    public static DigitalProductComponent getDigitalProductComponent(Application application) {
        if (digitalProductComponent == null) {
            DigitalCommonComponent digitalCommonComponent = DaggerDigitalCommonComponent.builder().baseAppComponent(
                    ((BaseMainApplication) application).getBaseAppComponent()).build();
            DigitalComponent digitalComponent = DaggerDigitalComponent.builder()
                    .digitalCommonComponent(digitalCommonComponent)
                    .build();

            digitalProductComponent = DaggerDigitalProductComponent.builder()
                    .digitalComponent(digitalComponent).build();
        }
        return digitalProductComponent;
    }

}