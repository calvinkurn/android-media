package com.tokopedia.content.product.preview.data.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.product.preview.data.response.LikeReviewResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * @author by astidhiyaa on 30/11/23
 */
@GqlQuery(ReviewLikeUseCase.QUERY_NAME, ReviewLikeUseCase.QUERY)
class ReviewLikeUseCase @Inject constructor(
    @ApplicationContext private val repo: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<ReviewLikeUseCase.Param, LikeReviewResponse>(dispatchers.io) {

    private val query: GqlQueryInterface = ReviewLikeUseCaseQuery()

    override suspend fun execute(params: Param): LikeReviewResponse {
        return repo.request(query, params)
    }

    override fun graphqlQuery(): String = query.getQuery()

    data class Param(
        @SerializedName("feedbackID")
        val reviewId: String,
        @SerializedName("likeStatus")
        val likeStatus: Int
    ) : GqlParam

    companion object {
        const val QUERY_NAME = "ReviewLikeUseCaseQuery"
        const val QUERY = """
            mutation likeReview(${'$'}feedbackID: String!, ${'$'}likeStatus: Int!) {
              productrevLikeReview(feedbackID: ${'$'}feedbackID, likeStatus: ${'$'}likeStatus){
                feedbackID
                likeStatus
                totalLike
                totalDislike
              }
            }
        """
    }
}
