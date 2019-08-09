package com.tokopedia.logisticaddaddress.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DistrictRecommendationBottomSheetFragment;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 20/11/17.
 */
@DistrictRecommendationScope
@Component(modules = DistrictRecommendationModule.class, dependencies = BaseAppComponent.class)
public interface DistrictRecommendationComponent {
    void inject(DiscomFragment discomFragment);
    void inject(DistrictRecommendationBottomSheetFragment fragment);
}
