package com.tokopedia.logisticaddaddress.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DistrictRecommendationActivity;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DistrictRecommendationFragment;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 20/11/17.
 */
@DistrictRecommendationScope
@Component(modules = DistrictRecommendationModule.class, dependencies = BaseAppComponent.class)
public interface DistrictRecommendationComponent {
    void inject(DistrictRecommendationFragment districtRecommendationFragment);
}
