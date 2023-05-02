package com.tokopedia.logisticcart.shipping.usecase

internal const val QUERY_RATESV3 = "ratesV3"
internal const val QUERY_RATESV3_API = "ratesV3Api"

internal fun ratesQuery(queryName: String) = """
query $queryName(${"$"}param : OngkirRatesV3Input!) {
  $queryName(input: ${"$"}param) {
    ratesv3 {
      id
      rates_id
      type
      error {
        error_id
        error_message
      }
      services {
        service_name
        service_id
        service_order
        status
        is_promo
        ui_rates_hidden
        selected_shipper_product_id
        range_price {
          min_price
          max_price
        }
        etd {
          min_etd
          max_etd
        }
        texts {
          text_range_price
          text_etd
          text_notes
          text_service_desc
          text_eta_summarize
          error_code
        }
        features {
          dynamic_price {
            text_label
          }
        }
        order_priority {
          is_now
          inactive_message
          price
          formatted_price
          static_messages {
            duration_message
            checkbox_message
            warningbox_message
            fee_message
            pdp_message
          }
        }
        error {
          error_id
          error_message
        }
        status
        cod {
          is_cod
          cod_text
        }
        mvc {
          is_mvc
          mvc_title
          mvc_logo
          mvc_error_message
        }
        products {
          shipper_name
          shipper_id
          shipper_product_id
          shipper_product_name
          shipper_product_desc
          is_show_map
          shipper_weight
          status
          recommend
          checksum
          ut
          promo_code
          ui_rates_hidden
          price {
            price
            formatted_price
          }
          etd {
            min_etd
            max_etd
          }
          texts {
            text_price
            text_etd
          }
          insurance {
            insurance_price
            insurance_type
            insurance_type_info
            insurance_used_type
            insurance_used_info
            insurance_used_default
          }
          error {
            error_id
            error_message
          }
          cod {
            is_cod_available
            cod_price
            cod_text
            formatted_price
            tnc_text
            tnc_link
          }
          features {
            ontime_delivery_guarantee {
              available
              value
              text_label
              text_detail
              url_detail
              icon_url
              url_text
            }
            mvc {
              is_mvc
              mvc_logo
              mvc_error_message
            }
            dynamic_price {
              text_label
            }
          }
          eta {
            text_eta
            error_code
          }
        }
      }
      promo_stacking {
        is_promo
        promo_code
        title
        shipper_id
        shipper_product_id
        shipper_name
        shipper_desc
        promo_detail
        benefit_desc
        point_change
        user_point
        promo_tnc_html
        shipper_disable_text
        service_id
        is_applied
        image_url
        discounted_rate
        shipping_rate
        benefit_amount
        disabled
        hide_shipper_name
        cod {
          is_cod_available
          cod_text
          cod_price
          formatted_price
          tnc_text
          tnc_link
        }
        eta {
          text_eta
          error_code
        }
        is_bebas_ongkir_extra
        texts {
          bottom_sheet
          chosen_courier
          ticker_courier
          bottom_sheet_description
        }
        free_shipping_metadata {
          sent_shipper_partner
          benefit_class
          shipping_subsidy
          additional_data
        }
      }
      promo_stackings {
        is_promo
        promo_code
        title
        shipper_id
        shipper_product_id
        shipper_name
        shipper_desc
        promo_detail
        benefit_desc
        point_change
        user_point
        promo_tnc_html
        shipper_disable_text
        service_id
        is_applied
        image_url
        discounted_rate
        shipping_rate
        benefit_amount
        disabled
        hide_shipper_name
        cod {
          is_cod_available
          cod_text
          cod_price
          formatted_price
          tnc_text
          tnc_link
        }
        eta {
          text_eta
          error_code
        }
        is_bebas_ongkir_extra
        texts {
          bottom_sheet
          chosen_courier
          ticker_courier
          bottom_sheet_description
          title_promo_message
        }
        free_shipping_metadata {
          sent_shipper_partner
          benefit_class
          shipping_subsidy
          additional_data
        }
      }
      pre_order {
        header
        label
        display
      }
      info {
        blackbox_info {
          text_info
        }
      }
      error {
        error_id
        error_message
      }
    }
  }
}
""".trimIndent()

internal fun ratesQuery() = """
    query ratesV3(${"$"}param : OngkirRatesV3Input!, ${"$"}metadata: Metadata) {
      ratesV3(input: ${"$"}param, metadata: ${"$"}metadata) {
        ratesv3 {
          id
          rates_id
          type
          error {
            error_id
            error_message
          }
          services {
            service_name
            service_id
            service_order
            status
            is_promo
            ui_rates_hidden
            selected_shipper_product_id
            range_price {
              min_price
              max_price
            }
            etd {
              min_etd
              max_etd
            }
            texts {
              text_range_price
              text_etd
              text_notes
              text_service_desc
              text_eta_summarize
              error_code
            }
            features {
              dynamic_price {
                text_label
              }
            }
            order_priority {
              is_now
              inactive_message
              price
              formatted_price
              static_messages {
                duration_message
                checkbox_message
                warningbox_message
                fee_message
                pdp_message
              }
            }
            error {
              error_id
              error_message
            }
            status
            cod {
              is_cod
              cod_text
            }
            mvc {
              is_mvc
              mvc_title
              mvc_logo
              mvc_error_message
            }
            products {
              shipper_name
              shipper_id
              shipper_product_id
              shipper_product_name
              shipper_product_desc
              is_show_map
              shipper_weight
              status
              recommend
              checksum
              ut
              promo_code
              ui_rates_hidden
              price {
                price
                formatted_price
              }
              etd {
                min_etd
                max_etd
              }
              texts {
                text_price
                text_etd
              }
              insurance {
                insurance_price
                insurance_type
                insurance_type_info
                insurance_used_type
                insurance_used_info
                insurance_used_default
              }
              error {
                error_id
                error_message
              }
              cod {
                is_cod_available
                cod_price
                cod_text
                formatted_price
                tnc_text
                tnc_link
              }
              features {
                ontime_delivery_guarantee {
                  available
                  value
                  text_label
                  text_detail
                  url_detail
                  icon_url
                  url_text
                }
                mvc {
                  is_mvc
                  mvc_logo
                  mvc_error_message
                }
                dynamic_price {
                  text_label
                }
              }
              eta {
                text_eta
                error_code
              }
            }
          }
          promo_stacking {
            is_promo
            promo_code
            title
            shipper_id
            shipper_product_id
            shipper_name
            shipper_desc
            promo_detail
            benefit_desc
            point_change
            user_point
            promo_tnc_html
            shipper_disable_text
            service_id
            is_applied
            image_url
            discounted_rate
            shipping_rate
            benefit_amount
            disabled
            hide_shipper_name
            cod {
              is_cod_available
              cod_text
              cod_price
              formatted_price
              tnc_text
              tnc_link
            }
            eta {
              text_eta
              error_code
            }
            is_bebas_ongkir_extra
            texts {
              bottom_sheet
              chosen_courier
              ticker_courier
              bottom_sheet_description
            }
            free_shipping_metadata {
              sent_shipper_partner
              benefit_class
              shipping_subsidy
              additional_data
            }
          }
          promo_stackings {
            is_promo
            promo_code
            title
            shipper_id
            shipper_product_id
            shipper_name
            shipper_desc
            promo_detail
            benefit_desc
            point_change
            user_point
            promo_tnc_html
            shipper_disable_text
            service_id
            is_applied
            image_url
            discounted_rate
            shipping_rate
            benefit_amount
            disabled
            hide_shipper_name
            cod {
              is_cod_available
              cod_text
              cod_price
              formatted_price
              tnc_text
              tnc_link
            }
            eta {
              text_eta
              error_code
            }
            is_bebas_ongkir_extra
            texts {
              bottom_sheet
              chosen_courier
              ticker_courier
              bottom_sheet_description
              promo_message
              title_promo_message
            }
            free_shipping_metadata {
              sent_shipper_partner
              benefit_class
              shipping_subsidy
              additional_data
            }
            bo_campaign_id
          }
          pre_order {
            header
            label
            display
          }
          info {
            blackbox_info {
              text_info
            }
          }
          error {
            error_id
            error_message
          }
        }
      }
    }
""".trimIndent()

internal fun scheduleDeliveryRatesQuery() = """
    query ongkirGetScheduledDeliveryRates(${"$"}param: OngkirGetScheduledDeliveryRatesInput!) {
      ongkirGetScheduledDeliveryRates(input: ${"$"}param) {
        data {
          rates_id
          available
          hidden
          recommend
          delivery_type
          title
          text
          notice {
            title
            text
          }
          error {
            error_id
            error_message
          }
          delivery_services {
            title
            title_label
            id
            available
            hidden
            error {
              error_id
              error_message
            }
            delivery_products {
              title
              text
              text_eta
              promo_text
              timeslot_id
              available
              hidden
              recommend
              service_id
              service_name
              shipper_id
              shipper_name
              shipper_product_id
              shipper_product_name
              final_price
              real_price
              text_final_price
              text_real_price
              checksum
              insurance {
                insurance_type
                insurance_price
                insurance_type_info
                insurance_used_type
                insurance_used_info
                insurance_used_default
              }
              features {
                ontime_delivery_guarantee {
                  available
                  value
                  text_label
                  text_detail
                  icon_url
                  url_detail
                }
              }
              ut
              promo_stacking {
                promo_code
                promo_chargeable
                benefit_class
                is_bebas_ongkir_extra
                benefit_amount
                bo_campaign_id
                disabled
                free_shipping_metadata {
                  sent_shipper_partner
                  benefit_class
                  shipping_subsidy
                  additional_data
                }
              }
              validation_metadata
            }
          }
        }
      }
    }
""".trimIndent()

internal const val DEFAULT_ERROR_MESSAGE = "Terjadi kesalahan. Ulangi beberapa saat lagi"
