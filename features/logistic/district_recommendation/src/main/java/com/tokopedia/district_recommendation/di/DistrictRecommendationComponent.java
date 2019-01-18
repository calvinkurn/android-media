package com.tokopedia.district_recommendation.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.district_recommendation.view.DistrictRecommendationFragment;
import com.tokopedia.district_recommendation.view.shopsettings.DistrictRecommendationShopSettingsActivity;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 20/11/17.
 */
@DistrictRecommendationScope
@Component(modules = DistrictRecommendationModule.class, dependencies = BaseAppComponent.class)
public interface DistrictRecommendationComponent {
    void inject(DistrictRecommendationFragment districtRecommendationFragment);

    void inject(DistrictRecommendationShopSettingsActivity districtRecommendationShopSettingsActivity);
}
