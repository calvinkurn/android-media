package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.home.beranda.data.query.SuggestedReviewQuery
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.domain.gql.tokopoint.TokopointQuery
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetHomeReviewSuggestedUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<SuggestedProductReview>
): UseCase<SuggestedProductReview>(){
    override suspend fun executeOnBackground(): SuggestedProductReview {
        graphqlUseCase.clearCache()
        graphqlUseCase.setTypeClass(SuggestedProductReview::class.java)
        graphqlUseCase.setGraphqlQuery(SuggestedReviewQuery.suggestedReviewQuery)
        graphqlUseCase.setRequestParams(mapOf())
        return graphqlUseCase.executeOnBackground()
    }
}