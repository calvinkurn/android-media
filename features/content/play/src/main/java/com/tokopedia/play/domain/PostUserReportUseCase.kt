package com.tokopedia.play.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.UserReportSubmissionResponse
import com.tokopedia.play.ui.toolbar.model.PartnerType
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
        setGraphqlQuery(PostUserReportUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(UserReportSubmissionResponse::class.java)
    }

    fun createParam(
       params: ChannelReportParams
    ): RequestParams{
        val param = mutableMapOf(
            REPORTER_ID_PARAM to params.reporterId,
            CHANNEL_ID_PARAM to params.channelId,
            MEDIA_URL_PARAM to params.mediaUrl,
            REASON_ID_PARAM to params.reasonId,
            TIMESTAMP_PARAM to params.timestamp,
            DESCRIPTION_PARAM to params.desc,
        ).apply {
            if (params.partnerType == PartnerType.Buyer) put(USER_ID_PARAM, params.partnerId)
            else put(SHOP_ID_PARAM, params.partnerId)
        }

        return RequestParams.create().apply {
            putObject(INPUT, param)
        }
    }

    data class ChannelReportParams(
        val channelId: Long,
        val partnerId: Long,
        val partnerType: PartnerType,
        val reasonId: Int,
        val timestamp: Long,
        val desc: String,
        val reporterId: Long,
        val mediaUrl: String,
    )

    companion object {
        private const val REPORTER_ID_PARAM = "reporter_id"
        private const val CHANNEL_ID_PARAM = "channel_id"
        private const val MEDIA_URL_PARAM = "media_url"
        private const val REASON_ID_PARAM = "reason_id"
        private const val TIMESTAMP_PARAM = "timestamp"
        private const val DESCRIPTION_PARAM = "description"
        private const val SHOP_ID_PARAM = "shop_id"
        private const val INPUT = "input"
        private const val USER_ID_PARAM = "user_id"

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
