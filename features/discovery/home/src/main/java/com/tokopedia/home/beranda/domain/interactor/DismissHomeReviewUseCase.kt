package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class DismissHomeReviewUseCase @Inject constructor(
private val graphqlUseCase: GraphqlUseCase<ProductrevDismissSuggestion>
): UseCase<ProductrevDismissSuggestion>(){
    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(ProductrevDismissSuggestion::class.java)
        graphqlUseCase.setRequestParams(mapOf())
    }
    override suspend fun executeOnBackground(): ProductrevDismissSuggestion {
        graphqlUseCase.clearCache()
        return graphqlUseCase.executeOnBackground()
    }
}