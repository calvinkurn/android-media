package com.tokopedia.search.result.domain.usecase.searchsamesessionrecommendation

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.SearchSameSessionRecommendationModel
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class SearchSameSessionRecommendationUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.SEARCH_SAME_SESSION_RECOMMENDATION_USE_CASE)
    fun provideGetLocalSearchRecommendationUseCase(): UseCase<SearchSameSessionRecommendationModel> {
        return SearchSameSessionRecommendationGqlUseCase(GraphqlUseCase())
    }
}
