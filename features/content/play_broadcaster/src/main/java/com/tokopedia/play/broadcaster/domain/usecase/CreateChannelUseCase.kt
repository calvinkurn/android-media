package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.ChannelId
import com.tokopedia.play.broadcaster.domain.model.CreateChannelBroadcastResponse
import com.tokopedia.play.broadcaster.ui.model.PlayChannelStatus
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import javax.inject.Inject


/**
 * Created by mzennis on 05/06/20.
 */
class CreateChannelUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : BaseUseCase<ChannelId>() {

    private val query = """
           mutation createChannel(${'$'}authorId: String!, ${'$'}authorType: Int!, ${'$'}status: Int!){
              broadcasterCreateChannel(req: {
                    authorID: ${'$'}authorId,
                    authorType: ${'$'}authorType, 
                    status: ${'$'}status
              }){
                channelID
              }
            }
        """

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): ChannelId {
        val gqlResponse = configureGqlResponse(graphqlRepository, query, CreateChannelBroadcastResponse::class.java, params, GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<CreateChannelBroadcastResponse>(CreateChannelBroadcastResponse::class.java)
        if (response?.getChannelId != null) {
            return response.getChannelId
        }
        throw DefaultErrorThrowable()
    }

    companion object {

        private const val PARAMS_AUTHOR_ID = "authorId"
        private const val PARAMS_AUTHOR_TYPE = "authorType"
        private const val PARAMS_STATUS = "status"

        private const val VALUE_SHOP_TYPE = 2 // shop type

        fun createParams(
                authorId: String,
                authorType: Int = VALUE_SHOP_TYPE,
                status: PlayChannelStatus  = PlayChannelStatus.Draft
        ): Map<String, Any> = mapOf(
                PARAMS_AUTHOR_ID to authorId,
                PARAMS_AUTHOR_TYPE to authorType,
                PARAMS_STATUS to status.value.toInt()
        )
    }

}