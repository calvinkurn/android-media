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
                    val shopGroupSimplifiedGqlResponse = it.getData<ShopGroupSimplifiedGqlResponse>(ShopGroupSimplifiedGqlResponse::class.java)
//                    val shopGroupSimplifiedGqlResponse = Gson().fromJson(RESPONSE, ShopGroupSimplifiedGqlResponse::class.java)
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
            "total_wording": "Total Harga (2 Barang)",
            "total_value": 9555500,
            "discount_total_wording": "Total Diskon Barang",
            "discount_value": 500000,
            "payment_total_wording": "Total Bayar",
            "promo_wording": "Hemat pakai promo",
            "promo_value": 0,
            "seller_cashback_wording": "Cashback Penjual",
            "seller_cashback_value": 0
          },
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
                  "shop_location": "Jakarta Utara",
                  "estimation": "Estimasi Tiba: 3 Hari",
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
                  "shop_id": 6248891,
                  "user_id": 69040867,
                  "admin_ids": [],
                  "shop_name": "Huawei Official Store",
                  "shop_image": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2020/3/31/6248891/6248891_26f27e07-f2fd-4154-aa29-e8a4a5cb994f.jpg",
                  "shop_url": "https://www.tokopedia.com/huawei",
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
                  "address_id": 2286,
                  "postal_code": "14440",
                  "latitude": "-6.135310799999999",
                  "longitude": "106.765992",
                  "district_id": 2286,
                  "district_name": "",
                  "origin": 0,
                  "address_street": "",
                  "province_id": 0,
                  "city_id": 177,
                  "city_name": "Jakarta Utara",
                  "province_name": "",
                  "country_name": "",
                  "is_allow_manage": false,
                  "shop_domain": "huawei",
                  "shop_shipments": [
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
                      "ship_id": 999,
                      "ship_name": "Custom Logistik",
                      "ship_code": "custom_6248891_6248891",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-custom.png",
                      "ship_prods": [
                        {
                          "ship_prod_id": 999,
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
                        }
                      ],
                      "is_dropship_enabled": 0
                    }
                  ]
                },
                "promo_codes": [],
                "cart_string": "6248891-0-6506547",
                "cart_details": [
                  {
                    "cart_id": "1953535355",
                    "product": {
                      "product_information": [],
                      "product_tracker_data": {
                        "attribution": "",
                        "tracker_list_name": ""
                      },
                      "isWishlist": false,
                      "product_id": 904557456,
                      "product_name": "Huawei ANA-NX9 P40 8GB RAM 128GB ROM Silver Frost",
                      "product_price_fmt": "Rp 9.499.000",
                      "product_price": 9499000,
                      "parent_id": 0,
                      "category_id": 3054,
                      "category": "Handphone & Tablet / Handphone / Android OS",
                      "catalog_id": 0,
                      "wholesale_price": [],
                      "product_weight": 1000,
                      "product_weight_fmt": "1000gr",
                      "product_condition": 1,
                      "product_status": 1,
                      "product_url": "https://www.tokopedia.com/huawei/huawei-ana-nx9-p40-8gb-ram-128gb-rom-silver-frost",
                      "product_returnable": 0,
                      "is_freereturns": 0,
                      "is_preorder": 0,
                      "product_cashback": "15%",
                      "product_min_order": 1,
                      "product_max_order": 3,
                      "product_rating": "0.000000",
                      "product_invenage_value": 3,
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
                        "image_src": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/10/11/69040867/69040867_e41311a3-5a5a-4714-9876-9db2d7af10f7_600_600",
                        "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/10/11/69040867/69040867_e41311a3-5a5a-4714-9876-9db2d7af10f7_600_600",
                        "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/10/11/69040867/69040867_e41311a3-5a5a-4714-9876-9db2d7af10f7_600_600",
                        "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/product-1/2020/10/11/69040867/69040867_e41311a3-5a5a-4714-9876-9db2d7af10f7_600_600",
                        "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/product-1/2020/10/11/69040867/69040867_e41311a3-5a5a-4714-9876-9db2d7af10f7_600_600"
                      },
                      "product_all_images": "[{\"file_name\":\"69040867_e41311a3-5a5a-4714-9876-9db2d7af10f7_600_600\",\"file_path\":\"product-1/2020/10/11/69040867\",\"status\":2},{\"file_name\":\"69040867_fcaad62f-83c6-4250-977f-96424bd18e75_600_600\",\"file_path\":\"product-1/2020/6/12/69040867\",\"status\":1}]",
                      "product_notes": "",
                      "product_quantity": 1,
                      "product_weight_unit_code": 1,
                      "product_weight_unit_text": "gr",
                      "last_update_price": "1591890897",
                      "is_update_price": false,
                      "product_preorder": {
                        "duration_day": "0",
                        "duration_text": "",
                        "duration_unit_code": "0",
                        "duration_unit_text": "",
                        "duration_value": "0"
                      },
                      "product_showcase": {
                        "name": "Smartphone",
                        "id": 20170768
                      },
                      "product_alias": "huawei-ana-nx9-p40-8gb-ram-128gb-rom-silver-frost",
                      "sku": "HHW-P40-SIL",
                      "campaign_id": -10000,
                      "product_original_price": 9999000,
                      "product_price_original_fmt": "Rp 9.999.000",
                      "is_slash_price": false,
                      "free_returns": {
                        "free_returns_logo": ""
                      },
                      "product_finsurance": 1,
                      "is_wishlisted": true,
                      "is_ppp": false,
                      "is_cod": false,
                      "warehouse_id": 6506547,
                      "is_parent": false,
                      "is_campaign_error": false,
                      "is_blacklisted": false,
                      "free_shipping": {
                        "eligible": false,
                        "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                      },
                      "booking_stock": 0,
                      "is_product_volume_weight": false,
                      "initial_price": 0,
                      "initial_price_fmt": "Rp0",
                      "slash_price_label": "6%",
                      "product_warning_message": "Sisa 3",
                      "product_alert_message": "",
                      "variant_description_detail": {
                        "variant_name": [],
                        "variant_description": ""
                      }
                    },
                    "errors": [],
                    "messages": [],
                    "checkbox_state": false
                  }
                ],
                "total_cart_details_error": 0,
                "total_cart_price": 9499000,
                "errors": [],
                "sort_key": 1953535355,
                "is_fulfillment_service": false,
                "warehouse": {
                  "warehouse_id": 6506547,
                  "partner_id": 0,
                  "shop_id": 6248891,
                  "warehouse_name": "Shop Location",
                  "district_id": 2286,
                  "district_name": "Penjaringan",
                  "city_id": 177,
                  "city_name": "Jakarta Utara",
                  "province_id": 13,
                  "province_name": "DKI Jakarta",
                  "status": 1,
                  "postal_code": "14440",
                  "is_default": 1,
                  "latlon": "-6.135310799999999,106.765992",
                  "latitude": "-6.135310799999999",
                  "longitude": "106.765992",
                  "email": "",
                  "address_detail": "Jl. Kapuk Utara II No.2 RT 01/03, Kapuk Muara,Penjaringan, Jakarta Utara (Masuk dari jalan dekat SPBU AKR dari arah jalan raya kapuk pertigaan belok kiri posisi gudang disebelah kanan pagar warna hijau besar). SENIN-JUMAT BUKA SAMPAI JAM 5, SABTU SAMPAI JAM 3 DILUAR ITU DAN TANGGAL MERAH TUTUP",
                  "country_name": "Indonesia",
                  "is_fulfillment": false
                },
                "checkbox_state": false
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
                      "product_cashback": "5%",
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
                      "price_changes": {
                        "changes_state": 0,
                        "amount_difference": 0,
                        "original_amount": 56500,
                        "description": "Harga Normal"
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
                      "last_update_price": "1602608152",
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
                      "initial_price": 56500,
                      "initial_price_fmt": "Rp56.500",
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
          "unavailable_section": [],
          "total_product_price": 9555500,
          "total_product_count": 2,
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
    """

}