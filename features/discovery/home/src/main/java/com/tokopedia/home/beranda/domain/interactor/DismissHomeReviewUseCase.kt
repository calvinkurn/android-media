package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.query.SuggestedReviewQuery
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class DismissHomeReviewUseCase @Inject constructor(
private val graphqlUseCase: GraphqlUseCase<ProductrevDismissSuggestion>
): UseCase<ProductrevDismissSuggestion>(){
    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(ProductrevDismissSuggestion::class.java)
    }
    override suspend fun executeOnBackground(): ProductrevDismissSuggestion {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(SuggestedReviewQuery.dismissSuggestedReviewQuery)
        graphqlUseCase.setRequestParams(mapOf())
        return graphqlUseCase.executeOnBackground()
    }
}