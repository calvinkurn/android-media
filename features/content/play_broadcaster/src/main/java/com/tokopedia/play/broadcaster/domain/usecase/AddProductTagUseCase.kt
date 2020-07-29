package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.domain.model.AddProductTagChannelResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 05/06/20.
 */
class AddProductTagUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<AddProductTagChannelResponse.GetProductId>() {

    private val query = """
            mutation setProductTag(${'$'}channelId: String!, ${'$'}productIds: [String]!){
              broadcasterSetActiveProductTags(req: {
                channelID: ${'$'}channelId,
                productIDs: ${'$'}productIds,
              }){
                    productIDs
              }
            }
        """

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): AddProductTagChannelResponse.GetProductId {
        val gqlRequest = GraphqlRequest(query, AddProductTagChannelResponse.AddProductTagChannelData::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<AddProductTagChannelResponse.AddProductTagChannelData>(AddProductTagChannelResponse.AddProductTagChannelData::class.java)
        response?.data?.productId?.let {
            return it
        }
        throw MessageErrorException("Terjadi kesalahan pada server") // TODO("replace with default error message")
    }

    companion object {

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