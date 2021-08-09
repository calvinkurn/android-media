package com.tokopedia.play_common.domain.usecase.interactive

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play_common.domain.model.interactive.GetCurrentInteractiveResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 07/07/21
 */
class GetCurrentInteractiveUseCase @Inject constructor(
        gqlRepository: GraphqlRepository,
        private val dispatchers: CoroutineDispatchers,
): RetryableGraphqlUseCase<GetCurrentInteractiveResponse>(gqlRepository) {

    private val query = """
        query GetCurrentInteractive(${"$$PARAM_CHANNEL_ID"}: String!) {
          playInteractiveGetCurrentInteractive(req:{
            channelID: ${"$$PARAM_CHANNEL_ID"}
          }){
            interactive {
              interactive_id: interactiveID
              interactive_type: interactiveType
              title
              status
              countdown_start: countdownStart
              countdown_end: countdownEnd
            }
          }
        }
    """.trimIndent()

    init {
        setGraphqlQuery(query)
        setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetCurrentInteractiveResponse::class.java)
    }

    override suspend fun executeOnBackground(): GetCurrentInteractiveResponse = withContext(dispatchers.io) {
        return@withContext super.executeOnBackground()
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelID"

        fun createParams(channelId: String): Map<String, Any> = mapOf(
                PARAM_CHANNEL_ID to channelId
        )
    }
}