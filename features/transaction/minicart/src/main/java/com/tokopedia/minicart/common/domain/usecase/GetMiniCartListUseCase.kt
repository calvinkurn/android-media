package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetMiniCartListUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) : UseCase<MiniCartData>() {

    // Todo : set params

    override suspend fun executeOnBackground(): MiniCartData {
        val params = mapOf<String, String>()
        val request = GraphqlRequest(QUERY, MiniCartGqlResponse::class.java, params)
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<MiniCartGqlResponse>()

        if (response.miniCart.status == "OK") {
            return response.miniCart
        } else {
            throw ResponseErrorException(response.miniCart.errorMessage.joinToString(", "))
        }
    }

    companion object {
        val QUERY = """
        query mini_cart(${'$'}lang: String, ${'$'}additional_params: CartRevampAdditionalParams, ${'$'}shop_ids : [String]) {
          status
          mini_cart(lang:${'$'}lang, additional_params:"${'$'}"additional_params, shop_ids:${'$'}shop_ids}) {
            error_message
            status
            data {
              errors        
              pop_up_message
              empty_cart {
                title
                image
                description
                buttons {
                  id
                  code
                  message
                  color
                  __typename
                }
                __typename
              }
              out_of_service {
                id
                code
                image
                title
                description
                buttons {
                  id
                  code
                  message
                  color
                  __typename
                }
                __typename
              }
              messages {
                ErrorFieldBetween
                ErrorFieldMaxChar
                ErrorFieldRequired
                ErrorProductMaxQuantity
                ErrorProductAvailableStock
                ErrorProductAvailableStockDetail
                ErrorProductMinQuantity
                __typename
              }
              header_title
              shopping_summary {
                total_wording
                total_value
                discount_total_wording
                discount_value
                payment_total_wording
                payment_total_value
                promo_wording
                promo_value
                seller_cashback_wording
                seller_cashback_value
              },
              max_quantity
              max_char_note
              available_section {
                action {
                  id
                  code
                  message
                  __typename
                }
                available_group {
                  user_address_id
                  shipment_information {
                    shop_location
                    estimation
                    free_shipping {
                      eligible
                      badge_url
                      __typename
                    }
                    free_shipping_extra {
                      eligible
                      badge_url
                      __typename
                    }
                    preorder {
                      is_preorder
                      duration
                      __typename
                    }
                    __typename
                  }
                  shop {
                    shop_alert_message
                    shop_ticker
                    maximum_weight_wording
                    maximum_shipping_weight
                    shop_id
                    user_id
                    shop_name
                    shop_image
                    shop_url
                    shop_status
                    is_gold
                    is_official
                    gold_merchant {
                      is_gold
                      is_gold_badge
                      gold_merchant_logo_url
                      __typename
                    }
                    shop_type_info {
                      shop_tier
                      shop_grade
                      badge
                      badge_svg
                      title
                      __typename
                    }
                    official_store {
                      is_official
                      os_logo_url
                      __typename
                    }
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
                    province_id
                    province_name
                    country_name
                    is_allow_manage
                    shop_domain
                    __typename
                  }
                  promo_codes
                  cart_string
                  cart_details {
                    cart_id
                    product {
                      product_tracker_data {
                        attribution
                        tracker_list_name
                        __typename
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
                        __typename
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
                          __typename
                        }
                        by_user_text {
                          in_cart
                          last_stock_less_than
                          complete
                          __typename
                        }
                        is_counted_by_user
                        by_product {
                          in_cart
                          last_stock_less_than
                          __typename
                        }
                        by_product_text {
                          in_cart
                          last_stock_less_than
                          complete
                          __typename
                        }
                        is_counted_by_product
                        __typename
                      }
                      product_switch_invenage
                      product_information
                      price_changes {
                        changes_state
                        amount_difference
                        original_amount
                        description
                        __typename
                      }
                      product_image {
                        image_src_100_square
                        __typename
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
                        __typename
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
                      free_shipping_extra {
                        eligible
                        badge_url
                        __typename
                      }
                      free_shipping {
                        eligible
                        badge_url
                        __typename
                      }
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
                                __typename
                              }
                              __typename
                            }
                            __typename
                          }
                          __typename
                        }
                        __typename
                      }
                      is_product_volume_weight
                      initial_price
                      initial_price_fmt
                      slash_price_label
                      product_warning_message
                      product_alert_message
                      variant_description_detail {
                        variant_name
                        variant_description
                        __typename
                      }
                      __typename
                    }
                    errors
                    messages
                    checkbox_state
                    __typename
                  }
                  errors
                  sort_key
                  is_fulfillment_service
                  warehouse {
                    warehouse_id
                    shop_id
                    warehouse_name
                    is_fulfillment
                    __typename
                  }
                  checkbox_state
                  __typename
                }
                __typename
              }
              unavailable_ticker
              unavailable_section {
                title
                unavailable_description
                selected_unavailable_action_id
                action {
                  id
                  code
                  message
                  __typename
                }
                unavailable_group {
                  user_address_id
                  shipment_information {
                    shop_location
                    estimation
                    free_shipping_extra {
                      eligible
                      badge_url
                      __typename
                    }
                    free_shipping {
                      eligible
                      badge_url
                      __typename
                    }
                    preorder {
                      is_preorder
                      duration
                      __typename
                    }
                    __typename
                  }
                  shop {
                    shop_alert_message
                    shop_ticker
                    maximum_weight_wording
                    maximum_shipping_weight
                    shop_id
                    user_id
                    shop_name
                    shop_image
                    shop_url
                    shop_status
                    is_gold
                    is_official
                    is_free_returns
                    gold_merchant {
                      is_gold
                      is_gold_badge
                      gold_merchant_logo_url
                      __typename
                    }
                    shop_type_info {
                      shop_tier
                      shop_grade
                      badge
                      badge_svg
                      title
                      __typename
                    }
                    official_store {
                      is_official
                      os_logo_url
                      __typename
                    }
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
                    province_id
                    province_name
                    country_name
                    is_allow_manage
                    shop_domain
                    __typename
                  }
                  promo_codes
                  cart_string
                  cart_details {
                    cart_id
                    selected_unavailable_action_link
                    product {
                      product_tracker_data {
                        attribution
                        tracker_list_name
                        __typename
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
                      product_status
                      product_url
                      is_preorder
                      product_cashback
                      product_switch_invenage
                      product_information
                      price_changes {
                        changes_state
                        amount_difference
                        original_amount
                        description
                        __typename
                      }
                      product_image {
                        image_src_100_square
                        __typename
                      }
                      product_notes
                      product_quantity
                      product_weight_unit_code
                      product_weight_unit_text
                      last_update_price
                      is_update_price
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
                      free_shipping {
                        eligible
                        badge_url
                        __typename
                      }
                      booking_stock
                      is_product_volume_weight
                      initial_price
                      initial_price_fmt
                      slash_price_label
                      product_warning_message
                      product_alert_message
                      variant_description_detail {
                        variant_name
                        variant_description
                        __typename
                      }
                      __typename
                    }
                    errors
                    messages
                    checkbox_state
                    __typename
                  }
                  errors
                  sort_key
                  is_fulfillment_service
                  warehouse {
                    warehouse_id
                    shop_id
                    warehouse_name
                    is_fulfillment
                    __typename
                  }
                  checkbox_state
                  __typename
                }
                __typename
              }
              total_product_price
              total_product_count
              total_product_error
              tickers {
                id
                message
                page
                __typename
              }
              hashed_email
              __typename
            }
            __typename
          }
        }
    """.trimIndent()
    }

}