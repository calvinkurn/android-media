package com.tokopedia.review.feature.createreputation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.createreputation.model.ProductrevSubmitReviewResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ProductrevSubmitReviewUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<ProductrevSubmitReviewResponseWrapper>(graphqlRepository){

    companion object {
        const val PARAM_REPUTATION_ID = "reputationID"
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_SHOP_ID = "shopID"
        const val PARAM_REPUTATION_SCORE = "reputationScore"
        const val PARAM_RATING = "rating"
        const val PARAM_REVIEW_TEXT = "reviewText"
        const val PARAM_IS_ANONYMOUS = "isAnonymous"
        const val PARAM_ATTACHMENT_ID = "attachmentIDs"
        private val query by lazy {
            """
                mutation productrevSubmitReview(${'$'}reputationID: Int!,${'$'}productID: Int!, ${'$'}shopID: Int!, ${'$'}reputationScore: Int, ${'$'}rating: Int!, ${'$'}reviewText: String, ${'$'}isAnonymous: Boolean, ${'$'}attachmentIDs: [String]) {
                  productrevSubmitReview(reputationID: ${'$'}reputationID, productID: ${'$'}productID , shopID: ${'$'}shopID, reputationScore: ${'$'}reputationScore, rating: ${'$'}rating, reviewText: ${'$'}reviewText , isAnonymous: ${'$'}isAnonymous, attachmentIDs: ${'$'}attachmentIDs) {
                    success
                  }
                }
            """.trimIndent()
        }
    }

    init {
        setTypeClass(ProductrevSubmitReviewResponseWrapper::class.java)
        setGraphqlQuery(query)
    }

    fun setParams(reputationId: Int, productId: Int, shopId: Int, reputationScore: Int = 0, rating: Int, reviewText: String, isAnonymous: Boolean, attachmentIds: List<String> = emptyList()) {
        setRequestParams(RequestParams.create().apply {
            putInt(PARAM_REPUTATION_ID, reputationId)
            putInt(PARAM_PRODUCT_ID, productId)
            putInt(PARAM_SHOP_ID, shopId)
            if(reputationScore != 0) {
                putInt(PARAM_REPUTATION_SCORE, reputationScore)
            }
            putInt(PARAM_RATING, rating)
            if(reviewText.isNotBlank()) {
                putString(PARAM_REVIEW_TEXT, reviewText)
            }
            if(isAnonymous) {
                putBoolean(PARAM_IS_ANONYMOUS, isAnonymous)
            }
            if(attachmentIds.isNotEmpty()) {
                putObject(PARAM_ATTACHMENT_ID, attachmentIds)
            }
        }.parameters)
    }
}