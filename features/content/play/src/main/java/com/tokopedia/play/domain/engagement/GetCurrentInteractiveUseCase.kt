package com.tokopedia.play.domain.engagement

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.engagement.GetCurrentInteractiveResponse
import com.tokopedia.play.domain.GetChannelStatusUseCase
import javax.inject.Inject

/**
 * Created by jegul on 28/06/21
 */
class GetCurrentInteractiveUseCase @Inject constructor(
        gqlRepository: GraphqlRepository
): GraphqlUseCase<GetCurrentInteractiveResponse>(gqlRepository) {

    private val query = """
        query GetCurrentInteractive(${"$$PARAM_CHANNEL_ID"}: String!) {
          playInteractiveGetCurrentInteractive(req:{
            channelID: ${"$$PARAM_CHANNEL_ID"}
          }){
            interactive {
              interactiveID
              interactiveType
              title
              status
              countdownStart
              countdownEnd
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

    companion object {
        private const val PARAM_CHANNEL_ID = "channelID"

        fun createParams(channelId: String): Map<String, Any> = mapOf(
                PARAM_CHANNEL_ID to channelId
        )
    }
}