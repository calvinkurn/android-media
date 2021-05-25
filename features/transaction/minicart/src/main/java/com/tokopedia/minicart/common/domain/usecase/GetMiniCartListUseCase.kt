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
                  total_product_count
                  total_product_error
                  total_product_price
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
                  max_quantity
                  max_char_note
                  messages {
                    ErrorFieldBetween
                    ErrorFieldMaxChar
                    ErrorFieldRequired
                    ErrorProductAvailableStock
                    ErrorProductAvailableStockDetail
                    ErrorProductMaxQuantity
                    ErrorProductMinQuantity
                  }
                  header_title
                  shopping_summary {
                    total_wording
                    total_value
                    discount_total_wording
                    discount_value
                    payment_total_wording
                    payment_total_value
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
                      user_address_id
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
                        preorder {
                          is_preorder
                          duration
                        }
                      }
                      shop {
                        shop_alert_message
                        shop_id
                        user_id
                        shop_name
                        shop_image
                        shop_url
                        shop_status
                        is_gold
                        is_gold_badge
                        is_official
                        is_free_returns
                        address_id
                        postal_code
                        latitude
                        longitude
                        district_id
                        district_name
                        origin
                        address_street
                        province_id
                        city_id
                        city_name
                        province_name
                        country_name
                        is_allow_manage
                        shop_domain
                        shop_ticker
                        maximum_weight_wording
                        maximum_shipping_weight
                      }
                      promo_codes
                      cart_string
                      total_cart_details_error
                      total_cart_price
                      errors
                      sort_key
                      is_fulfillment_service
                      warehouse {
                        warehouse_id
                        partner_id
                        shop_id
                        warehouse_name
                        district_id
                        district_name
                        city_id
                        city_name
                        province_id
                        province_name
                        status
                        postal_code
                        is_default
                        latlon
                        latitude
                        longitude
                        email
                        address_detail
                        country_name
                        is_fulfillment
                      }
                      checkbox_state
                    }
                  }
                  available_section {
                    action {
                      id
                      code
                      message
                    }
                    available_group {
                      shop {
                        shop_alert_message
                        shop_id
                        user_id
                        shop_name
                        shop_image
                        shop_url
                        shop_status
                        is_gold
                        is_gold_badge
                        is_official
                        is_free_returns
                        address_id
                        postal_code
                        latitude
                        longitude
                        district_id
                        district_name
                        origin
                        address_street
                        province_id
                        city_id
                        city_name
                        province_name
                        country_name
                        is_allow_manage
                        shop_domain
                        shop_ticker
                        maximum_weight_wording
                        maximum_shipping_weight
                        shop_type_info {
                          shop_tier
                          shop_grade
                          badge
                          badge_svg
                          title
                          title_fmt
                        }
                        official_store {
                          is_official
                          os_logo_url
                        }
                        gold_merchant {
                          is_gold
                          is_gold_badge
                          gold_merchant_logo_url
                        }
                        shop_shipments {
                          ship_id
                          ship_name
                          ship_code
                          ship_logo
                          is_dropship_enabled
                          ship_prods {
                            ship_prod_id
                            ship_prod_name
                            ship_group_name
                            ship_group_id
                            minimum_weight
                            additional_fee
                          }
                        }
                      }
                      shipment_information {
                        estimation
                        shop_location
                        preorder {
                          is_preorder
                          duration
                        }
                      }
                      cart_details {
                        cart_id
                        product {
                          product_tracker_data {
                            attribution
                            tracker_list_name
                          }
                          isWishlist
                          product_id
                          product_name
                          product_price_fmt
                          product_price
                          parent_id
                          category_id
                          category
                          catalog_id
                          wholesale_price {
                            qty_min
                            qty_min_fmt
                            qty_max
                            qty_max_fmt
                            prd_prc
                            prd_prc_fmt
                          }
                          product_weight
                          product_weight_fmt
                          product_condition
                          product_status
                          product_url
                          is_preorder
                          product_cashback
                          product_min_order
                          product_max_order
                          product_rating
                          product_invenage_value
                          product_invenage_total {
                            by_user {
                              in_cart
                              last_stock_less_than
                            }
                            by_user_text {
                              in_cart
                              last_stock_less_than
                              complete
                            }
                            is_counted_by_user
                            by_product {
                              in_cart
                              last_stock_less_than
                            }
                            by_product_text {
                              in_cart
                              last_stock_less_than
                              complete
                            }
                            is_counted_by_product
                          }
                          product_switch_invenage
                          product_information
                          price_changes {
                            changes_state
                            amount_difference
                            original_amount
                            description
                          }
                          product_image {
                            image_src_100_square
                          }
                          product_notes
                          product_quantity
                          product_weight_unit_code
                          product_weight_unit_text
                          last_update_price
                          is_update_price
                          product_preorder {
                            duration_day
                            duration_text
                            duration_unit_code
                            duration_unit_text
                            duration_value
                          }
                          campaign_id
                          product_original_price
                          product_price_original_fmt
                          is_slash_price
                          product_finsurance
                          is_wishlisted
                          is_ppp
                          is_cod
                          warehouse_id
                          is_parent
                          is_campaign_error
                          is_blacklisted
                          booking_stock
                          product_variant {
                            parent_id
                            default_child
                            variant {
                              product_variant_id
                              variant_id
                              variant_unit_id
                              name
                              identifier
                              unit_name
                              position
                              option {
                                product_variant_option_id
                                variant_unit_value_id
                                value
                                picture {
                                  url
                                  url200
                                  picture_detail {
                                    file_name
                                    file_path
                                    width
                                    height
                                  }
                                }
                              }
                            }
                          }
                          is_product_volume_weight
                          initial_price
                          initial_price_fmt
                          slash_price_label
                          product_warning_message
                          product_alert_message
                        }
                        errors
                        messages
                        checkbox_state
                      }
                    }
                  }
                }
              }
            }
    """.trimIndent()
    }

}