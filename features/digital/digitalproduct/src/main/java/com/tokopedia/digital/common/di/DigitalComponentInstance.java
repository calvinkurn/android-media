package com.tokopedia.digital.common.di;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.common_digital.common.di.DaggerDigitalCommonComponent;
import com.tokopedia.common_digital.common.di.DigitalCommonComponent;

public class DigitalComponentInstance {
    private static DigitalComponent digitalComponent;

    public static DigitalComponent getInstance(Application application) {
        if (digitalComponent == null) {

            DigitalCommonComponent digitalCommonComponent = DaggerDigitalCommonComponent.builder().baseAppComponent(
                    ((BaseMainApplication) application).getBaseAppComponent()).build();
            digitalComponent = DaggerDigitalComponent.builder()
                    .digitalCommonComponent(digitalCommonComponent)
                    .build();
        }
        return digitalComponent;
    }
}
