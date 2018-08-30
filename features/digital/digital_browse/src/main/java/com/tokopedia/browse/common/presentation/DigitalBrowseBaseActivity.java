package com.tokopedia.browse.common.presentation;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.browse.common.di.DigitalBrowseComponent;
import com.tokopedia.browse.common.di.utils.DigitalBrowseComponentUtils;
import com.tokopedia.browse.common.util.DigitalBrowseAnalytics;

import javax.inject.Inject;

/**
 * @author by furqan on 30/08/18.
 */

public abstract class DigitalBrowseBaseActivity extends BaseSimpleActivity {

    private DigitalBrowseComponent digitalBrowseComponent;

    @Inject
    DigitalBrowseAnalytics digitalBrowseAnalytics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        initInjector();
    }

    private void initInjector() {
        if (digitalBrowseComponent == null) {
            digitalBrowseComponent = DigitalBrowseComponentUtils.getDigitalBrowseComponent(getApplication());
        }
        digitalBrowseComponent.inject(this);
    }
}
