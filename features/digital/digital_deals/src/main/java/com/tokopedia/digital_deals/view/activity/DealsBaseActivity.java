package com.tokopedia.digital_deals.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsComponent;

public class DealsBaseActivity extends BaseSimpleActivity implements HasComponent<DealsComponent> {

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public DealsComponent getComponent() {
        return  DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication)getApplication()).getBaseAppComponent())
                .build();
    }
}
