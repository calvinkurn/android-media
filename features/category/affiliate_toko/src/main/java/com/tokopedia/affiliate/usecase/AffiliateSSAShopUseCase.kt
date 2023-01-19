package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateSSAShopListResponse
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateSSAShopUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {

    private fun createRequestParamsList(page: Int, limit: Int): HashMap<String, Any> {
        return hashMapOf(
            KEY_PAGE to page,
            KEY_LIMIT to limit,
            KEY_SITE_ID to SITE_ID,
            KEY_VERTICAL_ID to VERTICAL_ID
        )
    }

    suspend fun getSSAShopList(page: Int, limit: Int): AffiliateSSAShopListResponse {
        return repository.getGQLData(
            GET_SSA_SHOP_LIST,
            AffiliateSSAShopListResponse::class.java,
            createRequestParamsList(page, limit)
        )
    }

    companion object {
        private const val KEY_PAGE = "page"
        private const val KEY_LIMIT = "limit"
        private const val KEY_SITE_ID = "siteId"
        private const val KEY_VERTICAL_ID = "verticalId"
        private const val SITE_ID = 1
        private const val VERTICAL_ID = 1
        private val GET_SSA_SHOP_LIST =
            """query getSSAShopList(${'$'}page: Int, ${'$'}limit: Int, ${'$'}siteId: Int, ${'$'}verticalId: Int){
              getSSAShopList(
               page: ${'$'}page, limit: ${'$'}limit, siteId: ${'$'}siteId, verticalId: ${'$'}verticalId
               ) {
                Data {
                  ShopData {
                    SSAShopDetail {
                      ShopId
                      ShopName
                      ShopType
                      ShopLocation
                      SSAStatus
                      QuantitySold
                      Rating
                      BadgeURL
                      Message
                      SSAMessage
                      URLDetail {
                        DesktopURL
                        MobileURL
                        AndroidURL
                        IosURL
                      }
                      ImageURL {
                        DesktopURL
                        MobileURL
                        AndroidURL
                        IosURL
                      }
                    }
                    CommissionDetail {
                      CumulativePercentage
                      CumulativePercentageFormatted
                      SellerPercentage
                      SellerPercentageformatted
                      ExpiredDate
                      ExpiredDate_formatted
                    }
                  }
                }
              }
            }
            }
            """.trimIndent()
    }
}
