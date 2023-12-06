package com.tokopedia.feedplus.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedplus.data.FeedGetChannelStatusEntity
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 04/12/23
 *
 * duplicate from : com.tokopedia.play.domain.GetChannelStatusUseCase
 * need to revisit after "Redifine Common Modules in Content"
 */
class FeedGetChannelStatusUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<List<String>, FeedGetChannelStatusEntity>(dispatcher.io) {

    override fun graphqlQuery(): String = """
            query GetChannelStatus(${'$'}channelIds: [String]){
              playGetChannelsStatus(req: {
                ids: ${'$'}channelIds
              }) {
                data {
                  id
                  status
                }
                waiting_duration
              }
            }
        """

    override suspend fun execute(params: List<String>): FeedGetChannelStatusEntity =
        graphqlRepository.request<Map<String, Any>, FeedGetChannelStatusEntity>(
            graphqlQuery(),
            mapOf(PARAM_CHANNEL_IDS to params)
        )

    companion object {
        private const val PARAM_CHANNEL_IDS = "channelIds"
    }

}
