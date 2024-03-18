package com.tokopedia.content.common.track.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.track.response.ReportSummaries
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created by jegul on 28/01/21
 */
class GetReportSummariesUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetReportSummariesUseCase.Param, ReportSummaries.Response>(dispatchers.io) {


    private val query = GetReportSummariesUseCaseQuery()

    @GqlQuery(QUERY_NAME, QUERY)
    override fun graphqlQuery(): String = query.getQuery()

    override suspend fun execute(params: Param): ReportSummaries.Response {
        return graphqlRepository.request(query, params)
    }

    data class Param(
        @SerializedName("contentIDs")
        val contentId: String,

        @SerializedName("contentType")
        val contentType: String,
    ) : GqlParam

    companion object {

        const val QUERY_NAME = "GetReportSummariesUseCaseQuery"
        const val QUERY = """
             query GetReportSummaries(${'$'}contentIDs: [String]!, ${'$'}contentType: String!) {
              broadcasterReportSummariesBulkV2(contentIDs: ${'$'}contentIDs, contentType: ${'$'}contentType) {
                reportData {
                  content {
                    contentType
                    contentID
                    metrics {
                      id
                      addToCart
                      removeFromCart
                      wishList
                      removeWishList
                      paymentVerified
                      followShop
                      unFollowShop
                      likeContent
                      visitShop
                      visitPDP
                      unLike
                      visitContent
                      totalLike
                      productSoldQty
                      uniqueUsers
                      maxConcurrentUsers
                      liveConcurrentUsers
                      estimatedIncome
                      completedProductSoldQty
                      addToCartFmt
                      removeFromCartFmt
                      wishListFmt
                      removeWishListFmt
                      paymentVerifiedFmt
                      followShopFmt
                      unFollowShopFmt
                      likeContentFmt
                      visitShopFmt
                      visitPDPFmt
                      unLikeFmt
                      visitContentFmt
                      totalLikeFmt
                      productSoldQtyFmt
                      uniqueUsersFmt
                      maxConcurrentUsersFmt
                      liveConcurrentUsersFmt
                      estimatedIncomeFmt
                      completedProductSoldQtyFmt
                    }
                    type
                    userMetrics {
                      id
                      addToCart
                      removeFromCart
                      wishList
                      removeWishList
                      paymentVerified
                      followShop
                      unFollowShop
                      likeContent
                      visitShop
                      visitPDP
                      unLike
                      visitContent
                      totalLike
                      productSoldQty
                      uniqueUsers
                      maxConcurrentUsers
                      liveConcurrentUsers
                      estimatedIncome
                      completedProductSoldQty
                      addToCartFmt
                      removeFromCartFmt
                      wishListFmt
                      removeWishListFmt
                      paymentVerifiedFmt
                      followShopFmt
                      unFollowShopFmt
                      likeContentFmt
                      visitShopFmt
                      visitPDPFmt
                      unLikeFmt
                      visitContentFmt
                      totalLikeFmt
                      productSoldQtyFmt
                      uniqueUsersFmt
                      maxConcurrentUsersFmt
                      liveConcurrentUsersFmt
                      estimatedIncomeFmt
                      completedProductSoldQtyFmt
                    }
                    showCompletedProductSoldQty      
                  }
                  products {
                    contentType
                    contentID
                    productID
                    metrics{
                      addToCart
                      removeFromCart
                      wishList
                      removeWishList
                      paymentVerified
                      visitPDP
                      productSoldQty
                      estimatedIncome
                      completedProductSoldQty
                      addToCartFmt
                      removeFromCartFmt
                      wishListFmt
                      removeWishListFmt
                      paymentVerifiedFmt
                      visitPDPFmt
                      productSoldQtyFmt
                      estimatedIncomeFmt
                      completedProductSoldQtyFmt
                    }
                  }
                  duration      
                }
              }
            }
        """
    }
}
