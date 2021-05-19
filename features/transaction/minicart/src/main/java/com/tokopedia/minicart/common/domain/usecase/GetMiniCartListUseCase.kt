package com.tokopedia.minicart.common.domain.usecase

import com.google.gson.Gson
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
        val response = Gson().fromJson(MOCK_RESPONSE, MiniCartGqlResponse::class.java)
        return response.miniCart
/*
        val params = mapOf<String, String>()
        val request = GraphqlRequest(QUERY, MiniCartGqlResponse::class.java, params)
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<MiniCartGqlResponse>()

        if (response.miniCart.status == "OK") {
            return response.miniCart
        } else {
            throw ResponseErrorException(response.miniCart.errorMessage.joinToString(", "))
        }
*/
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

    val MOCK_RESPONSE = """
        {
          "mini_cart": {
            "error_message": [],
            "status": "OK",
            "data": {
              "errors": [],
              "empty_cart": {
                "image": "ecs7.tokopedia.com/................",
                "title": "Wah, keranjang belanjamu kosong",
                "description": "Daripada dianggurin,..............",
                "buttons": [
                  {
                    "id": 1,
                    "code": "STARTSHOPPING",
                    "message": "Mulai Belanja",
                    "color": "green"
                  }
                ]
              },
              "out_of_service": {
                "id": 1,
                "code": "MAINTANANCE",
                "image": "ecs7.tokopedia.com/................",
                "title": "Waktunya kerja bakti!",
                "description": "Kami sedang bersih-bersih........",
                "buttons": [
                  {
                    "id": 1,
                    "code": "RETRY",
                    "message": "Coba Lagi",
                    "color": "green"
                  },
                  {
                    "id": 2,
                    "code": "SETTING",
                    "message": "Ke Pengaturan",
                    "color": "white"
                  }
                ]
              },
              "max_quantity": 0,
              "max_char_note": 0,
              "messages": null,
              "header_title": "Belanjaanmu di TokoNOW!",
              "shopping_summary": {
                "total_wording": "Total Harga (x barang)",
                "total_value": 22650000,
                "discount_total_wording": "Total Diskon Barang",
                "discount_value": 650000,
                "payment_total_wording": "Total Bayar",
                "payment_total_value": 22000000,
                "promo_wording": "Hemat Pakai Promo",
                "promo_value": 50000,
                "seller_cashback_wording": "Cashback Penjual",
                "seller_cashback_value": 25000
              },
              "available_section": {
                "action": [
                  {
                    "id": 1,
                    "code": "NOTIFY",
                    "message": "Notify me when the stock is ready"
                  },
                  {
                    "id": 2,
                    "code": "DELETE",
                    "message": "Hapus barang"
                  },
                  {
                    "id": 4,
                    "code": "NOTES",
                    "message": "Catatan untuk penjual"
                  },
                  {
                    "id": 5,
                    "code": "SHOWLESS",
                    "message": "Tampilkan Lebih Sedikit"
                  },
                  {
                    "id": 6,
                    "code": "SHOWMORE",
                    "message": "Tampilkan Lebih Banyak"
                  }
                ],
                "available_group": [
                  {
                    "user_address_id": 0,
                    "shipment_information": {
                      "shop_location": "Jakarta Selatan",
                      "estimation": "Estimasi Tiba: 2 hari"
                    },
                    "shop": {
                      "shop_ticker": "",
                      "maximum_weight_wording": "",
                      "maximum_shipping_weight": 1000,
                      "shop_alert_message": "FOR URGENCY NEEDS",
                      "shop_id": 479986,
                      "user_id": 5512189,
                      "admin_ids": [],
                      "shop_name": "Zelda OS Testing 01",
                      "shop_image": "https://ecs7.tokopedia.net/img/default_v3-shopnophoto.png",
                      "shop_url": "https://staging.tokopedia.com/zos1",
                      "shop_status": 1,
                      "is_gold": 0,
                      "is_gold_badge": false,
                      "is_official": 0,
                      "is_free_returns": 0,
                      "gold_merchant": {
                        "is_gold": 0,
                        "is_gold_badge": false,
                        "gold_merchant_logo_url": ""
                      },
                      "shop_type_info": {
                        "shop_tier": 0,
                        "shop_grade": 0,
                        "badge": "",
                        "badge_svg": "",
                        "title": ""
                      },
                      "official_store": {
                        "is_official": 0,
                        "os_logo_url": ""
                      },
                      "address_id": 2270,
                      "postal_code": "12930",
                      "latitude": "-6.221180109754967",
                      "longitude": "106.81955066523437",
                      "district_id": 2270,
                      "district_name": "Setiabudi",
                      "origin": 2270,
                      "address_street": "Jalan Karet Sawah, Kecamatan Setiabudi, 12930",
                      "province_id": 13,
                      "city_id": 175,
                      "city_name": "Jakarta Selatan",
                      "province_name": "DKI Jakarta",
                      "country_name": "Indonesia",
                      "is_allow_manage": false,
                      "shop_domain": "zos1"
                    },
                    "cart_string": "479986-0-1886",
                    "cart_details": [
                      {
                        "cart_id": 33376874,
                        "product": {
                          "variant_description_detail": {
                            "variant_name": [
                              "blue",
                              "23"
                            ],
                            "variant_description": "Varian: blue,23"
                          },
                          "product_information": [
                            "Cashback 3%",
                            "Harga Turun"
                          ],
                          "product_id": 15262849,
                          "product_name": "Test vdoang",
                          "product_alias": "test-vdoang",
                          "parent_id": 0,
                          "variant": {
                            "parent_id": 0,
                            "is_parent": false,
                            "is_variant": false,
                            "children_id": null
                          },
                          "sku": "",
                          "campaign_id": 0,
                          "is_big_campaign": false,
                          "initial_price": 0,
                          "initial_price_fmt": "",
                          "product_price_fmt": "Rp10.000",
                          "product_price": 10000,
                          "product_original_price": 0,
                          "product_price_original_fmt": "",
                          "slash_price_label": "50%",
                          "is_slash_price": false,
                          "category_id": 636,
                          "category": "Elektronik / Tool & Kit",
                          "catalog_id": 0,
                          "wholesale_price": [],
                          "product_weight_fmt": "1gr",
                          "product_condition": 1,
                          "product_status": 3,
                          "product_url": "https://staging.tokopedia.com//test-vdoang",
                          "product_returnable": 0,
                          "is_freereturns": 0,
                          "free_returns": {
                            "free_returns_logo": "https://ecs7.tokopedia.net/img/icon-frs.png"
                          },
                          "is_preorder": 0,
                          "product_cashback": "",
                          "product_cashback_value": 0,
                          "product_min_order": 1,
                          "product_max_order": 10000,
                          "product_rating": 0,
                          "product_invenage_value": 2,
                          "product_switch_invenage": 1,
                          "product_warning_message": "sisa 3",
                          "product_alert_message": "FOR URGENCY NEEDS",
                          "currency_rate": 1,
                          "product_price_currency": 1,
                          "product_image": {
                            "image_src": "https://cdn-staging.tokopedia.com/img/cache/700/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                            "image_src_100_square": "https://cdn-staging.tokopedia.com/img/cache/100-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                            "image_src_200_square": "https://cdn-staging.tokopedia.com/img/cache/200-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                            "image_src_300": "https://cdn-staging.tokopedia.com/img/cache/300/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                            "image_src_square": "https://cdn-staging.tokopedia.com/img/cache/500-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919"
                          },
                          "product_all_images": "[{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919\",\"status\":2},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_417e3582-9671-46b4-af26-2c61d0051444_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_8854f7d1-ab75-4b9c-ba15-0a88b6ae27b1_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_345508de-2bfe-4bbd-90f6-7ce86f436f41_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_33a77436-9623-4ceb-93d6-7af16a035eb6_1919_1919\",\"status\":1}]",
                          "product_notes": "",
                          "product_quantity": 7,
                          "price_changes": {
                            "changes_state": 0,
                            "amount_difference": 0,
                            "original_amount": 0,
                            "description": ""
                          },
                          "product_weight": 1,
                          "product_weight_unit_code": 1,
                          "product_weight_unit_text": "gr",
                          "last_update_price": 1569480222,
                          "is_update_price": false,
                          "product_preorder": {},
                          "product_showcase": {
                            "name": "testing",
                            "id": 1405133
                          },
                          "product_finsurance": 1,
                          "product_shop_id": 480829,
                          "is_wishlisted": false,
                          "product_tracker_data": {
                            "attribution": "none/other",
                            "tracker_list_name": "none/other"
                          },
                          "is_ppp": false,
                          "is_cod": false,
                          "warehouse_id": 96,
                          "warehouses": {
                            "96": {
                              "warehouseID": 96,
                              "product_price": 10000,
                              "price_currency": 1,
                              "price_currency_name": "IDR",
                              "product_price_idr": 10000,
                              "last_update_price": {
                                "unix": 1569505422,
                                "yyyymmddhhmmss": ""
                              },
                              "product_switch_invenage": 1,
                              "product_invenage_value": 2
                            }
                          },
                          "is_parent": false,
                          "is_campaign_error": false,
                          "campaign_type_name": "",
                          "hide_gimmick": false,
                          "is_blacklisted": false,
                          "categories": [
                            {
                              "category_id": 60,
                              "category_name": "Elektronik"
                            },
                            {
                              "category_id": 636,
                              "category_name": "Tool & Kit"
                            }
                          ],
                          "free_shipping": {
                            "eligible": false,
                            "badge_url": ""
                          }
                        },
                        "errors": [
                          "Stok barang ini kosong."
                        ],
                        "messages": [],
                        "checkbox_state": true,
                        "selected_unavailable_action_link": "m.tokopedia.com"
                      }
                    ],
                    "total_cart_details_error": 1,
                    "total_cart_price": 2400000,
                    "errors": null,
                    "sort_key": 33388925,
                    "is_fulfillment_service": false,
                    "warehouse": {
                      "warehouse_id": 1886,
                      "partner_id": 0,
                      "shop_id": 479986,
                      "warehouse_name": "Shop location",
                      "district_id": 2270,
                      "district_name": "Setiabudi",
                      "city_id": 175,
                      "city_name": "Jakarta Selatan",
                      "province_id": 13,
                      "province_name": "DKI Jakarta",
                      "status": 1,
                      "postal_code": "12930",
                      "is_default": 1,
                      "latlon": "-6.221180109754967,106.81955066523437",
                      "latitude": "-6.221180109754967",
                      "longitude": "106.81955066523437",
                      "email": "",
                      "address_detail": "Jalan Karet Sawah, Kecamatan Setiabudi, 12930",
                      "country_name": "Indonesia",
                      "is_fulfillment": false,
                      "tkpd_preferred_logistic_spid": []
                    },
                    "checkbox_state": true
                  }
                ]
              },
              "unavailable_ticker": "Terdapat {{NumberOfProducts}} bermasalah dalam keranjang",
              "unavailable_section_action": [
                {
                  "id": 5,
                  "code": "SHOWLESS",
                  "message": "Tampilkan Lebih Sedikit"
                },
                {
                  "id": 6,
                  "code": "SHOWMORE",
                  "message": "Tampilkan Lebih Banyak"
                }
              ],
              "unavailable_section": [
                {
                  "title": "Toko Tutup",
                  "unavailable_description": "Tidak bisa dibeli di ios dan android",
                  "action": [
                    {
                      "id": 1,
                      "code": "NOTIFY",
                      "message": "Notify me when the stock is ready"
                    },
                    {
                      "id": 2,
                      "code": "DELETE",
                      "message": "Hapus barang"
                    },
                    {
                      "id": 3,
                      "code": "CHECKOUTBROWSER",
                      "message": "Checkout di browser"
                    }
                  ],
                  "selected_unavailable_action_id": 3,
                  "unavailable_group": [
                    {
                      "user_address_id": 0,
                      "shipment_information": {
                        "shop_location": "",
                        "estimation": "",
                        "is_free_shipping": false,
                        "free_shipping_extra": {
                          "eligible": false,
                          "badge_url": ""
                        }
                      },
                      "shop": {
                        "shop_ticker": "",
                        "maximum_weight_wording": "",
                        "maximum_shipping_weight": 1000,
                        "shop_id": 480829,
                        "user_id": 5510908,
                        "admin_ids": [],
                        "shop_name": "mattleeshoppe",
                        "shop_image": "https://ecs7.tokopedia.net/img/default_v3-shopnophoto.png",
                        "shop_url": "https://staging.tokopedia.com/mattleeshoppe",
                        "shop_status": 1,
                        "is_gold": 0,
                        "is_gold_badge": false,
                        "is_official": 0,
                        "is_free_returns": 0,
                        "gold_merchant": {
                          "is_gold": 0,
                          "is_gold_badge": false,
                          "gold_merchant_logo_url": ""
                        },
                        "shop_type_info": {
                          "shop_tier": 0,
                          "shop_grade": 0,
                          "badge": "",
                          "badge_svg": "",
                          "title": ""
                        },
                        "official_store": {
                          "is_official": 0,
                          "os_logo_url": ""
                        },
                        "address_id": 1,
                        "postal_code": "123456",
                        "latitude": "123456",
                        "longitude": "7890",
                        "district_id": 1,
                        "district_name": "Kaway XVI",
                        "origin": 1,
                        "address_street": "Alamat ini milik Dragon",
                        "province_id": 1,
                        "city_id": 1,
                        "city_name": "Kab. Aceh Barat",
                        "province_name": "D.I. Aceh",
                        "country_name": "Indonesia",
                        "is_allow_manage": false,
                        "shop_domain": "mattleeshoppe"
                      },
                      "cart_string": "480829-0-96",
                      "cart_details": [
                        {
                          "cart_id": 33376874,
                          "product": {
                            "variant_description_detail": {
                              "variant_name": [
                                "blue",
                                "23"
                              ],
                              "variant_description": "Varian: blue,23"
                            },
                            "product_information": [
                              "Cashback 3%",
                              "Harga Turun"
                            ],
                            "product_id": 15262849,
                            "product_name": "Test vdoang",
                            "product_alias": "test-vdoang",
                            "parent_id": 0,
                            "variant": {
                              "parent_id": 0,
                              "is_parent": false,
                              "is_variant": false,
                              "children_id": null
                            },
                            "sku": "",
                            "campaign_id": 0,
                            "is_big_campaign": false,
                            "initial_price": 0,
                            "initial_price_fmt": "",
                            "product_price_fmt": "Rp10.000",
                            "product_price": 10000,
                            "product_original_price": 0,
                            "product_price_original_fmt": "",
                            "slash_price_label": "50%",
                            "is_slash_price": false,
                            "category_id": 636,
                            "category": "Elektronik / Tool & Kit",
                            "catalog_id": 0,
                            "wholesale_price": [],
                            "product_weight_fmt": "1gr",
                            "product_condition": 1,
                            "product_status": 3,
                            "product_url": "https://staging.tokopedia.com//test-vdoang",
                            "product_returnable": 0,
                            "is_freereturns": 0,
                            "free_returns": {
                              "free_returns_logo": "https://ecs7.tokopedia.net/img/icon-frs.png"
                            },
                            "is_preorder": 0,
                            "product_cashback": "",
                            "product_cashback_value": 0,
                            "product_min_order": 1,
                            "product_max_order": 10000,
                            "product_rating": 0,
                            "product_invenage_value": 2,
                            "product_switch_invenage": 1,
                            "product_warning_message": "sisa 3",
                            "product_alert_message": "FOR URGENCY NEEDS",
                            "currency_rate": 1,
                            "product_price_currency": 1,
                            "product_image": {
                              "image_src": "https://cdn-staging.tokopedia.com/img/cache/700/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                              "image_src_100_square": "https://cdn-staging.tokopedia.com/img/cache/100-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                              "image_src_200_square": "https://cdn-staging.tokopedia.com/img/cache/200-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                              "image_src_300": "https://cdn-staging.tokopedia.com/img/cache/300/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                              "image_src_square": "https://cdn-staging.tokopedia.com/img/cache/500-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919"
                            },
                            "product_all_images": "[{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919\",\"status\":2},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_417e3582-9671-46b4-af26-2c61d0051444_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_8854f7d1-ab75-4b9c-ba15-0a88b6ae27b1_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_345508de-2bfe-4bbd-90f6-7ce86f436f41_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_33a77436-9623-4ceb-93d6-7af16a035eb6_1919_1919\",\"status\":1}]",
                            "product_notes": "",
                            "product_quantity": 7,
                            "price_changes": {
                              "changes_state": 0,
                              "amount_difference": 0,
                              "original_amount": 0,
                              "description": ""
                            },
                            "product_weight": 1,
                            "product_weight_unit_code": 1,
                            "product_weight_unit_text": "gr",
                            "last_update_price": 1569480222,
                            "is_update_price": false,
                            "product_preorder": {},
                            "product_showcase": {
                              "name": "testing",
                              "id": 1405133
                            },
                            "product_finsurance": 1,
                            "product_shop_id": 480829,
                            "is_wishlisted": false,
                            "product_tracker_data": {
                              "attribution": "none/other",
                              "tracker_list_name": "none/other"
                            },
                            "is_ppp": false,
                            "is_cod": false,
                            "warehouse_id": 96,
                            "warehouses": {
                              "96": {
                                "warehouseID": 96,
                                "product_price": 10000,
                                "price_currency": 1,
                                "price_currency_name": "IDR",
                                "product_price_idr": 10000,
                                "last_update_price": {
                                  "unix": 1569505422,
                                  "yyyymmddhhmmss": ""
                                },
                                "product_switch_invenage": 1,
                                "product_invenage_value": 2
                              }
                            },
                            "is_parent": false,
                            "is_campaign_error": false,
                            "campaign_type_name": "",
                            "hide_gimmick": false,
                            "is_blacklisted": false,
                            "categories": [
                              {
                                "category_id": 60,
                                "category_name": "Elektronik"
                              },
                              {
                                "category_id": 636,
                                "category_name": "Tool & Kit"
                              }
                            ],
                            "free_shipping": {
                              "eligible": false,
                              "badge_url": ""
                            }
                          },
                          "errors": [
                            "Stok barang ini kosong."
                          ],
                          "messages": [],
                          "checkbox_state": true,
                          "selected_unavailable_action_link": "m.tokopedia.com"
                        }
                      ],
                      "total_cart_details_error": 1,
                      "total_cart_price": 0,
                      "errors": null,
                      "sort_key": 33376874,
                      "is_fulfillment_service": false,
                      "warehouse": {
                        "warehouse_id": 96,
                        "partner_id": 0,
                        "shop_id": 480829,
                        "warehouse_name": "Shop Location",
                        "district_id": 1,
                        "district_name": "Kaway XVI",
                        "city_id": 1,
                        "city_name": "Kab. Aceh Barat",
                        "province_id": 1,
                        "province_name": "D.I. Aceh",
                        "status": 1,
                        "postal_code": "123456",
                        "is_default": 1,
                        "latlon": "123456,7890",
                        "latitude": "123456",
                        "longitude": "7890",
                        "email": "",
                        "address_detail": "Alamat ini milik Dragon",
                        "country_name": "Indonesia",
                        "is_fulfillment": false,
                        "tkpd_preferred_logistic_spid": []
                      },
                      "checkbox_state": true
                    }
                  ]
                }
              ],
              "total_product_price": 2403000,
              "total_product_count": 2,
              "total_product_error": 1,
              "tickers": [],
              "hashed_email": "fe51cd30c27c5c660de629bd1c58a1aa"
            }
          }
        }
    """.trimIndent()
}