package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.ChannelEntity
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 03/08/20.
 */
class GetChannelDetailUseCase @Inject constructor(private val gqlUseCase: GraphqlRepository): UseCase<ChannelEntity>() {

    private val query = """
          query GetChannel(${'$'}channelId: String){
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
              pinned_product{
                id
                name
                description
                original_price
                original_price_fmt
                price
                price_fmt
                discount
                quantity
                has_variant
                is_available
                order
                app_link
                web_link
                min_quantity
                is_free_shipping
              }
              pinned_message {
                id
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
              }
              app_link
              web_link
            }
          }
        }
        """

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): ChannelEntity {
        val gqlRequest = GraphqlRequest(query, ChannelEntity.Response::class.java, params)
        val gqlResponse = gqlUseCase.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val error = gqlResponse.getError(ChannelEntity.Response::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(ChannelEntity.Response::class.java) as ChannelEntity.Response).data.channel
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