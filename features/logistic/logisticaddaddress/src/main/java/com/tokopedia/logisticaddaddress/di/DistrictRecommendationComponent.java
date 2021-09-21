package com.tokopedia.logisticaddaddress.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomBottomSheetFragment;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomBottomSheetRevamp;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 20/11/17.
 */
@DistrictRecommendationScope
@Component(modules = {DistrictRecommendationModule.class, GqlQueryModule.class}, dependencies = BaseAppComponent.class)
public interface DistrictRecommendationComponent {
    void inject(DiscomFragment discomFragment);
    void inject(DiscomBottomSheetFragment fragment);
    void inject(DiscomBottomSheetRevamp discomBottomSheetRevamp);
}
