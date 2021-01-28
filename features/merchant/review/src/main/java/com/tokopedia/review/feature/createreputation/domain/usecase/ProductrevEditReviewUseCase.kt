package com.tokopedia.review.feature.createreputation.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.review.feature.createreputation.model.ProductRevEditReviewResponseWrapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ProductrevEditReviewUseCase @Inject constructor(private val gqlUseCase: MultiRequestGraphqlUseCase): UseCase<ProductRevEditReviewResponseWrapper>() {

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
        const val EDIT_REVIEW_QUERY_CLASS_NAME = "EditReview"
        const val EDIT_REVIEW_MUTATION =
            """
                mutation productrevEditReview(${'$'}feedbackID: String!, ${'$'}reputationID: String!, ${'$'}productID : String!, ${'$'}shopID: String!, ${'$'}rating: Int!, ${'$'}reviewText: String, ${'$'}isAnonymous: Boolean, ${'$'}currentAttachmentURLs: [String],${'$'}newAttachmentIDs: [String]) {
                  productrevEditReview(feedbackID: ${'$'}feedbackID, reputationID: ${'$'}reputationID, productID: ${'$'}productID, shopID: ${'$'}shopID, rating: ${'$'}rating, reviewText: ${'$'}reviewText, isAnonymous: ${'$'}isAnonymous , currentAttachmentURLs: ${'$'}currentAttachmentURLs, newAttachmentIDs: ${'$'}newAttachmentIDs) {
                    success
                  }
                }
            """
    }

    private var requestParams = RequestParams.EMPTY

    fun setParams(feedbackId: Long, reputationId: Long, productId: Long, shopId: Long, reputationScore: Int = 0, rating: Int, reviewText: String, isAnonymous: Boolean, oldAttachmentUrls: List<String> = emptyList(), attachmentIds: List<String> = emptyList()) {
        requestParams = RequestParams.create().apply {
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
        }
    }

    @GqlQuery(EDIT_REVIEW_QUERY_CLASS_NAME, EDIT_REVIEW_MUTATION)
    override suspend fun executeOnBackground(): ProductRevEditReviewResponseWrapper {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(GraphqlRequest(EditReview.GQL_QUERY, ProductRevEditReviewResponseWrapper::class.java, requestParams.parameters))
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error: List<GraphqlError>? = gqlResponse.getError(ProductRevEditReviewResponseWrapper::class.java)
        if (error != null && error.isNotEmpty()) {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "), error.firstOrNull()?.extensions?.code.toString())
        }
        return gqlResponse.getData(ProductRevEditReviewResponseWrapper::class.java)
    }
}