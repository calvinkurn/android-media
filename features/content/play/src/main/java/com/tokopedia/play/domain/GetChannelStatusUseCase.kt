package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.ChannelStatusResponse
import javax.inject.Inject


/**
 * Created by mzennis on 01/02/21.
 */
class GetChannelStatusUseCase @Inject constructor(
        gqlRepository: GraphqlRepository
): GraphqlUseCase<ChannelStatusResponse>(gqlRepository) {

    private val query = """
        query GetChannelStatus(${'$'}channelIds: [String]){
          playGetChannelsStatus(req: {
            ids: ${'$'}channelIds
          }) {
            data {
              id
              status
            }
          }
        }
    """.trimIndent()

    init {
        setGraphqlQuery(query)
        setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ChannelStatusResponse::class.java)
    }

    companion object {
        private const val PARAM_CHANNEL_IDS = "channelIds"

        fun createParams(channelIds: Array<String>): Map<String, Any> = mapOf(
                PARAM_CHANNEL_IDS to channelIds
        )
    }
}