package com.tokopedia.search.result.domain.usecase.getdynamicfilter

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.coroutines.Repository
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.common.UseCase
import com.tokopedia.search.result.data.repository.dynamicfilter.DynamicFilterCoroutineRepositoryModule
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SearchScope
@Module(includes = [DynamicFilterCoroutineRepositoryModule::class])
class GetDynamicFilterCoroutineUseCaseModule {

    @Provides
    @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_SHOP_USE_CASE)
    fun provideGetDynamicFilterUseCase(
            @Named(SearchConstant.DynamicFilter.DYNAMIC_FILTER_REPOSITORY) repository: Repository<DynamicFilterModel>): UseCase<DynamicFilterModel> {
        return GetDynamicFilterCoroutineUseCase(repository)
    }
}