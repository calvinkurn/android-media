package com.tokopedia.search.result.domain.usecase.getdynamicfilter;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.data.repository.dynamicfilter.DynamicFilterRepositoryModule;
import com.tokopedia.usecase.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module(includes = DynamicFilterRepositoryModule.class)
public class GetDynamicFilterUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_GQL_USE_CASE)
    UseCase<DynamicFilterModel> provideGetDynamicFilterGqlUseCase(
            @Named(SearchConstant.DynamicFilter.DYNAMIC_FILTER_GQL_REPOSITORY) Repository<DynamicFilterModel> repository) {
        return new GetDynamicFilterUseCase(repository);
    }
}
