package com.tokopedia.logisticinsurance.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.logisticdata.data.module.qualifier.InsuranceTnCScope;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticTokopediaWsV4ResponseConverterQualifier;
import com.tokopedia.logisticinsurance.view.InsuranceTnCFragment;

import dagger.Component;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

@InsuranceTnCScope
@Component(modules = InsuranceTnCModule.class, dependencies = BaseAppComponent.class)
public interface InsuranceTnCComponent {

    void inject(InsuranceTnCFragment insuranceTnCFragment);
}
