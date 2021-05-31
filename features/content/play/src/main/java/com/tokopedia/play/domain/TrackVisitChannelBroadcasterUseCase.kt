package com.tokopedia.play.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.VisitChannelTracking
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by mzennis on 05/02/21.
 */
class TrackVisitChannelBroadcasterUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers,
): GraphqlUseCase<VisitChannelTracking.Response>(graphqlRepository) {


    init {
        setGraphqlQuery(query)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(VisitChannelTracking.Response::class.java)
    }

    override suspend fun executeOnBackground(): VisitChannelTracking.Response {
        var count = 0

        while(true) {
            try {
                val response = withContext(dispatcher.io) { super.executeOnBackground() }
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

        private val query = """
        mutation trackVisitChannelBroadcaster(${'$'}channelId: String!) {
          broadcasterReportVisitChannel(channelID: ${'$'}channelId) {
            success
          }
        }
        """.trimIndent()

        fun createParams(
                channelId: String,
        ): Map<String, Any> = mapOf(
                PARAMS_CHANNEL_ID to channelId,
        )
    }
}