package com.tokopedia.checkout.domain.usecase

const val SHIPMENT_ADDRESS_FORM_V3_QUERY =
        """
        query shipmentAddressFormV3(${'$'}params: ShipmentAddressFormParams) {
          shipment_address_form_v3(params: ${'$'}params) {
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
              error_code
              kero_token
              kero_discom_token
              kero_unix_time
              is_robinhood
              is_hide_courier_name
              is_show_onboarding
              is_ineligible_promo_dialog_enabled
              disabled_features
              donation_checkbox_status
              image_upload {
                show_image_upload
                text
                left_icon_url
                right_icon_url
                checkout_id
                front_end_validation
              }
              open_prerequisite_site
              eligible_new_shipping_experience
              pop_up_message
              pop_up {
                title
                description
                button {
                  text
                }
              }
              error_ticker
              group_address {
                errors
                user_address {
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
                group_shop {
                  add_ons {
                    status
                    add_on_data {
                      add_on_id
                      add_on_qty
                      add_on_price
                      add_on_metadata {
                        add_on_note {
                            is_custom_note
                            to
                            from
                            notes
                        }
                      }
                    }
                    add_on_button {
                      title
                      description
                      left_icon_url
                      right_icon_url
                      action
                    }
                    add_on_bottomsheet {
                      header_title
                      description
                      products {
                        product_name
                        product_image_url
                      }
                      ticker {
                        text
                      }
                    }
                  }
                  errors
                  errors_unblocking
                  shipping_id
                  sp_id
                  scheduled_delivery {
                    timeslot_id
                    schedule_date
                    validation_metadata
                  }
                  rates_validation_flow
                  bo_code
                  is_insurance
                  is_fulfillment_service
                  toko_cabang {
                    message
                    badge_url
                  }
                  cart_string
                  has_promo_list
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
                  is_disable_change_courier
                  auto_courier_selection
                  courier_selection_error {
                    title
                    description
                  }
                  bo_metadata {
                    bo_type
                    bo_eligibilities {
                      key
                      value
                    }
                  }
                  save_state_flag
                  promo_codes
                  vehicle_leasing {
                    booking_fee
                    is_leasing_product
                  }
                  shop {
                    shop_id
                    shop_name
                    shop_image
                    shop_url
                    shop_status
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
                    shop_alert_message
                    is_tokonow
                    is_gold
                    is_official
                    shop_ticker
                    shop_ticker_title
                    shop_type_info {
                      shop_tier
                      shop_grade
                      badge
                      badge_svg
                      title
                    }
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
                      additional_fee
                      minimum_weight
                    }
                  }
                  dropshipper {
                    name
                    telp_no
                  }
                  cart_details {
                    errors
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
                      slash_price_label
                      bundle_icon_url
                    }
                    products {
                      add_ons {
                        status
                        add_on_data {
                            add_on_id
                            add_on_qty
                            add_on_price
                            add_on_metadata {
                                add_on_note {
                                    is_custom_note
                                    to
                                    from
                                    notes
                                }
                            }
                          }
                          add_on_button {
                            title
                            description
                            left_icon_url
                            right_icon_url
                            action
                          }
                          add_on_bottomsheet {
                            header_title
                            description
                            products {
                               product_name
                               product_image_url
                            }
                            ticker {
                               text
                            }
                          }
                      }
                      errors
                      cart_id
                      product_id
                      product_name
                      product_price_fmt
                      product_price
                      product_original_price
                      product_wholesale_price
                      product_wholesale_price_fmt
                      product_weight_fmt
                      product_weight
                      product_weight_actual
                      product_condition
                      product_url
                      product_returnable
                      product_is_free_returns
                      product_is_preorder
                      product_cashback
                      product_min_order
                      product_invenage_value
                      product_switch_invenage
                      product_price_currency
                      product_image_src_200_square
                      product_notes
                      product_quantity
                      product_menu_id
                      product_finsurance
                      product_fcancel_partial
                      product_cat_id
                      product_catalog_id
                      product_category
                      purchase_protection_plan_data {
                        protection_available
                        protection_type_id
                        protection_price_per_product
                        protection_price
                        protection_title
                        protection_subtitle
                        protection_link_text
                        protection_link_url
                        protection_opt_in
                        protection_checkbox_disabled
                      }
                      product_variants {
                        parent_id
                      }
                      product_tracker_data {
                        attribution
                        tracker_list_name
                      }
                      product_preorder {
                        duration_text
                        duration_day
                      }
                      trade_in_info {
                        is_valid_trade_in
                        new_device_price
                        new_device_price_fmt
                        old_device_price
                        old_device_price_fmt
                        drop_off_enable
                        device_model
                        diagnostic_id
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
                      product_ticker {
                        show_ticker
                        message
                      }
                      variant_description_detail {
                        variant_name
                        variant_description
                      }
                      product_alert_message
                      product_information
                      campaign_id
                      ethical_drug {
                        need_prescription
                        icon_url
                        text
                      }
                    }
                  }
                  warehouse {
                    warehouse_id
                    city_name
                  }
                }
              }
              donation {
                Title
                Nominal
                Description
              }
              cod {
                is_cod
                counter_cod
              }
              message {
                message_info
                message_link
                message_logo
              }
              egold_attributes {
                eligible
                is_tiering
                is_opt_in
                range {
                  min
                  max
                }
                message {
                  title_text
                  sub_text
                  ticker_text
                  tooltip_text
                }
                tier_data {
                  minimum_total_amount
                  minimum_amount
                  maximum_amount
                  basis_amount
                }
                hyperlink_text {
                  text
                  url
                  is_show
                }
              }
              tickers {
                id
                title
                message
                page
              }
              campaign_timer {
                description
                show_timer
                expired_timer_message {
                  Button
                  description
                  title
                }
                timer_detail {
                  deduct_time
                  expire_duration
                  expired_time
                  server_time
                }
              }
              addresses {
                active
                disable_tabs
                data {
                  key
                  value {
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
                  }
                }
              }
              disabled_features_detail {
                disabled_multi_address_message
              }
              promo {
                error_default {
                  title
                  description
                }
                last_apply {
                  code
                  data {
                    codes
                    promo_code_id
                    cashback_advocate_referral_amount
                    cashback_wallet_amount
                    discount_amount
                    discount_amount
                    title_description
                    is_tokopedia_gerai
                    global_success
                    gateway_id
                    is_coupon
                    success
                    invoice_description
                    voucher_orders {
                      cashback_wallet_amount
                      code
                      unique_id
                      discount_amount
                      address_id
                      title_description
                      is_po
                      type
                      cart_id
                      shop_id
                      success
                      invoice_description
                      message {
                        color
                        state
                        text
                      }
                      benefit_details {
                        code
                        unique_id
                        cashback_amount
                        discount_amount
                        type
                        order_id
                        benefit_product_details {
                          cashback_amount
                          discount_amount
                          product_id
                          is_bebas_ongkir
                        }
                        promo_type {
                          is_exclusive_shipping
                          is_bebas_ongkir
                        }
                      }
                    }
                    clashing_info_detail {
                      is_clashed_promos
                      clash_reason
                      clash_message
                    }
                    tracking_details {
                      product_id
                      promo_codes_tracking
                      promo_details_tracking
                    }
                    message {
                      color
                      state
                      text
                    }
                    additional_info {
                      sp_ids
                      usage_summaries {
                        description
                        type
                        amount_str
                        amount
                        currency_details_str
                      }
                      message_info {
                        message
                        detail
                      }
                      error_detail {
                        message
                      }
                      empty_cart_info {
                        image_url
                        detail
                        message
                      }
                      promo_sp_ids {
                        unique_id
                        mvc_shipping_benefits {
                          benefit_amount
                          sp_id
                        }
                      }
                      poml_auto_applied
                      bebas_ongkir_info {
                        is_bo_unstack_enabled
                      }
                    }
                    benefit_summary_info {
                      final_benefit_amount_str
                      final_benefit_amount
                      final_benefit_text
                      summaries {
                        amount
                        section_name
                        description
                        section_description
                        type
                        amount_str
                        details {
                          amount
                          section_name
                          description
                          type
                          amount_str
                          points
                          points_str
                        }
                      }
                    }
                    ticker_info {
                      unique_id
                      status_code
                      message
                    }
                  }
                }
              }
              cross_sell {
                id
                checkbox_disabled
                is_checked
                price
                info {
                  title
                  sub_title
                  tooltip_text
                  icon_url
                }
                order_summary {
                  title
                  price_wording
                }
                bottom_sheet {
                  title
                  sub_title
                }
                additional_vertical_id
                transaction_type
              }
              add_on_wording {
                packaging_and_greeting_card
                only_greeting_card
                invoice_not_sent_to_recipient
              }
              upsell {
                is_show
                title
                description
                app_link
                image
              }
              cart_data
              upsell_v2 {
                is_show
                is_selected
                price
                price_fmt
                duration
                description
                summary_info
                image
                app_link
                button {
                  text
                }
                id
                additional_vertical_id
                transaction_type
              }
            }
          }
        }"""
