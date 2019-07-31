package com.tokopedia.search.result.domain.usecase.getdynamicfilter;

import com.tokopedia.discovery.common.Repository;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.data.repository.dynamicfilter.DynamicFilterRepositoryModule;
import com.tokopedia.usecase.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module(includes = DynamicFilterRepositoryModule.class)
public class GetDynamicFilterUseCaseModule {

    @Provides
    @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE)
    UseCase<DynamicFilterModel> provideGetDynamicFilterUseCase(
            @Named(SearchConstant.DynamicFilter.DYNAMIC_FILTER_REPOSITORY) Repository<DynamicFilterModel> repository) {
        return new GetDynamicFilterUseCase(repository);
    }

    @Provides
    @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_V4_USE_CASE)
    UseCase<DynamicFilterModel> provideGetDynamicFilterV4UseCase(
            @Named(SearchConstant.DynamicFilter.DYNAMIC_FILTER_REPOSITORY_V4) Repository<DynamicFilterModel> repository) {
        return new GetDynamicFilterUseCase(repository);
    }
}
