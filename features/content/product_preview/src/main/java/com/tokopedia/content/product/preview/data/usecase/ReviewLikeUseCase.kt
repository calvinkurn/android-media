package com.tokopedia.content.product.preview.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.product.preview.data.LikeReviewResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * @author by astidhiyaa on 30/11/23
 */
class ReviewLikeUseCase @Inject constructor(
    @ApplicationContext private val repo: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, LikeReviewResponse>(dispatchers.io) {
    override suspend fun execute(params: Map<String, Any>): LikeReviewResponse {
        return repo.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = QUERY

    fun convertToMap(param: Param) = mapOf(
        REVIEW_ID_PARAM to param.reviewId,
        LIKE_STATUS_PARAM to param.likeStatus.value
    )

    data class Param(
        val reviewId: String,
        val likeStatus: LikeStatus,
    )

    enum class LikeStatus(val value: Int) {
        Like(1),
        Dislike(2),
        Reset(3);
    }

    companion object {
        private const val REVIEW_ID_PARAM = "feedbackID"
        private const val LIKE_STATUS_PARAM = "likeStatus"

        private const val QUERY = """
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
