package com.tokopedia.content.common.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.BroadcasterReportTrackViewerResponse
import com.tokopedia.content.common.types.TrackContentType
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by mzennis on 07/01/21.
 */
@GqlQuery(BroadcasterReportTrackViewerUseCase.QUERY_NAME, BroadcasterReportTrackViewerUseCase.QUERY)
class BroadcasterReportTrackViewerUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val dispatcher: CoroutineDispatchers
) : GraphqlUseCase<Boolean>(graphqlRepository) {

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): Boolean {
        var count = 0

        while (true) {
            try {
                val response = getResponse()
                if (response) {
                    return response
                } else {
                    error("Error Report Track Viewer")
                }
            } catch (e: Throwable) {
                if (++count > RETRY_COUNT) error("Error Report Track Viewer ${RETRY_COUNT}x")
            }
        }
    }

    private suspend fun getResponse(): Boolean = withContext(dispatcher.io) {
        val gqlRequest = GraphqlRequest(BroadcasterReportTrackViewerUseCaseQuery(), BroadcasterReportTrackViewerResponse.Response::class.java, params)
        val gqlResponse = graphqlRepository.response(
            listOf(gqlRequest),
            GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        )

        val errors = gqlResponse.getError(BroadcasterReportTrackViewerResponse.Response::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors.first().message)
        }
        val response = gqlResponse.getData<BroadcasterReportTrackViewerResponse.Response>(
            BroadcasterReportTrackViewerResponse.Response::class.java
        )
        if (response?.broadcasterReportTrackViewer != null) {
            return@withContext response.broadcasterReportTrackViewer.success
        } else {
            throw MessageErrorException("Ada sedikit kendala pada sistem.")
        }
    }

    companion object {

        private const val RETRY_COUNT = 3

        private const val PARAMS_CHANNEL_ID = "channelID"
        private const val PARAMS_PRODUCT_ID = "productIDs"
        private const val PARAMS_EVENT = "event"
        private const val PARAMS_SOURCE = "source"

        const val QUERY_NAME = "BroadcasterReportTrackViewerUseCaseQuery"
        const val QUERY = """
            mutation broadcasterReportTrackViewer(${'$'}channelID: String!, ${'$'}productIDs: [String], ${'$'}event: String, ${'$'}source: broadcasterReportTrackViewerSource){
              broadcasterReportTrackViewer(
                channelID: ${'$'}channelID,
                productIDs: ${'$'}productIDs,
                event: ${'$'}event,
                source: ${'$'}source) {
                    success
              }
            }
        """

        fun createParams(
            channelId: String,
            productIds: List<String>,
            event: Event,
            type: TrackContentType,
        ): Map<String, Any> = mapOf(
            PARAMS_CHANNEL_ID to channelId,
            PARAMS_PRODUCT_ID to productIds,
            PARAMS_EVENT to event.value,
            PARAMS_SOURCE to mapOf(
                "ID" to channelId,
                "type" to type.value
            )
        )

        enum class Event(val value: String) {
            Visit("visit"),
            ProductChanges("product_change"),
            Unknown("");
        }
        val Event.isVisit : Boolean get() = this == Event.Visit

    }
}
