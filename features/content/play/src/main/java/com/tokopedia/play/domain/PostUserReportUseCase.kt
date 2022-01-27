package com.tokopedia.play.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.UserReportSubmissionResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * @author by astidhiyaa on 13/12/21
 */
@GqlQuery(PostUserReportUseCase.QUERY_NAME, PostUserReportUseCase.QUERY)
class PostUserReportUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<UserReportSubmissionResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(PostUserReportUseCaseQuery.GQL_QUERY)
        setCacheStrategy(
            GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(UserReportSubmissionResponse::class.java)
    }

    fun createParam(
        reporterId: Long,
        channelId: Long,
        mediaUrl: String,
        reasonId: Int,
        timestamp: Long,
        reportDesc: String,
        shopId: Long
    ): RequestParams{
        val params = mapOf(
            REPORTER_ID_PARAM to reporterId,
            CHANNEL_ID_PARAM to channelId,
            MEDIA_URL_PARAM to mediaUrl,
            REASON_ID_PARAM to reasonId,
            TIMESTAMP_PARAM to timestamp,
            DESCRIPTION_PARAM to reportDesc,
            SHOP_ID_PARAM to shopId
        )
        return RequestParams.create().apply {
            putObject(INPUT, params)
        }
    }

    companion object {
        private const val REPORTER_ID_PARAM = "reporter_id"
        private const val CHANNEL_ID_PARAM = "channel_id"
        private const val MEDIA_URL_PARAM = "media_url"
        private const val REASON_ID_PARAM = "reason_id"
        private const val TIMESTAMP_PARAM = "timestamp"
        private const val DESCRIPTION_PARAM = "description"
        private const val SHOP_ID_PARAM = "shop_id"
        private const val INPUT = "input"

        const val QUERY_NAME = "PostUserReportUseCaseQuery"
        const val QUERY = """
            mutation submitUserReport(${'$'}input: ReportVideoPlayRequest!){
             visionPostReportVideoPlay($INPUT: ${'$'}input) {
                status
            }
        }
        """
    }
}