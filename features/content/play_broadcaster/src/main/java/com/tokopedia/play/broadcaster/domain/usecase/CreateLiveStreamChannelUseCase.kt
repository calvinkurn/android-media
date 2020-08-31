package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.CreateLiveStreamChannelResponse
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import javax.inject.Inject


/**
 * Created by mzennis on 05/06/20.
 */
class CreateLiveStreamChannelUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : BaseUseCase<CreateLiveStreamChannelResponse.GetMedia>() {

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
        val gqlResponse = configureGqlResponse(graphqlRepository, query, CreateLiveStreamChannelResponse::class.java, params, GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<CreateLiveStreamChannelResponse>(CreateLiveStreamChannelResponse::class.java)
        response?.media?.let {
            return it
        }
        throw DefaultErrorThrowable()
    }

    companion object {

        private const val PARAMS_CHANNEL_ID = "channelId"
        private const val PARAMS_TITLE = "title"
        private const val PARAMS_THUMBNAIL = "thumbnail"

        fun createParams(
                channelId: String,
                title: String, // TODO("After MVP, confirm to BE that this should not be required")
                thumbnail: String // TODO("After MVP, confirm to BE that this should not be required")
        ): Map<String, Any> = mapOf(
                PARAMS_CHANNEL_ID to channelId,
                PARAMS_TITLE to title,
                PARAMS_THUMBNAIL to thumbnail
        )
    }

}