package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.feedcomponent.data.pojo.VisitChannelTracking
import com.tokopedia.feedcomponent.domain.SUSPEND_GRAPHQL_REPOSITORY
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject
import javax.inject.Named

/**
 * duplicate: com.tokopedia.play.domain.TrackVisitChannelBroadcasterUseCase
 */
@GqlQuery(FeedBroadcastTrackerUseCase.QUERY_NAME, FeedBroadcastTrackerUseCase.QUERY)
class FeedBroadcastTrackerUseCase @Inject constructor(
        @param:Named(SUSPEND_GRAPHQL_REPOSITORY) private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<VisitChannelTracking.Response>(graphqlRepository) {


    init {
        setGraphqlQuery(FeedBroadcastTrackerUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(VisitChannelTracking.Response::class.java)
    }

    override suspend fun executeOnBackground(): VisitChannelTracking.Response {
        var count = 0

        while(true) {
            try {
                val response = super.executeOnBackground()
                if (response.reportVisitChannelTracking.success) return response
                else error("Error Report Track Visit Channel")
            } catch (e: Throwable) {
                if (++count > RETRY_COUNT) error("Error Report Track Visit Channel ${RETRY_COUNT}x")
            }
        }
    }

    companion object {

        private const val RETRY_COUNT = 3

        private const val PARAMS_CHANNEL_ID = "channelId"
        private const val PARAMS_ENTRY_POINT = "entryPoint"
        private const val FEED_ENTRY_POINT_VALUE = "VOD_POST"

        const val QUERY_NAME = "FeedBroadcastTrackerUseCaseQuery"
        const val QUERY = """
            mutation trackVisitChannelBroadcaster(${'$'}channelId: String!, ${'$'}entryPoint: String) {
               broadcasterReportVisitChannel(channelID: ${'$'}channelId, entryPoint: ${'$'}entryPoint) {
                success
              }
            }
        """

        fun createParams(
                channelId: String
        ): Map<String, Any> = mapOf(
                PARAMS_CHANNEL_ID to channelId,
                PARAMS_ENTRY_POINT to FEED_ENTRY_POINT_VALUE
        )
    }
}