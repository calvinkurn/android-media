package com.tokopedia.play.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.ProductTracking
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * Created by mzennis on 07/01/21.
 */
class TrackProductTagBroadcasterUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers,
): GraphqlUseCase<Boolean>(graphqlRepository) {

    var params: Map<String, Any> = emptyMap()

    private val query = """
        mutation trackProductTagBroadcaster(${'$'}channelId: String!, ${'$'}productIds: [String]){
          broadcasterReportTrackViewer(
            channelID: ${'$'}channelId,
            productIDs: ${'$'}productIds) {
            success
          }
        }
    """.trimIndent()

    override suspend fun executeOnBackground(): Boolean {
        var count = 0

        while(true) {
            try {
                val response = getResponse()
                if (response) return response
                else error("Error Report Track Viewer")
            } catch (e: Throwable) {
                if (++count > RETRY_COUNT) error("Error Report Track Viewer ${RETRY_COUNT}x")
            }
        }
    }

    private suspend fun getResponse(): Boolean = withContext(dispatcher.io) {
        val gqlRequest = GraphqlRequest(query, ProductTracking.Response::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(
                listOf(gqlRequest),
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        )

        val errors = gqlResponse.getError(ProductTracking.Response::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors.first().message)
        }
        val response = gqlResponse.getData<ProductTracking.Response>(ProductTracking.Response::class.java)
        if (response?.productTracking != null) {
            return@withContext response.productTracking.success
        } else {
            throw MessageErrorException("Ada sedikit kendala pada sistem.")
        }
    }

    companion object {

        private const val RETRY_COUNT = 3

        private const val PARAMS_CHANNEL_ID = "channelId"
        private const val PARAMS_PRODUCT_ID = "productIds"

        fun createParams(
                channelId: String,
                productIds: List<String>
        ): Map<String, Any> = mapOf(
                PARAMS_CHANNEL_ID to channelId,
                PARAMS_PRODUCT_ID to productIds
        )
    }
}