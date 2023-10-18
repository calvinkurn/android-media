package com.tokopedia.play.broadcaster.domain.usecase

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.domain.model.AddProductTagChannelResponse.GetProductId
import com.tokopedia.play.broadcaster.domain.model.addproduct.AddProductTagChannelRequest
import javax.inject.Inject

/**
 * Created by mzennis on 05/06/20.
 */
class AddProductTagUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers,
) : CoroutineUseCase<AddProductTagChannelRequest, GetProductId>(dispatcher.io) {

    override suspend fun execute(params: AddProductTagChannelRequest): GetProductId {
        return try {
            val param = generateParams(params)
            repository.request(graphqlQuery(), param)
        } catch (expected: Exception) {
            FirebaseCrashlytics.getInstance().recordException(expected)
            throw MessageErrorException(expected.message)
        }
    }

    override fun graphqlQuery(): String {
        return """
            mutation setProductTag(${'$'}channelId: String!, ${'$'}productIds: [String]!){
              broadcasterSetActiveProductTags(req: {
                channelID: ${'$'}channelId,
                productIDs: ${'$'}productIds,
              }){
                 productIDs
              }
            }
        """.trimIndent()
    }

    private fun generateParams(params: AddProductTagChannelRequest): Map<String, Any> {
        return mapOf(
            PARAMS_CHANNEL_ID to params.channelId,
            PARAMS_PRODUCT_ID to params.productIds,
        )
    }

    companion object {
        private const val PARAMS_CHANNEL_ID = "channelId"
        private const val PARAMS_PRODUCT_ID = "productIds"
    }

}
