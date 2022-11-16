package com.tokopedia.tokofood.feature.merchant.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.feature.merchant.domain.model.response.GetMerchantDataResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetMerchantDataUseCase @Inject constructor(@ApplicationContext repository: GraphqlRepository)
    : GraphqlUseCase<GetMerchantDataResponse>(repository) {

    companion object {
        private const val KEY_MERCHANT_ID = "merchantID"
        private const val KEY_LATLONG = "latlong"
        private const val KEY_TIMEZONE = "timezone"

        @JvmStatic
        fun createRequestParams(merchantId: String, latlong: String, timezone: String): RequestParams {
            return RequestParams.create().apply {
                putString(KEY_MERCHANT_ID, merchantId)
                putString(KEY_LATLONG, latlong)
                putString(KEY_TIMEZONE, timezone)
            }
        }
    }

    private val query = """
        query getMerchantData(${'$'}merchantID : String!, ${'$'}latlong : String!, ${'$'}timezone: String) {
          tokofoodGetMerchantData(merchantID: ${'$'}merchantID, latlong: ${'$'}latlong, timezone: ${'$'}timezone) {
            ticker {
              title
              subtitle
              link
              type
            }
            topBanner {
              title
              subtitle
              imageURL
              isShown
            }
            merchantProfile {
              name
              address
              imageURL
              latitude
              longitude
              merchantCategories
              closeWarning
              rating
              ratingFmt
              totalRating
              totalRatingFmt
              distance
              distanceFmt {
                content
                isWarning
              }
              deliverable
              etaFmt {
                content
                isWarning
              }
              opsHourFmt {
                content
                isWarning
              }
              opsHourDetail {
                day
                time
                isWarning                
              }
            }
            filters {
              key
              title
              subtitle
            }
            categories {
              id
              key
              categoryName
              catalogs {
                id
                name
                description
                imageURL
                price
                priceFmt
                slashPrice
                slashPriceFmt
                isOutOfStock
                variants {
                  id
                  name
                  options {
                      id
                      name
                      price
                      priceFmt
                      status
                  }
                  isRequired
                  maxQty
                  minQty
                }
              }
            }
          }
        }
    """.trimIndent()

    init {
        setGraphqlQuery(query)
        setTypeClass(GetMerchantDataResponse::class.java)
    }
}
