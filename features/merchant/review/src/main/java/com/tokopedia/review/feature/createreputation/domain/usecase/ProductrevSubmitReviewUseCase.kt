package com.tokopedia.review.feature.createreputation.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.createreputation.model.ProductrevSubmitReviewResponseWrapper
import javax.inject.Inject

@GqlQuery(
    ProductrevSubmitReviewUseCase.SUBMIT_REVIEW_QUERY_CLASS_NAME,
    ProductrevSubmitReviewUseCase.SUBMIT_REVIEW_MUTATION
)
class ProductrevSubmitReviewUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<ProductrevSubmitReviewResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_REPUTATION_ID = "reputationID"
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_SHOP_ID = "shopID"
        const val PARAM_REPUTATION_SCORE = "reputationScore"
        const val PARAM_RATING = "rating"
        const val PARAM_REVIEW_TEXT = "reviewText"
        const val PARAM_IS_ANONYMOUS = "isAnonymous"
        const val PARAM_ATTACHMENT_ID = "attachmentIDs"
        const val PARAM_UTM_SOURCE = "utmSource"
        const val PARAM_BAD_RATING_CATEGORY_IDS = "badRatingCategoryIDs"
        const val SUBMIT_REVIEW_QUERY_CLASS_NAME = "SubmitReview"
        const val SUBMIT_REVIEW_MUTATION =
            """
                mutation productrevSubmitReviewV2(${'$'}reputationID: String!,${'$'}productID: String!, ${'$'}shopID: String!, ${'$'}reputationScore: Int, ${'$'}rating: Int!, ${'$'}reviewText: String, ${'$'}isAnonymous: Boolean, ${'$'}attachmentIDs: [String], ${'$'}utmSource: String, ${'$'}badRatingCategoryIDs: [String]) {
                  productrevSubmitReviewV2(reputationID: ${'$'}reputationID, productID: ${'$'}productID , shopID: ${'$'}shopID, reputationScore: ${'$'}reputationScore, rating: ${'$'}rating, reviewText: ${'$'}reviewText , isAnonymous: ${'$'}isAnonymous, attachmentIDs: ${'$'}attachmentIDs, utmSource: ${'$'}utmSource, badRatingCategoryIDs: ${'$'}badRatingCategoryIDs) {
                    success
                    feedbackID
                  }
                }
            """
    }

    init {
        setTypeClass(ProductrevSubmitReviewResponseWrapper::class.java)
        setGraphqlQuery(SubmitReview.GQL_QUERY)
    }

    fun setParams(requestParams: SubmitReviewRequestParams) {
        setRequestParams(requestParams.toRequestParamMap())
    }

    data class SubmitReviewRequestParams(
        val reputationId: String,
        val productId: String,
        val shopId: String,
        val reputationScore: Int = 0,
        val rating: Int,
        val reviewText: String,
        val isAnonymous: Boolean,
        val attachmentIds: List<String> = emptyList(),
        val utmSource: String,
        val badRatingCategoryIds: List<String>
    ) {
        fun toRequestParamMap(): Map<String, Any> {
            return mapOf(
                PARAM_REPUTATION_ID to reputationId,
                PARAM_PRODUCT_ID to productId,
                PARAM_SHOP_ID to shopId,
                PARAM_REPUTATION_SCORE to reputationScore,
                PARAM_RATING to rating,
                PARAM_REVIEW_TEXT to reviewText,
                PARAM_IS_ANONYMOUS to isAnonymous,
                PARAM_ATTACHMENT_ID to attachmentIds,
                PARAM_UTM_SOURCE to utmSource,
                PARAM_BAD_RATING_CATEGORY_IDS to badRatingCategoryIds
            )
        }
    }
}