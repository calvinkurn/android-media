package com.tokopedia.search.result.domain.usecase.getlocalsearchrecommendation

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.data.mapper.searchproduct.SearchProductMapperModule
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import rx.functions.Func1
import javax.inject.Named

@Module(includes = [SearchProductMapperModule::class])
class GetLocalSearchRecommendationUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProduct.GET_LOCAL_SEARCH_RECOMMENDATION_USE_CASE)
    fun provideGetLocalSearchRecommendationUseCase(
            searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>
    ): UseCase<SearchProductModel> {
        return GetLocalSearchRecommendationUseCase(GraphqlUseCase(), searchProductModelMapper)
    }
}