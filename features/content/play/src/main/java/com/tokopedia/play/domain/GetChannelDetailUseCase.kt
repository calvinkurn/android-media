package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.Channel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by mzennis on 03/08/20.
 */
class GetChannelDetailUseCase @Inject constructor(private val gqlUseCase: GraphqlRepository): UseCase<Channel>() {

    private val query = """
          query GetPlayChannelDetail(${'$'}channelId: String){
              playGetChannelDetails(req: { id: ${'$'}channelId}) {
               data {
                  id
                  title
                  description
                  start_time
                  end_time
                  is_live
                  partner {
                    id
                    type
                    name
                    thumbnail_url
                    badge_url
                  }
                  video{
                    id
                    orientation
                    type
                    cover_url
                    stream_source
                    autoplay
                    buffer_control{
                      max_buffer_in_seconds
                      min_buffer_in_seconds
                      buffer_for_playback
                      buffer_for_playback_after_rebuffer
                    }
                  }
                  pinned_message {
                    id
                    title
                    message
                    redirect_url
                  }
                  quick_replies
                  configurations {
                    ping_interval
                    max_chars
                    max_retries
                    min_reconnect_delay
                    show_cart
                    show_pinned_product
                    published
                    active
                    freezed
                    has_promo
                    feeds_like_params{
                      content_type
                      content_id
                      like_type
                    }
                    channel_freeze_screen {
                      category
                      title
                      description
                      button_text
                      button_app_link
                    }
                    channel_exit_message {
                      title
                      message
                    }
                    channel_banned_message {
                      title
                      message
                      button_text
                    }
                    pinned_product_config {
                      pin_title
                      bottom_sheet_title
                    }  
                    room_background {
                      image_url
                    }
                  }
                  stats {
                    view {
                      value
                      formatted
                    }
                  }
                  app_link
                  web_link
                }
              }
            }
        """

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): Channel {
        val gqlRequest = GraphqlRequest(query, Channel.Response::class.java, params)
        val gqlResponse = gqlUseCase.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val error = gqlResponse.getError(Channel.Response::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(Channel.Response::class.java) as Channel.Response).data.channel
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelId"

        fun createParams(channelId: String): Map<String, Any> = mapOf(
                PARAM_CHANNEL_ID to channelId
        )
    }
}