package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.GetLiveStatisticsResponse
import com.tokopedia.play.broadcaster.domain.model.LiveStats
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import javax.inject.Inject

/**
 * @author by jessica on 05/06/20
 */

class GetLiveStatisticsUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : BaseUseCase<LiveStats>() {

    private val query = """
        query liveReport(${'$'}channelId: String!){
            broadcasterReportLiveSummaries(channelID: ${'$'}channelId){
                channel{
                  channel_id: channelID
                  metrics{
                    add_to_cart: addToCart
                    remove_from_cart: removeFromCart
                    wish_list: wishList
                    remove_wish_list: removeWishList
                    payment_verified: paymentVerified
                    follow_shop: followShop
                    unfollow_shop: unFollowShop
                    like_channel: likeChannel
                    unlike_channel: unLike
                    visit_shop: visitShop
                    visit_pdp: visitPDP
                    visit_channel: visitChannel
                  }
                }
              }
            }
        """

    var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): LiveStats {
        val gqlResponse = configureGqlResponse(graphqlRepository, query, GetLiveStatisticsResponse::class.java, params, GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<GetLiveStatisticsResponse>(GetLiveStatisticsResponse::class.java)
        return try { response.response.channel.metrics
        } catch (e: Exception) { throw DefaultErrorThrowable() }
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelId"

        fun createParams(channelId: String): Map<String, Any> = mapOf(PARAM_CHANNEL_ID to channelId)
    }
}
