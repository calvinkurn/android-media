package com.tokopedia.district_recommendation.di;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.district_recommendation.data.mapper.DistrictRecommendationEntityMapper;
import com.tokopedia.district_recommendation.data.repository.DistrictRecommendationRepository;
import com.tokopedia.district_recommendation.data.service.KeroApi;
import com.tokopedia.district_recommendation.data.source.DistrictRecommendationDataStore;
import com.tokopedia.district_recommendation.domain.usecase.GetDistrictRequestUseCase;
import com.tokopedia.district_recommendation.view.DistrictRecommendationContract;
import com.tokopedia.district_recommendation.view.DistrictRecommendationPresenter;
import com.tokopedia.logisticdata.data.converter.GeneratedHostConverter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.converter.StringResponseConverter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Irfan Khoirul on 20/11/17.
 */
@Module
public class DistrictRecommendationModule {

    public DistrictRecommendationModule() {

    }

    @Provides
    @DistrictRecommendationScope
    Retrofit providePeopleActRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.KERO_DOMAIN)
                .addConverterFactory(new GeneratedHostConverter())
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @DistrictRecommendationScope
    KeroApi provideKeroApi(Retrofit retrofit) {
        return retrofit.create(KeroApi.class);
    }

    // Provide Data Store
    @Provides
    @DistrictRecommendationScope
    DistrictRecommendationDataStore provideDistrictRecommendationDataStore(KeroApi keroApi) {
        return new DistrictRecommendationDataStore(keroApi);
    }

    // Provide EntityMapper
    @Provides
    @DistrictRecommendationScope
    DistrictRecommendationEntityMapper provideEntityMapper() {
        return new DistrictRecommendationEntityMapper();
    }

    // Provide Repository
    @Provides
    @DistrictRecommendationScope
    DistrictRecommendationRepository provideDistrictRecommendationRepository(
            DistrictRecommendationDataStore districtRecommendationDataStore,
            DistrictRecommendationEntityMapper districtRecommendationEntityMapper
    ) {
        return new DistrictRecommendationRepository(districtRecommendationDataStore,
                districtRecommendationEntityMapper);
    }

    // Provide Use Case
    @Provides
    @DistrictRecommendationScope
    GetDistrictRequestUseCase provideGetDistrictRequestUseCase(
            DistrictRecommendationRepository districtRecommendationRepository
    ) {
        return new GetDistrictRequestUseCase(districtRecommendationRepository);
    }

    // Provide Presenter
    @Provides
    @DistrictRecommendationScope
    DistrictRecommendationContract.Presenter provideDistrictRecommendationPresenter(
            GetDistrictRequestUseCase getDistrictRequestUseCase) {
        return new DistrictRecommendationPresenter(getDistrictRequestUseCase);
    }

}
