package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood
import com.tokopedia.tokofood.common.domain.param.CheckoutTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckoutTokoFoodUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper,
    dispatchers: CoroutineDispatchers
): FlowUseCase<String, CheckoutTokoFood>(dispatchers.io) {

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(additionalAttributes: String,
                                   source: String): Map<String, Any> {
            val params = CheckoutTokoFoodParam(
                additionalAttributes = additionalAttributes,
                source = source
            )
            return mapOf(PARAMS_KEY to params)
        }
    }

    override fun graphqlQuery(): String = """
        query CartListTokofood($$PARAMS_KEY: cartTokofoodParams!) {
          cart_list_tokofood(params: $$PARAMS_KEY) {
            message
            status
            data {
              popup_message
              popup_error_message
              popup_message_type
              shop {
                shop_id
                name
                distance
              }
              tickers {
                top {
                  id
                  message
                  page
                }
                bottom {
                  id
                  message
                  page
                }
              }
              error_tickers {
                top {
                  id
                  message
                  page
                }
                bottom {
                  id
                  message
                  page
                }
              }
              error_unblocking
              user_address {
                address_id
                address_name
                address
                phone
                receiver_name
                status
              }
              available_section {
                products {
                  cart_id
                  product_id
                  category_id
                  name
                  description
                  image_url
                  price
                  price_fmt
                  original_price
                  original_price_fmt
                  discount_percentage
                  notes
                  quantity
                  variants {
                    variant_id
                    name
                    rules {
                      selection_rule {
                        type
                        max_quantity
                        min_quantity
                        required
                      }
                    }
                    options {
                      is_selected
                      option_id
                      name
                      price
                      price_fmt
                      status
                    }
                  }
                }
              }
              unavailable_section_header
              unavailable_section {
                title
                products {
                  cart_id
                  product_id
                  category_id
                  name
                  description
                  image_url
                  price
                  price_fmt
                  original_price
                  original_price_fmt
                  discount_percentage
                  notes
                  quantity
                  variants {
                    variant_id
                    name
                    rules {
                      selection_rule {
                        type
                        max_quantity
                        min_quantity
                        required
                      }
                    }
                    options {
                      is_selected
                      option_id
                      name
                      price
                      price_fmt
                      status
                    }
                  }
                }
              }
              shipping {
                name
                logo_url
                eta
                price
                price_fmt
              }
              promo {
                is_promo_applied
                hide_promo
                title
                subtitle
              }
              checkout_consent_bottomsheet {
                is_show_bottomsheet
                image_url
                title
                description   
                terms_and_condition
              }
              shopping_summary {
                total {
                  cost
                  savings
                }
                cost_breakdown {
                  total_cart_price {
                    original_amount
                    amount
                  }
                  outlet_fee {
                    original_amount
                    amount
                  }
                  platform_fee {
                    original_amount
                    amount
                  }
                  delivery_fee {
                    original_amount
                    amount
                    surge {
                      is_surge_price
                      factor
                    }
                  }
                  reimbursement_fee {
                    original_amount
                    amount
                  }
                }
                discount_breakdown {
                  discount_id
                  title
                  amount
                  scope
                  type
                }  
              }
              summary_detail {
                hide_summary
                total_items
                total_price
                details {
                  title
                  price_fmt
                  info {
                    image_url
                    bottomsheet {
                      title
                      description
                    }
                  }
                }
              }
              checkout_additional_data {
                data_type
                checkout_business_id
              }
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: String): Flow<CheckoutTokoFood> = flow {
//        val additionalAttributes = CartAdditionalAttributesTokoFood(
//            chosenAddressRequestHelper.getChosenAddress()
//        )
//        val param = generateParams(additionalAttributes.generateString(), params)
//        val response =
//            repository.request<Map<String, Any>, CheckoutTokoFoodResponse>(graphqlQuery(), param)
//        if (response.cartListTokofood.isSuccess()) {
//            emit(response.cartListTokofood)
//        } else {
//            throw MessageErrorException(response.cartListTokofood.getMessageIfError())
//        }
        emit(getDummyJson().cartListTokofood)
    }

    private suspend fun getDummyJson(): CheckoutTokoFoodResponse {
        val resultJson = """
            {
      "cart_list_tokofood": {
        "message": "",
        "status": "OK",
        "data": {
          "popup_message": "Yay, ada promo yang otomatis terpakai!",
          "popup_error_message": "",
          "popup_message_type": "promo",
          "shop": {
            "shop_id": "0d3a8735-6751-48fc-8168-cf3b425f1983",
            "name": "TokoFood4",
            "distance": "Jarak Pengiriman 1.6 km"
          },
          "tickers": {
            "top": {
              "id": 0,
              "message": "",
              "page": ""
            },
            "bottom": {
              "id": 0,
              "message": "",
              "page": ""
            }
          },
          "error_tickers": {
            "top": {
              "id": 0,
              "message": "",
              "page": ""
            },
            "bottom": {
              "id": 0,
              "message": "",
              "page": ""
            }
          },
          "error_unblocking": "",
          "user_address": {
            "address_id": 4678593,
            "address_name": "Rumah",
            "address": "tokopedia tower lt 52 jl dr satrio",
            "phone": "628159965151",
            "receiver_name": "irvanno",
            "status": 2
          },
          "available_section": {
            "products": [
              {
                "cart_id": 1957,
                "product_id": "42a6e627-ca43-4ec5-8006-5da7b2d82ed3",
                "category_id": "90c0e39a-1187-4fcd-a7bb-6ff1d7e4cd07",
                "name": "Sprite",
                "description": "Minuman Soda",
                "image_url": "https://i-integration.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/03bcde35-44b9-4faa-a850-d679c0eae7a7_Go-Biz_20220525_111921.jpeg",
                "price": 5000,
                "price_fmt": "Rp5.000",
                "original_price": 10000,
                "original_price_fmt": "Rp10.000",
                "discount_percentage": "50%",
                "notes": "",
                "quantity": 1,
                "variants": []
              },
              {
                "cart_id": 1990,
                "product_id": "66436e92-96d1-4615-9911-c69f9d385096",
                "category_id": "45ea1047-6be1-4663-8516-24add3a04a3b",
                "name": "Mie Goreng",
                "description": "Mie Goreng + Sayur",
                "image_url": "https://i-integration.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/2e8684f8-20df-4575-b459-2ea699d6251f_Go-Biz_20220519_164118.jpeg",
                "price": 61000,
                "price_fmt": "Rp61.000",
                "original_price": 0,
                "original_price_fmt": "",
                "discount_percentage": "",
                "notes": "",
                "quantity": 1,
                "variants": [
                  {
                    "variant_id": "b351d6c0-c3d3-4ec3-9bc1-4da68d498a25",
                    "name": "Spicy Level",
                    "rules": {
                      "selection_rule": {
                        "type": 1,
                        "max_quantity": 1,
                        "min_quantity": 1,
                        "required": true
                      }
                    },
                    "options": [
                      {
                        "is_selected": false,
                        "option_id": "aff948ee-4b84-4567-add8-e6f42bebfc6f",
                        "name": "Level 1",
                        "price": 2000,
                        "price_fmt": "Rp2.000",
                        "status": 1
                      },
                      {
                        "is_selected": false,
                        "option_id": "e1509c1b-cb0b-4288-8f09-9e1b76b5aef5",
                        "name": "Level 2",
                        "price": 4000,
                        "price_fmt": "Rp4.000",
                        "status": 1
                      },
                      {
                        "is_selected": false,
                        "option_id": "5fd329be-c363-437c-92cb-dd251b245b7f",
                        "name": "Level 3",
                        "price": 6000,
                        "price_fmt": "Rp6.000",
                        "status": 1
                      },
                      {
                        "is_selected": false,
                        "option_id": "8af32ad6-1070-42d8-b277-093f74879422",
                        "name": "Level 4",
                        "price": 8000,
                        "price_fmt": "Rp8.000",
                        "status": 1
                      },
                      {
                        "is_selected": true,
                        "option_id": "5bc9357d-76fc-4065-8e84-c96c85fe0fbe",
                        "name": "Level 5",
                        "price": 10000,
                        "price_fmt": "Rp10.000",
                        "status": 1
                      }
                    ]
                  },
                  {
                    "variant_id": "12808c85-7d5c-4058-86a4-1acab2958f28",
                    "name": "Food Topping",
                    "rules": {
                      "selection_rule": {
                        "type": 2,
                        "max_quantity": 6,
                        "min_quantity": 0,
                        "required": false
                      }
                    },
                    "options": [
                      {
                        "is_selected": true,
                        "option_id": "1cc2a86f-d148-4bba-b1fa-bd9a8647103e",
                        "name": "Udang",
                        "price": 10000,
                        "price_fmt": "Rp10.000",
                        "status": 1
                      },
                      {
                        "is_selected": false,
                        "option_id": "6e9a271f-8171-4faf-97d1-f704893cbbed",
                        "name": "Jamur",
                        "price": 9000,
                        "price_fmt": "Rp9.000",
                        "status": 1
                      },
                      {
                        "is_selected": false,
                        "option_id": "ebd29623-bc89-4e03-8938-f893c24584cb",
                        "name": "Telur",
                        "price": 8000,
                        "price_fmt": "Rp8.000",
                        "status": 1
                      },
                      {
                        "is_selected": true,
                        "option_id": "fde2ed80-3f19-4db0-bae3-5a7bb3671c35",
                        "name": "Tahu",
                        "price": 6000,
                        "price_fmt": "Rp6.000",
                        "status": 1
                      },
                      {
                        "is_selected": false,
                        "option_id": "d5a7994c-c281-4d5d-930f-25e60b074d53",
                        "name": "Tempe",
                        "price": 6000,
                        "price_fmt": "Rp6.000",
                        "status": 1
                      },
                      {
                        "is_selected": false,
                        "option_id": "2a765129-db1e-497c-836a-65e53d3871a9",
                        "name": "Lalapan",
                        "price": 5000,
                        "price_fmt": "Rp5.000",
                        "status": 1
                      }
                    ]
                  },
                  {
                    "variant_id": "983b88c2-2e94-4a9a-896f-31925fe28fdd",
                    "name": "Chicken",
                    "rules": {
                      "selection_rule": {
                        "type": 2,
                        "max_quantity": 4,
                        "min_quantity": 0,
                        "required": false
                      }
                    },
                    "options": [
                      {
                        "is_selected": false,
                        "option_id": "2ded392b-aa1e-4134-9643-7bcf6f3af3b8",
                        "name": "Paha Bawah",
                        "price": 10000,
                        "price_fmt": "Rp10.000",
                        "status": 1
                      },
                      {
                        "is_selected": false,
                        "option_id": "22afd608-4b8f-44ef-a6f7-b01dd0049a76",
                        "name": "Paha Atas",
                        "price": 8000,
                        "price_fmt": "Rp8.000",
                        "status": 3
                      },
                      {
                        "is_selected": false,
                        "option_id": "1636b5ab-9c21-4608-a20e-7d1f8bb01aa9",
                        "name": "Dada",
                        "price": 11000,
                        "price_fmt": "Rp11.000",
                        "status": 3
                      },
                      {
                        "is_selected": false,
                        "option_id": "15988a44-ca73-432b-babd-3ac72a69c387",
                        "name": "Sayap",
                        "price": 9000,
                        "price_fmt": "Rp9.000",
                        "status": 3
                      }
                    ]
                  }
                ]
              }
            ]
          },
          "unavailable_section_header": "",
          "unavailable_section": {
            "title": "",
            "products": []
          },
          "shipping": {
            "name": "Gojek Instan",
            "logo_url": "https://images.tokopedia.net/img/tokofood/cart/gojek.png",
            "eta": "Tiba Dalam 34 menit",
            "price": 13600,
            "price_fmt": "Rp13.600"
          },
          "promo": {
            "is_promo_applied": true,
            "hide_promo": false,
            "title": "Kamu Bisa Hemat Rp1.400",
            "subtitle": "1 promo dipakai"
          },
          "checkout_consent_bottomsheet": {
            "is_show_bottomsheet": false,
            "image_url": "",
            "title": "",
            "description": "",
            "terms_and_condition": ""
          },
          "shopping_summary": {
            "total": {
              "cost": 86600,
              "savings": 6400
            },
            "cost_breakdown": {
              "total_cart_price": {
                "original_amount": 71000,
                "amount": 66000
              },
              "outlet_fee": {
                "original_amount": 0,
                "amount": 0
              },
              "platform_fee": {
                "original_amount": 2000,
                "amount": 2000
              },
              "delivery_fee": {
                "original_amount": 15000,
                "amount": 13600,
                "surge": {
                  "is_surge_price": false,
                  "factor": 0
                }
              },
              "reimbursement_fee": {
                "original_amount": 5000,
                "amount": 5000
              }
            },
            "discount_breakdown": [
              {
                "discount_id": "9fb91904-af2a-45ae-aeb4-309697ec63d7",
                "title": "list1",
                "amount": 1400,
                "scope": "DELIVERY",
                "type": "MARKDOWN"
              },
              {
                "discount_id": "",
                "title": "Promo",
                "amount": 5000,
                "scope": "CART",
                "type": "SKU_PROMOTIONS"
              }
            ]
          },
          "checkout_additional_data": {
            "data_type": "TOKOFOOD",
            "checkout_business_id": 42
          },
          "summary_detail": {
            "hide_summary": false,
            "total_items": 2,
            "total_price": "",
            "details": [
              {
                "title": "Total Harga (2 item)",
                "price_fmt": "Rp66.000",
                "info": {
                  "image_url": "",
                  "bottomsheet": {
                    "title": "",
                    "description": ""
                  }
                }
              },
              {
                "title": "Total Ongkos Kirim",
                "price_fmt": "Rp13.600",
                "info": {
                  "image_url": "",
                  "bottomsheet": {
                    "title": "",
                    "description": ""
                  }
                }
              },
              {
                "title": "Total Diskon Ongkos Kirim",
                "price_fmt": "Rp1.400",
                "info": {
                  "image_url": "",
                  "bottomsheet": {
                    "title": "",
                    "description": ""
                  }
                }
              },
              {
                "title": "Biaya Jasa Aplikasi",
                "price_fmt": "Rp2.000",
                "info": {
                  "image_url": "",
                  "bottomsheet": {
                    "title": "",
                    "description": ""
                  }
                }
              },
              {
                "title": "Biaya Parkir",
                "price_fmt": "Rp5.000",
                "info": {
                  "image_url": "",
                  "bottomsheet": {
                    "title": "",
                    "description": ""
                  }
                }
              }
            ]
          }
        }
      }
    }
        """.trimIndent()
        return Gson().fromJson(resultJson, CheckoutTokoFoodResponse::class.java)
    }

}