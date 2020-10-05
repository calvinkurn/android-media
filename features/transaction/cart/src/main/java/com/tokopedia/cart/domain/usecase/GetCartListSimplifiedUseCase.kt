package com.tokopedia.cart.domain.usecase

import com.google.gson.Gson
import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopGroupSimplifiedGqlResponse
import com.tokopedia.cart.domain.mapper.CartSimplifiedMapper
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-10-18.
 */

class GetCartListSimplifiedUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                                       private val cartSimplifiedMapper: CartSimplifiedMapper,
                                                       private val schedulers: ExecutorSchedulers) : UseCase<CartListData>() {

    companion object {
        const val PARAM_SELECTED_CART_ID = "PARAM_SELECTED_CART_ID"

        const val PARAM_KEY_LANG = "lang"
        const val PARAM_VALUE_ID = "id"
        const val PARAM_KEY_SELECTED_CART_ID = "selected_cart_id"
    }

    override fun createObservable(requestParam: RequestParams?): Observable<CartListData> {
        val cartId = requestParam?.getString(PARAM_SELECTED_CART_ID, "") ?: ""
        val variables = mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_KEY_SELECTED_CART_ID to cartId
        )

        val queryString = getQueryCartRevamp()
        val graphqlRequest = GraphqlRequest(queryString, ShopGroupSimplifiedGqlResponse::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
//                    val shopGroupSimplifiedGqlResponse = it.getData<ShopGroupSimplifiedGqlResponse>(ShopGroupSimplifiedGqlResponse::class.java)
                    val shopGroupSimplifiedGqlResponse = Gson().fromJson(RESPONSE, ShopGroupSimplifiedGqlResponse::class.java)
                    if (shopGroupSimplifiedGqlResponse != null) {
                        if (shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.status == "OK") {
                            cartSimplifiedMapper.convertToCartItemDataList(shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.data)
                        } else {
                            if (shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.errorMessages.isNotEmpty()) {
                                throw CartResponseErrorException(shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.errorMessages.joinToString())
                            } else {
                                throw ResponseErrorException()
                            }
                        }
                    } else {
                        throw ResponseErrorException()
                    }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }
}

val RESPONSE = """
    {
      "status": "OK",
      "cart_revamp": {
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
                "id": 1,
                "code": "STARTSHOPPING",
                "message": "Mulai Belanja",
                "color": "green"
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
          "shopping_summary": {
            "total_wording": "Total Harga (6 Barang)",
            "total_value": 753550,
            "discount_total_wording": "Total Diskon Barang",
            "discount_value": 65450,
            "payment_total_wording": "Total Bayar",
            "promo_wording": "Hemat pakai promo",
            "promo_value": 0,
            "seller_cashback_wording": "Cashback Penjual",
            "seller_cashback_value": 0
          },
          "max_quantity": 32767,
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
          "fulfillment_message": "Dilayani Tokopedia",
          "available_section": {
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
                "id": 3,
                "code": "NOTES",
                "message": "Tulis catatan untuk barang ini"
              },
              {
                "id": 4,
                "code": "VALIDATENOTES",
                "message": "Maks. 144 character"
              },
              {
                "id": 10,
                "code": "WISHLISTED",
                "message": "Sudah ada di wishlist"
              }
            ],
            "available_group": [
              {
                "user_address_id": 0,
                "shipment_information": {
                  "shop_location": "Jakarta Barat",
                  "estimation": "",
                  "free_shipping": {
                    "eligible": true,
                    "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                  },
                  "preorder": {
                    "is_preorder": false,
                    "duration": ""
                  }
                },
                "shop": {
                  "shop_alert_message": "",
                  "shop_id": 2916280,
                  "user_id": 4480043,
                  "admin_ids": [],
                  "shop_name": "Kalostee ID",
                  "shop_image": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2018/1/15/4480043/4480043_3fb03a3e-198b-4a5f-9810-408ae23f3c3c.jpeg",
                  "shop_url": "https://www.tokopedia.com/kalostee",
                  "shop_status": 1,
                  "is_gold": 1,
                  "is_official": 0,
                  "is_free_returns": 0,
                  "gold_merchant": {
                    "is_gold": 1,
                    "is_gold_badge": true,
                    "gold_merchant_logo_url": "https://ecs7.tokopedia.net/img/newtkpd/powermerchant/ic-powermerchant-130px.png"
                  },
                  "official_store": {
                    "is_official": 0,
                    "os_logo_url": ""
                  },
                  "address_id": 2253,
                  "postal_code": "11730",
                  "latitude": "-6.133306316962535",
                  "longitude": "106.73358622938395",
                  "district_id": 2253,
                  "district_name": "",
                  "origin": 0,
                  "address_street": "",
                  "province_id": 0,
                  "city_id": 174,
                  "city_name": "Jakarta Barat",
                  "province_name": "",
                  "country_name": "",
                  "is_allow_manage": false,
                  "shop_domain": "kalostee",
                  "shop_shipments": [
                    {
                      "ship_id": 14,
                      "ship_name": "J&T",
                      "ship_code": "jnt",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-jnt.png",
                      "ship_prods": [
                        {
                          "ship_prod_id": 27,
                          "ship_prod_name": "Reguler",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        }
                      ],
                      "is_dropship_enabled": 0
                    },
                    {
                      "ship_id": 23,
                      "ship_name": "AnterAja",
                      "ship_code": "anteraja",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-anteraja.png",
                      "ship_prods": [
                        {
                          "ship_prod_id": 45,
                          "ship_prod_name": "Reguler",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        },
                        {
                          "ship_prod_id": 46,
                          "ship_prod_name": "Next Day",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        }
                      ],
                      "is_dropship_enabled": 0
                    },
                    {
                      "ship_id": 4,
                      "ship_name": "Pos Indonesia",
                      "ship_code": "pos",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-pos.png",
                      "ship_prods": [
                        {
                          "ship_prod_id": 10,
                          "ship_prod_name": "Pos Kilat Khusus",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        }
                      ],
                      "is_dropship_enabled": 0
                    },
                    {
                      "ship_id": 6,
                      "ship_name": "Wahana",
                      "ship_code": "wahana",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-wahana.png",
                      "ship_prods": [
                        {
                          "ship_prod_id": 8,
                          "ship_prod_name": "Service Normal",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        }
                      ],
                      "is_dropship_enabled": 0
                    },
                    {
                      "ship_id": 10,
                      "ship_name": "Gojek",
                      "ship_code": "gojek",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-gosend.png",
                      "ship_prods": [
                        {
                          "ship_prod_id": 20,
                          "ship_prod_name": "Same Day",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        },
                        {
                          "ship_prod_id": 28,
                          "ship_prod_name": "Instant Courier",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        }
                      ],
                      "is_dropship_enabled": 0
                    },
                    {
                      "ship_id": 12,
                      "ship_name": "Ninja Xpress",
                      "ship_code": "ninja",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-ninja.png",
                      "ship_prods": [
                        {
                          "ship_prod_id": 25,
                          "ship_prod_name": "Reguler",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        }
                      ],
                      "is_dropship_enabled": 0
                    },
                    {
                      "ship_id": 13,
                      "ship_name": "GrabExpress",
                      "ship_code": "grab",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-grab.png",
                      "ship_prods": [
                        {
                          "ship_prod_id": 24,
                          "ship_prod_name": "Same Day",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        },
                        {
                          "ship_prod_id": 37,
                          "ship_prod_name": "Instant",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        }
                      ],
                      "is_dropship_enabled": 0
                    },
                    {
                      "ship_id": 16,
                      "ship_name": "REX",
                      "ship_code": "rex",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-rex.png",
                      "ship_prods": [
                        {
                          "ship_prod_id": 32,
                          "ship_prod_name": "REX-10",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        }
                      ],
                      "is_dropship_enabled": 0
                    },
                    {
                      "ship_id": 1,
                      "ship_name": "JNE",
                      "ship_code": "jne",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-jne.png",
                      "ship_prods": [
                        {
                          "ship_prod_id": 1,
                          "ship_prod_name": "Reguler",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        },
                        {
                          "ship_prod_id": 2,
                          "ship_prod_name": "OKE",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        },
                        {
                          "ship_prod_id": 6,
                          "ship_prod_name": "YES",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        }
                      ],
                      "is_dropship_enabled": 0
                    },
                    {
                      "ship_id": 2,
                      "ship_name": "TIKI",
                      "ship_code": "tiki",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-tiki.png",
                      "ship_prods": [
                        {
                          "ship_prod_id": 3,
                          "ship_prod_name": "Reguler",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        },
                        {
                          "ship_prod_id": 16,
                          "ship_prod_name": "Over Night Service",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        }
                      ],
                      "is_dropship_enabled": 0
                    },
                    {
                      "ship_id": 11,
                      "ship_name": "SiCepat",
                      "ship_code": "sicepat",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-sicepat.png",
                      "ship_prods": [
                        {
                          "ship_prod_id": 18,
                          "ship_prod_name": "Regular Package",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        },
                        {
                          "ship_prod_id": 33,
                          "ship_prod_name": "BEST",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        },
                        {
                          "ship_prod_id": 43,
                          "ship_prod_name": "GOKIL",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        },
                        {
                          "ship_prod_id": 44,
                          "ship_prod_name": "Regular Package",
                          "ship_group_id": 0,
                          "ship_group_name": "",
                          "minimum_weight": 0,
                          "additional_fee": 0
                        }
                      ],
                      "is_dropship_enabled": 0
                    }
                  ]
                },
                "promo_codes": [],
                "cart_string": "2916280-0-2338813",
                "cart_details": [
                  {
                    "cart_id": "1890888466",
                    "product": {
                      "product_information": [],
                      "product_tracker_data": {
                        "attribution": "",
                        "tracker_list_name": ""
                      },
                      "isWishlist": false,
                      "product_id": 280292232,
                      "product_name": "Kaos Polos KALOSTEE Premium Basic 28s 100% Cotton Size XXL",
                      "product_price_fmt": "Rp43.000",
                      "product_price": 43000,
                      "parent_id": 0,
                      "category_id": 1808,
                      "category": "Fashion Pria / Atasan Pria / Kaos Pria",
                      "catalog_id": 0,
                      "wholesale_price": [
                        {
                          "qty_min": 6,
                          "qty_min_fmt": "6",
                          "qty_max": 10000,
                          "qty_max_fmt": "10.000",
                          "prd_prc": 40850,
                          "prd_prc_fmt": "Rp40.850"
                        }
                      ],
                      "product_weight": 300,
                      "product_weight_fmt": "300gr",
                      "product_condition": 1,
                      "product_status": 1,
                      "product_url": "https://www.tokopedia.com/kalostee/kaos-polos-kalostee-premium-basic-28s-100-cotton-size-xxl-1",
                      "product_returnable": 0,
                      "is_freereturns": 0,
                      "is_preorder": 0,
                      "product_cashback": "",
                      "product_min_order": 1,
                      "product_max_order": 32767,
                      "product_rating": "0.000000",
                      "product_invenage_value": 999801,
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
                      "price_changes": {
                        "changes_state": 0,
                        "amount_difference": 0,
                        "original_amount": 0,
                        "description": ""
                      },
                      "product_price_currency": 1,
                      "product_image": {
                        "image_src": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/6/27/4480043/4480043_11d64df9-271f-435f-9ac1-7c74f0c6e05a_2048_2048",
                        "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/6/27/4480043/4480043_11d64df9-271f-435f-9ac1-7c74f0c6e05a_2048_2048",
                        "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/6/27/4480043/4480043_11d64df9-271f-435f-9ac1-7c74f0c6e05a_2048_2048",
                        "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/product-1/2020/6/27/4480043/4480043_11d64df9-271f-435f-9ac1-7c74f0c6e05a_2048_2048",
                        "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/product-1/2020/6/27/4480043/4480043_11d64df9-271f-435f-9ac1-7c74f0c6e05a_2048_2048"
                      },
                      "product_all_images": "[{\"file_name\":\"4480043_11d64df9-271f-435f-9ac1-7c74f0c6e05a_2048_2048\",\"file_path\":\"product-1/2020/6/27/4480043\",\"status\":2},{\"file_name\":\"4480043_7ad2a185-7774-4244-9b6f-99a889f04b36_1575_1575\",\"file_path\":\"product-1/2020/6/27/4480043\",\"status\":1}]",
                      "product_notes": "",
                      "product_quantity": 1,
                      "product_weight_unit_code": 1,
                      "product_weight_unit_text": "gr",
                      "last_update_price": "1546489468",
                      "is_update_price": false,
                      "product_preorder": {
                        "duration_day": "0",
                        "duration_text": "",
                        "duration_unit_code": "0",
                        "duration_unit_text": "",
                        "duration_value": "0"
                      },
                      "product_showcase": {
                        "name": "KALSOTEE - PREMIUM BASIC",
                        "id": 11918922
                      },
                      "product_alias": "kaos-polos-kalostee-premium-basic-28s-100-cotton-size-xxl-1",
                      "sku": "",
                      "campaign_id": 0,
                      "product_original_price": 0,
                      "product_price_original_fmt": "",
                      "is_slash_price": false,
                      "free_returns": {
                        "free_returns_logo": ""
                      },
                      "product_finsurance": 0,
                      "is_wishlisted": false,
                      "is_ppp": false,
                      "is_cod": false,
                      "warehouse_id": 2338813,
                      "is_parent": false,
                      "is_campaign_error": false,
                      "is_blacklisted": false,
                      "free_shipping": {
                        "eligible": true,
                        "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                      },
                      "booking_stock": 0,
                      "is_product_volume_weight": false,
                      "initial_price": 0,
                      "initial_price_fmt": "Rp0",
                      "slash_price_label": "",
                      "product_warning_message": "",
                      "product_alert_message": "",
                      "variant_description_detail": {
                        "variant_name": [],
                        "variant_description": ""
                      }
                    },
                    "errors": [],
                    "messages": [],
                    "checkbox_state": true
                  }
                ],
                "total_cart_details_error": 0,
                "total_cart_price": 43000,
                "errors": [],
                "sort_key": 1890888466,
                "is_fulfillment_service": false,
                "warehouse": {
                  "warehouse_id": 2338813,
                  "partner_id": 0,
                  "shop_id": 2916280,
                  "warehouse_name": "Shop Location",
                  "district_id": 2253,
                  "district_name": "Cengkareng",
                  "city_id": 174,
                  "city_name": "Jakarta Barat",
                  "province_id": 13,
                  "province_name": "DKI Jakarta",
                  "status": 1,
                  "postal_code": "11730",
                  "is_default": 1,
                  "latlon": "-6.133306316962535,106.73358622938395",
                  "latitude": "-6.133306316962535",
                  "longitude": "106.73358622938395",
                  "email": "",
                  "address_detail": "Ruko Hawaii City Resort, Blok B No. 22, Cengkareng - Jakarta Barat",
                  "country_name": "Indonesia",
                  "is_fulfillment": false
                },
                "checkbox_state": true
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
              "title": "Barang ini dilarang",
              "selected_unavailable_action_id": 11,
              "unavailable_description": "Tidak bisa dibeli melalui aplikasi Android",
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
                  "id": 10,
                  "code": "WISHLISTED",
                  "message": "Sudah ada di wishlist"
                },
                {
                  "id": 11,
                  "code": "FOLLOWSHOP",
                  "message": "Follow Toko"
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
                    "preorder": {
                      "is_preorder": false,
                      "duration": ""
                    }
                  },
                  "shop": {
                    "shop_alert_message": "",
                    "shop_id": 423243,
                    "user_id": 3262022,
                    "admin_ids": [],
                    "shop_name": "Toko Pucang",
                    "shop_image": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2016/9/11/423243/423243_5d6f5e6f-4a5c-41f9-8cfd-c2b8561c606f.jpg",
                    "shop_url": "https://www.tokopedia.com/agroup",
                    "shop_status": 1,
                    "is_gold": 1,
                    "is_official": 0,
                    "is_free_returns": 0,
                    "gold_merchant": {
                      "is_gold": 1,
                      "is_gold_badge": true,
                      "gold_merchant_logo_url": "https://ecs7.tokopedia.net/img/newtkpd/powermerchant/ic-powermerchant-130px.png"
                    },
                    "official_store": {
                      "is_official": 0,
                      "os_logo_url": ""
                    },
                    "address_id": 2601,
                    "postal_code": "56195",
                    "latitude": "-7.418575684156216",
                    "longitude": "110.25589026510715",
                    "district_id": 2601,
                    "district_name": "",
                    "origin": 0,
                    "address_street": "",
                    "province_id": 0,
                    "city_id": 195,
                    "city_name": "Kab. Magelang",
                    "province_name": "",
                    "country_name": "",
                    "is_allow_manage": false,
                    "shop_domain": "agroup",
                    "shop_shipments": [
                      {
                        "ship_id": 1,
                        "ship_name": "JNE",
                        "ship_code": "jne",
                        "ship_logo": "https://ecs7.tokopedia.net/img/kurir-jne.png",
                        "ship_prods": [
                          {
                            "ship_prod_id": 1,
                            "ship_prod_name": "Reguler",
                            "ship_group_id": 0,
                            "ship_group_name": "",
                            "minimum_weight": 0,
                            "additional_fee": 0
                          },
                          {
                            "ship_prod_id": 2,
                            "ship_prod_name": "OKE",
                            "ship_group_id": 0,
                            "ship_group_name": "",
                            "minimum_weight": 0,
                            "additional_fee": 0
                          },
                          {
                            "ship_prod_id": 6,
                            "ship_prod_name": "YES",
                            "ship_group_id": 0,
                            "ship_group_name": "",
                            "minimum_weight": 0,
                            "additional_fee": 0
                          },
                          {
                            "ship_prod_id": 22,
                            "ship_prod_name": "JNE Trucking",
                            "ship_group_id": 0,
                            "ship_group_name": "",
                            "minimum_weight": 0,
                            "additional_fee": 0
                          }
                        ],
                        "is_dropship_enabled": 0
                      },
                      {
                        "ship_id": 4,
                        "ship_name": "Pos Indonesia",
                        "ship_code": "pos",
                        "ship_logo": "https://ecs7.tokopedia.net/img/kurir-pos.png",
                        "ship_prods": [
                          {
                            "ship_prod_id": 10,
                            "ship_prod_name": "Pos Kilat Khusus",
                            "ship_group_id": 0,
                            "ship_group_name": "",
                            "minimum_weight": 0,
                            "additional_fee": 0
                          }
                        ],
                        "is_dropship_enabled": 0
                      },
                      {
                        "ship_id": 6,
                        "ship_name": "Wahana",
                        "ship_code": "wahana",
                        "ship_logo": "https://ecs7.tokopedia.net/img/kurir-wahana.png",
                        "ship_prods": [
                          {
                            "ship_prod_id": 8,
                            "ship_prod_name": "Service Normal",
                            "ship_group_id": 0,
                            "ship_group_name": "",
                            "minimum_weight": 0,
                            "additional_fee": 0
                          }
                        ],
                        "is_dropship_enabled": 0
                      },
                      {
                        "ship_id": 10,
                        "ship_name": "Gojek",
                        "ship_code": "gojek",
                        "ship_logo": "https://ecs7.tokopedia.net/img/kurir-gosend.png",
                        "ship_prods": [
                          {
                            "ship_prod_id": 28,
                            "ship_prod_name": "Instant Courier",
                            "ship_group_id": 0,
                            "ship_group_name": "",
                            "minimum_weight": 0,
                            "additional_fee": 0
                          }
                        ],
                        "is_dropship_enabled": 0
                      },
                      {
                        "ship_id": 11,
                        "ship_name": "SiCepat",
                        "ship_code": "sicepat",
                        "ship_logo": "https://ecs7.tokopedia.net/img/kurir-sicepat.png",
                        "ship_prods": [
                          {
                            "ship_prod_id": 18,
                            "ship_prod_name": "Regular Package",
                            "ship_group_id": 0,
                            "ship_group_name": "",
                            "minimum_weight": 0,
                            "additional_fee": 0
                          },
                          {
                            "ship_prod_id": 43,
                            "ship_prod_name": "GOKIL",
                            "ship_group_id": 0,
                            "ship_group_name": "",
                            "minimum_weight": 0,
                            "additional_fee": 0
                          },
                          {
                            "ship_prod_id": 44,
                            "ship_prod_name": "Regular Package",
                            "ship_group_id": 0,
                            "ship_group_name": "",
                            "minimum_weight": 0,
                            "additional_fee": 0
                          }
                        ],
                        "is_dropship_enabled": 0
                      },
                      {
                        "ship_id": 14,
                        "ship_name": "J&T",
                        "ship_code": "jnt",
                        "ship_logo": "https://ecs7.tokopedia.net/img/kurir-jnt.png",
                        "ship_prods": [
                          {
                            "ship_prod_id": 27,
                            "ship_prod_name": "Reguler",
                            "ship_group_id": 0,
                            "ship_group_name": "",
                            "minimum_weight": 0,
                            "additional_fee": 0
                          }
                        ],
                        "is_dropship_enabled": 0
                      }
                    ]
                  },
                  "promo_codes": [],
                  "cart_string": "423243-0-337586",
                  "cart_details": [
                    {
                      "cart_id": "1924615534",
                      "selected_unavailable_action_link": "",
                      "product": {
                        "product_information": [],
                        "product_tracker_data": {
                          "attribution": "",
                          "tracker_list_name": ""
                        },
                        "isWishlist": false,
                        "product_id": 53864426,
                        "product_name": "Rokok Klobot dari Gudang Garam | GG Kelobot",
                        "product_price_fmt": "Rp9.000",
                        "product_price": 9000,
                        "parent_id": 0,
                        "category_id": 4398,
                        "category": "Kesehatan / Produk Dewasa / Produk Dewasa Lainnya",
                        "catalog_id": 0,
                        "wholesale_price": [
                          {
                            "qty_min": 5,
                            "qty_min_fmt": "5",
                            "qty_max": 10000,
                            "qty_max_fmt": "10.000",
                            "prd_prc": 8000,
                            "prd_prc_fmt": "Rp8.000"
                          }
                        ],
                        "product_weight": 35,
                        "product_weight_fmt": "35gr",
                        "product_condition": 1,
                        "product_status": 1,
                        "product_url": "https://www.tokopedia.com/agroup/rokok-klobot-dari-gudang-garam-gg-kelobot",
                        "product_returnable": 0,
                        "is_freereturns": 0,
                        "is_preorder": 0,
                        "product_cashback": "",
                        "product_min_order": 1,
                        "product_max_order": 32767,
                        "product_rating": "0.000000",
                        "product_invenage_value": 999393,
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
                        "price_changes": {
                          "changes_state": 0,
                          "amount_difference": 0,
                          "original_amount": 9000,
                          "description": "Harga Normal"
                        },
                        "product_price_currency": 1,
                        "product_image": {
                          "image_src": "https://ecs7.tokopedia.net/img/cache/700/product-1/2016/7/30/3262022/3262022_555672dc-f183-4498-b830-ef75513a2001.jpg",
                          "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2016/7/30/3262022/3262022_555672dc-f183-4498-b830-ef75513a2001.jpg",
                          "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2016/7/30/3262022/3262022_555672dc-f183-4498-b830-ef75513a2001.jpg",
                          "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/product-1/2016/7/30/3262022/3262022_555672dc-f183-4498-b830-ef75513a2001.jpg",
                          "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/product-1/2016/7/30/3262022/3262022_555672dc-f183-4498-b830-ef75513a2001.jpg"
                        },
                        "product_all_images": "[{\"file_name\":\"3262022_555672dc-f183-4498-b830-ef75513a2001.jpg\",\"file_path\":\"product-1/2016/7/30/3262022\",\"status\":2}]",
                        "product_notes": "",
                        "product_quantity": 1,
                        "product_weight_unit_code": 1,
                        "product_weight_unit_text": "gr",
                        "last_update_price": "1576989915",
                        "is_update_price": false,
                        "product_preorder": {
                          "duration_day": "0",
                          "duration_text": "",
                          "duration_unit_code": "0",
                          "duration_unit_text": "",
                          "duration_value": "0"
                        },
                        "product_showcase": {
                          "name": "Rokok dan tembakau",
                          "id": 3945730
                        },
                        "product_alias": "rokok-klobot-dari-gudang-garam-gg-kelobot",
                        "sku": "",
                        "campaign_id": 0,
                        "product_original_price": 0,
                        "product_price_original_fmt": "",
                        "is_slash_price": false,
                        "free_returns": {
                          "free_returns_logo": ""
                        },
                        "product_finsurance": 0,
                        "is_wishlisted": false,
                        "is_ppp": false,
                        "is_cod": false,
                        "warehouse_id": 337586,
                        "is_parent": false,
                        "is_campaign_error": false,
                        "is_blacklisted": true,
                        "free_shipping": {
                          "eligible": false,
                          "badge_url": ""
                        },
                        "booking_stock": 0,
                        "is_product_volume_weight": false,
                        "initial_price": 9000,
                        "initial_price_fmt": "Rp9.000",
                        "slash_price_label": "",
                        "product_warning_message": "",
                        "product_alert_message": "",
                        "variant_description_detail": {
                          "variant_name": [],
                          "variant_description": ""
                        }
                      },
                      "errors": [
                        "Dilarang"
                      ],
                      "messages": [],
                      "checkbox_state": true
                    }
                  ],
                  "total_cart_details_error": 0,
                  "total_cart_price": 9000,
                  "errors": [],
                  "sort_key": 1924615534,
                  "is_fulfillment_service": false,
                  "warehouse": {
                    "warehouse_id": 337586,
                    "partner_id": 0,
                    "shop_id": 423243,
                    "warehouse_name": "Shop Location",
                    "district_id": 2601,
                    "district_name": "Secang",
                    "city_id": 195,
                    "city_name": "Kab. Magelang",
                    "province_id": 14,
                    "province_name": "Jawa Tengah",
                    "status": 1,
                    "postal_code": "56195",
                    "is_default": 1,
                    "latlon": "-7.418575684156216,110.25589026510715",
                    "latitude": "-7.418575684156216",
                    "longitude": "110.25589026510715",
                    "email": "",
                    "address_detail": "AGEN JNE Pucang Jl Raya Pasar Pucang No 17a Karang Wetan Pucang Secang Magelang, 56195",
                    "country_name": "Indonesia",
                    "is_fulfillment": false
                  },
                  "checkbox_state": true
                }
              ]
            }
          ],
          "total_product_price": 762550,
          "total_product_count": 7,
          "total_product_error": 0,
          "global_coupon_attr": {
            "description": "Gunakan promo Tokopedia",
            "quantity_label": "11 Kupon"
          },
          "global_checkbox_state": false,
          "tickers": [
            {
              "id": 0,
              "message": "Hai Member Gold, kuota Bebas Ongkir kamu sisa 6x (untuk 1 pesanan/transaksi) buat minggu ini.",
              "page": "cart"
            }
          ],
          "hashed_email": "bafd5794d0087249cab2cc01536457ce",
          "promo": {
            "last_apply": {
              "data": {
                "global_success": true,
                "success": true,
                "message": {
                  "state": "",
                  "color": "",
                  "text": ""
                },
                "codes": [],
                "promo_code_id": 0,
                "title_description": "",
                "discount_amount": 0,
                "cashback_wallet_amount": 0,
                "cashback_advocate_referral_amount": 0,
                "cashback_voucher_description": "",
                "invoice_description": "",
                "is_coupon": 0,
                "gateway_id": "",
                "is_tokopedia_gerai": false,
                "clashing_info_detail": {
                  "clash_message": "",
                  "clash_reason": "",
                  "is_clashed_promos": false,
                  "options": []
                },
                "tokopoints_detail": {
                  "conversion_rate": {
                    "rate": 0,
                    "points_coefficient": 0,
                    "external_currency_coefficient": 0
                  }
                },
                "voucher_orders": [],
                "additional_info": {
                  "message_info": {
                    "message": "Makin hemat pakai promo",
                    "detail": ""
                  },
                  "error_detail": {
                    "message": ""
                  },
                  "empty_cart_info": {
                    "image_url": "",
                    "message": "",
                    "detail": ""
                  },
                  "usage_summaries": [],
                  "sp_ids": []
                }
              },
              "code": "200000"
            },
            "error_default": {
              "title": "promo tidak dapat digunakan",
              "description": "silahkan coba beberapa saat lagi"
            }
          },
          "ab_test_button": {
            "enable": false
          }
        }
      }
    }
""".trimIndent()