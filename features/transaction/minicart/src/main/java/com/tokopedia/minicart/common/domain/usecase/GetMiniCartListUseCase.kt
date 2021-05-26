package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetMiniCartListUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository,
                                                 private val chosenAddressRequestHelper: ChosenAddressRequestHelper) : UseCase<MiniCartData>() {

    private var params: Map<String, Any>? = null

    fun setParams(shopIds: List<String>) {
        params = mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_KEY_ADDITIONAL to mapOf(
                        PARAM_KEY_SHOP_IDS to shopIds,
                        KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress()
                )
        )
    }

    override suspend fun executeOnBackground(): MiniCartData {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }

        val request = GraphqlRequest(QUERY, MiniCartGqlResponse::class.java, params)
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<MiniCartGqlResponse>()
        if (response.miniCart.status == "OK") {
            return response.miniCart
        } else {
            throw ResponseErrorException(response.miniCart.errorMessage.joinToString(", "))
        }
    }

    companion object {
        const val PARAM_KEY_LANG = "lang"
        const val PARAM_KEY_SELECTED_CART_ID = "selected_cart_id"
        const val PARAM_KEY_ADDITIONAL = "additional_params"
        const val PARAM_KEY_SHOP_IDS = "shop_ids"

        const val PARAM_VALUE_ID = "id"

        val QUERY = """
            query mini_cart(${'$'}dummy: Int, ${'$'}lang: String, ${'$'}additional_params: CartRevampAdditionalParams) {
              status
              mini_cart(dummy: ${'$'}dummy, lang: ${'$'}lang, additional_params: ${'$'}additional_params) {
                error_message
                status
                data {
                  errors
                  empty_cart {
                    title
                    image
                    description
                    buttons {
                      id
                    }
                  }
                  out_of_service {
                    id
                    code
                    image
                    title
                    description
                    buttons {
                      id
                    }
                  }
                  max_char_note
                  header_title
                  shopping_summary {
                    total_wording
                    total_value
                    discount_total_wording
                    discount_value
                    payment_total_wording
                    payment_total_value
                  }
                  available_section {
                    action {
                      id
                      code
                      message
                    }
                    available_group {
                      cart_string
                      errors
                      shop {
                        maximum_weight_wording
                        maximum_shipping_weight
                        shop_id
                        shop_type_info {
                          badge
                          shop_grade
                          shop_tier
                          title
                          title_fmt
                        }
                      }
                      shipment_information {
                        shop_location
                        estimation
                        free_shipping {
                          eligible
                          badge_url
                        }
                        free_shipping_extra {
                          eligible
                          badge_url
                        }
                      }
                      cart_details {
                        cart_id
                        selected_unavailable_action_link
                        errors
                        product {
                          product_id
                          product_weight
                          product_quantity
                          product_name
                          product_image {
                            image_src_100_square
                          }
                          variant_description_detail {
                            variant_name
                            variant_description
                          }
                          product_warning_message
                          slash_price_label
                          product_original_price
                          initial_price
                          product_price
                          product_information
                          product_notes
                          product_min_order
                          product_max_order
                          parent_id
                          wholesale_price {
                            qty_min
                            qty_max
                            prd_prc
                          }
                        }
                      }
                    }
                  }
                  unavailable_ticker
                  unavailable_section_action {
                    id
                    code
                    message
                  }
                  unavailable_section {
                    title
                    unavailable_description
                    selected_unavailable_action_id
                    action {
                      id
                      code
                      message
                    }
                    unavailable_group {
                      shipment_information {
                        shop_location
                        estimation
                        free_shipping {
                          eligible
                          badge_url
                        }
                        free_shipping_extra {
                          eligible
                          badge_url
                        }
                      }
                      shop {
                        maximum_weight_wording
                        maximum_shipping_weight
                        shop_id
                        shop_type_info {
                          badge
                          shop_grade
                          shop_tier
                          title
                          title_fmt
                        }
                      }
                      cart_string
                      errors
                      cart_details {
                        cart_id
                        selected_unavailable_action_link
                        errors
                        product {
                          product_id
                          product_weight
                          product_quantity
                          product_name
                          product_image {
                            image_src_100_square
                          }
                          variant_description_detail {
                            variant_name
                            variant_description
                          }
                          product_warning_message
                          slash_price_label
                          product_original_price
                          initial_price
                          product_price
                          product_information
                          product_notes
                          product_min_order
                          product_max_order
                          parent_id
                          wholesale_price {
                            qty_min
                            qty_max
                            prd_prc
                          }
                        }
                      }
                    }
                  }
                  total_product_count
                  total_product_price
                  total_product_error
                }
              }
            }
        """.trimIndent()
    }

}