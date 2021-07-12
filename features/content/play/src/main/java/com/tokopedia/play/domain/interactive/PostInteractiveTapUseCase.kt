package com.tokopedia.play.domain.interactive

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.interactive.PostInteractiveTapResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import javax.inject.Inject

/**
 * Created by jegul on 30/06/21
 */
class PostInteractiveTapUseCase @Inject constructor(
        gqlRepository: GraphqlRepository
): RetryableGraphqlUseCase<PostInteractiveTapResponse>(gqlRepository, 3) {

    private val query = """
        mutation PostInteractiveTap(${"$$PARAM_CHANNEL_ID"}: String!, ${"$$PARAM_INTERACTIVE_ID"}: String!){
          playInteractiveUserTapSession(input: {
            channelID: ${"$$PARAM_CHANNEL_ID"},
            interactiveID: ${"$$PARAM_INTERACTIVE_ID"}
          }){
            header{
              status
              message
            }
          }
        }
    """.trimIndent()

    init {
        setGraphqlQuery(query)
        setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(PostInteractiveTapResponse::class.java)
    }

    override fun isResponseSuccess(response: PostInteractiveTapResponse): Boolean {
        return response.data.header.status == 200
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelID"
        private const val PARAM_INTERACTIVE_ID = "interactiveID"

        fun createParams(channelId: String, interactiveId: String): Map<String, Any> = mapOf(
                PARAM_CHANNEL_ID to channelId,
                PARAM_INTERACTIVE_ID to interactiveId
        )
    }
}