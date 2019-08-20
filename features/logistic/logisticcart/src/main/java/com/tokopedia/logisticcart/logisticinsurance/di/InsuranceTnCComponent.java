package com.tokopedia.logisticcart.logisticinsurance.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.logisticdata.data.module.qualifier.InsuranceTnCScope;
import com.tokopedia.logisticcart.logisticinsurance.view.InsuranceTnCFragment;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

@InsuranceTnCScope
@Component(modules = InsuranceTnCModule.class, dependencies = BaseAppComponent.class)
public interface InsuranceTnCComponent {

    void inject(InsuranceTnCFragment insuranceTnCFragment);
}
