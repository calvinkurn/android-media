package com.tokopedia.tokomart.searchcategory.domain.usecase

import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides

@Module
class GetFilterUseCaseModule {

    @Provides
    fun provideGetFilterUseCase(): UseCase<DynamicFilterModel> {
        return GetFilterUseCase(GraphqlInteractor.getInstance().multiRequestGraphqlUseCase)
    }
}