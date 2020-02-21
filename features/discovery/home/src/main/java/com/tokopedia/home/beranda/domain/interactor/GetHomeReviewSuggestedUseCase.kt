package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetHomeReviewSuggestedUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<SuggestedProductReview>
): UseCase<SuggestedProductReview>(){
    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(SuggestedProductReview::class.java)
        graphqlUseCase.setRequestParams(mapOf())
    }
    override suspend fun executeOnBackground(): SuggestedProductReview {
        graphqlUseCase.clearCache()
        return graphqlUseCase.executeOnBackground()
    }
}