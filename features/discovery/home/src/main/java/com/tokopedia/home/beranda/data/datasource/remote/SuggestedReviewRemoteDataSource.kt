package com.tokopedia.home.beranda.data.datasource.remote

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.beranda.common.HomeDispatcherProvider
import com.tokopedia.home.beranda.data.query.SuggestedReviewQuery
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import kotlinx.coroutines.withContext

class SuggestedReviewRemoteDataSource(
    private val graphqlRepository: GraphqlRepository,
    private val dispatchers: HomeDispatcherProvider
){
    suspend fun getSuggestedReview() = withContext(dispatchers.io()){
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val gqlRecommendationRequest = GraphqlRequest(
                SuggestedReviewQuery.suggestedReviewQuery,
                SuggestedProductReview::class.java,
                mapOf()
        )
        val response = graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
        val errors = response.getError(SuggestedProductReview::class.java)
        if(errors?.isNotEmpty() == true){
            error(errors.first().message)
        }
        response.getSuccessData<SuggestedProductReview>()
    }

    suspend fun dismissSuggestedReview() = withContext(dispatchers.io()){
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val gqlRecommendationRequest = GraphqlRequest(
                SuggestedReviewQuery.dismissSuggestedReviewQuery,
                ProductrevDismissSuggestion::class.java,
                mapOf()
        )
        val response = graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
        val errors = response.getError(ProductrevDismissSuggestion::class.java)
        if(errors?.isNotEmpty() == true){
            error(errors.first().message)
        }
        response.getSuccessData<ProductrevDismissSuggestion>()
    }
}