package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.domain.model.CreateLiveStreamChannelResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 05/06/20.
 */
class CreateLiveStreamChannelUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<CreateLiveStreamChannelResponse.GetMedia>() {

    private val query = """
            mutation createLivestream(${'$'}channelId: String!, ${'$'}title: String!, ${'$'}thumbnail: String!) {
              broadcasterCreateLivestream(req: {
                channelID: ${'$'}channelId,
                title: ${'$'}title,
                thumbnail: ${'$'}thumbnail
              }){
                ingestURL
                livestreamID
                mediaID
                streamURL
              }
            }
        """

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): CreateLiveStreamChannelResponse.GetMedia {
        val gqlRequest = GraphqlRequest(query, CreateLiveStreamChannelResponse.CreateLiveStreamChannelData::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<CreateLiveStreamChannelResponse.CreateLiveStreamChannelData>(CreateLiveStreamChannelResponse.CreateLiveStreamChannelData::class.java)
        response?.data?.media?.let {
            return it
        }
        throw MessageErrorException("Terjadi kesalahan pada server") // TODO("replace with default error message")
    }

    companion object {

        private const val PARAMS_CHANNEL_ID = "channelId"
        private const val PARAMS_TITLE = "title"
        private const val PARAMS_THUMBNAIL = "thumbnail"

        fun createParams(
                channelId: String,
                title: String = "", // TODO("ask BE")
                thumbnail: String = "" // TODO("ask BE")
        ): Map<String, Any> = mapOf(
                PARAMS_CHANNEL_ID to channelId,
                PARAMS_TITLE to title,
                PARAMS_THUMBNAIL to thumbnail
        )
    }

}