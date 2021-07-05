package com.tokopedia.search.result.domain.usecase.getproductcount

import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.GET_PRODUCT_COUNT_USE_CASE
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GetProductCountUseCaseModule {

    @SearchScope
    @Provides
    @Named(GET_PRODUCT_COUNT_USE_CASE)
    fun provideGetProductCountUseCase(): UseCase<String> {
        return GetProductCountUseCase(GraphqlUseCase())
    }
}