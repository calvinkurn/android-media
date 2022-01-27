package com.tokopedia.campaignlist.common.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.campaignlist.common.data.model.request.GetMerchantCampaignBannerGeneratorDataRequest
import com.tokopedia.campaignlist.common.data.model.response.GetMerchantCampaignBannerGeneratorDataResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetMerchantBannerUseCase @Inject constructor(
        @ApplicationContext repository: GraphqlRepository
) : GraphqlUseCase<GetMerchantCampaignBannerGeneratorDataResponse>(repository) {

    companion object {

        private const val KEY_PARAMS = "params"
        private const val INTEGER_ZERO = 0
        private const val SOURCE_ANDROID_NPL = "android-npl"

        private val query = """
            query getMerchantCampaignBannerGeneratorData(${'$'}params:GetMerchantCampaignBannerGeneratorDataRequest!) {
                getMerchantCampaignBannerGeneratorData(params:${'$'}params) {
                    response_header {
                      status
                      success
                      processTime
                    }
                    campaign {
                      campaign_id
                      name
                      start_date
                      end_date
                      review_end_date
                      status_text
                      status_id
                      discount_percentage_text
                      total_product
                      total_product_overload
                      highlight_products {
                        wording
                        total_product_wording
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
                          campaign {
                            CampaignID
                            DiscountPercentage
                            DiscountedPrice
                            OriginalPrice
                            DiscountedPriceFmt
                            OriginalPriceFmt
                          }
                        }
                      }
                    }
                    shop_data {
                      ShopID
                      Name
                      URL
                      URLMobile
                      URLApps
                      IsGold
                      IsOfficial
                      Logo
                      City
                      Domain
                      UserID
                      Badge {
                        Title
                        ImageURL
                      }
                    }
                    formatted_start_date
                    formatted_end_date
                    formatted_review_end_date
                    formatted_sharing_start_date
                    formatted_sharing_end_date
                    formatted_sharing_review_end_date
                }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(campaignId: Int): RequestParams {
            return RequestParams.create().apply {
                val params = GetMerchantCampaignBannerGeneratorDataRequest(
                        campaignId = campaignId.toString(),
                        rows = INTEGER_ZERO,
                        source = SOURCE_ANDROID_NPL
                )
                putObject(KEY_PARAMS, params)
            }
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(GetMerchantCampaignBannerGeneratorDataResponse::class.java)
    }
}