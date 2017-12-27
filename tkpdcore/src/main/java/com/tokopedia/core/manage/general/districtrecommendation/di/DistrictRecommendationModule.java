package com.tokopedia.core.manage.general.districtrecommendation.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.manage.general.districtrecommendation.data.DistrictRecommendationEntityMapper;
import com.tokopedia.core.manage.general.districtrecommendation.data.repository.DistrictRecommendationRepository;
import com.tokopedia.core.manage.general.districtrecommendation.data.source.DistrictRecommendationDataStore;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;
import com.tokopedia.core.manage.general.districtrecommendation.domain.usecase.GetDistrictRequestUseCase;
import com.tokopedia.core.manage.general.districtrecommendation.view.DistrictRecommendationContract;
import com.tokopedia.core.manage.general.districtrecommendation.view.DistrictRecommendationPresenter;
import com.tokopedia.core.network.apiservices.kero.KeroAuthService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 20/11/17.
 */
@Module
public class DistrictRecommendationModule {

    private static final int RETRY_COUNT = 0;

    public DistrictRecommendationModule() {

    }

    // Provide KeroAuthService
    @Provides
    @DistrictRecommendationScope
    KeroAuthService provideKeroAuthService() {
        return new KeroAuthService(RETRY_COUNT);
    }

    // Provide Data Store
    @Provides
    @DistrictRecommendationScope
    DistrictRecommendationDataStore provideDistrictRecommendationDataStore(KeroAuthService keroAuthService) {
        return new DistrictRecommendationDataStore(keroAuthService);
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
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            DistrictRecommendationRepository districtRecommendationRepository
    ) {
        return new GetDistrictRequestUseCase(threadExecutor, postExecutionThread,
                districtRecommendationRepository);
    }

    // Provide Presenter
    @Provides
    @DistrictRecommendationScope
    DistrictRecommendationContract.Presenter provideDistrictRecommendationPresenter(
            GetDistrictRequestUseCase getDistrictRequestUseCase) {
        return new DistrictRecommendationPresenter(getDistrictRequestUseCase);
    }

}
