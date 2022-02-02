package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.mapper.MiniCartSimplifiedMapper
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetMiniCartListSimplifiedUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository,
                                                           private val miniCartSimplifiedMapper: MiniCartSimplifiedMapper,
                                                           private val chosenAddressRequestHelper: ChosenAddressRequestHelper) : UseCase<MiniCartSimplifiedData>() {

    private var params: Map<String, Any>? = null

    fun setParams(shopIds: List<String>) {
        params = mapOf(
                GetMiniCartListUseCase.PARAM_KEY_LANG to GetMiniCartListUseCase.PARAM_VALUE_ID,
                GetMiniCartListUseCase.PARAM_KEY_ADDITIONAL to mapOf(
                        GetMiniCartListUseCase.PARAM_KEY_SHOP_IDS to shopIds,
                        ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress()
                )
        )
    }

    fun setParams(shopIds: List<String>, promoId: String, promoCode: String) {
        params = mapOf(
                GetMiniCartListUseCase.PARAM_KEY_LANG to GetMiniCartListUseCase.PARAM_VALUE_ID,
                GetMiniCartListUseCase.PARAM_KEY_ADDITIONAL to mapOf(
                        GetMiniCartListUseCase.PARAM_KEY_SHOP_IDS to shopIds,
                        GetMiniCartListUseCase.PARAM_KEY_PROMO to mapOf(
                            GetMiniCartListUseCase.PARAM_KEY_PROMO_ID to promoId,
                            GetMiniCartListUseCase.PARAM_KEY_PROMO_CODE to promoCode
                        ),
                        ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress()
                )
        )
    }

    override suspend fun executeOnBackground(): MiniCartSimplifiedData {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }

        val request = GraphqlRequest(QUERY, MiniCartGqlResponse::class.java, params)
        val response = graphqlRepository.response(listOf(request)).getSuccessData<MiniCartGqlResponse>()

        if (response.miniCart.status == "OK") {
            return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(response.miniCart)
        } else {
            throw ResponseErrorException(response.miniCart.errorMessage.joinToString(", "))
        }
    }

    companion object {
        val QUERY = """
        query mini_cart(${'$'}dummy: Int, ${'$'}lang: String, ${'$'}additional_params: CartRevampAdditionalParams) {
          status
          mini_cart(dummy: ${'$'}dummy, lang: ${'$'}lang, additional_params: ${'$'}additional_params) {
            error_message
            status
            data {
              errors
              beli_button_config {
                button_type
                button_wording
              }
              total_product_count
              total_product_error
              total_product_price
              available_section {
                available_group {
                  shop {
                    shop_id
                    shop_name
                    shop_type_info {
                      title_fmt
                    }
                  }
                  shipment_information {                                        
                    free_shipping {
                      eligible
                    }
                    free_shipping_extra {
                      eligible
                    }
                  }
                  cart_details {
                    cart_id
                    product {
                      parent_id
                      product_id
                      product_quantity
                      product_notes
                      campaign_id
                      product_tracker_data {
                        attribution
                      }
                      product_weight
                      slash_price_label                                                                   
                      warehouse_id                      
                      category_id
                      category
                      product_name
                      variant_description_detail {
                        variant_name
                      }
                      product_price
                      product_quantity
                      product_invenage_value
                      product_switch_invenage
                    }
                  }
                }
              }
              unavailable_section {
                unavailable_group {
                  shop {
                    shop_id
                    shop_name
                    shop_type_info {
                      title_fmt
                    }
                  }
                  shipment_information {                                        
                    free_shipping {
                      eligible
                    }
                    free_shipping_extra {
                      eligible
                    }
                  }
                  cart_details {
                    cart_id
                    product {
                      parent_id
                      product_id
                      product_quantity
                      product_notes                      
                      campaign_id
                      product_tracker_data {
                        attribution
                      }
                      product_weight
                      slash_price_label                                                                   
                      warehouse_id                      
                      category_id
                      category
                      product_name
                      variant_description_detail {
                        variant_name
                      }
                      product_price
                      product_quantity
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent()
    }

}