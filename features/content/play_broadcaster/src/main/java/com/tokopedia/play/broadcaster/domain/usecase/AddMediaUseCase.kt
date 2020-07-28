package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.domain.model.AddMediaChannelResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 05/06/20.
 */
class AddMediaUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<AddMediaChannelResponse.GetMediaId>() {

    private val query = """
            mutation addMedias(${'$'}channelId: String!, ${'$'}coverUrl: String, ${'$'}source: String!, ${'$'}type: Int!) {
              broadcasterAddMedias(req: [{
                channelID: ${'$'}channelId,
                source: ${'$'}source,
                coverURL: ${'$'}coverUrl,
                type: ${'$'}type,
              }]){
                    mediaIDs
              }
            }
        """

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): AddMediaChannelResponse.GetMediaId {
        val gqlRequest = GraphqlRequest(query, AddMediaChannelResponse.AddMediaChannelData::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<AddMediaChannelResponse.AddMediaChannelData>(AddMediaChannelResponse.AddMediaChannelData::class.java)
        response?.data?.mediaId?.let {
            return it
        }
        throw MessageErrorException("Terjadi kesalahan pada server") // TODO("replace with default error message")
    }

    companion object {

        private const val PARAMS_CHANNEL_ID = "channelId"
        private const val PARAMS_COVER_URL = "coverUrl"
        private const val PARAMS_SOURCE = "source"
        private const val PARAMS_TYPE = "type"

        private const val VALUE_TYPE = 1

        fun createParams(
                channelId: String,
                coverUrl: String,
                source: String = "", // TODO("ask BE")
                type: Int  = VALUE_TYPE // TODO("ask BE")
        ): Map<String, Any> = mapOf(
                PARAMS_CHANNEL_ID to channelId,
                PARAMS_COVER_URL to coverUrl,
                PARAMS_SOURCE to source,
                PARAMS_TYPE to type
        )
    }

}