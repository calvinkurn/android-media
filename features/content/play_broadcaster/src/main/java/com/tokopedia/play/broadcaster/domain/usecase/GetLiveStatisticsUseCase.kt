package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.domain.model.GetLiveStatisticsResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by jessica on 05/06/20
 */

class GetLiveStatisticsUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<GetLiveStatisticsResponse>() {

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

    override suspend fun executeOnBackground(): GetLiveStatisticsResponse {
        val gqlRequest = GraphqlRequest(query, GetLiveStatisticsResponse::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(GetLiveStatisticsResponse::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors[0].message)
        } else {
            return gqlResponse.getData(GetLiveStatisticsResponse::class.java)
        }
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelId"

        fun createParams(channelId: String): Map<String, Any> = mapOf(PARAM_CHANNEL_ID to channelId)
    }
}
