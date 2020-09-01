package com.tokopedia.cart.domain.usecase

fun getQueryCartRevamp(): String {
    return """
        query cart_revamp(${'$'}lang: String, ${'$'}selected_cart_id: String) {
          status
          cart_revamp(lang:${'$'}lang, selected_cart_id: ${'$'}selected_cart_id) {
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
                  code
                  message
                  color
                }
              }
              out_of_service {
                id
                code
                image
                title
                buttons {
                  id
                  code
                  message
                  color
                }
              }
              shopping_summary {
                total_wording
                total_value
                discount_total_wording
                discount_value
                payment_total_wording
                promo_wording
                promo_value
                seller_cashback_wording
                seller_cashback_value
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
              fulfillment_message
              available_section {
                action {
                  id
                  code
                  message
                }
                available_group {
                  user_address_id
                  shipment_information {
                    shop_location
                    estimation
                    free_shipping {
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
                    admin_ids
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
                    province_id
                    province_name
                    country_name
                    is_allow_manage
                    shop_domain
                    shop_shipments {
                      ship_id
                      ship_name
                      ship_code
                      ship_logo
                      ship_prods {
                        ship_prod_id
                        ship_prod_name
                        ship_group_id
                        ship_group_name
                        minimum_weight
                        additional_fee
                      }
                      is_dropship_enabled
                    }
                  }
                  promo_codes
                  cart_string
                  cart_details {
                    cart_id
                    product {
                      product_information
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
                      product_returnable
                      is_freereturns
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
                      price_changes {
                        changes_state
                        amount_difference
                        original_amount
                        description
                      }
                      product_price_currency
                      product_image {
                        image_src
                        image_src_100_square
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
                      product_preorder {
                        duration_day
                        duration_text
                        duration_unit_code
                        duration_unit_text
                        duration_value
                      }
                      product_showcase {
                        name
                        id
                      }
                      product_alias
                      sku
                      campaign_id
                      product_original_price
                      product_price_original_fmt
                      is_slash_price
                      free_returns {
                        free_returns_logo
                      }
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
                      variant_description_detail {
                        variant_name
                        variant_description
                      }
                    }
                    errors
                    messages
                    checkbox_state
                    similar_product_url
                    similar_product {
                      text
                      url
                    }
                    nicotine_lite_message {
                      text
                      url
                    }
                  }
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
              unavailable_ticker
              unavailable_section_action {
                id
                code
                message
              }
              unavailable_section {
                title
                selected_unavailable_action_id
                unavailable_description
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
                    preorder {
                      is_preorder
                      duration
                    }
                  }
                  shop {
                    shop_alert_message
                    shop_id
                    user_id
                    admin_ids
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
                    province_id
                    province_name
                    country_name
                    is_allow_manage
                    shop_domain
                    shop_shipments {
                      ship_id
                      ship_name
                      ship_code
                      ship_logo
                      ship_prods {
                        ship_prod_id
                        ship_prod_name
                        ship_group_id
                        ship_group_name
                        minimum_weight
                        additional_fee
                      }
                      is_dropship_enabled
                    }
                  }
                  promo_codes
                  cart_string
                  cart_details {
                    cart_id
                    selected_unavailable_action_link
                    product {
                      product_information
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
                      product_returnable
                      is_freereturns
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
                      price_changes {
                        changes_state
                        amount_difference
                        original_amount
                        description
                      }
                      product_price_currency
                      product_image {
                        image_src
                        image_src_100_square
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
                      product_preorder {
                        duration_day
                        duration_text
                        duration_unit_code
                        duration_unit_text
                        duration_value
                      }
                      product_showcase {
                        name
                        id
                      }
                      product_alias
                      sku
                      campaign_id
                      product_original_price
                      product_price_original_fmt
                      is_slash_price
                      free_returns {
                        free_returns_logo
                      }
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
                      variant_description_detail {
                        variant_name
                        variant_description
                      }
                    }
                    errors
                    messages
                    checkbox_state
                    similar_product_url
                    similar_product {
                      text
                      url
                    }
                    nicotine_lite_message {
                      text
                      url
                    }
                  }
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
              total_product_price,
              total_product_count,
              total_product_error,
              global_coupon_attr {
                description, quantity_label
              },
              global_checkbox_state,
              tickers {
                id
                message
                page
              },
              hashed_email, promo {
                last_apply {
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
                        discount_details{
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
                          is_bebas_ongkir
                          is_exclusive_shipping
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
                  }
                  code
                }
                error_default {
                  title
                  description
                }
              }, customer_data {
                name
                email
                phone
              }
            }
          }
        }
    """.trimIndent()
}