package com.tokopedia.review.feature.createreputation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.createreputation.model.ProductRevEditReviewResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ProductrevEditReviewUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<ProductRevEditReviewResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_FEEDBACK_ID = "feedbackID"
        const val PARAM_REPUTATION_ID = "reputationID"
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_SHOP_ID = "shopID"
        const val PARAM_REPUTATION_SCORE = "reputationScore"
        const val PARAM_RATING = "rating"
        const val PARAM_REVIEW_TEXT = "reviewText"
        const val PARAM_IS_ANONYMOUS = "isAnonymous"
        const val PARAM_CURRENT_ATTACHMENT_URL = "currentAttachmentURLs"
        const val PARAM_NEW_ATTACHMENT_ID = "newAttachmentIDs"
        private val query by lazy {
            """
                mutation productrevEditReview(${'$'}feedbackID: String!, ${'$'}reputationID: String!, ${'$'}productID : String!, ${'$'}shopID: String!, ${'$'}rating: Int!, ${'$'}reviewText: String, ${'$'}isAnonymous: Boolean, ${'$'}currentAttachmentURLs: [String],${'$'}newAttachmentIDs: [String]) {
                  productrevEditReview(feedbackID: ${'$'}feedbackID, reputationID: ${'$'}reputationID, productID: ${'$'}productID, shopID: ${'$'}shopID, rating: ${'$'}rating, reviewText: ${'$'}reviewText, isAnonymous: ${'$'}isAnonymous , currentAttachmentURLs: ${'$'}currentAttachmentURLs, newAttachmentIDs: ${'$'}newAttachmentIDs) {
                    success
                  }
                }
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(ProductRevEditReviewResponseWrapper::class.java)
    }

    fun setParams(feedbackId: Int, reputationId: Int, productId: Int, shopId: Int, reputationScore: Int = 0, rating: Int, reviewText: String, isAnonymous: Boolean, oldAttachmentUrls: List<String>, attachmentIds: List<String> = emptyList()) {
        setRequestParams(RequestParams.create().apply {
            putString(PARAM_FEEDBACK_ID, feedbackId.toString())
            putString(PARAM_REPUTATION_ID, reputationId.toString())
            putString(PARAM_PRODUCT_ID, productId.toString())
            putString(PARAM_SHOP_ID, shopId.toString())
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
            if(oldAttachmentUrls.isNotEmpty()) {
                putObject(PARAM_CURRENT_ATTACHMENT_URL, oldAttachmentUrls)
            }
            if(attachmentIds.isNotEmpty()) {
                putObject(PARAM_NEW_ATTACHMENT_ID, attachmentIds)
            }
        }.parameters)
    }
}