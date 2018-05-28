package com.tokopedia.district_recommendation.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.district_recommendation.view.DistrictRecommendationFragment;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 20/11/17.
 */
@DistrictRecommendationScope
@Component(modules = DistrictRecommendationModule.class, dependencies = AppComponent.class)
public interface DistrictRecommendationComponent {
    void inject(DistrictRecommendationFragment districtRecommendationFragment);

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

}
