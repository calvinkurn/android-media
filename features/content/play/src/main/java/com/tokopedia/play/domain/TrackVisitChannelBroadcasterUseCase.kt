package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.VisitChannelTracking
import javax.inject.Inject


/**
 * Created by mzennis on 05/02/21.
 */
class TrackVisitChannelBroadcasterUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<Boolean>(graphqlRepository) {

    var params: Map<String, Any> = emptyMap()

    private val query = """
        mutation trackVisitChannelBroadcaster(${'$'}channelId: String!) {
          broadcasterReportVisitChannel(channelID: ${'$'}channelId) {
            success
          }
        }
    """.trimIndent()

    override suspend fun executeOnBackground(): Boolean {
        val gqlRequest = GraphqlRequest(query, VisitChannelTracking.Response::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(
                listOf(gqlRequest),
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        )

        val errors = gqlResponse.getError(VisitChannelTracking.Response::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors.first().message)
        }
        val response = gqlResponse.getData<VisitChannelTracking.Response>(VisitChannelTracking.Response::class.java)
        if (response?.reportVisitChannelTracking != null) {
            return response.reportVisitChannelTracking.success
        } else {
            throw MessageErrorException("Ada sedikit kendala pada sistem.")
        }
    }

    companion object {

        private const val PARAMS_CHANNEL_ID = "channelId"

        fun createParams(
                channelId: String,
        ): Map<String, Any> = mapOf(
                PARAMS_CHANNEL_ID to channelId,
        )
    }
}