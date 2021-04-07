package com.tokopedia.search.result.domain.usecase.getinspirationcarouselchips

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GetInspirationCarouselChipsProductUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_GET_INSPIRATION_CAROUSEL_CHIPS_PRODUCTS_USE_CASE)
    fun provideGetInspirationCarouselChipsProductUseCase(): UseCase<InspirationCarouselChipsProductModel> {
        return GetInspirationCarouselChipsProductsUseCase(GraphqlUseCase())
    }
}