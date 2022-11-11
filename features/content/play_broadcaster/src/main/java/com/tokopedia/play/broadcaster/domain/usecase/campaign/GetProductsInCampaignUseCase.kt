package com.tokopedia.play.broadcaster.domain.usecase.campaign

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.broadcaster.domain.model.campaign.GetCampaignProductResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by meyta.taliti on 25/01/22.
 */
@GqlQuery(GetProductsInCampaignUseCase.QUERY_NAME, GetProductsInCampaignUseCase.QUERY)
class GetProductsInCampaignUseCase @Inject constructor(
    gqlRepository: GraphqlRepository,
    private val dispatchers: CoroutineDispatchers,
) : RetryableGraphqlUseCase<GetCampaignProductResponse>(gqlRepository) {

    init {
        setGraphqlQuery(GetCampaignProductUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetCampaignProductResponse::class.java)
    }

    override suspend fun executeOnBackground() = withContext(dispatchers.io) {
        super.executeOnBackground()
    }

    companion object {

        private const val PARAM_USER_ID = "UserID"
        private const val PARAM_CAMPAIGN_ID = "CampaignID"
        private const val PARAM_PAGE = "Page"
        private const val PARAM_PER_PAGE = "ResultPerPage"
        private const val PARAM_SOURCE = "Source"

        const val QUERY_NAME = "GetCampaignProductUseCaseQuery"
        const val QUERY = """
            query getFlashSaleProductList(
                ${"$${PARAM_CAMPAIGN_ID}"}: Int, 
                ${"$${PARAM_USER_ID}"}: Int!,
                ${"$${PARAM_PAGE}"}: Int,
                ${"$${PARAM_PER_PAGE}"}: Int
            ) {
              getFlashSaleProductList(params: {
                $PARAM_USER_ID: ${"$${PARAM_USER_ID}"},
                $PARAM_SOURCE: "broadcaster",
                $PARAM_CAMPAIGN_ID: ${"$${PARAM_CAMPAIGN_ID}"},
                $PARAM_PAGE: ${"$${PARAM_PAGE}"},
                $PARAM_PER_PAGE: ${"$${PARAM_PER_PAGE}"}
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
                  shop {
                    ShopID
                    Name
                    URL
                    URLMobile
                    URLApps
                    IsGold
                    IsOfficial
                  }
                  wholesale {
                    QuantityMin
                    QuantityMax
                    Price
                  }
                  courierCount
                  condition
                  departmentID
                  labels {
                    Title
                    Color
                  }
                  badges {
                    Title
                    ImageURL
                  }
                  rating
                  starRating
                  countReview
                  countSold
                  SKU
                  stock
                  campaign {
                    CampaignID
                    DiscountPercentage
                    DiscountedPrice
                    OriginalPrice
                    Cashback
                    CustomStock
                    StockSoldPercentage
                    CampaignStatus
                    ProductSystemStatus
                    ProductAdminStatus
                    StartDate
                    EndDate
                    CampaignTypeName
                    CampaignShortName
                    MaxOrder
                    OriginalMaxOrder
                    DiscountedPriceFmt
                    OriginalPriceFmt
                    OriginalStock
                    OriginalStockStatus
                    CampaignSoldCount
                    OriginalCustomStock
                    AppsOnly
                    Applinks
                    FinalPrice
                    SellerPrice
                    TokopediaSubsidy
                    BookingStock
                    IsBigCampaign
                    MinOrder
                    RedirectPageUrl
                    RedirectPageApplink
                    SpoilerPriceFmt
                    SpoilerPrice
                    HideGimmick
                  }
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
                  variantsFilter
                  childIds
                  siblingIds
                  eggCrackingValidation
                  min_order
                },
                TotalProduct
              }
            }
        """

        fun createParams(
            userId: String,
            campaignId: String,
            page: Int,
            perPage: Int,
        ): Map<String, Any> {
            return mapOf(
                PARAM_USER_ID to userId.toLongOrZero(),
                PARAM_CAMPAIGN_ID to campaignId.toLongOrZero(),
                PARAM_PAGE to page,
                PARAM_PER_PAGE to perPage,
            )
        }
    }
}
