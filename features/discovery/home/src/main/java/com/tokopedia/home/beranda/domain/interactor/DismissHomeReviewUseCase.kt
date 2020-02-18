package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.home.beranda.data.query.SuggestedReviewQuery
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class DismissHomeReviewUseCase @Inject constructor(
private val graphqlUseCase: GraphqlUseCase<ProductrevDismissSuggestion>
): UseCase<ProductrevDismissSuggestion>(){
    override suspend fun executeOnBackground(): ProductrevDismissSuggestion {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(SuggestedReviewQuery.dismissSuggestedReviewQuery)
        graphqlUseCase.setRequestParams(mapOf())
        return graphqlUseCase.executeOnBackground()
    }
}