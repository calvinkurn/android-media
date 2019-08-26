package com.tokopedia.search.result.domain.usecase.getdynamicfilter

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.coroutines.Repository
import com.tokopedia.discovery.common.data.DynamicFilterModel
import com.tokopedia.search.result.data.repository.dynamicfilter.DynamicFilterCoroutineRepositoryModule
import com.tokopedia.search.result.domain.usecase.SearchUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [
    DynamicFilterCoroutineRepositoryModule::class
])
class GetDynamicFilterCoroutineUseCaseModule {

    @Provides
    @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE)
    fun provideDynamicFilterUseCase(
            @Named(SearchConstant.DynamicFilter.DYNAMIC_FILTER_REPOSITORY)
            repository: Repository<DynamicFilterModel>
    ): SearchUseCase<DynamicFilterModel> {
        return GetDynamicFilterCoroutineUseCase(repository)
    }

    @Provides
    @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_V4_USE_CASE)
    fun provideDynamicFilterV4UseCase(
            @Named(SearchConstant.DynamicFilter.DYNAMIC_FILTER_REPOSITORY_V4)
            repository: Repository<DynamicFilterModel>
    ): SearchUseCase<DynamicFilterModel> {
        return GetDynamicFilterCoroutineUseCase(repository)
    }
}
