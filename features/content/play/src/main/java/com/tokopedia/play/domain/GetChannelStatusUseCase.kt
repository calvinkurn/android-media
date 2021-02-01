package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.ChannelStatusResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 01/02/21.
 */
class GetChannelStatusUseCase @Inject constructor(private val gqlUseCase: GraphqlRepository): UseCase<ChannelStatusResponse>() {

    var params: Map<String, Any> = emptyMap()

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
    """

    override suspend fun executeOnBackground(): ChannelStatusResponse {
        val gqlRequest = GraphqlRequest(query, ChannelStatusResponse::class.java, params)
        val gqlResponse = gqlUseCase.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val error = gqlResponse.getError(ChannelStatusResponse::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(ChannelStatusResponse::class.java) as ChannelStatusResponse)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val PARAM_CHANNEL_IDS = "channelIds"

        fun createParams(channelIds: Array<StrictMath>): Map<String, Any> = mapOf(
                PARAM_CHANNEL_IDS to channelIds
        )
    }
}