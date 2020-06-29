package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.play.broadcaster.domain.model.ChannelId
import com.tokopedia.play.broadcaster.domain.model.UpdateChannelResponse
import com.tokopedia.play.broadcaster.ui.model.PlayChannelStatus
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 26/06/20.
 */
class UpdateChannelUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<ChannelId>() {

    private val query = """
            mutation UpdateChannel(${'$'}channelId: String!, ${'$'}authorId: String, ${'$'}status: Int!){
              broadcasterUpdateChannel(
                req : {
                  channelID: ${'$'}channelId,
                  fieldsToUpdate: ["status","authorID"],
                  authorID: ${'$'}authorId,
                  status: ${'$'}status
                }
              ) {
                channelID
              }
            }
        """

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): ChannelId {
        val gqlRequest = GraphqlRequest(query, UpdateChannelResponse::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<UpdateChannelResponse>(UpdateChannelResponse::class.java)
        response?.updateChannel?.let {
            return it
        }
        throw DefaultErrorThrowable()
    }

    companion object {

        private const val PARAMS_CHANNEL_ID = "channelId"
        private const val PARAMS_AUTHOR_ID = "authorId"
        private const val PARAMS_STATUS = "status"

        fun createParams(
                channelId: String,
                authorId: String,
                status: PlayChannelStatus
        ): Map<String, Any> = mapOf(
                PARAMS_CHANNEL_ID to channelId,
                PARAMS_AUTHOR_ID to authorId,
                PARAMS_STATUS to status.value.toInt()
        )
    }
}