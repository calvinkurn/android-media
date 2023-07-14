package com.tokopedia.logisticaddaddress.di.districtrecommendation;

import com.tokopedia.abstraction.common.di.scope.ActivityScope;
import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery;
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase;
import com.tokopedia.logisticaddaddress.domain.executor.MainSchedulerProvider;
import com.tokopedia.logisticaddaddress.domain.executor.SchedulerProvider;
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper;
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecommendation;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomContract;
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomPresenter;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

/**
 * Created by Irfan Khoirul on 20/11/17.
 */
@Deprecated
@Module
public class DistrictRecommendationModule {

    public DistrictRecommendationModule() {

    }

    @Provides
    @ActivityScope
    SchedulerProvider provideSchedulerProvider() {
        return new MainSchedulerProvider();
    }

    @Provides
    @ActivityScope
    DiscomContract.Presenter provideDistrictRecommendationPresenter(
            RevGeocodeUseCase revGeocodeUseCase,
            GetDistrictRecommendation getDistrictRecommendation,
            DistrictRecommendationMapper mapper) {
        return new DiscomPresenter(revGeocodeUseCase, getDistrictRecommendation, mapper);
    }

    @Provides
    @IntoMap
    @StringKey(RawQueryConstant.GET_DISTRICT_RECOMMENDATION)
    String provideQueryDiscom() {
        return KeroLogisticQuery.INSTANCE.getDistrict_recommendation();
    }
}
