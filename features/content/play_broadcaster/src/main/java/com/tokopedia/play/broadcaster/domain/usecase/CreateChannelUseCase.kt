package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.domain.model.CreateChannelBroadcastResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 05/06/20.
 */
class CreateChannelUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<CreateChannelBroadcastResponse.GetChannelId>() {

    private val query = """
           mutation createChannel(${'$'}title: String, ${'$'}authorId: String!, ${'$'}authorType: Int!, ${'$'}status: Int!){
              broadcasterCreateChannel(req: {
                    title: ${'$'}title,
                    description: ${'$'}description,
                    authorID: ${'$'}authorId,
                    authorType: ${'$'}authorType, 
                    status: ${'$'}status
              }){
                channelID
              }
            }
        """

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): CreateChannelBroadcastResponse.GetChannelId {
        val gqlRequest = GraphqlRequest(query, CreateChannelBroadcastResponse.CreateChannelBroadcastData::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<CreateChannelBroadcastResponse.CreateChannelBroadcastData>(CreateChannelBroadcastResponse.CreateChannelBroadcastData::class.java)
        response?.data?.channelId?.let {
            return it
        }
        throw MessageErrorException("Terjadi kesalahan pada server") // TODO("replace with default error message")
    }

    companion object {

        private const val PARAMS_TITLE = "title"
        private const val PARAMS_AUTHOR_ID = "authorId"
        private const val PARAMS_AUTHOR_TYPE = "authorType"
        private const val PARAMS_STATUS = "status"

        private const val VALUE_SHOP_TYPE = 2
        private const val VALUE_STATUS = 0

        fun createParams(
                authorId: String,
                authorType: Int = VALUE_SHOP_TYPE,
                status: Int  = VALUE_STATUS // TODO("ask BE")
        ): Map<String, Any> = mapOf(
                PARAMS_TITLE to "", // empty by default
                PARAMS_AUTHOR_ID to authorId,
                PARAMS_AUTHOR_TYPE to authorType,
                PARAMS_STATUS to status
        )
    }

}