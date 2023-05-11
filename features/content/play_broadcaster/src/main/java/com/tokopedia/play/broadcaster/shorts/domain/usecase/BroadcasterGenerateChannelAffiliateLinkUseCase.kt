package com.tokopedia.play.broadcaster.shorts.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.play.broadcaster.shorts.domain.model.GenerateChannelAffiliateLinkResponseModel
import javax.inject.Inject

class BroadcasterGenerateChannelAffiliateLinkUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers,
) : CoroutineUseCase<String, GenerateChannelAffiliateLinkResponseModel>(dispatcher.io) {

    override suspend fun execute(params: String): GenerateChannelAffiliateLinkResponseModel {
        return repository.request(graphqlQuery(), generateParams(params))
    }

    private fun generateParams(params: String): Map<String, Any> {
        return mapOf(PARAMS_CHANNEL_ID to params)
    }

    override fun graphqlQuery(): String {
        return """
            {
              broadcasterGenerateChannelAffiliateLink (
                  $PARAMS_CHANNEL_ID: ${"$$PARAMS_CHANNEL_ID"}
                ) {
                  success
                }
            }
        """.trimIndent()
    }

    companion object {
        private const val PARAMS_CHANNEL_ID = "channelID"
    }

}
