package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.pojo.payment.Payment
import com.tokopedia.homenav.mainnav.data.pojo.payment.PaymentQuery
import com.tokopedia.homenav.mainnav.data.pojo.review.ReviewProduct
import com.tokopedia.homenav.mainnav.domain.model.NavPaymentOrder
import com.tokopedia.homenav.mainnav.domain.model.NavReviewOrder
import com.tokopedia.homenav.mainnav.domain.usecases.query.ProductRevWaitForFeedbackQuery
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created by dhaba
 */
class GetReviewProductUseCase (
        private val graphqlUseCase: GraphqlUseCase<ReviewProduct>
): UseCase<List<NavReviewOrder>>(){

    private var params : Map<String, Any> = mapOf()

    init {
        graphqlUseCase.setGraphqlQuery(ProductRevWaitForFeedbackQuery())
        graphqlUseCase.setRequestParams(generateParam())
        graphqlUseCase.setTypeClass(ReviewProduct::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    override suspend fun executeOnBackground(): List<NavReviewOrder> {
        val responseData = Success(graphqlUseCase.executeOnBackground().productRevWaitForFeedback)
        val navReviewList = mutableListOf<NavReviewOrder>()

        if (responseData.data.list.isNotEmpty()) {
            responseData.data.list.map {
                navReviewList.add(
                    NavReviewOrder(
                        productId = it.product.productID.toString(),
                        appLink = "",
                        productName = it.product.productName,
                        imageUrl = it.product.productImageURL
                    )
                )
            }
        }
        return navReviewList
    }

    companion object{
        private const val LANG = "lang"
        private const val DEFAULT_VALUE_LANG = "ID"
    }

    private fun generateParam(): Map<String, Any?> {
        return mapOf(LANG to DEFAULT_VALUE_LANG)
    }
}