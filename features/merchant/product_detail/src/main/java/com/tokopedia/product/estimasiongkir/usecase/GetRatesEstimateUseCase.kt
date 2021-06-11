package com.tokopedia.product.estimasiongkir.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.data.util.getSuccessData
import com.tokopedia.product.estimasiongkir.data.model.v3.RatesEstimationModel
import com.tokopedia.product.estimasiongkir.di.RatesEstimationScope
import com.tokopedia.product.info.model.productdetail.response.BottomSheetProductDetailInfoResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Yehezkiel on 16/02/21
 */
@RatesEstimationScope
class GetRatesEstimateUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) : UseCase<RatesEstimationModel>() {

    companion object {
        private const val PARAM_PRODUCT_WEIGHT = "weight"
        private const val PARAM_SHOP_DOMAIN = "domain"
        private const val PARAM_ORIGIN = "origin"
        private const val PARAM_SHOP_ID = "shop_id"
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_IS_FULFILLMENT = "is_fulfillment"
        private const val PARAM_DESTINATION = "destination"
        private const val PARAM_FREE_SHIPPING = "free_shipping_flag"
        private const val PARAM_PO_TIME = "po_time"
        private const val PARAM_SHOP_TIER = "shop_tier"

        fun createParams(productWeight: Float, shopDomain: String, origin: String?, productId: String,
                         shopId: String, isFulfillment: Boolean, destination: String, free_shipping_flag: Int,
                         poTime: Long, shopTier:Int): Map<String, Any?> = mapOf(
                PARAM_PRODUCT_WEIGHT to productWeight,
                PARAM_SHOP_DOMAIN to shopDomain,
                PARAM_ORIGIN to origin,
                PARAM_SHOP_ID to shopId,
                PARAM_PRODUCT_ID to productId,
                PARAM_IS_FULFILLMENT to isFulfillment,
                PARAM_DESTINATION to destination,
                PARAM_PO_TIME to poTime,
                PARAM_FREE_SHIPPING to free_shipping_flag,
                PARAM_SHOP_TIER to shopTier)

        val QUERY = """
            query RateEstimate(${'$'}weight: Float!, ${'$'}domain: String!, ${'$'}origin: String, ${'$'}shop_id: String, ${'$'}product_id: String, ${'$'}destination: String!, ${'$'}is_fulfillment: Boolean,${'$'}free_shipping_flag: Int, ${'$'}po_time: Int, ${'$'}shop_tier: Int) {
                  ratesEstimateV3(input: {weight: ${'$'}weight, domain: ${'$'}domain, origin: ${'$'}origin, shop_id: ${'$'}shop_id, product_id: ${'$'}product_id,destination: ${'$'}destination, is_fulfillment: ${'$'}is_fulfillment,free_shipping_flag: ${'$'}free_shipping_flag, po_time: ${'$'}po_time,shop_tier: ${'$'}shop_tier }) {
                      data{
                          tokocabang_from{
                               icon_url
                               title
                               content
                          }
                          free_shipping{
                              flag 
                              shipping_price 
                              eta_text
                              error_code 
                          }
                          address {
                              city_name
                              province_name
                              district_name
                              country
                              postal_code
                              address
                              lat
                              long
                              phone
                              addr_name
                              address_1
                              receiver_name
                          }
                          shop {
                              district_name
                              city_name
                          }
                          texts {
                              text_min_price
                              text_destination
                          }
                          rates {
                              id
                              rates_id
                              type
                              services {
                                  service_name
                                  service_id
                                  service_order
                                  status
                                  range_price {
                                      min_price
                                      max_price
                                  }
                                  etd {
                                      min_etd
                                      max_etd
                                  }
                                  texts {
                                      text_range_price
                                      text_etd
                                      text_notes
                                      text_service_notes
                                      text_price
                                      text_service_desc
                                  }
                                  products {
                                      shipper_name
                                      shipper_id
                                      shipper_product_id
                                      shipper_product_name
                                      shipper_product_desc
                                      shipper_weight
                                      promo_code
                                      is_show_map
                                      status
                                      recommend
                                      checksum
                                      ut
                                      price {
                                          price
                                          formatted_price
                                      }
                                      etd {
                                          min_etd
                                          max_etd
                                      }
                                      eta {
                                        text_eta
                                      }
                                      texts {
                                          text_range_price
                                          text_etd
                                          text_notes
                                          text_service_notes
                                          text_price
                                          text_service_desc
                                      }
                                      insurance {
                                          insurance_price
                                          insurance_type
                                          insurance_type_info
                                          insurance_used_type
                                          insurance_used_default
                                          insurance_used_info
                                      }
                                      error {
                                          error_id
                                          error_message
                                      }
                                      cod {
                                          is_cod_available
                                          cod_text
                                          cod_price
                                          formatted_price
                                      }
                                      features {
                                          dynamic_price {
                                            text_label
                                          }
                                      }
                                  }
                                  error {
                                      error_id
                                      error_message
                                  }
                                  is_promo
                                  cod {
                                      is_cod
                                      cod_text
                                  }
                                  order_priority {
                                      is_now
                                      price
                                      formatted_price
                                      inactive_message
                                  }
                
                              }
                              recommendations {
                                  service_name
                                  shipping_id
                                  shipping_product_id
                                  price {
                                      price
                                      formatted_price
                                  }
                                  etd {
                                      min_etd
                                      max_etd
                                  }
                                  texts {
                                      text_range_price
                                      text_etd
                                      text_notes
                                      text_service_notes
                                      text_price
                                      text_service_desc
                                  }
                                  insurance {
                                      insurance_price
                                      insurance_type
                                      insurance_type_info
                                      insurance_used_type
                                      insurance_used_default
                                      insurance_used_info
                                  }
                                  error {
                                      error_id
                                      error_message
                                  }
                              }
                              info {
                                  cod_info {
                                      failed_message
                                  }
                                  blackbox_info{
                                      text_info
                                  }
                              }
                              error {
                                  error_id
                                  error_message
                              }
                          }
                          is_blackbox
                      }
                  }
                }""".trimIndent()
    }

    private var requestParams: Map<String, Any?> = mapOf()
    private var forceRefresh = false

    suspend fun executeOnBackground(requestParams: Map<String, Any?>, forceRefresh: Boolean): RatesEstimationModel {
        this.requestParams = requestParams
        this.forceRefresh = forceRefresh
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): RatesEstimationModel {
        val request = GraphqlRequest(QUERY, RatesEstimationModel.Response::class.java, requestParams)
        val cacheStrategy = GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST)
                .setSessionIncluded(false)
                .build()

        val response = graphqlRepository.getReseponse(listOf(request), cacheStrategy)
        val error: List<GraphqlError>? = response.getError(BottomSheetProductDetailInfoResponse::class.java)
        val data = response.getSuccessData<RatesEstimationModel.Response>().data?.data
                ?: throw NullPointerException()

        if (error != null && error.isNotEmpty()) {
            throw MessageErrorException(error.firstOrNull()?.message ?: "")
        }

        val filteredService = data.rates.services.filter { it.products.isNotEmpty() }

        data.copy(rates = data.rates.copy(services = filteredService))
        return data
    }

}