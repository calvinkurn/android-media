package com.tokopedia.cart.domain.usecase

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
                    val shopGroupSimplifiedGqlResponse = it.getData<ShopGroupSimplifiedGqlResponse>(ShopGroupSimplifiedGqlResponse::class.java)
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
                  "shop_location": "Kota Tangerang Selatan",
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
                  "shop_id": 5665147,
                  "user_id": 29240564,
                  "admin_ids": [],
                  "shop_name": "L'oreal Paris",
                  "shop_image": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2019/3/25/5665147/5665147_aee092b5-c126-47b8-82f9-d6b99487a378.jpg",
                  "shop_url": "https://www.tokopedia.com/lorealparis",
                  "shop_status": 1,
                  "is_gold": 1,
                  "is_official": 1,
                  "is_free_returns": 0,
                  "gold_merchant": {
                    "is_gold": 0,
                    "is_gold_badge": false,
                    "gold_merchant_logo_url": ""
                  },
                  "official_store": {
                    "is_official": 1,
                    "os_logo_url": "https://ecs7.tokopedia.net/img/official_store/badge_os128.png"
                  },
                  "address_id": 5583,
                  "postal_code": "15314",
                  "latitude": "-6.3357202",
                  "longitude": "106.67664930000001",
                  "district_id": 5583,
                  "district_name": "",
                  "origin": 0,
                  "address_street": "",
                  "province_id": 0,
                  "city_id": 463,
                  "city_name": "Kota Tangerang Selatan",
                  "province_name": "",
                  "country_name": "",
                  "is_allow_manage": false,
                  "shop_domain": "lorealparis",
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
                        }
                      ],
                      "is_dropship_enabled": 0
                    }
                  ]
                },
                "promo_codes": [],
                "cart_string": "5665147-0-5945262",
                "cart_details": [
                  {
                    "cart_id": "1908450794",
                    "product": {
                      "product_information": [],
                      "product_tracker_data": {
                        "attribution": "",
                        "tracker_list_name": ""
                      },
                      "isWishlist": false,
                      "product_id": 698116189,
                      "product_name": "L'Oreal Paris Fall Resist 3X Anti Hair-Fall Shampoo 450 ml Twin Pack",
                      "product_price_fmt": "Rp 77.550",
                      "product_price": 77550,
                      "parent_id": 0,
                      "category_id": 2174,
                      "category": "Perawatan Tubuh / Perawatan Rambut / Shampoo",
                      "catalog_id": 0,
                      "wholesale_price": [],
                      "product_weight": 1100,
                      "product_weight_fmt": "1100gr",
                      "product_condition": 1,
                      "product_status": 1,
                      "product_url": "https://www.tokopedia.com/lorealparis/l-oreal-paris-fall-resist-3x-anti-hair-fall-shampoo-450-ml-twin-pack",
                      "product_returnable": 0,
                      "is_freereturns": 0,
                      "is_preorder": 0,
                      "product_cashback": "",
                      "product_min_order": 1,
                      "product_max_order": 297,
                      "product_rating": "0.000000",
                      "product_invenage_value": 297,
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
                        "image_src": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/9/15/29240564/29240564_64c7fcb3-8eb8-4ba8-9fab-205e5ab27e28_1300_1300",
                        "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/9/15/29240564/29240564_64c7fcb3-8eb8-4ba8-9fab-205e5ab27e28_1300_1300",
                        "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/9/15/29240564/29240564_64c7fcb3-8eb8-4ba8-9fab-205e5ab27e28_1300_1300",
                        "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/product-1/2020/9/15/29240564/29240564_64c7fcb3-8eb8-4ba8-9fab-205e5ab27e28_1300_1300",
                        "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/product-1/2020/9/15/29240564/29240564_64c7fcb3-8eb8-4ba8-9fab-205e5ab27e28_1300_1300"
                      },
                      "product_all_images": "[{\"file_name\":\"29240564_64c7fcb3-8eb8-4ba8-9fab-205e5ab27e28_1300_1300\",\"file_path\":\"product-1/2020/9/15/29240564\",\"status\":2},{\"file_name\":\"29240564_8e53713f-4825-45ee-8be3-43023f10e697_1300_1300\",\"file_path\":\"product-1/2020/9/15/29240564\",\"status\":1},{\"file_name\":\"29240564_685cab6c-b2ee-4940-9b4c-f0f4cda53573_1300_1300\",\"file_path\":\"product-1/2020/9/15/29240564\",\"status\":1},{\"file_name\":\"29240564_e824f495-12d2-46b4-a6fe-a872690310c8_1300_1300\",\"file_path\":\"product-1/2020/9/15/29240564\",\"status\":1}]",
                      "product_notes": "",
                      "product_quantity": 1,
                      "product_weight_unit_code": 1,
                      "product_weight_unit_text": "gr",
                      "last_update_price": "1581670318",
                      "is_update_price": false,
                      "product_preorder": {
                        "duration_day": "0",
                        "duration_text": "",
                        "duration_unit_code": "0",
                        "duration_unit_text": "",
                        "duration_value": "0"
                      },
                      "product_showcase": {
                        "name": "Hair Care",
                        "id": 18692524
                      },
                      "product_alias": "l-oreal-paris-fall-resist-3x-anti-hair-fall-shampoo-450-ml-twin-pack",
                      "sku": "LRA1A36002",
                      "campaign_id": -10000,
                      "product_original_price": 141000,
                      "product_price_original_fmt": "Rp 141.000",
                      "is_slash_price": false,
                      "free_returns": {
                        "free_returns_logo": ""
                      },
                      "product_finsurance": 0,
                      "is_wishlisted": false,
                      "is_ppp": false,
                      "is_cod": false,
                      "warehouse_id": 5945262,
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
                      "slash_price_label": "45%",
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
                "total_cart_price": 77550,
                "errors": [],
                "sort_key": 1908450794,
                "is_fulfillment_service": false,
                "warehouse": {
                  "warehouse_id": 5945262,
                  "partner_id": 0,
                  "shop_id": 5665147,
                  "warehouse_name": "Shop Location",
                  "district_id": 5583,
                  "district_name": "Setu",
                  "city_id": 463,
                  "city_name": "Kota Tangerang Selatan",
                  "province_id": 11,
                  "province_name": "Banten",
                  "status": 1,
                  "postal_code": "15314",
                  "is_default": 1,
                  "latlon": "-6.3357202,106.67664930000001",
                  "latitude": "-6.3357202",
                  "longitude": "106.67664930000001",
                  "email": "",
                  "address_detail": "Sirclo Warehouse, Jl. Taman Tekno Blok D2 No.2, Kel Setu, Kec Setu, Kota Tangerang Selatan Banten 15314",
                  "country_name": "Indonesia",
                  "is_fulfillment": false
                },
                "checkbox_state": true
              },
              {
                "user_address_id": 0,
                "shipment_information": {
                  "shop_location": "Kab. Cirebon",
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
                  "shop_id": 9153664,
                  "user_id": 107884083,
                  "admin_ids": [],
                  "shop_name": "faris olstore",
                  "shop_image": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2020/7/24/9153664/9153664_1eb3cbe9-a2cc-447b-a2a0-b89c0fb68aa7.jpg",
                  "shop_url": "https://www.tokopedia.com/farisolstore-1",
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
                  "address_id": 1822,
                  "postal_code": "45162",
                  "latitude": "-6.6550845",
                  "longitude": "108.4158327",
                  "district_id": 1822,
                  "district_name": "",
                  "origin": 0,
                  "address_street": "",
                  "province_id": 0,
                  "city_id": 154,
                  "city_name": "Kab. Cirebon",
                  "province_name": "",
                  "country_name": "",
                  "is_allow_manage": false,
                  "shop_domain": "farisolstore-1",
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
                        },
                        {
                          "ship_prod_id": 51,
                          "ship_prod_name": "HALU",
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
                          "ship_prod_id": 37,
                          "ship_prod_name": "Instant",
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
                "cart_string": "9153664-0-9291388",
                "cart_details": [
                  {
                    "cart_id": "1905192274",
                    "product": {
                      "product_information": [],
                      "product_tracker_data": {
                        "attribution": "",
                        "tracker_list_name": ""
                      },
                      "isWishlist": false,
                      "product_id": 1122668709,
                      "product_name": "rok plisket - Mocca, all size",
                      "product_price_fmt": "Rp23.000",
                      "product_price": 23000,
                      "parent_id": 1036424435,
                      "category_id": 3009,
                      "category": "Fashion Wanita / Bawahan Wanita / Rok Wanita",
                      "catalog_id": 0,
                      "wholesale_price": [],
                      "product_weight": 225,
                      "product_weight_fmt": "225gr",
                      "product_condition": 1,
                      "product_status": 1,
                      "product_url": "https://www.tokopedia.com/farisolstore-1/rok-plisket-mocca-all-size",
                      "product_returnable": 0,
                      "is_freereturns": 0,
                      "is_preorder": 0,
                      "product_cashback": "",
                      "product_min_order": 1,
                      "product_max_order": 32767,
                      "product_rating": "0.000000",
                      "product_invenage_value": 95,
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
                        "changes_state": 1,
                        "amount_difference": 2000,
                        "original_amount": 21000,
                        "description": "Harga Naik"
                      },
                      "product_price_currency": 1,
                      "product_image": {
                        "image_src": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/7/19/9153664/9153664_e70eaaf5-ea8e-41e5-90a1-06137bfcb23e_536_536.jpg",
                        "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/7/19/9153664/9153664_e70eaaf5-ea8e-41e5-90a1-06137bfcb23e_536_536.jpg",
                        "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/7/19/9153664/9153664_e70eaaf5-ea8e-41e5-90a1-06137bfcb23e_536_536.jpg",
                        "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/product-1/2020/7/19/9153664/9153664_e70eaaf5-ea8e-41e5-90a1-06137bfcb23e_536_536.jpg",
                        "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/product-1/2020/7/19/9153664/9153664_e70eaaf5-ea8e-41e5-90a1-06137bfcb23e_536_536.jpg"
                      },
                      "product_all_images": "[{\"file_name\":\"9153664_e70eaaf5-ea8e-41e5-90a1-06137bfcb23e_536_536.jpg\",\"file_path\":\"product-1/2020/7/19/9153664\",\"status\":2},{\"file_name\":\"9153664_01ccc854-ff5b-42f2-b647-da5000a946a8_700_700.jpg\",\"file_path\":\"product-1/2020/7/19/9153664\",\"status\":1},{\"file_name\":\"9153664_678c0ad7-8592-4a90-8158-a2a67591c2ca_554_554.jpg\",\"file_path\":\"product-1/2020/7/19/9153664\",\"status\":1},{\"file_name\":\"58c4533a-8dca-469e-b9ca-413fc30af325.jpg\",\"file_path\":\"VqbcmM/2020/9/11\",\"status\":1}]",
                      "product_notes": "",
                      "product_quantity": 1,
                      "product_weight_unit_code": 1,
                      "product_weight_unit_text": "gr",
                      "last_update_price": "1601823124",
                      "is_update_price": true,
                      "product_preorder": {
                        "duration_day": "0",
                        "duration_text": "",
                        "duration_unit_code": "0",
                        "duration_unit_text": "",
                        "duration_value": "0"
                      },
                      "product_showcase": {
                        "name": "Rok",
                        "id": 25541101
                      },
                      "product_alias": "rok-plisket-mocca-all-size",
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
                      "warehouse_id": 9291388,
                      "is_parent": false,
                      "is_campaign_error": false,
                      "is_blacklisted": false,
                      "free_shipping": {
                        "eligible": true,
                        "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                      },
                      "booking_stock": 0,
                      "is_product_volume_weight": false,
                      "initial_price": 21000,
                      "initial_price_fmt": "Rp21.000",
                      "slash_price_label": "",
                      "product_warning_message": "",
                      "product_alert_message": "",
                      "variant_description_detail": {
                        "variant_name": [
                          "Mocca",
                          "all size"
                        ],
                        "variant_description": "Mocca, all size"
                      }
                    },
                    "errors": [],
                    "messages": [
                      "Harga produk ini telah berubah"
                    ],
                    "checkbox_state": true
                  }
                ],
                "total_cart_details_error": 0,
                "total_cart_price": 23000,
                "errors": [],
                "sort_key": 1905192274,
                "is_fulfillment_service": false,
                "warehouse": {
                  "warehouse_id": 9291388,
                  "partner_id": 0,
                  "shop_id": 9153664,
                  "warehouse_name": "Shop Location",
                  "district_id": 1822,
                  "district_name": "Arjawinangun",
                  "city_id": 154,
                  "city_name": "Kab. Cirebon",
                  "province_id": 12,
                  "province_name": "Jawa Barat",
                  "status": 1,
                  "postal_code": "45162",
                  "is_default": 1,
                  "latlon": "-6.6550845,108.4158327",
                  "latitude": "-6.6550845",
                  "longitude": "108.4158327",
                  "email": "",
                  "address_detail": "Jenun, Kec. Arjawinangun, Cirebon, Jawa Barat, 45162",
                  "country_name": "Indonesia",
                  "is_fulfillment": false
                },
                "checkbox_state": true
              },
              {
                "user_address_id": 0,
                "shipment_information": {
                  "shop_location": "Jakarta Selatan",
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
                  "shop_id": 886437,
                  "user_id": 2013421,
                  "admin_ids": [],
                  "shop_name": "Cavalese",
                  "shop_image": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2017/4/9/886437/886437_46c963c6-aad3-496e-9240-f1dc7e57e18e.jpg",
                  "shop_url": "https://www.tokopedia.com/221b",
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
                  "address_id": 2262,
                  "postal_code": "12410",
                  "latitude": "-6.277926485700288",
                  "longitude": "106.80641457438469",
                  "district_id": 2262,
                  "district_name": "",
                  "origin": 0,
                  "address_street": "",
                  "province_id": 0,
                  "city_id": 175,
                  "city_name": "Jakarta Selatan",
                  "province_name": "",
                  "country_name": "",
                  "is_allow_manage": false,
                  "shop_domain": "221b",
                  "shop_shipments": [
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
                        },
                        {
                          "ship_prod_id": 49,
                          "ship_prod_name": "Same Day",
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
                    }
                  ]
                },
                "promo_codes": [],
                "cart_string": "886437-0-479536",
                "cart_details": [
                  {
                    "cart_id": "1901152917",
                    "product": {
                      "product_information": [],
                      "product_tracker_data": {
                        "attribution": "",
                        "tracker_list_name": ""
                      },
                      "isWishlist": false,
                      "product_id": 343231068,
                      "product_name": "Dodocool Macbook Pro USB C Hub 7 in 1 DC53 Hyperdrive",
                      "product_price_fmt": "Rp550.000",
                      "product_price": 550000,
                      "parent_id": 0,
                      "category_id": 3897,
                      "category": "Komputer & Laptop / Kabel & Adaptor / USB Hub & Extension",
                      "catalog_id": 0,
                      "wholesale_price": [],
                      "product_weight": 200,
                      "product_weight_fmt": "200gr",
                      "product_condition": 1,
                      "product_status": 1,
                      "product_url": "https://www.tokopedia.com/221b/dodocool-macbook-pro-usb-c-hub-7-in-1-dc53-hyperdrive",
                      "product_returnable": 0,
                      "is_freereturns": 0,
                      "is_preorder": 0,
                      "product_cashback": "",
                      "product_min_order": 1,
                      "product_max_order": 32767,
                      "product_rating": "0.000000",
                      "product_invenage_value": 28,
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
                        "image_src": "https://ecs7.tokopedia.net/img/cache/700/product-1/2018/10/10/2013421/2013421_048dd150-0a25-4674-ba60-ea3a355f7259_559_531.png",
                        "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2018/10/10/2013421/2013421_048dd150-0a25-4674-ba60-ea3a355f7259_559_531.png",
                        "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2018/10/10/2013421/2013421_048dd150-0a25-4674-ba60-ea3a355f7259_559_531.png",
                        "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/product-1/2018/10/10/2013421/2013421_048dd150-0a25-4674-ba60-ea3a355f7259_559_531.png",
                        "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/product-1/2018/10/10/2013421/2013421_048dd150-0a25-4674-ba60-ea3a355f7259_559_531.png"
                      },
                      "product_all_images": "[{\"file_name\":\"2013421_048dd150-0a25-4674-ba60-ea3a355f7259_559_531.png\",\"file_path\":\"product-1/2018/10/10/2013421\",\"status\":2},{\"file_name\":\"2013421_dd0acc24-7f08-4c44-9d2d-89b5db96b78f_504_550.png\",\"file_path\":\"product-1/2018/10/10/2013421\",\"status\":1},{\"file_name\":\"2013421_3de0702d-34b0-4102-bf71-f7a3ac7949d7_559_562.png\",\"file_path\":\"product-1/2018/10/10/2013421\",\"status\":1}]",
                      "product_notes": "",
                      "product_quantity": 1,
                      "product_weight_unit_code": 1,
                      "product_weight_unit_text": "gr",
                      "last_update_price": "1586760403",
                      "is_update_price": false,
                      "product_preorder": {
                        "duration_day": "0",
                        "duration_text": "",
                        "duration_unit_code": "0",
                        "duration_unit_text": "",
                        "duration_value": "0"
                      },
                      "product_showcase": {
                        "name": "Aksesoris PC",
                        "id": 7274599
                      },
                      "product_alias": "dodocool-macbook-pro-usb-c-hub-7-in-1-dc53-hyperdrive",
                      "sku": "US020903",
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
                      "warehouse_id": 479536,
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
                "total_cart_price": 550000,
                "errors": [],
                "sort_key": 1901152917,
                "is_fulfillment_service": false,
                "warehouse": {
                  "warehouse_id": 479536,
                  "partner_id": 0,
                  "shop_id": 886437,
                  "warehouse_name": "Shop Location",
                  "district_id": 2262,
                  "district_name": "Cilandak",
                  "city_id": 175,
                  "city_name": "Jakarta Selatan",
                  "province_id": 13,
                  "province_name": "DKI Jakarta",
                  "status": 1,
                  "postal_code": "12410",
                  "is_default": 1,
                  "latlon": "-6.277926485700288,106.80641457438469",
                  "latitude": "-6.277926485700288",
                  "longitude": "106.80641457438469",
                  "email": "",
                  "address_detail": "Toko Bangunan Mutiara JayaJl. Cipete Raya No.40",
                  "country_name": "Indonesia",
                  "is_fulfillment": false
                },
                "checkbox_state": true
              },
              {
                "user_address_id": 0,
                "shipment_information": {
                  "shop_location": "Kab. Bandung",
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
                  "shop_id": 9167703,
                  "user_id": 108486668,
                  "admin_ids": [],
                  "shop_name": "dhabithstore",
                  "shop_image": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2020/10/3/9167703/9167703_86a90a4b-fb31-41e9-8a6e-50d9a3a0f859.jpg",
                  "shop_url": "https://www.tokopedia.com/dhabithstore",
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
                  "address_id": 1660,
                  "postal_code": "40239",
                  "latitude": "-6.9650181",
                  "longitude": "107.5940717",
                  "district_id": 1660,
                  "district_name": "",
                  "origin": 0,
                  "address_street": "",
                  "province_id": 0,
                  "city_id": 148,
                  "city_name": "Kab. Bandung",
                  "province_name": "",
                  "country_name": "",
                  "is_allow_manage": false,
                  "shop_domain": "dhabithstore",
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
                        },
                        {
                          "ship_prod_id": 51,
                          "ship_prod_name": "HALU",
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
                    }
                  ]
                },
                "promo_codes": [],
                "cart_string": "9167703-0-9304751",
                "cart_details": [
                  {
                    "cart_id": "1891383084",
                    "product": {
                      "product_information": [],
                      "product_tracker_data": {
                        "attribution": "",
                        "tracker_list_name": ""
                      },
                      "isWishlist": false,
                      "product_id": 1162107281,
                      "product_name": "BAJU KAOS PRIA COTTON COMBET 30,S FASHION CASUAL PRIA BAJU DISTRO 110 - Merah, XL",
                      "product_price_fmt": "Rp56.500",
                      "product_price": 56500,
                      "parent_id": 1162107267,
                      "category_id": 1808,
                      "category": "Fashion Pria / Atasan Pria / Kaos Pria",
                      "catalog_id": 0,
                      "wholesale_price": [],
                      "product_weight": 160,
                      "product_weight_fmt": "160gr",
                      "product_condition": 1,
                      "product_status": 1,
                      "product_url": "https://www.tokopedia.com/dhabithstore/baju-kaos-pria-cotton-combet-30-s-fashion-casual-pria-baju-distro-110-merah-xl",
                      "product_returnable": 0,
                      "is_freereturns": 0,
                      "is_preorder": 0,
                      "product_cashback": "",
                      "product_min_order": 1,
                      "product_max_order": 32767,
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
                      "price_changes": {
                        "changes_state": 0,
                        "amount_difference": 0,
                        "original_amount": 0,
                        "description": ""
                      },
                      "product_price_currency": 1,
                      "product_image": {
                        "image_src": "https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2020/9/9/67e41df6-a8a2-4533-a11a-df7100b9b3c4.jpg",
                        "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2020/9/9/67e41df6-a8a2-4533-a11a-df7100b9b3c4.jpg",
                        "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/VqbcmM/2020/9/9/67e41df6-a8a2-4533-a11a-df7100b9b3c4.jpg",
                        "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/VqbcmM/2020/9/9/67e41df6-a8a2-4533-a11a-df7100b9b3c4.jpg",
                        "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/VqbcmM/2020/9/9/67e41df6-a8a2-4533-a11a-df7100b9b3c4.jpg"
                      },
                      "product_all_images": "[{\"file_name\":\"67e41df6-a8a2-4533-a11a-df7100b9b3c4.jpg\",\"file_path\":\"VqbcmM/2020/9/9\",\"status\":2},{\"file_name\":\"8f5bcfab-e9b7-4984-8ed4-2015b23f3848.jpg\",\"file_path\":\"VqbcmM/2020/9/9\",\"status\":1},{\"file_name\":\"0a5f2d27-1934-4e32-a8c7-9592ab17dbf3.jpg\",\"file_path\":\"VqbcmM/2020/9/9\",\"status\":1},{\"file_name\":\"a3dd6706-29be-4f4d-b43f-9d24e0e4cc6f.jpg\",\"file_path\":\"VqbcmM/2020/9/9\",\"status\":1},{\"file_name\":\"03276ac0-5fde-455d-97a1-dd28cd9d0a5d.jpg\",\"file_path\":\"VqbcmM/2020/9/9\",\"status\":1},{\"file_name\":\"ff224514-ab8b-40d5-b86f-06c87e595496.jpg\",\"file_path\":\"VqbcmM/2020/9/9\",\"status\":1}]",
                      "product_notes": "",
                      "product_quantity": 1,
                      "product_weight_unit_code": 1,
                      "product_weight_unit_text": "gr",
                      "last_update_price": "1599655298",
                      "is_update_price": false,
                      "product_preorder": {
                        "duration_day": "0",
                        "duration_text": "",
                        "duration_unit_code": "0",
                        "duration_unit_text": "",
                        "duration_value": "0"
                      },
                      "product_showcase": {
                        "name": "BAJU KAOS PRIA",
                        "id": 26324848
                      },
                      "product_alias": "baju-kaos-pria-cotton-combet-30-s-fashion-casual-pria-baju-distro-110-merah-xl",
                      "sku": "",
                      "campaign_id": 0,
                      "product_original_price": 0,
                      "product_price_original_fmt": "",
                      "is_slash_price": false,
                      "free_returns": {
                        "free_returns_logo": ""
                      },
                      "product_finsurance": 0,
                      "is_wishlisted": true,
                      "is_ppp": false,
                      "is_cod": false,
                      "warehouse_id": 9304751,
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
                        "variant_name": [
                          "Merah",
                          "XL"
                        ],
                        "variant_description": "Merah, XL"
                      }
                    },
                    "errors": [],
                    "messages": [],
                    "checkbox_state": true
                  }
                ],
                "total_cart_details_error": 0,
                "total_cart_price": 56500,
                "errors": [],
                "sort_key": 1891383084,
                "is_fulfillment_service": false,
                "warehouse": {
                  "warehouse_id": 9304751,
                  "partner_id": 0,
                  "shop_id": 9167703,
                  "warehouse_name": "Shop Location",
                  "district_id": 1660,
                  "district_name": "Dayeuhkolot",
                  "city_id": 148,
                  "city_name": "Kab. Bandung",
                  "province_id": 12,
                  "province_name": "Jawa Barat",
                  "status": 1,
                  "postal_code": "40239",
                  "is_default": 1,
                  "latlon": "-6.9650181,107.5940717",
                  "latitude": "-6.9650181",
                  "longitude": "107.5940717",
                  "email": "",
                  "address_detail": "gang situtarate 5 no 61 rt 10 RW 1 Cangkuang kulon kec. Dayeuhkolot kab. Bandung Jawa barat",
                  "country_name": "Indonesia",
                  "is_fulfillment": false
                },
                "checkbox_state": true
              },
              {
                "user_address_id": 0,
                "shipment_information": {
                  "shop_location": "Jakarta Utara",
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
                  "shop_id": 510696,
                  "user_id": 4224937,
                  "admin_ids": [],
                  "shop_name": "hafami matras dan balmut",
                  "shop_image": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2020/7/23/510696/510696_7e89d6cb-48c8-4c42-8269-853d7fc43a2a.jpg",
                  "shop_url": "https://www.tokopedia.com/hafami",
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
                  "address_id": 2283,
                  "postal_code": "14240",
                  "latitude": "-6.17050865857832",
                  "longitude": "106.90913379192352",
                  "district_id": 2283,
                  "district_name": "",
                  "origin": 0,
                  "address_street": "",
                  "province_id": 0,
                  "city_id": 177,
                  "city_name": "Jakarta Utara",
                  "province_name": "",
                  "country_name": "",
                  "is_allow_manage": false,
                  "shop_domain": "hafami",
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
                    }
                  ]
                },
                "promo_codes": [],
                "cart_string": "510696-0-355517",
                "cart_details": [
                  {
                    "cart_id": "1890901743",
                    "product": {
                      "product_information": [],
                      "product_tracker_data": {
                        "attribution": "",
                        "tracker_list_name": ""
                      },
                      "isWishlist": false,
                      "product_id": 713533322,
                      "product_name": "Stiker smile happy star bintang anak lucu",
                      "product_price_fmt": "Rp3.500",
                      "product_price": 3500,
                      "parent_id": 0,
                      "category_id": 653,
                      "category": "Office & Stationery / Pengikat & Perekat / Stiker & Label",
                      "catalog_id": 0,
                      "wholesale_price": [],
                      "product_weight": 10,
                      "product_weight_fmt": "10gr",
                      "product_condition": 1,
                      "product_status": 1,
                      "product_url": "https://www.tokopedia.com/hafami/stiker-smile-happy-star-bintang-anak-lucu",
                      "product_returnable": 0,
                      "is_freereturns": 0,
                      "is_preorder": 0,
                      "product_cashback": "",
                      "product_min_order": 1,
                      "product_max_order": 32767,
                      "product_rating": "0.000000",
                      "product_invenage_value": 217,
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
                        "image_src": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/2/25/4224937/4224937_5788c3fa-c141-4d80-8b94-2ebf428da5a6_550_550",
                        "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/2/25/4224937/4224937_5788c3fa-c141-4d80-8b94-2ebf428da5a6_550_550",
                        "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/2/25/4224937/4224937_5788c3fa-c141-4d80-8b94-2ebf428da5a6_550_550",
                        "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/product-1/2020/2/25/4224937/4224937_5788c3fa-c141-4d80-8b94-2ebf428da5a6_550_550",
                        "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/product-1/2020/2/25/4224937/4224937_5788c3fa-c141-4d80-8b94-2ebf428da5a6_550_550"
                      },
                      "product_all_images": "[{\"file_name\":\"4224937_5788c3fa-c141-4d80-8b94-2ebf428da5a6_550_550\",\"file_path\":\"product-1/2020/2/25/4224937\",\"status\":2},{\"file_name\":\"4224937_bdc884bc-293e-40df-a32f-00d1d6ab32a4_548_548\",\"file_path\":\"product-1/2020/2/25/4224937\",\"status\":1},{\"file_name\":\"4224937_3471ddfd-cd22-498f-b631-fc2d5365c3c2_578_578\",\"file_path\":\"product-1/2020/2/25/4224937\",\"status\":1},{\"file_name\":\"4224937_87059a22-50d7-4f44-9230-42202ed05915_562_562\",\"file_path\":\"product-1/2020/2/25/4224937\",\"status\":1},{\"file_name\":\"4224937_5cfcbde2-014b-4c24-89fd-678ee6f110fb_600_600\",\"file_path\":\"product-1/2020/2/25/4224937\",\"status\":1}]",
                      "product_notes": "",
                      "product_quantity": 1,
                      "product_weight_unit_code": 1,
                      "product_weight_unit_text": "gr",
                      "last_update_price": "1582604176",
                      "is_update_price": false,
                      "product_preorder": {
                        "duration_day": "0",
                        "duration_text": "",
                        "duration_unit_code": "0",
                        "duration_unit_text": "",
                        "duration_value": "0"
                      },
                      "product_showcase": {
                        "name": "alat tulis",
                        "id": 16777394
                      },
                      "product_alias": "stiker-smile-happy-star-bintang-anak-lucu",
                      "sku": "",
                      "campaign_id": 0,
                      "product_original_price": 0,
                      "product_price_original_fmt": "",
                      "is_slash_price": false,
                      "free_returns": {
                        "free_returns_logo": ""
                      },
                      "product_finsurance": 1,
                      "is_wishlisted": false,
                      "is_ppp": false,
                      "is_cod": false,
                      "warehouse_id": 355517,
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
                "total_cart_price": 3500,
                "errors": [],
                "sort_key": 1890901743,
                "is_fulfillment_service": false,
                "warehouse": {
                  "warehouse_id": 355517,
                  "partner_id": 0,
                  "shop_id": 510696,
                  "warehouse_name": "Shop Location",
                  "district_id": 2283,
                  "district_name": "Kelapa Gading",
                  "city_id": 177,
                  "city_name": "Jakarta Utara",
                  "province_id": 13,
                  "province_name": "DKI Jakarta",
                  "status": 1,
                  "postal_code": "14240",
                  "is_default": 1,
                  "latlon": "-6.17050865857832,106.90913379192352",
                  "latitude": "-6.17050865857832",
                  "longitude": "106.90913379192352",
                  "email": "",
                  "address_detail": "jl. giring giring blok v no. 10",
                  "country_name": "Indonesia",
                  "is_fulfillment": false
                },
                "checkbox_state": true
              },
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
              "selected_unavailable_action_id": 0,
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