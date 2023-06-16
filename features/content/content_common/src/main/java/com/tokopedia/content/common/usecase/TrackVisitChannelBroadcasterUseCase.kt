package com.tokopedia.content.common.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.TrackVisitChannelResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by mzennis on 05/02/21.
 * duplicate: com.tokopedia.feedcomponent.domain.usecase.FeedBroadcastTrackerUseCase
 */
@GqlQuery(TrackVisitChannelBroadcasterUseCase.QUERY_NAME, TrackVisitChannelBroadcasterUseCase.QUERY)
class TrackVisitChannelBroadcasterUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers,
): GraphqlUseCase<TrackVisitChannelResponse.Response>(graphqlRepository) {

    init {
        setGraphqlQuery(TrackVisitChannelBroadcasterUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(TrackVisitChannelResponse.Response::class.java)
    }

    override suspend fun executeOnBackground(): TrackVisitChannelResponse.Response {
        var count = 0

        while(true) {
            try {
                val response = withContext(dispatcher.io) { super.executeOnBackground() }
                if (response.model.success) return response
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

        const val FEED_ENTRY_POINT_VALUE = "VOD_POST"

        const val QUERY_NAME = "TrackVisitChannelBroadcasterUseCaseQuery"
        const val QUERY = """
            mutation trackVisitChannelBroadcaster(${'$'}channelId: String!, ${'$'}entryPoint: String) {
              broadcasterReportVisitChannel(channelID: ${'$'}channelId, entryPoint: ${'$'}entryPoint) {
                success
              }
            }
        """

        fun createParams(
                channelId: String,
                entryPoint: String,
        ): Map<String, Any> = mapOf(
            PARAMS_CHANNEL_ID to channelId,
            PARAMS_ENTRY_POINT to entryPoint
        )
    }
}
