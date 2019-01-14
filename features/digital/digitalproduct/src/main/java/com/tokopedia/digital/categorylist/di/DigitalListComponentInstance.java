package com.tokopedia.digital.categorylist.di;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.common_digital.common.di.DaggerDigitalComponent;
import com.tokopedia.common_digital.common.di.DigitalComponent;

/**
 * @author by furqan on 25/06/18.
 */

public class DigitalListComponentInstance {

    private static DigitalListComponent digitalListComponent;

    public static DigitalListComponent getDigitalListComponent(Application application) {
        if (digitalListComponent == null) {
            DigitalComponent digitalComponent = DaggerDigitalComponent.builder().baseAppComponent(
                    ((BaseMainApplication) application).getBaseAppComponent()).build();
            digitalListComponent = DaggerDigitalListComponent.builder().digitalComponent(
                    digitalComponent).build();
        }
        return digitalListComponent;
    }

}