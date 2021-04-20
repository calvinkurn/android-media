package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.VisitChannelTracking
import javax.inject.Inject


/**
 * Created by mzennis on 05/02/21.
 */
class TrackVisitChannelBroadcasterUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
): GraphqlUseCase<VisitChannelTracking.Response>(graphqlRepository) {


    init {
        setGraphqlQuery(query)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(VisitChannelTracking.Response::class.java)
    }

    companion object {

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