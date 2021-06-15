package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.GetLiveStatisticsResponse
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import com.tokopedia.play.broadcaster.util.handler.DefaultUseCaseHandler
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by jessica on 05/06/20
 */

class GetLiveStatisticsUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<GetLiveStatisticsResponse.ReportChannelSummary>() {

    private val query = """
        query liveReport(${'$'}channelId: String!){
          broadcasterReportLiveSummaries(channelID: ${'$'}channelId, useDashboardFormat: true) {
            channel {
              channelID
              metrics {
                add_to_cart: addToCart
                add_to_cart_fmt: addToCartFmt
                payment_verified: paymentVerified
                payment_verified_fmt: paymentVerifiedFmt
                follow_shop: followShop
                follow_shop_fmt: followShopFmt
                like_channel: likeChannel
                like_channel_fmt: likeChannelFmt
                visit_shop: visitShop
                visit_shop_fmt: visitShopFmt
                visit_pdp: visitPDP
                visit_pdp_fmt: visitPDPFmt
                visit_channel: visitChannel
                visit_channel_fmt: visitChannelFmt
              }
            }
            duration
          }
        }
        """

    var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): GetLiveStatisticsResponse.ReportChannelSummary {
        val gqlResponse = DefaultUseCaseHandler(
                gqlRepository = graphqlRepository,
                query = query,
                typeOfT = GetLiveStatisticsResponse::class.java,
                params = params,
                gqlCacheStrategy = GraphqlCacheStrategy
                        .Builder(CacheType.ALWAYS_CLOUD).build()
        ).executeWithRetry()
        val response = gqlResponse.getData<GetLiveStatisticsResponse>(GetLiveStatisticsResponse::class.java)
        return try { response.reportChannelSummary
        } catch (e: Exception) { throw DefaultErrorThrowable() }
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelId"

        fun createParams(channelId: String): Map<String, Any> = mapOf(PARAM_CHANNEL_ID to channelId)
    }
}
