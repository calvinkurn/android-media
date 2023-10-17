package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.pojo.review.ReviewProduct
import com.tokopedia.homenav.mainnav.domain.model.NavReviewModel
import com.tokopedia.homenav.mainnav.domain.usecases.query.ProductRevWaitForFeedbackQuery
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created by dhaba
 */
class GetReviewProductUseCase (
        private val graphqlUseCase: GraphqlUseCase<ReviewProduct>
): UseCase<List<NavReviewModel>>(){

    init {
        graphqlUseCase.setGraphqlQuery(ProductRevWaitForFeedbackQuery())
        graphqlUseCase.setRequestParams(generateParam())
        graphqlUseCase.setTypeClass(ReviewProduct::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    override suspend fun executeOnBackground(): List<NavReviewModel> {
        val responseData = Success(graphqlUseCase.executeOnBackground().productRevWaitForFeedback)

        return responseData.data.list.map {
            NavReviewModel(
                productId = it.product.productIDStr,
                productName = it.product.productName,
                imageUrl = it.product.productImageURL,
                reputationId = it.reputationIDStr
            )
        }
    }

    companion object{
        private const val LIMIT = "limit"
        private const val MAX_LIMIT = 5
        private const val PAGE = "page"
        private const val PAGE_REVIEW = 1
    }

    private fun generateParam(): Map<String, Any?> {
        return mapOf(LIMIT to MAX_LIMIT, PAGE to PAGE_REVIEW)
    }
}
