package com.tokopedia.oneclickcheckout.order.domain

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.order.data.get.GetOccCartGqlResponse
import com.tokopedia.oneclickcheckout.order.domain.mapper.GetOccCartMapper
import com.tokopedia.oneclickcheckout.order.view.model.OrderData
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetOccCartUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository, private val mapper: GetOccCartMapper) {

    fun createRequestParams(source: String): RequestParams {
        return RequestParams.create().apply {
            putString(PARAM_SOURCE, source)
        }
    }

    suspend fun executeSuspend(params: RequestParams): OrderData {
        val graphqlRequest = GET_OCC_CART_PAGE_QUERY
        val request = GraphqlRequest(graphqlRequest, GetOccCartGqlResponse::class.java, params.parameters)
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<GetOccCartGqlResponse>()
        if (response.response.status.equals(STATUS_OK, true)) {
            val errorMessage = response.response.data.errors.firstOrNull()
            val cart = response.response.data.cartList.firstOrNull()
            if (!errorMessage.isNullOrEmpty() || cart == null) {
                throw MessageErrorException(errorMessage ?: DEFAULT_ERROR_MESSAGE)
            }
            return mapper.mapGetOccCartDataToOrderData(response.response.data)
        } else {
            throw MessageErrorException(response.response.errorMessages.firstOrNull()
                    ?: DEFAULT_ERROR_MESSAGE)
        }
    }

    companion object {
        private const val PARAM_SOURCE = "source"

        private const val GET_OCC_CART_PAGE_QUERY = """query get_occ_cart_page(${"$"}source: String) {
  get_occ_cart_page(source: ${"$"}source) {
    error_message
    status
    data {
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
      tickers {
        id
        message
        page
        title
      }
      ticker_message {
        message
        replacement {
            identifier
            value
        }
      }
      occ_main_onboarding {
        force_show_coachmark
        show_onboarding_ticker
        coachmark_type
        onboarding_ticker {
            title
            message
            image
            show_coachmark_link_text
            coachmark_link_text
        }
        onboarding_coachmark {
            skip_button_text
            detail {
                step
                title
                message
            }
        }
      }
      kero_token
      kero_unix_time
      kero_discom_token
      errors
      cart_list {
        errors
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
            qty_min_fmt
            qty_max_fmt
            qty_min
            qty_max
            prd_prc
            prd_prc_fmt
          }
          product_weight_fmt
          product_weight
          product_condition
          product_status
          product_url
          product_returnable
          is_freereturns
          is_preorder
          product_cashback
          product_min_order
          product_max_order
          product_rating
          product_invenage_value
          product_switch_invenage
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
          price_changes {
            changes_state
            amount_difference
            original_amount
            description
          }
          product_price_currency
          product_image {
            image_src
            image_src_200_square
            image_src_300
            image_src_square
          }
          product_all_images
          product_notes
          product_quantity
          product_weight_unit_code
          product_weight_unit_text
          last_update_price
          is_update_price
          product_alias
          sku
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
          }
          booking_stock
        }
        cart_string
        payment_profile
        shop {
          shop_id
          user_id
          admin_ids
          shop_name
          shop_image
          shop_url
          shop_status
          is_gold
          is_gold_badge
          is_official
          is_free_returns
          gold_merchant {
            is_gold
            is_gold_badge
            gold_merchant_logo_url
          }
          official_store {
            is_official
            os_logo_url
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
          province_name
          country_name
          is_allow_manage
          shop_domain
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
      }
      profile_index_wording
      profile_recommendation_wording
      profile {
        has_preference
        is_changed_profile
        message
        onboarding_header_message
        onboarding_component {
          header_title
          body_image
          body_message
          info_component {
            text
            link
          }
        }
        profile_id
        status
        address {
          address_id
          receiver_name
          address_name
          address_street
          district_id
          district_name
          city_id
          city_name
          province_id
          province_name
          phone
          longitude
          latitude
          postal_code
          geolocation
        }
        payment {
          enable
          active
          gateway_code
          gateway_name
          image
          description
          url
          fee
          minimum_amount
          maximum_amount
          flags {
            pin
          }
          wallet_amount
          metadata
          mdr
          credit_card {
            number_of_cards {
                available
                unavailable
                total
            }
            available_terms {
                term
                mdr
                mdr_subsidized
                min_amount
                is_selected
            }
            bank_code
            card_type
            is_expired
            tnc_info
          }
          error_message {
            message
            button {
                text
                link
            }
          }
          occ_revamp_error_message {
            message
            button {
                text
                action
            }
          }
          ticker_message
          is_enable_next_button
          is_disable_pay_button
          is_ovo_only_campaign
          ovo_additional_data {
            ovo_activation {
                is_required
                button_title
                error_message
                error_ticker
            }
            ovo_top_up {
                is_required
                button_title
                error_message
                error_ticker
                is_hide_digital
            }
            phone_number_registered {
                is_required
                button_title
                error_message
                error_ticker
            }
          }
        }
        shipment {
          service_id
          service_duration
          service_name
          sp_id
          recommendation_service_id
          recommendation_sp_id
          is_free_shipping_selected
        }
      }
      promo {
        last_apply {
          code
          data {
            global_success
            success
            message {
              state
              color
              text
            }
            codes
            promo_code_id
            title_description
            discount_amount
            cashback_wallet_amount
            cashback_advocate_referral_amount
            cashback_voucher_description
            invoice_description
            is_coupon
            gateway_id
            is_tokopedia_gerai
            clashing_info_detail {
              clash_message
              clash_reason
              is_clashed_promos
              options {
                voucher_orders {
                  cart_id
                  code
                  shop_name
                  potential_benefit
                  promo_name
                  unique_id
                }
              }
            }
            tokopoints_detail {
              conversion_rate {
                rate
                points_coefficient
                external_currency_coefficient
              }
            }
            voucher_orders {
              code
              success
              cart_id
              unique_id
              order_id
              shop_id
              is_po
              duration
              warehouse_id
              address_id
              type
              cashback_wallet_amount
              discount_amount
              title_description
              invoice_description
              message {
               state
               color
               text
              }
              benefit_details {
                code
                type
                order_id
                unique_id
                discount_amount
                discount_details {
                  amount
                  data_type
                }
                cashback_amount
                cashback_details {
                  amount_idr
                  amount_points
                  benefit_type
                }
                promo_type {
                  is_exclusive_shipping
                  is_bebas_ongkir
                }
                benefit_product_details {
                  product_id
                  cashback_amount
                  cashback_amount_idr
                  discount_amount
                  is_bebas_ongkir
                }
              }
            }
            benefit_summary_info {
              final_benefit_text
              final_benefit_amount
              final_benefit_amount_str
              summaries {
                section_name
                section_description
                description
                type
                amount_str
                amount
                details {
                  section_name
                  description
                  type
                  amount_str
                  amount
                  points
                  points_str
                }
              }
            }
            tracking_details {
              product_id
              promo_codes_tracking
              promo_details_tracking
            }
            ticker_info {
              unique_id
              status_code
              message
            }
            additional_info {
              message_info {
                message
                detail
              }
              error_detail {
                message
              }
              empty_cart_info {
                image_url
                message
                detail
              }
              usage_summaries {
                description
                type
                amount_str
                amount
              }
              sp_ids
            }
          }
        }
        error_default {
          title
          description
        }
      }
      customer_data {
        id
        name
        email
        msisdn
      }
      payment_additional_data {
        merchant_code
        profile_code
        signature
        change_cc_link
        callback_url
      }
      error_ticker
      prompt {
        type
        title
        description
        image_url
        buttons {
          text
          link
          action
          color
        }
      }
      occ_revamp {
        enable
        total_profile
        change_template_text
      }
    }
  }
}"""
    }
}