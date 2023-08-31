package com.tokopedia.cart.domain.usecase

const val CART_REVAMP_V4_QUERY =
    """
        query cartRevampV4(${'$'}lang: String, ${'$'}selected_cart_id: String, ${'$'}additional_params: CartRevampAdditionalParams) {
          cart_revamp_v4(lang: ${'$'}lang, selected_cart_id: ${'$'}selected_cart_id, additional_params: ${'$'}additional_params) {
            status
            error_message
            data {
              coachmark {
                Plus {
                  is_shown
                  title
                  content
                }
              }
              errors
              popup_error_message
              pop_up_message
              placeholder_note
              localization_choose_address {
                address_id
                address_name
                address
                postal_code
                phone
                receiver_name
                status
                country
                province_id
                province_name
                city_id
                city_name
                district_id
                district_name
                address_2
                latitude
                longitude
                corner_id
                is_corner
                is_primary
                buyer_store_code
                type
                state
                state_detail
                tokonow {
                  is_modified
                  shop_id
                  warehouse_id
                  warehouses {
                    warehouse_id
                    service_type
                  }
                  service_type
                }
              }
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
                description
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
                add_ons {
                    wording
                    type
                }
              }
              promo_summary {
                title
                detail {
                  description
                  type
                  amount_str
                  amount
                  currency_details_str
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
                ErrorBOAffordability
              }
              fulfillment_message
              toko_cabang {
                message
                badge_url
              }
              available_section {
                action {
                  id
                  code
                  message
                }
                available_group {
                  group_type
                  ui_group_type
                  group_information {
                    name
                    badge_url
                    app_link
                    description
                    description_badge_url
                  }
                  group_shop_v2_cart {
                    cart_string_order
                    shop {
                      shop_ticker
                      maximum_weight_wording
                      maximum_shipping_weight
                      is_tokonow
                      shop_alert_message
                      shop_id
                      admin_ids
                      shop_name
                      shop_image
                      shop_url
                      shop_status
                      postal_code
                      latitude
                      longitude
                      district_name
                      district_id
                      origin
                      address_street
                      city_name
                      province_name
                      country_name
                      is_allow_manage
                      shop_domain
                      is_gold
                      is_official
                      shop_type_info {
                        shop_tier
                        shop_grade
                        badge
                        badge_svg
                        title
                      }
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
                      enabler_data {
                        label_name
                        show_label
                      }
                    }
                    cart_details {
                      bundle_detail {
                        bundle_description
                        bundle_group_id
                        bundle_id
                        bundle_max_order
                        bundle_min_order
                        bundle_name
                        bundle_original_price
                        bundle_original_price_fmt
                        bundle_price
                        bundle_price_fmt
                        bundle_qty
                        bundle_quota
                        bundle_status
                        bundle_type
                        edit_app_link
                        slash_price_label
                        bundle_icon_url
                        bundle_grayscale_icon_url
                      }
                      cart_detail_info {
                        cart_detail_type
                        bmgm {
                            offer_id
                            offer_name
                            offer_icon
                            offer_message
                            offer_landing_page_link
                            offer_json_data
                            total_discount
                            offer_status
                            tier_product {
                                tier_id
                                tier_name
                                tier_message
                                tier_discount_text
                                tier_discount_amount
                                price_before_benefit
                                price_after_benefit
                                list_product {
                                    product_id
                                    warehouse_id
                                    quantity
                                    price_before_benefit
                                    price_after_benefit
                                    cart_id
                                }
                            }
                        }
                      }
                      products {
                          add_ons_product {
                            data {
                                id
                                unique_id
                                status
                                type
                                price
                            }
                            widget {
                                wording
                                left_icon_url
                                right_icon_url
                            }
                        }
                        origin_warehouse_ids
                        checkbox_state
                        cart_id
                        product_information
                        product_information_with_icon {
                          text
                          icon_url
                        }
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
                        product_status
                        product_url
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
                        campaign_id
                        product_original_price
                        product_finsurance
                        is_wishlisted
                        is_ppp
                        is_cod
                        warehouse_id
                        is_parent
                        is_campaign_error
                        ethical_drug {
                          need_prescription
                          icon_url
                          text
                        }
                        free_shipping {
                          eligible
                          badge_url
                        }
                        free_shipping_extra {
                          eligible
                          badge_url
                        }
                        free_shipping_general {
                          bo_name
                          bo_type
                          badge_url
                        }
                        initial_price
                        initial_price_fmt
                        slash_price_label
                        product_warning_message
                        product_alert_message
                        variant_description_detail {
                          variant_name
                          variant_description
                        }
                        bundle_ids
                      }
                      errors
                      messages
                    }
                  }
                  add_on {
                    ticker_text
                    icon_url
                    add_on_ids
                  }
                  epharmacy_consultation {
                    ticker_text
                    icon_url
                  }
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
                    free_shipping_general {
                      bo_name
                      bo_type
                      badge_url
                    }
                    preorder {
                      is_preorder
                      duration
                    }
                    enable_bo_affordability
                    enable_shop_group_ticker_cart_aggregator
                  }
                  pinned {
                    is_pinned
                    coachmark_message
                  }
                  bo_metadata {
                    bo_type
                    bo_eligibilities {
                      key
                      value
                    }
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
                products_count
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
                    free_shipping_general {
                      bo_name
                      bo_type
                      badge_url
                    }
                    preorder {
                      is_preorder
                      duration
                    }
                  }
                  shop {
                    shop_alert_message
                    is_tokonow
                    shop_id
                    admin_ids
                    shop_name
                    shop_image
                    shop_url
                    shop_status
                    postal_code
                    latitude
                    longitude
                    district_name
                    origin
                    address_street
                    city_name
                    province_id
                    province_name
                    country_name
                    is_allow_manage
                    shop_domain
                    is_gold
                    is_official
                    shop_type_info {
                      shop_tier
                      shop_grade
                      badge
                      badge_svg
                      title
                    }
                    enabler_data {
                      label_name
                      show_label
                    }
                  }
                  promo_codes
                  cart_string
                  cart_details {
                    bundle_detail {
                      bundle_description
                      bundle_group_id
                      bundle_id
                      bundle_max_order
                      bundle_min_order
                      bundle_name
                      bundle_original_price
                      bundle_original_price_fmt
                      bundle_price
                      bundle_price_fmt
                      bundle_qty
                      bundle_quota
                      bundle_status
                      bundle_type
                      edit_app_link
                      slash_price_label
                      bundle_icon_url
                    }
                    products {
                      origin_warehouse_ids
                      checkbox_state
                      selected_unavailable_action_link
                      cart_id
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
                      product_status
                      product_url
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
                      campaign_id
                      product_original_price
                      product_finsurance
                      is_wishlisted
                      is_ppp
                      is_cod
                      warehouse_id
                      is_parent
                      is_campaign_error
                      free_shipping {
                        eligible
                        badge_url
                      }
                      free_shipping_extra {
                        eligible
                        badge_url
                      }
                      free_shipping_general {
                        bo_name
                        bo_type
                        badge_url
                      }
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
              total_product_price
              total_product_count
              total_product_error
              global_checkbox_state
              show_bundle_price
              onboarding_data {
                text
              }
              tickers {
                id
                title
                message
                page
              }
              promo {
                show_choose_promo_widget
                ticker {
                  enable
                  text
                  icon_url
                }
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
                      cart_string_group
                      code
                      success
                      cart_id
                      unique_id
                      shipping_id
                      sp_id
                      shipping_subsidy
                      shipping_price
                      benefit_class
                      bo_campaign_id
                      eta_txt
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
                        currency_details_str
                      }
                      sp_ids
                      poml_auto_applied
                    }
                  }
                  code
                }
                error_default {
                  title
                  description
                }
              }
            }
          }
        }
"""
