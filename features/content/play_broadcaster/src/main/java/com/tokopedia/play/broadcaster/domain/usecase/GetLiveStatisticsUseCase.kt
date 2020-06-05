package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.data.model.BroadcasterReportLiveSummaries
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by jessica on 05/06/20
 */

class GetLiveStatisticsUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<BroadcasterReportLiveSummaries.Response>() {

    private val query = """
        query liveReport(${'$'}channelID: String!){
            broadcasterReportLiveSummaries(channelID: ${'$'}channelID){
                channel{
                  channelID
                  metrics{
                    addToCart
                    removeFromCart
                    wishList
                    removeWishList
                    paymentVerified
                    followShop
                    unFollowShop
                    likeChannel
                    unLike
                    visitShop
                    visitPDP
                    visitChannel
                  }
                }
              }
            }
        """

    var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): BroadcasterReportLiveSummaries.Response {
        val gqlRequest = GraphqlRequest(query, BroadcasterReportLiveSummaries.Response::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(BroadcasterReportLiveSummaries.Response::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors[0].message)
        } else {
            return gqlResponse.getData(BroadcasterReportLiveSummaries.Response::class.java)
        }
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelID"

        fun createParams(channelId: String): Map<String, Any> = mapOf(PARAM_CHANNEL_ID to channelId)
    }
}
