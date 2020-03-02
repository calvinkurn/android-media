package com.tokopedia.digital_deals.view.activity;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.di.DealsComponentInstance;

public class DealsBaseActivity extends BaseSimpleActivity implements HasComponent<DealsComponent> {

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public DealsComponent getComponent() {
        return DealsComponentInstance.getDealsComponent(getApplication());
    }
}
