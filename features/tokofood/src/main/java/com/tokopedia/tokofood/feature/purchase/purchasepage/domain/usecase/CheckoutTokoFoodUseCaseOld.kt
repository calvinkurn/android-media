package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood
import com.tokopedia.tokofood.common.domain.param.CheckoutTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodResponse
import javax.inject.Inject

private const val QUERY = """
        query CartListTokofood(${'$'}params: cartTokofoodParams!) {
          cart_list_tokofood(params: ${'$'}params) {
            message
            status
            data {
              popup_message
              popup_error_message
              popup_message_type
              shop {
                shop_id
                name
                distance
              }
              tickers {
                top {
                  id
                  message
                  page
                }
                bottom {
                  id
                  message
                  page
                }
              }
              error_tickers {
                top {
                  id
                  message
                  page
                }
                bottom {
                  id
                  message
                  page
                }
              }
              error_unblocking
              user_address {
                address_id
                address_name
                address
                phone
                receiver_name
                status
              }
              available_section {
                products {
                  cart_id
                  product_id
                  category_id
                  name
                  description
                  image_url
                  price
                  price_fmt
                  original_price
                  original_price_fmt
                  discount_percentage
                  notes
                  quantity
                  variants {
                    variant_id
                    name
                    rules {
                      selection_rule {
                        type
                        max_quantity
                        min_quantity
                        required
                      }
                    }
                    options {
                      is_selected
                      option_id
                      name
                      price
                      price_fmt
                      status
                    }
                  }
                }
              }
              unavailable_section_header
              unavailable_sections {
                title
                products {
                  cart_id
                  product_id
                  category_id
                  name
                  description
                  image_url
                  price
                  price_fmt
                  original_price
                  original_price_fmt
                  discount_percentage
                  notes
                  quantity
                  variants {
                    variant_id
                    name
                    rules {
                      selection_rule {
                        type
                        max_quantity
                        min_quantity
                        required
                      }
                    }
                    options {
                      is_selected
                      option_id
                      name
                      price
                      price_fmt
                      status
                    }
                  }
                }
              }
              shipping {
                name
                logo_url
                eta
                price
                price_fmt
              }
              promo {
                is_promo_applied
                hide_promo
                title
                subtitle
              }
              checkout_consent_bottomsheet {
                is_show_bottomsheet
                image_url
                title
                description   
                terms_and_condition
              }
              shopping_summary {
                total {
                  cost
                  savings
                }
                cost_breakdown {
                  total_cart_price {
                    original_amount
                    amount
                  }
                  outlet_fee {
                    original_amount
                    amount
                  }
                  platform_fee {
                    original_amount
                    amount
                  }
                  delivery_fee {
                    original_amount
                    amount
                    surge {
                      is_surge_price
                      factor
                    }
                  }
                  reimbursement_fee {
                    original_amount
                    amount
                  }
                }
                discount_breakdown {
                  discount_id
                  title
                  amount
                  scope
                  type
                }  
              }
              summary_detail {
                hide_summary
                total_items
                total_price
                details {
                  title
                  price_fmt
                  info {
                    image_url
                    bottomsheet {
                      title
                      description
                    }
                  }
                }
              }
              checkout_additional_data {
                data_type
                checkout_business_id
              }
            }
          }
        }
    """

@GqlQuery("CartListTokofoodOld", QUERY)
class CheckoutTokoFoodUseCaseOld @Inject constructor(
    repository: GraphqlRepository,
    private val chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper
): GraphqlUseCase<CheckoutTokoFoodResponse>(repository) {

    init {
        setTypeClass(CheckoutTokoFoodResponse::class.java)
        setGraphqlQuery(CartListTokofoodOld())
    }

    suspend fun execute(params: String): CheckoutTokoFood {
        val additionalAttributes = CartAdditionalAttributesTokoFood(
            chosenAddressRequestHelper.getChosenAddress()
        )
        val param = generateParams(additionalAttributes.generateString(), params)
        setRequestParams(param)
        val response = executeOnBackground()
        if (response.cartListTokofood.isSuccess()) {
            return response.cartListTokofood
        } else {
            throw MessageErrorException(response.cartListTokofood.getMessageIfError())
        }
    }

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(additionalAttributes: String,
                                   source: String): Map<String, Any> {
            val params = CheckoutTokoFoodParam(
                additionalAttributes = additionalAttributes,
                source = source
            )
            return mapOf(PARAMS_KEY to params)
        }
    }

}
