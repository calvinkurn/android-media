package com.tokopedia.review.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.common.data.ToggleLikeReviewResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery(ToggleLikeReviewUseCase.TOGGLE_LIKE_REVIEW_MUTATION_CLASS_NAME, ToggleLikeReviewUseCase.TOGGLE_LIKE_REVIEW_MUTATION)
class ToggleLikeReviewUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ToggleLikeReviewResponse>(graphqlRepository) {

    companion object {
        const val PARAM_REVIEW_ID = "reviewID"
        const val PARAM_SHOP_ID = "shopID"
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_LIKE_STATUS = "likeStatus"
        const val TOGGLE_LIKE_REVIEW_MUTATION_CLASS_NAME = "ToggleLikeReviewMutation"
        const val TOGGLE_LIKE_REVIEW_MUTATION = """
            mutation toogleLikeDislike(${'$'}reviewID: Int, ${'$'}shopID: Int, ${'$'}productID: Int, ${'$'}likeStatus: Int) {
              toggleProductReviewLike(reviewID: ${'$'}reviewID, shopID: ${'$'}shopID, productID: ${'$'}productID, likeStatus: ${'$'}likeStatus) {
                id
                likeStatus
                totalLike
                totalDislike
              }
            }
        """
    }

    init {
        setTypeClass(ToggleLikeReviewResponse::class.java)
        setGraphqlQuery(ToggleLikeReviewMutation.GQL_QUERY)
    }

    private val requestParams = RequestParams.create()

    fun setParams(reviewId: String, shopId: String, productId: String, likeStatus: Int) {
        requestParams.apply {
            putLong(PARAM_REVIEW_ID, reviewId.toLong())
            putLong(PARAM_SHOP_ID, shopId.toLong())
            putLong(PARAM_PRODUCT_ID, productId.toLong())
            putInt(PARAM_LIKE_STATUS, likeStatus)
        }
        setRequestParams(requestParams.parameters)
    }
}