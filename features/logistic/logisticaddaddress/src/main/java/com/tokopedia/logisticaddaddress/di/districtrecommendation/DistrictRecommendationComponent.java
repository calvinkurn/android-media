package com.tokopedia.logisticaddaddress.di.districtrecommendation;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.scope.ActivityScope;
import com.tokopedia.logisticaddaddress.di.AddAddressBaseModule;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 20/11/17.
 */
@ActivityScope
@Component(modules = {AddAddressBaseModule.class, DistrictRecommendationModule.class, DiscomViewModelModule.class}, dependencies = BaseAppComponent.class)
public interface DistrictRecommendationComponent {
    void inject(DiscomFragment discomFragment);
}
