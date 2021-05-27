package com.tokopedia.minicart.common.domain.usecase

import com.google.gson.Gson
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
                  max_char_note
                  header_title
                  shopping_summary {
                    total_wording
                    total_value
                    discount_total_wording
                    discount_value
                    payment_total_wording
                    payment_total_value
                  }
                  available_section {
                    action {
                      id
                      code
                      message
                    }
                    available_group {
                      cart_string
                      errors
                      shop {
                        maximum_weight_wording
                        maximum_shipping_weight
                        shop_id
                        shop_type_info {
                          badge
                          shop_grade
                          shop_tier
                          title
                          title_fmt
                        }
                      }
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
                      }
                      cart_details {
                        cart_id
                        selected_unavailable_action_link
                        errors
                        product {
                          product_id
                          product_weight
                          product_quantity
                          product_name
                          product_image {
                            image_src_100_square
                          }
                          variant_description_detail {
                            variant_name
                            variant_description
                          }
                          product_warning_message
                          slash_price_label
                          product_original_price
                          initial_price
                          product_price
                          product_information
                          product_notes
                          product_min_order
                          product_max_order
                          parent_id
                          wholesale_price {
                            qty_min
                            qty_max
                            prd_prc
                          }
                        }
                      }
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
                    unavailable_description
                    selected_unavailable_action_id
                    action {
                      id
                      code
                      message
                    }
                    unavailable_group {
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
                      }
                      shop {
                        maximum_weight_wording
                        maximum_shipping_weight
                        shop_id
                        shop_type_info {
                          badge
                          shop_grade
                          shop_tier
                          title
                          title_fmt
                        }
                      }
                      cart_string
                      errors
                      cart_details {
                        cart_id
                        selected_unavailable_action_link
                        errors
                        product {
                          product_id
                          product_weight
                          product_quantity
                          product_name
                          product_image {
                            image_src_100_square
                          }
                          variant_description_detail {
                            variant_name
                            variant_description
                          }
                          product_warning_message
                          slash_price_label
                          product_original_price
                          initial_price
                          product_price
                          product_information
                          product_notes
                          product_min_order
                          product_max_order
                          parent_id
                          wholesale_price {
                            qty_min
                            qty_max
                            prd_prc
                          }
                        }
                      }
                    }
                  }
                  total_product_count
                  total_product_price
                  total_product_error
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
              "max_char_note": 144,
              "header_title": "Belanjaanmu di TokoNOW!",
              "shopping_summary": {
                "total_wording": "Total Harga (2 Barang)",
                "total_value": 200000,
                "discount_total_wording": "Total Diskon Barang",
                "discount_value": 0,
                "payment_total_wording": "Total Bayar",
                "payment_total_value": 200000
              },
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
                    "cart_string": "6551304-0-341673",
                    "errors": [],
                    "shop": {
                      "maximum_weight_wording": "Oops, belanjaan TokoNOW! kamu terlalu berat {{weight}}kg untuk sekali pengiriman. Coba pilih lagi ya!",
                      "maximum_shipping_weight": 10000,
                      "shop_id": 6551304,
                      "shop_type_info": {
                        "badge": "https://images.tokopedia.net/img/official_store/badge_os.png",
                        "shop_grade": 0,
                        "shop_tier": 2,
                        "title": "Official Store",
                        "title_fmt": "official_store"
                      }
                    },
                    "shipment_information": {
                      "shop_location": "Jakarta Selatan",
                      "estimation": "",
                      "free_shipping": {
                        "eligible": false,
                        "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                      },
                      "free_shipping_extra": {
                        "eligible": false,
                        "badge_url": ""
                      }
                    },
                    "cart_details": [
                      {
                        "cart_id": "2147617194",
                        "selected_unavailable_action_link": "",
                        "errors": [],
                        "product": {
                          "product_id": 15628433,
                          "product_weight": 1000,
                          "product_quantity": 2,
                          "product_name": "BBO 01 non var",
                          "product_image": {
                            "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2021/3/4/a6343bb0-7820-4204-8c95-8b92f098d113.jpg"
                          },
                          "variant_description_detail": {
                            "variant_name": [],
                            "variant_description": ""
                          },
                          "product_warning_message": "",
                          "slash_price_label": "",
                          "product_original_price": 0,
                          "initial_price": 100000,
                          "product_price": 100000,
                          "product_information": [],
                          "product_notes": "",
                          "product_min_order": 2,
                          "product_max_order": 50,
                          "parent_id": 12345,
                          "wholesale_price": [
                            {
                                "qtyMin" : 5,
                                "qtyMax" : 10,
                                "prdPrice" : 90000
                            },
                            {
                                "qtyMin" : 11,
                                "qtyMax" : 15,
                                "prdPrice" : 80000
                            },
                            {
                                "qtyMin" : 16,
                                "qtyMax" : 20,
                                "prdPrice" : 70000
                            }
                          ]
                        }
                      },
                      {
                        "cart_id": "2147617156",
                        "selected_unavailable_action_link": "",
                        "errors": [],
                        "product": {
                          "product_id": 15628657,
                          "product_weight": 1000,
                          "product_quantity": 2,
                          "product_name": "Slash Price Revamp 03-04-21 18:10:84-Non Var-1",
                          "product_image": {
                            "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/6/19/13760252/13760252_15b73a95-ad1e-4b94-aaac-3db950b4070e_640_640"
                          },
                          "variant_description_detail": {
                            "variant_name": [],
                            "variant_description": ""
                          },
                          "product_warning_message": "",
                          "slash_price_label": "",
                          "product_original_price": 0,
                          "initial_price": 100000,
                          "product_price": 100000,
                          "product_information": [],
                          "product_notes": "",
                          "product_min_order": 2,
                          "product_max_order": 50,
                          "parent_id": 12345,
                          "wholesale_price": [
                            {
                                "qtyMin" : 5,
                                "qtyMax" : 10,
                                "prdPrice" : 90000
                            },
                            {
                                "qtyMin" : 11,
                                "qtyMax" : 15,
                                "prdPrice" : 80000
                            },
                            {
                                "qtyMin" : 16,
                                "qtyMax" : 20,
                                "prdPrice" : 70000
                            }
                          ]
                        }
                      }
                    ]
                  },
                  {
                    "cart_string": "4423004-0-3227765",
                    "errors": [],
                    "shop": {
                      "maximum_weight_wording": "Oops, belanjaan TokoNOW! kamu terlalu berat {{weight}}kg untuk sekali pengiriman. Coba pilih lagi ya!",
                      "maximum_shipping_weight": 10000,
                      "shop_id": 4423004,
                      "shop_type_info": {
                        "badge": "",
                        "shop_grade": 0,
                        "shop_tier": 0,
                        "title": "",
                        "title_fmt": ""
                      }
                    },
                    "shipment_information": {
                      "shop_location": "Jakarta Barat",
                      "estimation": "Estimasi tiba besok - 24 May (Reguler)",
                      "free_shipping": {
                        "eligible": false,
                        "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                      },
                      "free_shipping_extra": {
                        "eligible": false,
                        "badge_url": ""
                      }
                    },
                    "cart_details": [
                      {
                        "cart_id": "2713623917",
                        "selected_unavailable_action_link": "",
                        "errors": [],
                        "product": {
                          "product_id": 1687280143,
                          "product_weight": 1000,
                          "product_quantity": 1,
                          "product_name": "Case Anti Crack Bening Silikon Soft Case Samsung Galaxy A32",
                          "product_image": {
                            "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2021/4/29/dfcc4f33-5dbb-4d91-ae47-9e2cec0c6732.jpg"
                          },
                          "variant_description_detail": {
                            "variant_name": [],
                            "variant_description": ""
                          },
                          "product_warning_message": "Sisa 3",
                          "slash_price_label": "50%",
                          "product_original_price": 10000,
                          "initial_price": 10000,
                          "product_price": 5000,
                          "product_information": [
                            "Cashback 3%"
                          ],
                          "product_notes": "Ini notes",
                          "product_min_order": 1,
                          "product_max_order": 10,
                          "parent_id": 0,
                          "wholesale_price": []
                        }
                      }
                    ]
                  }
                ]
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
                        }
                      },
                      "shop": {
                        "maximum_weight_wording": "",
                        "maximum_shipping_weight": 0,
                        "shop_id": 2007325,
                        "shop_type_info": {
                          "badge": "",
                          "shop_grade": 0,
                          "shop_tier": 0,
                          "title": "",
                          "title_fmt": ""
                        }
                      },
                      "cart_string": "2007325-0-1603251",
                      "errors": [],
                      "cart_details": [
                        {
                          "cart_id": "2693179591",
                          "selected_unavailable_action_link": "https://www.tokopedia.com/rekomendasi/1809319671?ref=cart",
                          "errors": [
                            "Habis",
                            "Habis"
                          ],
                          "product": {
                            "product_id": 1809319671,
                            "product_weight": 1000,
                            "product_quantity": 1,
                            "product_name": "Poco X3 Pro ram 8/256 gb garansi resmi - Frost Blue",
                            "product_image": {
                              "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2021/4/24/8f42d9a6-aedd-4f7c-9a66-95d96fb79f8f.jpg"
                            },
                            "variant_description_detail": {
                              "variant_name": [
                                "Frost Blue"
                              ],
                              "variant_description": "Frost Blue"
                            },
                            "product_warning_message": "Sisa 0",
                            "slash_price_label": "",
                            "product_original_price": 0,
                            "initial_price": 0,
                            "product_price": 3920000,
                            "product_information": [],
                            "product_notes": "",
                            "product_min_order": 0,
                            "product_max_order": 0,
                            "parent_id": 1793152507,
                            "wholesale_price": []
                          }
                        }
                      ]
                    },
                    {
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
                        }
                      },
                      "shop": {
                        "maximum_weight_wording": "Oops, belanjaan TokoNOW! kamu terlalu berat {{weight}}kg untuk sekali pengiriman. Coba pilih lagi ya!",
                        "maximum_shipping_weight": 1000,
                        "shop_id": 11130255,
                        "shop_type_info": {
                          "badge": "",
                          "shop_grade": 0,
                          "shop_tier": 0,
                          "title": "",
                          "title_fmt": ""
                        }
                      },
                      "cart_string": "11130255-0-11131577",
                      "errors": [],
                      "cart_details": [
                        {
                          "cart_id": "2693177964",
                          "selected_unavailable_action_link": "https://www.tokopedia.com/rekomendasi/1815388587?ref=cart",
                          "errors": [
                            "Habis",
                            "Habis"
                          ],
                          "product": {
                            "product_id": 1815388587,
                            "product_weight": 1000,
                            "product_quantity": 1,
                            "product_name": "XIAOMI POCO X3 PRO RAM 8GB/256GB BLACK ADA NFC",
                            "product_image": {
                              "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2021/4/30/896792fa-b495-4924-9cd0-73f2614ad8de.jpg"
                            },
                            "variant_description_detail": {
                              "variant_name": [],
                              "variant_description": ""
                            },
                            "product_warning_message": "Sisa 0",
                            "slash_price_label": "",
                            "product_original_price": 0,
                            "initial_price": 0,
                            "product_price": 3939000,
                            "product_information": [],
                            "product_notes": "",
                            "product_min_order": 0,
                            "product_max_order": 0,
                            "parent_id": 0,
                            "wholesale_price": []
                          }
                        }
                      ]
                    }
                  ]
                }
              ],
              "total_product_count": 5,
              "total_product_price": 405000,
              "total_product_error": 2
            }
          }
        }
    """.trimIndent()
}