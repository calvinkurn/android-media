package com.tokopedia.play.broadcaster.domain.usecase.interactive

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.interactive.PostInteractiveCreateSessionResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by mzennis on 06/07/21.
 */
class PostInteractiveCreateSessionUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : RetryableGraphqlUseCase<PostInteractiveCreateSessionResponse>(gqlRepository, HIGH_RETRY_COUNT) {

    private val query = """
        mutation PostInteractiveCreateSession(${"$$PARAM_SHOP_ID"}: String!, ${"$$PARAM_CHANNEL_ID"}: String!, ${"$$PARAM_TITLE"}: String!, ${"$$PARAM_TIMER"}: String!) {
          playInteractiveSellerCreateSession(input: {
            channelID: ${"$$PARAM_CHANNEL_ID"},
            title: ${"$$PARAM_TITLE"},
            timer: ${"$$PARAM_TIMER"},
            shopID: ${"$$PARAM_SHOP_ID"}
          }){
            header{
              status
              message
            }
            data{
              interactiveID
            }
          }
        }
    """

    init {
        setGraphqlQuery(query)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(PostInteractiveCreateSessionResponse::class.java)
    }

    suspend fun execute(
        shopId: String,
        channelId: String,
        title: String,
        durationInMs: Long
    ): PostInteractiveCreateSessionResponse {
        setRequestParams(
            createParams(
                shopId = shopId,
                channelId = channelId,
                title = title,
                timer = TimeUnit.MILLISECONDS.toSeconds(durationInMs).toString()
            )
        )
        return executeOnBackground()
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_CHANNEL_ID = "channelID"
        private const val PARAM_TITLE = "title"
        private const val PARAM_TIMER = "timer"

        fun createParams(shopId: String, channelId: String, title: String, timer: String): Map<String, Any> = mapOf(
            PARAM_SHOP_ID to shopId,
            PARAM_CHANNEL_ID to channelId,
            PARAM_TITLE to title,
            PARAM_TIMER to timer,
        )
    }
}