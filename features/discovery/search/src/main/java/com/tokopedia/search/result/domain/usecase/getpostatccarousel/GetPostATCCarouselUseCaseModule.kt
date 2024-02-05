package com.tokopedia.search.result.domain.usecase.getpostatccarousel

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationCarousel
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GetPostATCCarouselUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.GET_POST_ATC_CAROUSEL_USE_CASE)
    fun provideGetPostATCCarouselUseCase(): UseCase<SearchInspirationCarousel> =
        GetPostATCCarouselUseCase(GraphqlUseCase())
}
