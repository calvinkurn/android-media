package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.detail.recom.ChannelDetailsWithRecomResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by jegul on 20/01/21
 */
class GetChannelDetailsWithRecomUseCase @Inject constructor(
        private val gqlUseCase: GraphqlRepository
): UseCase<ChannelDetailsWithRecomResponse>() {

    private val query = """
          query GetPlayChannelDetailWithRecom(${'$'}$PARAM_CHANNEL_ID: String, ${'$'}$PARAM_CURSOR: String, ${'$'}$PARAM_SOURCE_TYPE: String, ${'$'}$PARAM_SOURCE_ID: String){
              playGetChannelDetailsWithRecom(req: {
                origin_id: ${'$'}$PARAM_CHANNEL_ID,
                cursor: ${'$'}$PARAM_CURSOR,
                source_type: ${'$'}$PARAM_SOURCE_TYPE,
                source_id: ${'$'}$PARAM_SOURCE_ID
              }) {
                meta {
                  cursor
                }
                data {
                  id
                  title
                  is_live
                  partner {
                    id
                    type
                    name
                    thumbnail_url
                    badge_url
                    app_link
                    web_link
                  }
                  video {
                    id
                    orientation
                    type
                    stream_source
                    autoplay
                    buffer_control {
                      max_buffer_in_seconds
                      min_buffer_in_seconds
                      buffer_for_playback
                      buffer_for_playback_after_rebuffer
                    }
                  }
                  pinned_message{
                    id
                    title
                    redirect_url
                  }
                  quick_replies
                  configurations {
                    show_cart
                    show_pinned_product
                    ping_interval
                    max_chars
                    max_retries
                    min_reconnect_delay
                    has_promo
                    reminder{
                      is_set
                    }
                    channel_freeze_screen{
                      title
                      description
                      button_text
                      button_app_link
                    }
                    channel_banned_message{
                      title
                      message
                      button_text
                    }
                    chat_config{
                      chat_enabled
                      chat_disabled_message
                    }
                    feeds_like_params{
                      content_type
                      content_id
                      like_type
                    }
                    pinned_product_config{
                      pin_title
                      bottom_sheet_title
                    }
                    room_background{
                      image_url
                    }
                  }
                  app_link
                  web_link
                  share {
                    text
                    redirect_url
                    use_short_url
                    meta_title
                    meta_description
                    is_show_button
                  }
                }
              }
            }
        """

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): ChannelDetailsWithRecomResponse {
        val gqlRequest = GraphqlRequest(query, ChannelDetailsWithRecomResponse::class.java, params)
        val gqlResponse = gqlUseCase.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val error = gqlResponse.getError(ChannelDetailsWithRecomResponse::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(ChannelDetailsWithRecomResponse::class.java) as ChannelDetailsWithRecomResponse)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    sealed class ChannelDetailNextKey {

        data class ChannelId(val channelId: String, val sourceType: SourceType) : ChannelDetailNextKey()
        data class Cursor(val cursor: String) : ChannelDetailNextKey()
    }

    sealed class SourceType {

        abstract val key: String

        data class Shop(val sourceId: String) : SourceType() {
            override val key: String = SOURCE_TYPE_SHOP
        }
        object Home : SourceType() {
            override val key: String = SOURCE_TYPE_HOME
        }
        object Feeds : SourceType() {
            override val key: String = SOURCE_TYPE_FEEDS
        }
        object DiscoveryPage : SourceType() {
            override val key: String = SOURCE_TYPE_DISCOVERY_PAGE
        }
        object CLP : SourceType() {
            override val key: String = SOURCE_TYPE_CLP
        }
        object PlayHome : SourceType() {
            override val key: String = SOURCE_TYPE_PLAY_HOME
        }
        object Unknown : SourceType() {
            override val key: String = ""
        }

        companion object {

            private const val SOURCE_TYPE_SHOP = "SHOP"
            private const val SOURCE_TYPE_HOME = "HOME"
            private const val SOURCE_TYPE_FEEDS = "FEEDS"
            private const val SOURCE_TYPE_DISCOVERY_PAGE = "DISCOPAGE"
            private const val SOURCE_TYPE_CLP = "CLP"
            private const val SOURCE_TYPE_PLAY_HOME = "PLAY_HOME"

            fun getBySource(sourceType: String, sourceId: String? = null): SourceType {
                return when (sourceType) {
                    SOURCE_TYPE_SHOP -> {
                        Shop(sourceId.orEmpty())
                    }
                    SOURCE_TYPE_HOME -> Home
                    SOURCE_TYPE_FEEDS -> Feeds
                    SOURCE_TYPE_DISCOVERY_PAGE -> DiscoveryPage
                    SOURCE_TYPE_CLP -> CLP
                    SOURCE_TYPE_PLAY_HOME -> PlayHome
                    else -> Unknown
                }
            }
        }
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelId"
        private const val PARAM_SOURCE_TYPE = "sourceType"
        private const val PARAM_SOURCE_ID = "sourceId"
        private const val PARAM_CURSOR = "cursor"

        fun createParams(nextKey: ChannelDetailNextKey): Map<String, Any> {
            return when (nextKey) {
                is ChannelDetailNextKey.ChannelId -> createParamsWithChannelId(
                        nextKey.channelId,
                        nextKey.sourceType
                )
                is ChannelDetailNextKey.Cursor -> createParamsWithCursor(nextKey.cursor)
            }
        }

        private fun createParamsWithChannelId(channelId: String, sourceType: SourceType): Map<String, Any> = mutableMapOf(
                PARAM_CHANNEL_ID to channelId,
                PARAM_SOURCE_TYPE to sourceType.key
        ).apply {
            if (sourceType is SourceType.Shop) put(PARAM_SOURCE_ID, sourceType.sourceId)
        }

        private fun createParamsWithCursor(cursor: String): Map<String, Any> = mapOf(
                PARAM_CURSOR to cursor
        )
    }
}