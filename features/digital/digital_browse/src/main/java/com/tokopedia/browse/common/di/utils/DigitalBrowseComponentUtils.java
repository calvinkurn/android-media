package com.tokopedia.browse.common.di.utils;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.browse.common.di.DaggerDigitalBrowseComponent;
import com.tokopedia.browse.common.di.DigitalBrowseComponent;

/**
 * @author by furqan on 30/08/18.
 */

public class DigitalBrowseComponentUtils {
    private static DigitalBrowseComponent digitalBrowseComponent;

    public static DigitalBrowseComponent getDigitalBrowseComponent(Application application) {
        if (digitalBrowseComponent == null) {
            digitalBrowseComponent = DaggerDigitalBrowseComponent.builder().baseAppComponent(
                    ((BaseMainApplication) application).getBaseAppComponent()).build();
        }
        return digitalBrowseComponent;
    }
}
