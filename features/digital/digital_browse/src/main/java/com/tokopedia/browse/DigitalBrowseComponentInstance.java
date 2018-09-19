package com.tokopedia.browse;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.browse.common.di.DaggerDigitalBrowseComponent;
import com.tokopedia.browse.common.di.DigitalBrowseComponent;

/**
 * @author by furqan on 19/09/18.
 */

public class DigitalBrowseComponentInstance {
    private static DigitalBrowseComponent digitalBrowseComponent;

    public static DigitalBrowseComponent getDigitalBrowseComponent(Application application) {
        if (digitalBrowseComponent == null) {
            digitalBrowseComponent = DaggerDigitalBrowseComponent.builder().baseAppComponent(
                    ((BaseMainApplication) application).getBaseAppComponent()).build();
        }

        return digitalBrowseComponent;
    }
}
