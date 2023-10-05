package com.tokopedia.people.domains

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.people.model.SetLikeStatusRequest
import com.tokopedia.people.model.SetLikeStatusResponse
import com.tokopedia.people.views.uimodel.mapper.UserProfileLikeStatusMapper
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 16, 2023
 */
@GqlQuery(SetLikeStatusUseCase.QUERY_NAME, SetLikeStatusUseCase.QUERY)
class SetLikeStatusUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<SetLikeStatusRequest, SetLikeStatusResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = SetLikeStatusUseCaseQuery().getQuery()

    override suspend fun execute(params: SetLikeStatusRequest): SetLikeStatusResponse {
        val param = mapOf(
            PARAM_FEEDBACK_ID to params.feedbackID,
            PARAM_LIKE_STATUS to UserProfileLikeStatusMapper.getLikeStatus(params.isLike)
        )

        return repository.request(graphqlQuery(), param)
    }

    companion object {
        private const val PARAM_FEEDBACK_ID = "feedbackID"
        private const val PARAM_LIKE_STATUS = "likeStatus"

        const val QUERY_NAME = "SetLikeStatusUseCaseQuery"
        const val QUERY = """
            mutation ProductrevLikeReview(
                ${"$${PARAM_FEEDBACK_ID}"}: String!,
                ${"$${PARAM_LIKE_STATUS}"}: Int!
            ) {
                productrevLikeReview(
                    ${PARAM_FEEDBACK_ID}: ${"$${PARAM_FEEDBACK_ID}"},
                    ${PARAM_LIKE_STATUS}: ${"$${PARAM_LIKE_STATUS}"}
                ) {
                  feedbackID
                  likeStatus
                  totalLike
                  totalDislike
                }
            }
        """
    }
}
