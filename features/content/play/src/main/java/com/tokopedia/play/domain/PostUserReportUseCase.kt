package com.tokopedia.play.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.UserReportSubmissionResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * @author by astidhiyaa on 13/12/21
 */
@GqlQuery(PostUserReportUseCase.QUERY_NAME, PostUserReportUseCase.QUERY)
class PostUserReportUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<UserReportSubmissionResponse>(graphqlRepository) {

    var params: RequestParams = RequestParams.EMPTY

    fun createParam(
        reporterId: Long,
        channelId: Long,
        mediaUrl: String,
        ownerChannelUserId: Long,
        reasonId: Int,
        timestamp: Long,
        reportDesc: String
    ): HashMap<String, Any> {
        return hashMapOf(
            INPUT to hashMapOf(
                REPORTER_ID_PARAM to reporterId,
                CHANNEL_ID_PARAM to channelId,
                MEDIA_URL_PARAM to mediaUrl,
                USER_ID_PARAM to ownerChannelUserId,
                REASON_ID_PARAM to reasonId,
                TIMESTAMP_PARAM to timestamp,
                DESCRIPTION_PARAM to reportDesc
            )
        )
    }

    override suspend fun executeOnBackground(): UserReportSubmissionResponse {
        val gqlRequest = GraphqlRequest(
            PostUserReportUseCaseQuery.GQL_QUERY,
            UserReportSubmissionResponse::class.java,
            params.parameters
        )
        val gqlResponse = graphqlRepository.response(
            listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build()
        )

        val error = gqlResponse.getError(UserReportSubmissionResponse::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData(UserReportSubmissionResponse::class.java)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }
                .joinToString(separator = ", "))
        }
    }

    companion object {
        private const val REPORTER_ID_PARAM = "reporter_id"
        private const val CHANNEL_ID_PARAM = "channel_id"
        private const val MEDIA_URL_PARAM = "media_url"
        private const val USER_ID_PARAM = "user_id"
        private const val REASON_ID_PARAM = "reason_id"
        private const val TIMESTAMP_PARAM = "timestamp"
        private const val DESCRIPTION_PARAM = "description"
        private const val INPUT = "input"

        const val QUERY_NAME = "PostUserReportUseCaseQuery"
        const val QUERY = """
            mutation {
             visionPostReportVideoPlay(
                 $INPUT: {
                    $REPORTER_ID_PARAM: Int64!,
                    $CHANNEL_ID_PARAM: Int64,
                    $MEDIA_URL_PARAM: String!,
                    $USER_ID_PARAM: Int64,
                    $REASON_ID_PARAM: Int,
                    $TIMESTAMP_PARAM: Int64,
                    $DESCRIPTION_PARAM: "String"
                 }
             ) 
            {
                status
            }
        }
        """
    }
}