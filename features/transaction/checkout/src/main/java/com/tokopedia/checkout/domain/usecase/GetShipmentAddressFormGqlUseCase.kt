package com.tokopedia.checkout.domain.usecase

import com.google.gson.Gson
import com.tokopedia.checkout.data.model.response.shipment_address_form.ShipmentAddressFormGqlResponse
import com.tokopedia.checkout.domain.mapper.IShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.constant.CartConstant.CART_ERROR_GLOBAL
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class GetShipmentAddressFormGqlUseCase @Inject constructor(@Named(SHIPMENT_ADDRESS_FORM_QUERY) private val queryString: String,
                                                           private val graphqlUseCase: GraphqlUseCase,
                                                           private val shipmentMapper: IShipmentMapper,
                                                           private val schedulers: ExecutorSchedulers) : UseCase<CartShipmentAddressFormData>() {

    companion object {
        const val SHIPMENT_ADDRESS_FORM_QUERY = "SHIPMENT_ADDRESS_FORM_QUERY"
        const val SHIPMENT_ADDRESS_FORM_PARAMS = "params"

        const val PARAM_KEY_LANG = "lang"
        const val PARAM_KEY_IS_ONE_CLICK_SHIPMENT = "is_ocs"
        const val PARAM_KEY_CORNER_ID = "corner_id"
        const val PARAM_KEY_SKIP_ONBOARDING_UPDATE_STATE = "skip_onboarding"
        const val PARAM_KEY_IS_TRADEIN = "is_trade_in"
        const val PARAM_KEY_DEVICE_ID = "dev_id"
        const val PARAM_KEY_VEHICLE_LEASING_ID = "vehicle_leasing_id"
    }

    override fun createObservable(requestParam: RequestParams): Observable<CartShipmentAddressFormData> {
        val params = mapOf(SHIPMENT_ADDRESS_FORM_PARAMS to requestParam.parameters)
        val graphqlRequest = GraphqlRequest(queryString, ShipmentAddressFormGqlResponse::class.java, params)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val gqlResponse = it.getData<ShipmentAddressFormGqlResponse>(ShipmentAddressFormGqlResponse::class.java)
//                    val gqlResponse = Gson().fromJson(RESPONSE_TRADE_IN_ALL_ADDRESS, ShipmentAddressFormGqlResponse::class.java)
                    if (gqlResponse != null) {
                        if (gqlResponse.shipmentAddressFormResponse.status == "OK") {
                            shipmentMapper.convertToShipmentAddressFormData(gqlResponse.shipmentAddressFormResponse.data)
                        } else {
                            if (gqlResponse.shipmentAddressFormResponse.errorMessages.isNotEmpty()) {
                                throw CartResponseErrorException(gqlResponse.shipmentAddressFormResponse.errorMessages.joinToString())
                            } else {
                                throw CartResponseErrorException(CART_ERROR_GLOBAL)
                            }
                        }
                    } else {
                        throw CartResponseErrorException(CART_ERROR_GLOBAL)
                    }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }
}


val RESPONSE_TRADE_IN_ALL_ADDRESS = """
    {
      "shipment_address_form": {
        "status": "OK",
        "error_message": [],
        "data": {
          "errors": [],
          "error_code": 0,
          "kero_token": "Tokopedia+Kero:yljXWwFIJrfH4ZSNm3/Qgn2km9g=",
          "kero_discom_token": "Tokopedia+Kero:+Ivo5J/yvf7idzZTSTIK3LyvNEw=",
          "kero_unix_time": 1600221910,
          "is_robinhood": 1,
          "is_hide_courier_name": false,
          "is_blackbox": 0,
          "is_show_onboarding": false,
          "is_ineligible_promo_dialog_enabled": true,
          "disabled_features": [],
          "donation_checkbox_status": false,
          "open_prerequisite_site": false,
          "eligible_new_shipping_experience": true,
          "group_address": [
            {
              "errors": [],
              "user_address": {
                "address_id": 98675797,
                "address_name": "NGADIREJO MAGETAN",
                "address": "DS. NGADIREJO RT 005 RW 002 NGADIREJO",
                "postal_code": "63382",
                "phone": "628561533911",
                "receiver_name": "Irfan Khoirul Muhlishin",
                "status": 1,
                "country": "Indonesia",
                "province_id": 15,
                "province_name": "Jawa Timur",
                "city_id": 227,
                "city_name": "Kab. Magetan",
                "district_id": 3156,
                "district_name": "Kawedanan",
                "address_2": "-7.6775,111.40925",
                "latitude": "-7.6775",
                "longitude": "111.40925",
                "corner_id": 0,
                "is_corner": false
              },
              "group_shop": [
                {
                  "errors": [],
                  "shipping_id": 0,
                  "sp_id": 0,
                  "is_insurance": false,
                  "is_fulfillment_service": false,
                  "cart_string": "2400899-0-98570-98675797",
                  "has_promo_list": false,
                  "shipment_information": {
                    "shop_location": "Jakarta Utara",
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
                  "save_state_flag": false,
                  "promo_codes": [],
                  "vehicle_leasing": {
                    "booking_fee": 0,
                    "is_leasing_product": false
                  },
                  "shop": {
                    "shop_id": 2400899,
                    "user_id": 21722219,
                    "shop_name": "Samsung Mobile Indonesia",
                    "shop_image": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2017/12/22/2400899/2400899_a1a81e3e-9067-4685-a62f-7be063df8377.png",
                    "shop_url": "https://www.tokopedia.com/samsung",
                    "shop_status": 1,
                    "is_gold": 1,
                    "is_gold_badge": true,
                    "is_official": 1,
                    "is_free_returns": 0,
                    "address_id": 2286,
                    "postal_code": "14440",
                    "latitude": "-6.139255586142901",
                    "longitude": "106.76697918203126",
                    "district_id": 2286,
                    "district_name": "Penjaringan",
                    "origin": 2286,
                    "address_street": "Jl. Kapuk Utara II No.2 RT 01/03, Kapuk Muara,Penjaringan, Jakarta Utara (Masuk dari jalan dekat SPBU AKR dari arah jalan raya kapuk pertigaan belok kiri posisi gudang disebelah kanan pagar warna hijau besar). SENIN-JUMAT BUKA SAMPAI JAM 5, SABTU SAMPAI JAM 3 DILUAR ITU DAN TANGGAL MERAH TUTUP",
                    "province_id": 13,
                    "city_id": 177,
                    "city_name": "Jakarta Utara",
                    "gold_merchant": {
                      "is_gold": 0,
                      "is_gold_badge": false,
                      "gold_merchant_logo_url": ""
                    },
                    "official_store": {
                      "is_official": 1,
                      "os_logo_url": "https://ecs7.tokopedia.net/img/official_store/badge_os128.png"
                    },
                    "shop_alert_message": ""
                  },
                  "shop_shipments": [
                    {
                      "ship_id": 999,
                      "ship_name": "Custom Logistik",
                      "ship_code": "custom_2400899",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-custom.png",
                      "is_dropship_enabled": 0,
                      "ship_prods": [
                        {
                          "ship_prod_id": 999,
                          "ship_prod_name": "Service Normal",
                          "ship_group_name": "custom",
                          "ship_group_id": 999,
                          "additional_fee": 0,
                          "minimum_weight": 0
                        }
                      ]
                    }
                  ],
                  "dropshipper": {
                    "name": "",
                    "telp_no": ""
                  },
                  "products": [
                    {
                      "errors": [],
                      "cart_id": "16002227378256",
                      "product_id": 808702209,
                      "product_name": "Samsung Galaxy A31 6/128 GB A315G Black",
                      "product_price_fmt": "Rp3.049.000",
                      "product_price": 3049000,
                      "product_original_price": 3999000,
                      "product_wholesale_price": 3899000,
                      "product_wholesale_price_fmt": "Rp3.899.000",
                      "product_weight_fmt": "1000gr",
                      "product_weight": 1000,
                      "product_condition": 1,
                      "product_url": "https://www.tokopedia.com/samsung/samsung-galaxy-a31-6-128-gb-a315g-black",
                      "product_returnable": 0,
                      "product_is_free_returns": 0,
                      "product_is_preorder": 0,
                      "product_cashback": "",
                      "product_min_order": 1,
                      "product_invenage_value": 1,
                      "product_switch_invenage": 0,
                      "product_price_currency": 1,
                      "product_image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/5/9/21722219/21722219_2cfd628d-c052-4cf4-a954-b660ef3175ba_600_600",
                      "product_notes": "",
                      "product_quantity": 1,
                      "product_menu_id": 25256036,
                      "product_finsurance": 1,
                      "product_fcancel_partial": 0,
                      "product_cat_id": 3054,
                      "product_catalog_id": 0,
                      "product_category": "Handphone & Tablet / Handphone / Android OS",
                      "product_shipment": [],
                      "product_shipment_mapping": [],
                      "purchase_protection_plan_data": {
                        "protection_available": true,
                        "protection_type_id": 1357,
                        "protection_price_per_product": 46000,
                        "protection_price": 46000,
                        "protection_title": "Takut gadget baru kamu rusak?",
                        "protection_subtitle": "Lindungi gadget dari layar retak, terkena cairan, perampokan hanya Rp46,000",
                        "protection_link_text": "Pelajari",
                        "protection_link_url": "https://www.tokopedia.com/asuransi/proteksi/gadget/",
                        "protection_opt_in": true,
                        "protection_checkbox_disabled": false
                      },
                      "free_returns": {
                        "free_returns_logo": ""
                      },
                      "product_tracker_data": {
                        "attribution": "none/other",
                        "tracker_list_name": "none/other"
                      },
                      "product_preorder": {
                        "duration_text": "",
                        "duration_day": "0"
                      },
                      "trade_in_info": {
                        "is_valid_trade_in": true,
                        "new_device_price": 3899000,
                        "new_device_price_fmt": "Rp 3.899.000",
                        "old_device_price": 850000,
                        "old_device_price_fmt": "Rp850.000",
                        "drop_off_enable": true
                      },
                      "free_shipping": {
                        "eligible": false,
                        "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                      },
                      "product_ticker": {
                        "show_ticker": false,
                        "message": "Buruan bayar. Stok menipis dan sedang dibeli pembeli lain."
                      },
                      "variant_description_detail": {
                        "variant_name": [],
                        "variant_description": ""
                      },
                      "product_alert_message": "",
                      "product_information": []
                    }
                  ],
                  "warehouse": {
                    "warehouse_id": 98570,
                    "city_name": "Jakarta Utara"
                  }
                }
              ]
            }
          ],
          "donation": {
            "Title": "Donasi Rp 5.000 untuk APD Tenaga Medis",
            "Nominal": 5000,
            "Description": "Bantu garda terdepan COVID-19, jaga kesehatan para nakes melalui donasi pengadaan APD & Vitamin melalui partner BenihBaik."
          },
          "cod": {
            "is_cod": false,
            "counter_cod": -1
          },
          "message": {
            "message_info": "",
            "message_link": "",
            "message_logo": ""
          },
          "egold_attributes": {
            "eligible": true,
            "is_tiering": true,
            "range": {
              "min": 1500,
              "max": 5499
            },
            "message": {
              "title_text": "Yuk mulai nabung emas",
              "sub_text": "Bulatkan tagihan ini dengan",
              "ticker_text": "",
              "tooltip_text": "Nominal pembulatan disesuaikan dengan total tagihan setiap transaksi yang otomatis ditabung dan dapat di cek saldonya di Tokopedia Emas (berpartner dengan Pegadaian)."
            },
            "tier_data": [
              {
                "minimum_total_amount": 500,
                "minimum_amount": 1500,
                "maximum_amount": 5499,
                "basis_amount": 4000
              },
              {
                "minimum_total_amount": 98501,
                "minimum_amount": 1500,
                "maximum_amount": 11499,
                "basis_amount": 10000
              },
              {
                "minimum_total_amount": 998501,
                "minimum_amount": 1500,
                "maximum_amount": 51499,
                "basis_amount": 50000
              }
            ]
          },
          "tickers": [],
          "campaign_timer": {
            "description": "",
            "show_timer": false,
            "expired_timer_message": {
              "Button": "",
              "description": "",
              "title": ""
            },
            "timer_detail": {
              "deduct_time": "",
              "expire_duration": 0,
              "expired_time": "",
              "server_time": ""
            }
          },
          "addresses": {
            "active": "default_address",
            "disable_tabs": [],
            "data": [
              {
                "key": "default_address",
                "value": {
                  "address_id": 40031643,
                  "address_name": "Alamat Kantor a",
                  "address": "Mailing Room PT Tokopedia, Tokopedia Tower Lantai 29, Jl. Prof. Dr. Satrio Kav 11, Kelurahan Karet Semanggi",
                  "postal_code": "12940",
                  "phone": "628561533911",
                  "receiver_name": "Irfan Khoirul - Mobile Engineer Android",
                  "status": 2,
                  "country": "Indonesia",
                  "province_id": 13,
                  "province_name": "DKI Jakarta",
                  "city_id": 175,
                  "city_name": "Jakarta Selatan",
                  "district_id": 2270,
                  "district_name": "Setiabudi",
                  "address_2": "-6.22138492899301,106.82065308094023",
                  "latitude": "-6.22138492899301",
                  "longitude": "106.82065308094023",
                  "corner_id": 0,
                  "is_corner": false,
                  "is_primary": true,
                  "buyer_store_code": "",
                  "type": 1
                }
              },
              {
                "key": "trade_in_address",
                "value": {
                  "address_id": 98675797,
                  "address_name": "NGADIREJO MAGETAN",
                  "address": "DS. NGADIREJO RT 005 RW 002 NGADIREJO",
                  "postal_code": "63382",
                  "phone": "628561533911",
                  "receiver_name": "Irfan Khoirul Muhlishin",
                  "status": 1,
                  "country": "Indonesia",
                  "province_id": 15,
                  "province_name": "Jawa Timur",
                  "city_id": 227,
                  "city_name": "Kab. Magetan",
                  "district_id": 3156,
                  "district_name": "Kawedanan",
                  "address_2": "-7.6775,111.40925",
                  "latitude": "-7.6775",
                  "longitude": "111.40925",
                  "corner_id": 0,
                  "is_corner": false,
                  "is_primary": false,
                  "buyer_store_code": "F29I",
                  "type": 3
                }
              }
            ]
          },
          "disabled_features_detail": {
            "disabled_multi_address_message": ""
          },
          "promo": {
            "error_default": {
              "title": "promo tidak dapat digunakan",
              "description": "silahkan coba beberapa saat lagi"
            },
            "last_apply": {
              "code": "200000",
              "data": {
                "codes": [],
                "promo_code_id": 0,
                "cashback_advocate_referral_amount": 0,
                "cashback_wallet_amount": 0,
                "discount_amount": 0,
                "title_description": "",
                "is_tokopedia_gerai": false,
                "global_success": true,
                "gateway_id": "",
                "is_coupon": 0,
                "success": true,
                "invoice_description": "",
                "voucher_orders": [],
                "clashing_info_detail": {
                  "is_clashed_promos": false,
                  "clash_reason": "",
                  "clash_message": ""
                },
                "tracking_details": [],
                "message": {
                  "color": "",
                  "state": "",
                  "text": ""
                },
                "additional_info": {
                  "sp_ids": [],
                  "usage_summaries": [],
                  "message_info": {
                    "message": "Makin hemat pakai promo",
                    "detail": ""
                  },
                  "error_detail": {
                    "message": ""
                  },
                  "empty_cart_info": {
                    "image_url": "",
                    "detail": "",
                    "message": ""
                  }
                },
                "benefit_summary_info": {
                  "final_benefit_amount_str": "",
                  "final_benefit_amount": 0,
                  "final_benefit_text": "",
                  "summaries": []
                },
                "ticker_info": {
                  "unique_id": "",
                  "status_code": 0,
                  "message": ""
                }
              }
            }
          }
        }
      }
    }
""".trimIndent()

val RESPONSE_TRADE_IN_DEFAULT_ADDRESS = """
    {
      "shipment_address_form": {
        "status": "OK",
        "error_message": [],
        "data": {
          "errors": [],
          "error_code": 0,
          "kero_token": "Tokopedia+Kero:yljXWwFIJrfH4ZSNm3/Qgn2km9g=",
          "kero_discom_token": "Tokopedia+Kero:+Ivo5J/yvf7idzZTSTIK3LyvNEw=",
          "kero_unix_time": 1600221910,
          "is_robinhood": 1,
          "is_hide_courier_name": false,
          "is_blackbox": 0,
          "is_show_onboarding": false,
          "is_ineligible_promo_dialog_enabled": true,
          "disabled_features": [],
          "donation_checkbox_status": false,
          "open_prerequisite_site": false,
          "eligible_new_shipping_experience": true,
          "group_address": [
            {
              "errors": [],
              "user_address": {
                "address_id": 98675797,
                "address_name": "NGADIREJO MAGETAN",
                "address": "DS. NGADIREJO RT 005 RW 002 NGADIREJO",
                "postal_code": "63382",
                "phone": "628561533911",
                "receiver_name": "Irfan Khoirul Muhlishin",
                "status": 1,
                "country": "Indonesia",
                "province_id": 15,
                "province_name": "Jawa Timur",
                "city_id": 227,
                "city_name": "Kab. Magetan",
                "district_id": 3156,
                "district_name": "Kawedanan",
                "address_2": "-7.6775,111.40925",
                "latitude": "-7.6775",
                "longitude": "111.40925",
                "corner_id": 0,
                "is_corner": false
              },
              "group_shop": [
                {
                  "errors": [],
                  "shipping_id": 0,
                  "sp_id": 0,
                  "is_insurance": false,
                  "is_fulfillment_service": false,
                  "cart_string": "2400899-0-98570-98675797",
                  "has_promo_list": false,
                  "shipment_information": {
                    "shop_location": "Jakarta Utara",
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
                  "save_state_flag": false,
                  "promo_codes": [],
                  "vehicle_leasing": {
                    "booking_fee": 0,
                    "is_leasing_product": false
                  },
                  "shop": {
                    "shop_id": 2400899,
                    "user_id": 21722219,
                    "shop_name": "Samsung Mobile Indonesia",
                    "shop_image": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2017/12/22/2400899/2400899_a1a81e3e-9067-4685-a62f-7be063df8377.png",
                    "shop_url": "https://www.tokopedia.com/samsung",
                    "shop_status": 1,
                    "is_gold": 1,
                    "is_gold_badge": true,
                    "is_official": 1,
                    "is_free_returns": 0,
                    "address_id": 2286,
                    "postal_code": "14440",
                    "latitude": "-6.139255586142901",
                    "longitude": "106.76697918203126",
                    "district_id": 2286,
                    "district_name": "Penjaringan",
                    "origin": 2286,
                    "address_street": "Jl. Kapuk Utara II No.2 RT 01/03, Kapuk Muara,Penjaringan, Jakarta Utara (Masuk dari jalan dekat SPBU AKR dari arah jalan raya kapuk pertigaan belok kiri posisi gudang disebelah kanan pagar warna hijau besar). SENIN-JUMAT BUKA SAMPAI JAM 5, SABTU SAMPAI JAM 3 DILUAR ITU DAN TANGGAL MERAH TUTUP",
                    "province_id": 13,
                    "city_id": 177,
                    "city_name": "Jakarta Utara",
                    "gold_merchant": {
                      "is_gold": 0,
                      "is_gold_badge": false,
                      "gold_merchant_logo_url": ""
                    },
                    "official_store": {
                      "is_official": 1,
                      "os_logo_url": "https://ecs7.tokopedia.net/img/official_store/badge_os128.png"
                    },
                    "shop_alert_message": ""
                  },
                  "shop_shipments": [
                    {
                      "ship_id": 999,
                      "ship_name": "Custom Logistik",
                      "ship_code": "custom_2400899",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-custom.png",
                      "is_dropship_enabled": 0,
                      "ship_prods": [
                        {
                          "ship_prod_id": 999,
                          "ship_prod_name": "Service Normal",
                          "ship_group_name": "custom",
                          "ship_group_id": 999,
                          "additional_fee": 0,
                          "minimum_weight": 0
                        }
                      ]
                    }
                  ],
                  "dropshipper": {
                    "name": "",
                    "telp_no": ""
                  },
                  "products": [
                    {
                      "errors": [],
                      "cart_id": "16002227378256",
                      "product_id": 808702209,
                      "product_name": "Samsung Galaxy A31 6/128 GB A315G Black",
                      "product_price_fmt": "Rp3.049.000",
                      "product_price": 3049000,
                      "product_original_price": 3999000,
                      "product_wholesale_price": 3899000,
                      "product_wholesale_price_fmt": "Rp3.899.000",
                      "product_weight_fmt": "1000gr",
                      "product_weight": 1000,
                      "product_condition": 1,
                      "product_url": "https://www.tokopedia.com/samsung/samsung-galaxy-a31-6-128-gb-a315g-black",
                      "product_returnable": 0,
                      "product_is_free_returns": 0,
                      "product_is_preorder": 0,
                      "product_cashback": "",
                      "product_min_order": 1,
                      "product_invenage_value": 1,
                      "product_switch_invenage": 0,
                      "product_price_currency": 1,
                      "product_image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/5/9/21722219/21722219_2cfd628d-c052-4cf4-a954-b660ef3175ba_600_600",
                      "product_notes": "",
                      "product_quantity": 1,
                      "product_menu_id": 25256036,
                      "product_finsurance": 1,
                      "product_fcancel_partial": 0,
                      "product_cat_id": 3054,
                      "product_catalog_id": 0,
                      "product_category": "Handphone & Tablet / Handphone / Android OS",
                      "product_shipment": [],
                      "product_shipment_mapping": [],
                      "purchase_protection_plan_data": {
                        "protection_available": true,
                        "protection_type_id": 1357,
                        "protection_price_per_product": 46000,
                        "protection_price": 46000,
                        "protection_title": "Takut gadget baru kamu rusak?",
                        "protection_subtitle": "Lindungi gadget dari layar retak, terkena cairan, perampokan hanya Rp46,000",
                        "protection_link_text": "Pelajari",
                        "protection_link_url": "https://www.tokopedia.com/asuransi/proteksi/gadget/",
                        "protection_opt_in": true,
                        "protection_checkbox_disabled": false
                      },
                      "free_returns": {
                        "free_returns_logo": ""
                      },
                      "product_tracker_data": {
                        "attribution": "none/other",
                        "tracker_list_name": "none/other"
                      },
                      "product_preorder": {
                        "duration_text": "",
                        "duration_day": "0"
                      },
                      "trade_in_info": {
                        "is_valid_trade_in": true,
                        "new_device_price": 3899000,
                        "new_device_price_fmt": "Rp 3.899.000",
                        "old_device_price": 850000,
                        "old_device_price_fmt": "Rp850.000",
                        "drop_off_enable": true
                      },
                      "free_shipping": {
                        "eligible": false,
                        "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                      },
                      "product_ticker": {
                        "show_ticker": false,
                        "message": "Buruan bayar. Stok menipis dan sedang dibeli pembeli lain."
                      },
                      "variant_description_detail": {
                        "variant_name": [],
                        "variant_description": ""
                      },
                      "product_alert_message": "",
                      "product_information": []
                    }
                  ],
                  "warehouse": {
                    "warehouse_id": 98570,
                    "city_name": "Jakarta Utara"
                  }
                }
              ]
            }
          ],
          "donation": {
            "Title": "Donasi Rp 5.000 untuk APD Tenaga Medis",
            "Nominal": 5000,
            "Description": "Bantu garda terdepan COVID-19, jaga kesehatan para nakes melalui donasi pengadaan APD & Vitamin melalui partner BenihBaik."
          },
          "cod": {
            "is_cod": false,
            "counter_cod": -1
          },
          "message": {
            "message_info": "",
            "message_link": "",
            "message_logo": ""
          },
          "egold_attributes": {
            "eligible": true,
            "is_tiering": true,
            "range": {
              "min": 1500,
              "max": 5499
            },
            "message": {
              "title_text": "Yuk mulai nabung emas",
              "sub_text": "Bulatkan tagihan ini dengan",
              "ticker_text": "",
              "tooltip_text": "Nominal pembulatan disesuaikan dengan total tagihan setiap transaksi yang otomatis ditabung dan dapat di cek saldonya di Tokopedia Emas (berpartner dengan Pegadaian)."
            },
            "tier_data": [
              {
                "minimum_total_amount": 500,
                "minimum_amount": 1500,
                "maximum_amount": 5499,
                "basis_amount": 4000
              },
              {
                "minimum_total_amount": 98501,
                "minimum_amount": 1500,
                "maximum_amount": 11499,
                "basis_amount": 10000
              },
              {
                "minimum_total_amount": 998501,
                "minimum_amount": 1500,
                "maximum_amount": 51499,
                "basis_amount": 50000
              }
            ]
          },
          "tickers": [],
          "campaign_timer": {
            "description": "",
            "show_timer": false,
            "expired_timer_message": {
              "Button": "",
              "description": "",
              "title": ""
            },
            "timer_detail": {
              "deduct_time": "",
              "expire_duration": 0,
              "expired_time": "",
              "server_time": ""
            }
          },
          "addresses": {
            "active": "default_address",
            "disable_tabs": ["trade_in_address"],
            "data": [
              {
                "key": "default_address",
                "value": {
                  "address_id": 40031643,
                  "address_name": "Alamat Kantor a",
                  "address": "Mailing Room PT Tokopedia, Tokopedia Tower Lantai 29, Jl. Prof. Dr. Satrio Kav 11, Kelurahan Karet Semanggi",
                  "postal_code": "12940",
                  "phone": "628561533911",
                  "receiver_name": "Irfan Khoirul - Mobile Engineer Android",
                  "status": 2,
                  "country": "Indonesia",
                  "province_id": 13,
                  "province_name": "DKI Jakarta",
                  "city_id": 175,
                  "city_name": "Jakarta Selatan",
                  "district_id": 2270,
                  "district_name": "Setiabudi",
                  "address_2": "-6.22138492899301,106.82065308094023",
                  "latitude": "-6.22138492899301",
                  "longitude": "106.82065308094023",
                  "corner_id": 0,
                  "is_corner": false,
                  "is_primary": true,
                  "buyer_store_code": "",
                  "type": 1
                }
              }
            ]
          },
          "disabled_features_detail": {
            "disabled_multi_address_message": ""
          },
          "promo": {
            "error_default": {
              "title": "promo tidak dapat digunakan",
              "description": "silahkan coba beberapa saat lagi"
            },
            "last_apply": {
              "code": "200000",
              "data": {
                "codes": [],
                "promo_code_id": 0,
                "cashback_advocate_referral_amount": 0,
                "cashback_wallet_amount": 0,
                "discount_amount": 0,
                "title_description": "",
                "is_tokopedia_gerai": false,
                "global_success": true,
                "gateway_id": "",
                "is_coupon": 0,
                "success": true,
                "invoice_description": "",
                "voucher_orders": [],
                "clashing_info_detail": {
                  "is_clashed_promos": false,
                  "clash_reason": "",
                  "clash_message": ""
                },
                "tracking_details": [],
                "message": {
                  "color": "",
                  "state": "",
                  "text": ""
                },
                "additional_info": {
                  "sp_ids": [],
                  "usage_summaries": [],
                  "message_info": {
                    "message": "Makin hemat pakai promo",
                    "detail": ""
                  },
                  "error_detail": {
                    "message": ""
                  },
                  "empty_cart_info": {
                    "image_url": "",
                    "detail": "",
                    "message": ""
                  }
                },
                "benefit_summary_info": {
                  "final_benefit_amount_str": "",
                  "final_benefit_amount": 0,
                  "final_benefit_text": "",
                  "summaries": []
                },
                "ticker_info": {
                  "unique_id": "",
                  "status_code": 0,
                  "message": ""
                }
              }
            }
          }
        }
      }
    }
""".trimIndent()

val RESPONSE_TRADE_IN_DROP_OFF_ADDRESS = """
    {
      "shipment_address_form": {
        "status": "OK",
        "error_message": [],
        "data": {
          "errors": [],
          "error_code": 0,
          "kero_token": "Tokopedia+Kero:yljXWwFIJrfH4ZSNm3/Qgn2km9g=",
          "kero_discom_token": "Tokopedia+Kero:+Ivo5J/yvf7idzZTSTIK3LyvNEw=",
          "kero_unix_time": 1600221910,
          "is_robinhood": 1,
          "is_hide_courier_name": false,
          "is_blackbox": 0,
          "is_show_onboarding": false,
          "is_ineligible_promo_dialog_enabled": true,
          "disabled_features": [],
          "donation_checkbox_status": false,
          "open_prerequisite_site": false,
          "eligible_new_shipping_experience": true,
          "group_address": [
            {
              "errors": [],
              "user_address": {
                "address_id": 98675797,
                "address_name": "NGADIREJO MAGETAN",
                "address": "DS. NGADIREJO RT 005 RW 002 NGADIREJO",
                "postal_code": "63382",
                "phone": "628561533911",
                "receiver_name": "Irfan Khoirul Muhlishin",
                "status": 1,
                "country": "Indonesia",
                "province_id": 15,
                "province_name": "Jawa Timur",
                "city_id": 227,
                "city_name": "Kab. Magetan",
                "district_id": 3156,
                "district_name": "Kawedanan",
                "address_2": "-7.6775,111.40925",
                "latitude": "-7.6775",
                "longitude": "111.40925",
                "corner_id": 0,
                "is_corner": false
              },
              "group_shop": [
                {
                  "errors": [],
                  "shipping_id": 0,
                  "sp_id": 0,
                  "is_insurance": false,
                  "is_fulfillment_service": false,
                  "cart_string": "2400899-0-98570-98675797",
                  "has_promo_list": false,
                  "shipment_information": {
                    "shop_location": "Jakarta Utara",
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
                  "save_state_flag": false,
                  "promo_codes": [],
                  "vehicle_leasing": {
                    "booking_fee": 0,
                    "is_leasing_product": false
                  },
                  "shop": {
                    "shop_id": 2400899,
                    "user_id": 21722219,
                    "shop_name": "Samsung Mobile Indonesia",
                    "shop_image": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2017/12/22/2400899/2400899_a1a81e3e-9067-4685-a62f-7be063df8377.png",
                    "shop_url": "https://www.tokopedia.com/samsung",
                    "shop_status": 1,
                    "is_gold": 1,
                    "is_gold_badge": true,
                    "is_official": 1,
                    "is_free_returns": 0,
                    "address_id": 2286,
                    "postal_code": "14440",
                    "latitude": "-6.139255586142901",
                    "longitude": "106.76697918203126",
                    "district_id": 2286,
                    "district_name": "Penjaringan",
                    "origin": 2286,
                    "address_street": "Jl. Kapuk Utara II No.2 RT 01/03, Kapuk Muara,Penjaringan, Jakarta Utara (Masuk dari jalan dekat SPBU AKR dari arah jalan raya kapuk pertigaan belok kiri posisi gudang disebelah kanan pagar warna hijau besar). SENIN-JUMAT BUKA SAMPAI JAM 5, SABTU SAMPAI JAM 3 DILUAR ITU DAN TANGGAL MERAH TUTUP",
                    "province_id": 13,
                    "city_id": 177,
                    "city_name": "Jakarta Utara",
                    "gold_merchant": {
                      "is_gold": 0,
                      "is_gold_badge": false,
                      "gold_merchant_logo_url": ""
                    },
                    "official_store": {
                      "is_official": 1,
                      "os_logo_url": "https://ecs7.tokopedia.net/img/official_store/badge_os128.png"
                    },
                    "shop_alert_message": ""
                  },
                  "shop_shipments": [
                    {
                      "ship_id": 999,
                      "ship_name": "Custom Logistik",
                      "ship_code": "custom_2400899",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-custom.png",
                      "is_dropship_enabled": 0,
                      "ship_prods": [
                        {
                          "ship_prod_id": 999,
                          "ship_prod_name": "Service Normal",
                          "ship_group_name": "custom",
                          "ship_group_id": 999,
                          "additional_fee": 0,
                          "minimum_weight": 0
                        }
                      ]
                    }
                  ],
                  "dropshipper": {
                    "name": "",
                    "telp_no": ""
                  },
                  "products": [
                    {
                      "errors": [],
                      "cart_id": "16002227378256",
                      "product_id": 808702209,
                      "product_name": "Samsung Galaxy A31 6/128 GB A315G Black",
                      "product_price_fmt": "Rp3.049.000",
                      "product_price": 3049000,
                      "product_original_price": 3999000,
                      "product_wholesale_price": 3899000,
                      "product_wholesale_price_fmt": "Rp3.899.000",
                      "product_weight_fmt": "1000gr",
                      "product_weight": 1000,
                      "product_condition": 1,
                      "product_url": "https://www.tokopedia.com/samsung/samsung-galaxy-a31-6-128-gb-a315g-black",
                      "product_returnable": 0,
                      "product_is_free_returns": 0,
                      "product_is_preorder": 0,
                      "product_cashback": "",
                      "product_min_order": 1,
                      "product_invenage_value": 1,
                      "product_switch_invenage": 0,
                      "product_price_currency": 1,
                      "product_image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/5/9/21722219/21722219_2cfd628d-c052-4cf4-a954-b660ef3175ba_600_600",
                      "product_notes": "",
                      "product_quantity": 1,
                      "product_menu_id": 25256036,
                      "product_finsurance": 1,
                      "product_fcancel_partial": 0,
                      "product_cat_id": 3054,
                      "product_catalog_id": 0,
                      "product_category": "Handphone & Tablet / Handphone / Android OS",
                      "product_shipment": [],
                      "product_shipment_mapping": [],
                      "purchase_protection_plan_data": {
                        "protection_available": true,
                        "protection_type_id": 1357,
                        "protection_price_per_product": 46000,
                        "protection_price": 46000,
                        "protection_title": "Takut gadget baru kamu rusak?",
                        "protection_subtitle": "Lindungi gadget dari layar retak, terkena cairan, perampokan hanya Rp46,000",
                        "protection_link_text": "Pelajari",
                        "protection_link_url": "https://www.tokopedia.com/asuransi/proteksi/gadget/",
                        "protection_opt_in": true,
                        "protection_checkbox_disabled": false
                      },
                      "free_returns": {
                        "free_returns_logo": ""
                      },
                      "product_tracker_data": {
                        "attribution": "none/other",
                        "tracker_list_name": "none/other"
                      },
                      "product_preorder": {
                        "duration_text": "",
                        "duration_day": "0"
                      },
                      "trade_in_info": {
                        "is_valid_trade_in": true,
                        "new_device_price": 3899000,
                        "new_device_price_fmt": "Rp 3.899.000",
                        "old_device_price": 850000,
                        "old_device_price_fmt": "Rp850.000",
                        "drop_off_enable": true
                      },
                      "free_shipping": {
                        "eligible": false,
                        "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                      },
                      "product_ticker": {
                        "show_ticker": false,
                        "message": "Buruan bayar. Stok menipis dan sedang dibeli pembeli lain."
                      },
                      "variant_description_detail": {
                        "variant_name": [],
                        "variant_description": ""
                      },
                      "product_alert_message": "",
                      "product_information": []
                    }
                  ],
                  "warehouse": {
                    "warehouse_id": 98570,
                    "city_name": "Jakarta Utara"
                  }
                }
              ]
            }
          ],
          "donation": {
            "Title": "Donasi Rp 5.000 untuk APD Tenaga Medis",
            "Nominal": 5000,
            "Description": "Bantu garda terdepan COVID-19, jaga kesehatan para nakes melalui donasi pengadaan APD & Vitamin melalui partner BenihBaik."
          },
          "cod": {
            "is_cod": false,
            "counter_cod": -1
          },
          "message": {
            "message_info": "",
            "message_link": "",
            "message_logo": ""
          },
          "egold_attributes": {
            "eligible": true,
            "is_tiering": true,
            "range": {
              "min": 1500,
              "max": 5499
            },
            "message": {
              "title_text": "Yuk mulai nabung emas",
              "sub_text": "Bulatkan tagihan ini dengan",
              "ticker_text": "",
              "tooltip_text": "Nominal pembulatan disesuaikan dengan total tagihan setiap transaksi yang otomatis ditabung dan dapat di cek saldonya di Tokopedia Emas (berpartner dengan Pegadaian)."
            },
            "tier_data": [
              {
                "minimum_total_amount": 500,
                "minimum_amount": 1500,
                "maximum_amount": 5499,
                "basis_amount": 4000
              },
              {
                "minimum_total_amount": 98501,
                "minimum_amount": 1500,
                "maximum_amount": 11499,
                "basis_amount": 10000
              },
              {
                "minimum_total_amount": 998501,
                "minimum_amount": 1500,
                "maximum_amount": 51499,
                "basis_amount": 50000
              }
            ]
          },
          "tickers": [],
          "campaign_timer": {
            "description": "",
            "show_timer": false,
            "expired_timer_message": {
              "Button": "",
              "description": "",
              "title": ""
            },
            "timer_detail": {
              "deduct_time": "",
              "expire_duration": 0,
              "expired_time": "",
              "server_time": ""
            }
          },
          "addresses": {
            "active": "trade_in_address",
            "disable_tabs": ["default_address"],
            "data": [
              {
                "key": "trade_in_address",
                "value": {
                  "address_id": 98675797,
                  "address_name": "NGADIREJO MAGETAN",
                  "address": "DS. NGADIREJO RT 005 RW 002 NGADIREJO",
                  "postal_code": "63382",
                  "phone": "628561533911",
                  "receiver_name": "Irfan Khoirul Muhlishin",
                  "status": 1,
                  "country": "Indonesia",
                  "province_id": 15,
                  "province_name": "Jawa Timur",
                  "city_id": 227,
                  "city_name": "Kab. Magetan",
                  "district_id": 3156,
                  "district_name": "Kawedanan",
                  "address_2": "-7.6775,111.40925",
                  "latitude": "-7.6775",
                  "longitude": "111.40925",
                  "corner_id": 0,
                  "is_corner": false,
                  "is_primary": false,
                  "buyer_store_code": "F29I",
                  "type": 3
                }
              }
            ]
          },
          "disabled_features_detail": {
            "disabled_multi_address_message": ""
          },
          "promo": {
            "error_default": {
              "title": "promo tidak dapat digunakan",
              "description": "silahkan coba beberapa saat lagi"
            },
            "last_apply": {
              "code": "200000",
              "data": {
                "codes": [],
                "promo_code_id": 0,
                "cashback_advocate_referral_amount": 0,
                "cashback_wallet_amount": 0,
                "discount_amount": 0,
                "title_description": "",
                "is_tokopedia_gerai": false,
                "global_success": true,
                "gateway_id": "",
                "is_coupon": 0,
                "success": true,
                "invoice_description": "",
                "voucher_orders": [],
                "clashing_info_detail": {
                  "is_clashed_promos": false,
                  "clash_reason": "",
                  "clash_message": ""
                },
                "tracking_details": [],
                "message": {
                  "color": "",
                  "state": "",
                  "text": ""
                },
                "additional_info": {
                  "sp_ids": [],
                  "usage_summaries": [],
                  "message_info": {
                    "message": "Makin hemat pakai promo",
                    "detail": ""
                  },
                  "error_detail": {
                    "message": ""
                  },
                  "empty_cart_info": {
                    "image_url": "",
                    "detail": "",
                    "message": ""
                  }
                },
                "benefit_summary_info": {
                  "final_benefit_amount_str": "",
                  "final_benefit_amount": 0,
                  "final_benefit_text": "",
                  "summaries": []
                },
                "ticker_info": {
                  "unique_id": "",
                  "status_code": 0,
                  "message": ""
                }
              }
            }
          }
        }
      }
    }
""".trimIndent()