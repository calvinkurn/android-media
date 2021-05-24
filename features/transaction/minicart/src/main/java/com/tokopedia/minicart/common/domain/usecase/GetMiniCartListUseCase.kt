package com.tokopedia.minicart.common.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.util.ChosenAddressRequestHelper
import com.tokopedia.localizationchooseaddress.util.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetMiniCartListUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                 private val chosenAddressRequestHelper: ChosenAddressRequestHelper) : UseCase<MiniCartData>() {

    private var params: Map<String, Any>? = null

    fun setParams(shopIds: List<String>) {
        params = mapOf(
//                "dummy" to 1,
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
//        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<MiniCartGqlResponse>()

        val response = Gson().fromJson(MOCK_RESPONSE, MiniCartGqlResponse::class.java)
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

    val MOCK_RESPONSE = """
        {
          "status": "OK",
          "mini_cart": {
            "error_message": [],
            "status": "OK",
            "data": {
              "errors": [],
              "empty_cart": {
                "title": "Wah, keranjang belanjamu kosong",
                "image": "https://ecs7.tokopedia.net/assets-tokopedia-lite/v2/zeus/kratos/103cf4bc.jpg",
                "description": "Daripada dianggurin, mending isi dengan barang-barang impianmu. Yuk, cek sekarang!",
                "buttons": [
                  {
                    "id": 1
                  }
                ]
              },
              "out_of_service": {
                "id": 0,
                "code": "",
                "image": "",
                "title": "",
                "description": "",
                "buttons": []
              },
              "total_product_count" : 1,
              "total_product_price" : 1000000,
              "max_quantity": 30000,
              "max_char_note": 144,
              "messages": {
                "ErrorFieldBetween": "Jumlah harus diisi antara 1 - {{value}}",
                "ErrorFieldMaxChar": "Catatan terlalu panjang, maks. {{value}} karakter.",
                "ErrorFieldRequired": "Jumlah harus diisi",
                "ErrorProductAvailableStock": "Stok tersedia:{{value}}",
                "ErrorProductAvailableStockDetail": "Harap kurangi jumlah barang",
                "ErrorProductMaxQuantity": "Maks. beli {{value}} barang",
                "ErrorProductMinQuantity": "Min. beli {{value}} barang"
              },
              "header_title": "Belanjaanmu di TokoNOW!",
              "shopping_summary": {
                "total_wording": "Total Harga (2 Barang)",
                "total_value": 200000,
                "discount_total_wording": "Total Diskon Barang",
                "discount_value": 0,
                "payment_total_wording": "Total Bayar",
                "payment_total_value": 200000
              },
              "unavailable_ticker": "",
              "unavailable_section_action": [
                {
                  "id": 7,
                  "code": "SHOWLESS",
                  "message": "Tampilkan lebih sedikit"
                },
                {
                  "id": 8,
                  "code": "SHOWMORE",
                  "message": "Tampilkan semua"
                }
              ],
              "unavailable_section": [
                {
                  "title": "Stok kosong",
                  "unavailable_description": "",
                  "selected_unavailable_action_id": 5,
                  "action": [
                    {
                      "id": 1,
                      "code": "WISHLIST",
                      "message": "Pindahkan ke Wishlist"
                    },
                    {
                      "id": 2,
                      "code": "DELETE",
                      "message": ""
                    },
                    {
                      "id": 5,
                      "code": "SIMILARPRODUCT",
                      "message": "Lihat Barang Serupa"
                    },
                    {
                      "id": 10,
                      "code": "WISHLISTED",
                      "message": "Sudah ada di wishlist"
                    }
                  ],
                  "unavailable_group": [
                    {
                      "user_address_id": 0,
                      "shipment_information": {
                        "shop_location": "",
                        "estimation": "",
                        "free_shipping": {
                          "eligible": false,
                          "badge_url": ""
                        },
                        "free_shipping_extra": {
                          "eligible": false,
                          "badge_url": ""
                        },
                        "preorder": {
                          "is_preorder": false,
                          "duration": ""
                        }
                      },
                      "shop": {
                        "shop_alert_message": "",
                        "shop_id": 2007325,
                        "user_id": 17605728,
                        "shop_name": "AWgadget",
                        "shop_image": "https://images.tokopedia.net/img/cache/215-square/shops-1/2019/10/4/2007325/2007325_80ab4370-e8f3-46d9-bd74-4b08ff3e0a59.jpg",
                        "shop_url": "https://www.tokopedia.com/awgadgetshop",
                        "shop_status": 1,
                        "is_gold": 1,
                        "is_gold_badge": false,
                        "is_official": 0,
                        "is_free_returns": 0,
                        "address_id": 2254,
                        "postal_code": "11440",
                        "latitude": "-6.1662209",
                        "longitude": "106.7995428",
                        "district_id": 2254,
                        "district_name": "",
                        "origin": 0,
                        "address_street": "",
                        "province_id": 0,
                        "city_id": 174,
                        "city_name": "Jakarta Barat",
                        "province_name": "",
                        "country_name": "",
                        "is_allow_manage": false,
                        "shop_domain": "awgadgetshop",
                        "shop_ticker": "",
                        "maximum_weight_wording": "",
                        "maximum_shipping_weight": 0
                      },
                      "promo_codes": [],
                      "cart_string": "2007325-0-1603251",
                      "total_cart_details_error": 0,
                      "total_cart_price": 0,
                      "errors": [],
                      "sort_key": 2693179591,
                      "is_fulfillment_service": false,
                      "warehouse": {
                        "warehouse_id": 1603251,
                        "partner_id": 0,
                        "shop_id": 2007325,
                        "warehouse_name": "Shop Location",
                        "district_id": 0,
                        "district_name": "",
                        "city_id": 0,
                        "city_name": "",
                        "province_id": 0,
                        "province_name": "",
                        "status": 0,
                        "postal_code": "",
                        "is_default": 0,
                        "latlon": "",
                        "latitude": "",
                        "longitude": "",
                        "email": "",
                        "address_detail": "",
                        "country_name": "",
                        "is_fulfillment": false
                      },
                      "checkbox_state": false
                    },
                    {
                      "user_address_id": 0,
                      "shipment_information": {
                        "shop_location": "",
                        "estimation": "",
                        "free_shipping": {
                          "eligible": false,
                          "badge_url": ""
                        },
                        "free_shipping_extra": {
                          "eligible": false,
                          "badge_url": ""
                        },
                        "preorder": {
                          "is_preorder": false,
                          "duration": ""
                        }
                      },
                      "shop": {
                        "shop_alert_message": "",
                        "shop_id": 11130255,
                        "user_id": 68866983,
                        "shop_name": "kingtel80 original100%",
                        "shop_image": "https://images.tokopedia.net/img/cache/215-square/shops-1/2021/5/10/11130255/11130255_417db489-913e-42ba-a8a5-ea39e7b00a89.jpg",
                        "shop_url": "https://www.tokopedia.com/kingtel80",
                        "shop_status": 1,
                        "is_gold": 1,
                        "is_gold_badge": false,
                        "is_official": 0,
                        "is_free_returns": 0,
                        "address_id": 2232,
                        "postal_code": "16431",
                        "latitude": "-6.3924909",
                        "longitude": "106.8235288",
                        "district_id": 2232,
                        "district_name": "",
                        "origin": 0,
                        "address_street": "",
                        "province_id": 0,
                        "city_id": 171,
                        "city_name": "Kota Depok",
                        "province_name": "",
                        "country_name": "",
                        "is_allow_manage": false,
                        "shop_domain": "kingtel80",
                        "shop_ticker": "",
                        "maximum_weight_wording": "Oops, belanjaan TokoNOW! kamu terlalu berat {{weight}}kg untuk sekali pengiriman. Coba pilih lagi ya!",
                        "maximum_shipping_weight": 3000
                      },
                      "promo_codes": [],
                      "cart_string": "11130255-0-11131577",
                      "total_cart_details_error": 0,
                      "total_cart_price": 0,
                      "errors": [],
                      "sort_key": 2693177964,
                      "is_fulfillment_service": false,
                      "warehouse": {
                        "warehouse_id": 11131577,
                        "partner_id": 0,
                        "shop_id": 11130255,
                        "warehouse_name": "Shop Location",
                        "district_id": 0,
                        "district_name": "",
                        "city_id": 0,
                        "city_name": "",
                        "province_id": 0,
                        "province_name": "",
                        "status": 0,
                        "postal_code": "",
                        "is_default": 0,
                        "latlon": "",
                        "latitude": "",
                        "longitude": "",
                        "email": "",
                        "address_detail": "",
                        "country_name": "",
                        "is_fulfillment": false
                      },
                      "checkbox_state": false
                    }
                  ]
                }
              ],
              "available_section": {
                "action": [
                  {
                    "id": 2,
                    "code": "DELETE",
                    "message": ""
                  },
                  {
                    "id": 3,
                    "code": "NOTES",
                    "message": "Tulis catatan untuk barang ini"
                  },
                  {
                    "id": 4,
                    "code": "VALIDATENOTES",
                    "message": "Maks. 144 character"
                  }
                ],
                "available_group": [
                  {
                    "shop": {
                      "shop_alert_message": "",
                      "shop_id": 6551304,
                      "user_id": 9070589,
                      "shop_name": "ahmadteststore",
                      "shop_image": "https://images.tokopedia.net/img/seller_no_logo_0.png",
                      "shop_url": "https://staging.tokopedia.com/ahmadte",
                      "shop_status": 1,
                      "is_gold": 1,
                      "is_gold_badge": true,
                      "is_official": 1,
                      "is_free_returns": 0,
                      "address_id": 2270,
                      "postal_code": "12930",
                      "latitude": "-6.221843970633553",
                      "longitude": "106.81970989845632",
                      "district_id": 2270,
                      "district_name": "Setiabudi",
                      "origin": 2270,
                      "address_street": "Jalan Guru Mughni, Kecamatan Setiabudi, 12930",
                      "province_id": 13,
                      "city_id": 175,
                      "city_name": "Jakarta Selatan",
                      "province_name": "DKI Jakarta",
                      "country_name": "Indonesia",
                      "is_allow_manage": false,
                      "shop_domain": "ahmadte",
                      "shop_ticker": "",
                      "maximum_weight_wording": "Oops, belanjaan TokoNOW! kamu terlalu berat {{weight}}kg untuk sekali pengiriman. Coba pilih lagi ya!",
                      "maximum_shipping_weight": 3000,
                      "shop_type_info": {
                        "shop_tier": 2,
                        "shop_grade": 0,
                        "badge": "https://images.tokopedia.net/img/official_store/badge_os.png",
                        "badge_svg": "https://images.tokopedia.net/img/official_store/badge_official.svg",
                        "title": "Official Store",
                        "title_fmt": "official_store"
                      },
                      "official_store": {
                        "is_official": 1,
                        "os_logo_url": "https://ecs7.tokopedia.net/img/official_store/badge_os128.png"
                      },
                      "gold_merchant": {
                        "is_gold": 0,
                        "is_gold_badge": false,
                        "gold_merchant_logo_url": ""
                      },
                      "shop_shipments": [
                        {
                          "ship_id": 14,
                          "ship_name": "J&T",
                          "ship_code": "jnt",
                          "ship_logo": "https://ecs7.tokopedia.net/img/kurir-jnt.png",
                          "is_dropship_enabled": 0,
                          "ship_prods": [
                            {
                              "ship_prod_id": 27,
                              "ship_prod_name": "Reguler",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            }
                          ]
                        },
                        {
                          "ship_id": 23,
                          "ship_name": "AnterAja",
                          "ship_code": "anteraja",
                          "ship_logo": "https://ecs7.tokopedia.net/img/kurir-anteraja.png",
                          "is_dropship_enabled": 0,
                          "ship_prods": [
                            {
                              "ship_prod_id": 45,
                              "ship_prod_name": "Reguler",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            }
                          ]
                        },
                        {
                          "ship_id": 12,
                          "ship_name": "Ninja Xpress",
                          "ship_code": "ninja",
                          "ship_logo": "https://ecs7.tokopedia.net/img/kurir-ninja.png",
                          "is_dropship_enabled": 0,
                          "ship_prods": [
                            {
                              "ship_prod_id": 25,
                              "ship_prod_name": "Reguler",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            }
                          ]
                        },
                        {
                          "ship_id": 4,
                          "ship_name": "Pos Indonesia",
                          "ship_code": "pos",
                          "ship_logo": "https://ecs7.tokopedia.net/img/kurir-pos.png",
                          "is_dropship_enabled": 0,
                          "ship_prods": [
                            {
                              "ship_prod_id": 10,
                              "ship_prod_name": "Pos Kilat Khusus",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            }
                          ]
                        },
                        {
                          "ship_id": 6,
                          "ship_name": "Wahana",
                          "ship_code": "wahana",
                          "ship_logo": "https://ecs7.tokopedia.net/img/kurir-wahana.png",
                          "is_dropship_enabled": 0,
                          "ship_prods": [
                            {
                              "ship_prod_id": 8,
                              "ship_prod_name": "Service Normal",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            }
                          ]
                        },
                        {
                          "ship_id": 11,
                          "ship_name": "SiCepat",
                          "ship_code": "sicepat",
                          "ship_logo": "https://ecs7.tokopedia.net/img/kurir-sicepat.png",
                          "is_dropship_enabled": 0,
                          "ship_prods": [
                            {
                              "ship_prod_id": 44,
                              "ship_prod_name": "REG",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            },
                            {
                              "ship_prod_id": 18,
                              "ship_prod_name": "Regular Package",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            }
                          ]
                        },
                        {
                          "ship_id": 16,
                          "ship_name": "REX",
                          "ship_code": "rex",
                          "ship_logo": "https://ecs7.tokopedia.net/img/kurir-rex.png",
                          "is_dropship_enabled": 0,
                          "ship_prods": [
                            {
                              "ship_prod_id": 32,
                              "ship_prod_name": "REX-10",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            }
                          ]
                        },
                        {
                          "ship_id": 2,
                          "ship_name": "TIKI",
                          "ship_code": "tiki",
                          "ship_logo": "https://ecs7.tokopedia.net/img/kurir-tiki.png",
                          "is_dropship_enabled": 0,
                          "ship_prods": [
                            {
                              "ship_prod_id": 16,
                              "ship_prod_name": "Over Night Service",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            },
                            {
                              "ship_prod_id": 3,
                              "ship_prod_name": "Reguler",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            }
                          ]
                        },
                        {
                          "ship_id": 10,
                          "ship_name": "Gojek",
                          "ship_code": "gojek",
                          "ship_logo": "https://ecs7.tokopedia.net/img/kurir-gosend.png",
                          "is_dropship_enabled": 0,
                          "ship_prods": [
                            {
                              "ship_prod_id": 28,
                              "ship_prod_name": "Instant Courier",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            },
                            {
                              "ship_prod_id": 20,
                              "ship_prod_name": "Same Day",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            }
                          ]
                        },
                        {
                          "ship_id": 24,
                          "ship_name": "Lion Parcel",
                          "ship_code": "lion",
                          "ship_logo": "https://ecs7.tokopedia.net/img/kurir-lionparcel.png",
                          "is_dropship_enabled": 0,
                          "ship_prods": [
                            {
                              "ship_prod_id": 47,
                              "ship_prod_name": "Reguler",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            }
                          ]
                        },
                        {
                          "ship_id": 13,
                          "ship_name": "GrabExpress",
                          "ship_code": "grab",
                          "ship_logo": "https://ecs7.tokopedia.net/img/kurir-grab.png",
                          "is_dropship_enabled": 0,
                          "ship_prods": [
                            {
                              "ship_prod_id": 24,
                              "ship_prod_name": "Same Day",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            },
                            {
                              "ship_prod_id": 37,
                              "ship_prod_name": "Instant",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            }
                          ]
                        },
                        {
                          "ship_id": 1,
                          "ship_name": "JNE",
                          "ship_code": "jne",
                          "ship_logo": "https://ecs7.tokopedia.net/img/kurir-jne.png",
                          "is_dropship_enabled": 0,
                          "ship_prods": [
                            {
                              "ship_prod_id": 6,
                              "ship_prod_name": "YES",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            },
                            {
                              "ship_prod_id": 22,
                              "ship_prod_name": "Trucking",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            },
                            {
                              "ship_prod_id": 1,
                              "ship_prod_name": "Reguler",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            },
                            {
                              "ship_prod_id": 2,
                              "ship_prod_name": "OKE",
                              "ship_group_name": "",
                              "ship_group_id": 0,
                              "minimum_weight": 0,
                              "additional_fee": 0
                            }
                          ]
                        }
                      ]
                    },
                    "shipment_information": {
                      "estimation": "",
                      "shop_location": "Jakarta Selatan",
                      "preorder": {
                        "is_preorder": false,
                        "duration": ""
                      }
                    },
                    "cart_details": [
                      {
                        "cart_id": "2147617194",
                        "product": {
                          "product_tracker_data": {
                            "attribution": "",
                            "tracker_list_name": ""
                          },
                          "isWishlist": false,
                          "product_id": 15628433,
                          "product_name": "BBO 01 non var",
                          "product_price_fmt": "Rp100.000",
                          "product_price": 100000,
                          "parent_id": 0,
                          "category_id": 777,
                          "category": "Buku / Hobi / Humor",
                          "catalog_id": 0,
                          "wholesale_price": [],
                          "product_weight": 100,
                          "product_weight_fmt": "100gr",
                          "product_condition": 1,
                          "product_status": 1,
                          "product_url": "https://staging.tokopedia.com/ahmadte/bbo-01-non-var",
                          "is_preorder": 0,
                          "product_cashback": "",
                          "product_min_order": 1,
                          "product_max_order": 30000,
                          "product_rating": "0.000000",
                          "product_invenage_value": 99,
                          "product_invenage_total": {
                            "by_user": {
                              "in_cart": 0,
                              "last_stock_less_than": 0
                            },
                            "by_user_text": {
                              "in_cart": "",
                              "last_stock_less_than": "",
                              "complete": ""
                            },
                            "is_counted_by_user": false,
                            "by_product": {
                              "in_cart": 0,
                              "last_stock_less_than": 0
                            },
                            "by_product_text": {
                              "in_cart": "",
                              "last_stock_less_than": "",
                              "complete": ""
                            },
                            "is_counted_by_product": false
                          },
                          "product_switch_invenage": 1,
                          "product_information": [],
                          "price_changes": {
                            "changes_state": 0,
                            "amount_difference": 0,
                            "original_amount": 100000,
                            "description": "Harga Normal"
                          },
                          "product_image": {
                            "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2021/3/4/a6343bb0-7820-4204-8c95-8b92f098d113.jpg"
                          },
                          "product_notes": "",
                          "product_quantity": 1,
                          "product_weight_unit_code": 1,
                          "product_weight_unit_text": "gr",
                          "last_update_price": "1617598265",
                          "is_update_price": false,
                          "product_preorder": {
                            "duration_day": "0",
                            "duration_text": "",
                            "duration_unit_code": "0",
                            "duration_unit_text": "",
                            "duration_value": "0"
                          },
                          "campaign_id": 0,
                          "product_original_price": 0,
                          "product_price_original_fmt": "",
                          "is_slash_price": false,
                          "product_finsurance": 1,
                          "is_wishlisted": false,
                          "is_ppp": false,
                          "is_cod": false,
                          "warehouse_id": 341673,
                          "is_parent": false,
                          "is_campaign_error": false,
                          "is_blacklisted": false,
                          "booking_stock": 0,
                          "product_variant": {
                            "parent_id": 0,
                            "default_child": 0,
                            "variant": []
                          },
                          "is_product_volume_weight": false,
                          "initial_price": 100000,
                          "initial_price_fmt": "Rp100.000",
                          "slash_price_label": "",
                          "product_warning_message": "",
                          "product_alert_message": ""
                        },
                        "errors": [],
                        "messages": [],
                        "checkbox_state": true
                      },
                      {
                        "cart_id": "2147617156",
                        "product": {
                          "product_tracker_data": {
                            "attribution": "",
                            "tracker_list_name": ""
                          },
                          "isWishlist": false,
                          "product_id": 15628657,
                          "product_name": "Slash Price Revamp 03-04-21 18:10:84-Non Var-1",
                          "product_price_fmt": "Rp100.000",
                          "product_price": 100000,
                          "parent_id": 0,
                          "category_id": 2134,
                          "category": "Handphone & Tablet / Handphone / IOS",
                          "catalog_id": 0,
                          "wholesale_price": [],
                          "product_weight": 1555,
                          "product_weight_fmt": "1555gr",
                          "product_condition": 1,
                          "product_status": 1,
                          "product_url": "https://staging.tokopedia.com/ahmadte/slash-price-revamp-03-04-21-18-10-84-non-var-1",
                          "is_preorder": 0,
                          "product_cashback": "",
                          "product_min_order": 1,
                          "product_max_order": 30000,
                          "product_rating": "0.000000",
                          "product_invenage_value": 100,
                          "product_invenage_total": {
                            "by_user": {
                              "in_cart": 0,
                              "last_stock_less_than": 0
                            },
                            "by_user_text": {
                              "in_cart": "",
                              "last_stock_less_than": "",
                              "complete": ""
                            },
                            "is_counted_by_user": false,
                            "by_product": {
                              "in_cart": 0,
                              "last_stock_less_than": 0
                            },
                            "by_product_text": {
                              "in_cart": "",
                              "last_stock_less_than": "",
                              "complete": ""
                            },
                            "is_counted_by_product": false
                          },
                          "product_switch_invenage": 1,
                          "product_information": [],
                          "price_changes": {
                            "changes_state": 0,
                            "amount_difference": 0,
                            "original_amount": 100000,
                            "description": "Harga Normal"
                          },
                          "product_image": {
                            "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/6/19/13760252/13760252_15b73a95-ad1e-4b94-aaac-3db950b4070e_640_640"
                          },
                          "product_notes": "",
                          "product_quantity": 1,
                          "product_weight_unit_code": 1,
                          "product_weight_unit_text": "gr",
                          "last_update_price": "1617619720",
                          "is_update_price": false,
                          "product_preorder": {
                            "duration_day": "0",
                            "duration_text": "",
                            "duration_unit_code": "0",
                            "duration_unit_text": "",
                            "duration_value": "0"
                          },
                          "campaign_id": 0,
                          "product_original_price": 0,
                          "product_price_original_fmt": "",
                          "is_slash_price": false,
                          "product_finsurance": 0,
                          "is_wishlisted": true,
                          "is_ppp": false,
                          "is_cod": false,
                          "warehouse_id": 341673,
                          "is_parent": false,
                          "is_campaign_error": false,
                          "is_blacklisted": false,
                          "booking_stock": 0,
                          "product_variant": {
                            "parent_id": 0,
                            "default_child": 0,
                            "variant": []
                          },
                          "is_product_volume_weight": false,
                          "initial_price": 100000,
                          "initial_price_fmt": "Rp100.000",
                          "slash_price_label": "",
                          "product_warning_message": "",
                          "product_alert_message": ""
                        },
                        "errors": [],
                        "messages": [],
                        "checkbox_state": true
                      }
                    ]
                  },
                  {
                    "shop": {
                      "shop_alert_message": "",
                      "shop_id": 4423004,
                      "user_id": 42298667,
                      "shop_name": "Lautanacc",
                      "shop_image": "https://images.tokopedia.net/img/cache/215-square/shops-1/2018/10/29/4423004/4423004_89b6c728-2ac8-47b5-b7e0-34c5615fb84c.jpeg",
                      "shop_url": "https://www.tokopedia.com/lautanacc57",
                      "shop_status": 1,
                      "is_gold": 0,
                      "is_gold_badge": false,
                      "is_official": 0,
                      "is_free_returns": 0,
                      "address_id": 2255,
                      "postal_code": "11840",
                      "latitude": "-6.152508499999982",
                      "longitude": "106.69514950000007",
                      "district_id": 2255,
                      "district_name": "",
                      "origin": 0,
                      "address_street": "",
                      "province_id": 0,
                      "city_id": 174,
                      "city_name": "Jakarta Barat",
                      "province_name": "",
                      "country_name": "",
                      "is_allow_manage": false,
                      "shop_domain": "lautanacc57",
                      "shop_ticker": "",
                      "maximum_weight_wording": "Oops, belanjaan TokoNOW! kamu terlalu berat {{weight}}kg untuk sekali pengiriman. Coba pilih lagi ya!",
                      "maximum_shipping_weight": 3000,
                      "shop_type_info": {
                        "shop_tier": 0,
                        "shop_grade": 0,
                        "badge": "",
                        "badge_svg": "",
                        "title": "",
                        "title_fmt": ""
                      },
                      "official_store": {
                        "is_official": 0,
                        "os_logo_url": ""
                      },
                      "gold_merchant": {
                        "is_gold": 0,
                        "is_gold_badge": false,
                        "gold_merchant_logo_url": ""
                      },
                      "shop_shipments": []
                    },
                    "shipment_information": {
                      "estimation": "Estimasi tiba besok - 24 May (Reguler)",
                      "shop_location": "Jakarta Barat",
                      "preorder": {
                        "is_preorder": false,
                        "duration": ""
                      }
                    },
                    "cart_details": [
                      {
                        "cart_id": "2713623917",
                        "product": {
                          "product_tracker_data": {
                            "attribution": "",
                            "tracker_list_name": ""
                          },
                          "isWishlist": false,
                          "product_id": 1687280143,
                          "product_name": "Case Anti Crack Bening Silikon Soft Case Samsung Galaxy A32",
                          "product_price_fmt": "Rp5.000",
                          "product_price": 5000,
                          "parent_id": 0,
                          "category_id": 69,
                          "category": "Handphone & Tablet / Aksesoris Handphone / Soft Case Handphone",
                          "catalog_id": 0,
                          "wholesale_price": [],
                          "product_weight": 25,
                          "product_weight_fmt": "25gr",
                          "product_condition": 1,
                          "product_status": 1,
                          "product_url": "https://www.tokopedia.com/lautanacc57/case-anti-crack-bening-silikon-soft-case-samsung-galaxy-a32",
                          "is_preorder": 0,
                          "product_cashback": "",
                          "product_min_order": 1,
                          "product_max_order": 30000,
                          "product_rating": "0.000000",
                          "product_invenage_value": 999497,
                          "product_invenage_total": {
                            "by_user": {
                              "in_cart": 0,
                              "last_stock_less_than": 0
                            },
                            "by_user_text": {
                              "in_cart": "",
                              "last_stock_less_than": "",
                              "complete": ""
                            },
                            "is_counted_by_user": false,
                            "by_product": {
                              "in_cart": 0,
                              "last_stock_less_than": 0
                            },
                            "by_product_text": {
                              "in_cart": "",
                              "last_stock_less_than": "",
                              "complete": ""
                            },
                            "is_counted_by_product": false
                          },
                          "product_switch_invenage": 1,
                          "product_information": [],
                          "price_changes": {
                            "changes_state": 0,
                            "amount_difference": 0,
                            "original_amount": 5000,
                            "description": "Harga Normal"
                          },
                          "product_image": {
                            "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2021/4/29/dfcc4f33-5dbb-4d91-ae47-9e2cec0c6732.jpg"
                          },
                          "product_notes": "",
                          "product_quantity": 1,
                          "product_weight_unit_code": 1,
                          "product_weight_unit_text": "gr",
                          "last_update_price": "1616404953",
                          "is_update_price": false,
                          "product_preorder": {
                            "duration_day": "0",
                            "duration_text": "",
                            "duration_unit_code": "0",
                            "duration_unit_text": "",
                            "duration_value": "0"
                          },
                          "campaign_id": 0,
                          "product_original_price": 0,
                          "product_price_original_fmt": "",
                          "is_slash_price": false,
                          "product_finsurance": 0,
                          "is_wishlisted": false,
                          "is_ppp": false,
                          "is_cod": false,
                          "warehouse_id": 3227765,
                          "is_parent": false,
                          "is_campaign_error": false,
                          "is_blacklisted": false,
                          "booking_stock": 0,
                          "product_variant": {
                            "parent_id": 0,
                            "default_child": 0,
                            "variant": []
                          },
                          "is_product_volume_weight": false,
                          "initial_price": 5000,
                          "initial_price_fmt": "Rp5.000",
                          "slash_price_label": "",
                          "product_warning_message": "",
                          "product_alert_message": ""
                        },
                        "errors": [],
                        "messages": [],
                        "checkbox_state": false
                      }
                    ]
                  }
                ]
              }
            }
          }
        }
    """.trimIndent()
}