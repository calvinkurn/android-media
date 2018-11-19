package com.tokopedia.district_recommendation.view.v2.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.district_recommendation.view.v2.DistrictRecommendationFragment;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 20/11/17.
 */
@DistrictRecommendationScopeV2
@Component(modules = DistrictRecommendationModuleV2.class, dependencies = BaseAppComponent.class)
public interface DistrictRecommendationComponentV2 {
    void inject(DistrictRecommendationFragment districtRecommendationFragment);
}
