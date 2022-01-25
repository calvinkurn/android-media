package com.tokopedia.play.broadcaster.domain.usecase.campaign

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import javax.inject.Inject

/**
 * Created by meyta.taliti on 25/01/22.
 */
@GqlQuery(GetProductsInCampaignUseCase.QUERY_NAME, GetProductsInCampaignUseCase.QUERY)
class GetProductsInCampaignUseCase @Inject constructor(
    gqlRepository: GraphqlRepository,
    private val dispatchers: CoroutineDispatchers,
) {

    companion object {

        private const val PARAM_USER_ID = "userId"
        private const val PARAM_CAMPAIGN_ID = "campaignId"

        const val QUERY_NAME = "GetCampaignProductUseCaseQuery"
        const val QUERY = """
            query GetCampaignProduct(${"$${PARAM_CAMPAIGN_ID}"}: Int, ${"$${PARAM_USER_ID}"}: Int!) {
              getCampaignProduct(params: {
                UserID: ${"$${PARAM_USER_ID}"},
                Source: "broadcaster",
                CampaignID: ${"$${PARAM_CAMPAIGN_ID}"}
              }) {
                Products {
                  ID
                  name
                  URL
                  URLApps
                  URLMobile
                  imageUrl
                  imageURL700
                  price
                  courierCount
                  condition
                  rating
                  starRating
                  countReview
                  countSold
                  SKU
                  stock
                  returnable
                  status
                  hasCashback
                  cashbackAmount
                  map_id
                  lockStatus
                  maxOrder
                  priceUnfmt
                  isVariant
                  parentId
                  eggCrackingValidation
                  min_order
                },
                TotalProduct
              }
            }
        """

        fun createParams(
            userId: String,
            campaignId: String
        ): Map<Any, Any> {
            return mapOf(
                PARAM_USER_ID to userId.toIntOrZero(),
                PARAM_CAMPAIGN_ID to campaignId.toIntOrZero()
            )
        }
    }
}