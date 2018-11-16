package com.tokopedia.district_recommendation.di;

import com.tokopedia.district_recommendation.view.DistrictRecommendationFragment;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 20/11/17.
 */
@DistrictRecommendationScope
@Component(modules = DistrictRecommendationModule.class)
public interface DistrictRecommendationComponent {
    void inject(DistrictRecommendationFragment districtRecommendationFragment);
}
