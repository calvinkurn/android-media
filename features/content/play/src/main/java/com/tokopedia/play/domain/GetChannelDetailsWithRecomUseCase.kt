package com.tokopedia.play.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.detail.recom.ChannelDetailsWithRecomResponse
import com.tokopedia.play.view.type.PlaySource
import javax.inject.Inject

/**
 * Created by jegul on 20/01/21
 */
/*
multiple_like{
  icon
  background_color
}
 */
@GqlQuery(GetChannelDetailsWithRecomUseCase.QUERY_NAME, GetChannelDetailsWithRecomUseCase.QUERY)
class GetChannelDetailsWithRecomUseCase @Inject constructor(
        gqlRepository: GraphqlRepository
): GraphqlUseCase<ChannelDetailsWithRecomResponse>(gqlRepository) {

    init {
        setGraphqlQuery(GetChannelDetailsWithRecomUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ChannelDetailsWithRecomResponse::class.java)
    }

    sealed class ChannelDetailNextKey {

        data class ChannelId(val channelId: String, val source: PlaySource) : ChannelDetailNextKey()
        data class Cursor(val cursor: String) : ChannelDetailNextKey()
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelId"
        private const val PARAM_SOURCE_TYPE = "sourceType"
        private const val PARAM_SOURCE_ID = "sourceId"
        private const val PARAM_CURSOR = "cursor"
        const val QUERY_NAME = "GetChannelDetailsWithRecomUseCaseQuery"
        const val QUERY = """
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
                  description
                  cover_url
                  start_time
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
                    welcome_format{
                        type
                        copy
                        background_color
                        icon
                    }
                    real_time_notif {
                        lifespan
                    }
                    show_cart
                    show_pinned_product
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
                    room_background{
                      image_url
                    }
                    multiple_like{
                      icon
                      background_color
                    }
                    has_follow_button
                    status
                    empty_bottom_sheet {
                        copy_text_header
                        copy_text_body
                        copy_text_redirect_button
                        image_url
                    }
                    explore_widget {
                      group
                      source_id
                      source_type
                    }
                    explore_widget_dominant {
                      group
                      source_id
                      source_type
                      has_dominant_category
                      category_name
                      category_id
                      category_level
                    }
                    channel_archived_screen {
                      title
                      description
                      button_text
                      button_app_link
                    }
                    pop_up {
                      is_enabled
                      duration_to_pop_up
                      copy_text_bottomsheet
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
                  air_time
                  performanceSummaryPageLink
                }
              }
            }
        """

        fun createParams(nextKey: ChannelDetailNextKey): Map<String, Any> {
            return when (nextKey) {
                is ChannelDetailNextKey.ChannelId -> createParamsWithChannelId(
                        nextKey.channelId,
                        nextKey.source
                )
                is ChannelDetailNextKey.Cursor -> createParamsWithCursor(nextKey.cursor)
            }
        }

        private fun createParamsWithChannelId(channelId: String, playSource: PlaySource): Map<String, Any> = mutableMapOf(
                PARAM_CHANNEL_ID to channelId,
                PARAM_SOURCE_TYPE to playSource.type
        ).apply {
            if (playSource.id.isNotBlank()) put(PARAM_SOURCE_ID, playSource.id)
        }

        private fun createParamsWithCursor(cursor: String): Map<String, Any> = mapOf(
                PARAM_CURSOR to cursor
        )
    }
}
