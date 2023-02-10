package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood
import com.tokopedia.tokofood.common.domain.param.CartListTokofoodParam
import com.tokopedia.tokofood.common.domain.param.CartListTokofoodParamBusinessData
import com.tokopedia.tokofood.common.domain.response.CartListData
import com.tokopedia.tokofood.common.domain.response.CartListTokofoodResponse
import javax.inject.Inject

// TODO: Remove unused field requested
private const val QUERY = """
        query MiniCartGeneralCartList(${'$'}params: cartGeneralParam!) {
          cart_general_cart_list(params: ${'$'}params) {
            message
            success
            data {
              shopping_summary {
                business_breakdown {
                  business_id
                  custom_response {
                    hide_summary
                  }
                  total_bill
                  total_bill_fmt
                  product {
                    title
                    total_price
                    total_price_fmt
                    total_quantity
                    total_cart
                  }
                  add_ons {
                    title
                    price
                    price_fmt
                    custom_response {
                      info {
                        image_url
                        bottomsheet {
                          title
                          description
                        }
                      }
                    }
                  }
                }
              }
              business_data {
                business_id
                success
                message
                ticker {
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
                }
                additional_grouping {
                  detail {
                    additional_group_id
                    cart_ids
                    message
                  }
                }
                custom_response {
                  error_unblocking
                  user_address {
                    address_id
                    address_name
                    address
                    phone
                    receiver_name
                    status
                  }
                  promo {
                    is_promo_applied
                    hide_promo
                    title
                    subtitle
                  }
                  popup_message
                  popup_error_message
                  popup_message_type
                  bottomsheet {
                    is_show_bottomsheet
                    title
                    description
                    image_url
                    buttons{
                        text
                        color
                        action
                        link
                    }
                  }
                  shop {
                    shop_id
                    name
                    distance
                  }
                  shipping {
                    name
                    logo_url
                    eta
                    price
                    price_fmt
                  }
                  checkout_consent_bottomsheet {
                    is_show_bottomsheet
                    image_url
                    title
                    description   
                    terms_and_condition
                  }
                  checkout_additional_data {
                    data_type
                    checkout_business_id
                  }
                  shopping_summary {
                    total_items
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
                }
                cart_groups {
                  cart_group_id
                  success
                  carts {
                    cart_id
                    success
                    product_id
                    shop_id
                    quantity
                    metadata {
                      notes
                      variants {
                        variant_id
                        option_id
                      }
                    }
                    custom_response {
                      category_id
                      notes
                      name
                      description
                      image_url
                      original_price
                      original_price_fmt
                      discount_percentage
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
                      price
                      price_fmt
                    }
                  }
                }
              }
            }
          }
        }
    """

@GqlQuery("MiniCartGeneralCartList", QUERY)
class MiniCartListTokofoodUseCase @Inject constructor(
    repository: GraphqlRepository,
    private val chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper
): GraphqlUseCase<CartListTokofoodResponse>(repository) {

    init {
        setTypeClass(CartListTokofoodResponse::class.java)
        setGraphqlQuery(CartGeneralCartLis())
    }

    suspend fun execute(source: String): CartListData {
        val additionalAttributes = CartAdditionalAttributesTokoFood(
            chosenAddressRequestHelper.getChosenAddress()
        )
        val param = generateParams(additionalAttributes, source)
        setRequestParams(param)
        val response = executeOnBackground()
        if (response.cartGeneralCartList.data.isSuccess()) {
            return response.cartGeneralCartList.data.data
        } else {
            throw MessageErrorException(response.cartGeneralCartList.data.message)
        }
    }

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(additionalAttributes: CartAdditionalAttributesTokoFood,
                                   source: String): Map<String, Any> {
            // TODO: Add businessId
            val params = CartListTokofoodParam(
                source = source,
                businessData = listOf(
                    CartListTokofoodParamBusinessData(
                        businessId = String.EMPTY,
                        customRequest = additionalAttributes
                    )
                )
            )
            return mapOf(PARAMS_KEY to params)
        }
    }

}
