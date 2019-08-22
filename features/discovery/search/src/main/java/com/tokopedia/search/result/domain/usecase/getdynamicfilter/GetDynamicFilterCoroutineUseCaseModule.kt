package com.tokopedia.search.result.domain.usecase.getdynamicfilter

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.data.DynamicFilterModel
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GetDynamicFilterCoroutineUseCaseModule {

    @Provides
    @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE)
    fun provideDynamicFilterUseCase(): UseCase<DynamicFilterModel> {
        return GetDynamicFilterCoroutineUseCase()
    }
}
