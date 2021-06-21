package com.tokopedia.tokomart.searchcategory.domain.usecase

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.tokomart.searchcategory.domain.model.GetProductCountModel
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides

@Module
class GetProductCountUseCaseModule {

    @Provides
    fun provideGetProductCountUseCase(): UseCase<String> {
        val graphqlUseCase = GraphqlUseCase<GetProductCountModel>(
                GraphqlInteractor.getInstance().graphqlRepository
        )
        return GetProductCountUseCase(graphqlUseCase)
    }
}