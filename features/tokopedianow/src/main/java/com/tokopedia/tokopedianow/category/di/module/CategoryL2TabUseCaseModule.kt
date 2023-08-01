package com.tokopedia.tokopedianow.category.di.module

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.tokopedianow.category.di.scope.CategoryL2TabScope
import com.tokopedia.tokopedianow.searchcategory.domain.model.GetProductCountModel
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.GetProductCountUseCase
import dagger.Module
import dagger.Provides

@Module
class CategoryL2TabUseCaseModule {

    @Provides
    @CategoryL2TabScope
    fun provideGetProductCountUseCase(): GetProductCountUseCase {
        val graphqlUseCase = GraphqlUseCase<GetProductCountModel>(
            GraphqlInteractor.getInstance().graphqlRepository
        )
        return GetProductCountUseCase(graphqlUseCase)
    }
}
