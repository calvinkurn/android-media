package com.tokopedia.cart

val availableCartItemMockData = """
    {
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
            "buttons": []
          },
          "shopping_summary": {
            "total_wording": "Total Harga (1 Barang)",
            "total_value": 2650000,
            "discount_total_wording": "Total Diskon Barang",
            "discount_value": 0,
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
              }
            ],
            "available_group": [
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
                  "shop_id": 1537583,
                  "user_id": 13077594,
                  "admin_ids": [],
                  "shop_name": "dgshop_id",
                  "shop_image": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2016/11/10/1537583/1537583_953bb48c-9cc2-4dd2-aef4-49568d704c81.jpg",
                  "shop_url": "https://www.tokopedia.com/dgshopid",
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
                  "address_id": 2286,
                  "postal_code": "14450",
                  "latitude": "-6.13845",
                  "longitude": "106.785367",
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
                  "shop_domain": "dgshopid",
                  "shop_shipments": [
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
                      "ship_id": 24,
                      "ship_name": "Lion Parcel",
                      "ship_code": "lion",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-lionparcel.png",
                      "ship_prods": [
                        {
                          "ship_prod_id": 47,
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
                "cart_string": "1537583-0-791002",
                "cart_details": [
                  {
                    "cart_id": "1829004756",
                    "product": {
                      "product_information": [],
                      "product_tracker_data": {
                        "attribution": "none/other",
                        "tracker_list_name": "none/other"
                      },
                      "isWishlist": false,
                      "product_id": 1109442445,
                      "product_name": "Mainan anak mobil aki Lambo Urus (MOB-2012)",
                      "product_price_fmt": "Rp2.650.000",
                      "product_price": 2650000,
                      "parent_id": 0,
                      "category_id": 516,
                      "category": "Mainan & Hobi / Mainan Remote Control / Mobil & Truk RC",
                      "catalog_id": 0,
                      "wholesale_price": [],
                      "product_weight": 99900,
                      "product_weight_fmt": "99900gr",
                      "product_condition": 1,
                      "product_status": 1,
                      "product_url": "https://www.tokopedia.com/dgshopid/mainan-anak-mobil-aki-lambo-urus-mob-2012",
                      "product_returnable": 0,
                      "is_freereturns": 0,
                      "is_preorder": 0,
                      "product_cashback": "",
                      "product_min_order": 1,
                      "product_max_order": 32767,
                      "product_rating": "0.000000",
                      "product_invenage_value": 12,
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
                        "original_amount": 2650000,
                        "description": "Harga Normal"
                      },
                      "product_price_currency": 1,
                      "product_image": {
                        "image_src": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/8/19/13077594/13077594_c3e46d81-7f2e-46cb-95e2-421b363553a8_621_621",
                        "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/8/19/13077594/13077594_c3e46d81-7f2e-46cb-95e2-421b363553a8_621_621",
                        "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/8/19/13077594/13077594_c3e46d81-7f2e-46cb-95e2-421b363553a8_621_621",
                        "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/product-1/2020/8/19/13077594/13077594_c3e46d81-7f2e-46cb-95e2-421b363553a8_621_621",
                        "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/product-1/2020/8/19/13077594/13077594_c3e46d81-7f2e-46cb-95e2-421b363553a8_621_621"
                      },
                      "product_all_images": "[{\"file_name\":\"13077594_c3e46d81-7f2e-46cb-95e2-421b363553a8_621_621\",\"file_path\":\"product-1/2020/8/19/13077594\",\"status\":2},{\"file_name\":\"13077594_b6cc7a0d-e82a-4abf-9b34-9cb87aa997d7_1080_1080\",\"file_path\":\"product-1/2020/8/19/13077594\",\"status\":1},{\"file_name\":\"13077594_2bab0fee-2b39-4266-9ac4-fbffdfcde1c5_1080_1080\",\"file_path\":\"product-1/2020/8/19/13077594\",\"status\":1},{\"file_name\":\"13077594_d18a68a6-619c-48dd-ba56-673dbc6224c6_776_776\",\"file_path\":\"product-1/2020/8/19/13077594\",\"status\":1}]",
                      "product_notes": "",
                      "product_quantity": 1,
                      "product_weight_unit_code": 1,
                      "product_weight_unit_text": "gr",
                      "last_update_price": "1597817847",
                      "is_update_price": false,
                      "product_preorder": {
                        "duration_day": "0",
                        "duration_text": "",
                        "duration_unit_code": "0",
                        "duration_unit_text": "",
                        "duration_value": "0"
                      },
                      "product_showcase": {
                        "name": "",
                        "id": 0
                      },
                      "product_alias": "mainan-anak-mobil-aki-lambo-urus-mob-2012",
                      "sku": "",
                      "campaign_id": 0,
                      "product_original_price": 0,
                      "product_price_original_fmt": "",
                      "is_slash_price": false,
                      "free_returns": {
                        "free_returns_logo": ""
                      },
                      "product_finsurance": 1,
                      "is_wishlisted": true,
                      "is_ppp": false,
                      "is_cod": false,
                      "warehouse_id": 791002,
                      "is_parent": false,
                      "is_campaign_error": false,
                      "is_blacklisted": false,
                      "free_shipping": {
                        "eligible": true,
                        "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                      },
                      "booking_stock": 0,
                      "product_variant": {
                        "parent_id": 0,
                        "default_child": 0,
                        "variant": []
                      },
                      "is_product_volume_weight": false,
                      "initial_price": 2650000,
                      "initial_price_fmt": "",
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
                    "checkbox_state": true,
                    "similar_product_url": "",
                    "similar_product": {
                      "text": "",
                      "url": ""
                    },
                    "nicotine_lite_message": {
                      "text": "",
                      "url": ""
                    }
                  }
                ],
                "total_cart_details_error": 0,
                "total_cart_price": 2650000,
                "errors": [],
                "sort_key": 1829004756,
                "is_fulfillment_service": false,
                "warehouse": {
                  "warehouse_id": 791002,
                  "partner_id": 0,
                  "shop_id": 1537583,
                  "warehouse_name": "Shop Location",
                  "district_id": 2286,
                  "district_name": "Penjaringan",
                  "city_id": 177,
                  "city_name": "Jakarta Utara",
                  "province_id": 13,
                  "province_name": "DKI Jakarta",
                  "status": 1,
                  "postal_code": "14450",
                  "is_default": 1,
                  "latlon": "-6.13845,106.785367",
                  "latitude": "-6.13845",
                  "longitude": "106.785367",
                  "email": "",
                  "address_detail": "Jl. Teluk Gong, Pangkalan Bambu No. 28 C Rt. 005 Rw. 017, Kec. Penjaringan, Kel. Pejagalan, Jakarta Utara, 14450. (Gang Helm)",
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
          "total_product_price": 2873000,
          "total_product_count": 2,
          "total_product_error": 0,
          "global_coupon_attr": {
            "description": "Gunakan promo Tokopedia",
            "quantity_label": "5 Kupon"
          },
          "global_checkbox_state": false,
          "tickers": [
            {
              "id": 0,
              "message": "Hai Member Gold, kuota Bebas Ongkir kamu sisa 3x (untuk 1 pesanan/transaksi) buat minggu ini.",
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
                "voucher_orders": []
              },
              "code": "200000"
            },
            "error_default": {
              "title": "promo tidak dapat digunakan",
              "description": "silahkan coba beberapa saat lagi"
            }
          },
          "customer_data": {
            "name": "",
            "email": "",
            "phone": ""
          }
        }
      }
    }
""".trimIndent()

val errorCartItemMockData = """
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
            "buttons": []
          },
          "shopping_summary": {
            "total_wording": "Total Harga (0 Barang)",
            "total_value": 0,
            "discount_total_wording": "Total Diskon Barang",
            "discount_value": 0,
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
              }
            ],
            "available_group": []
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
              "selected_unavailable_action_id": 6,
              "unavailable_description": "Tidak bisa dibeli melalui aplikasi Android dan iOS",
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
                  "id": 6,
                  "code": "CHECKOUTBROWSER",
                  "message": "Beli lewat Browser"
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
                    "shop_id": 2596159,
                    "user_id": 19889746,
                    "admin_ids": [],
                    "shop_name": "BERKAT TOPED",
                    "shop_image": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2017/10/18/2596159/2596159_89280fe9-1b72-4c9a-93c8-1445e4ceb87f.jpg",
                    "shop_url": "https://www.tokopedia.com/grouceries",
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
                    "address_id": 1595,
                    "postal_code": "15710",
                    "latitude": "-6.242832",
                    "longitude": "106.507443",
                    "district_id": 1595,
                    "district_name": "",
                    "origin": 0,
                    "address_street": "",
                    "province_id": 0,
                    "city_id": 144,
                    "city_name": "Kab. Tangerang",
                    "province_name": "",
                    "country_name": "",
                    "is_allow_manage": false,
                    "shop_domain": "grouceries",
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
                      }
                    ]
                  },
                  "promo_codes": [],
                  "cart_string": "2596159-0-109685",
                  "cart_details": [
                    {
                      "cart_id": "1825060567",
                      "selected_unavailable_action_link": "",
                      "product": {
                        "product_information": [],
                        "product_tracker_data": {
                          "attribution": "none/other",
                          "tracker_list_name": "none/other"
                        },
                        "isWishlist": false,
                        "product_id": 324954168,
                        "product_name": "Rokok sampoerna mild 16",
                        "product_price_fmt": "Rp223.000",
                        "product_price": 223000,
                        "parent_id": 0,
                        "category_id": 4398,
                        "category": "Kesehatan / Produk Dewasa / Produk Dewasa Lainnya",
                        "catalog_id": 0,
                        "wholesale_price": [],
                        "product_weight": 290,
                        "product_weight_fmt": "290gr",
                        "product_condition": 1,
                        "product_status": 1,
                        "product_url": "https://www.tokopedia.com/grouceries/rokok-sampoerna-mild-16",
                        "product_returnable": 0,
                        "is_freereturns": 0,
                        "is_preorder": 0,
                        "product_cashback": "",
                        "product_min_order": 1,
                        "product_max_order": 32767,
                        "product_rating": "0.000000",
                        "product_invenage_value": 155,
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
                          "original_amount": 223000,
                          "description": "Harga Normal"
                        },
                        "product_price_currency": 1,
                        "product_image": {
                          "image_src": "https://ecs7.tokopedia.net/img/cache/700/attachment/2019/12/5/157550531978767/157550531978767_6d2bbba4-32e4-4d64-b8b3-d0081fd34120.png",
                          "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/12/5/157550531978767/157550531978767_6d2bbba4-32e4-4d64-b8b3-d0081fd34120.png",
                          "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/12/5/157550531978767/157550531978767_6d2bbba4-32e4-4d64-b8b3-d0081fd34120.png",
                          "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/attachment/2019/12/5/157550531978767/157550531978767_6d2bbba4-32e4-4d64-b8b3-d0081fd34120.png",
                          "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/attachment/2019/12/5/157550531978767/157550531978767_6d2bbba4-32e4-4d64-b8b3-d0081fd34120.png"
                        },
                        "product_all_images": "[{\"file_name\":\"157550531978767_6d2bbba4-32e4-4d64-b8b3-d0081fd34120.png\",\"file_path\":\"attachment/2019/12/5/157550531978767\",\"status\":2}]",
                        "product_notes": "",
                        "product_quantity": 1,
                        "product_weight_unit_code": 1,
                        "product_weight_unit_text": "gr",
                        "last_update_price": "1599044904",
                        "is_update_price": false,
                        "product_preorder": {
                          "duration_day": "0",
                          "duration_text": "",
                          "duration_unit_code": "0",
                          "duration_unit_text": "",
                          "duration_value": "0"
                        },
                        "product_showcase": {
                          "name": "",
                          "id": 0
                        },
                        "product_alias": "rokok-sampoerna-mild-16",
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
                        "warehouse_id": 109685,
                        "is_parent": false,
                        "is_campaign_error": false,
                        "is_blacklisted": true,
                        "free_shipping": {
                          "eligible": false,
                          "badge_url": ""
                        },
                        "booking_stock": 0,
                        "product_variant": {
                          "parent_id": 0,
                          "default_child": 0,
                          "variant": []
                        },
                        "is_product_volume_weight": false,
                        "initial_price": 223000,
                        "initial_price_fmt": "",
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
                      "checkbox_state": true,
                      "similar_product_url": "",
                      "similar_product": {
                        "text": "",
                        "url": ""
                      },
                      "nicotine_lite_message": {
                        "text": "",
                        "url": ""
                      }
                    }
                  ],
                  "total_cart_details_error": 0,
                  "total_cart_price": 223000,
                  "errors": [],
                  "sort_key": 1825060567,
                  "is_fulfillment_service": false,
                  "warehouse": {
                    "warehouse_id": 109685,
                    "partner_id": 0,
                    "shop_id": 2596159,
                    "warehouse_name": "Shop Location",
                    "district_id": 1595,
                    "district_name": "Cikupa",
                    "city_id": 144,
                    "city_name": "Kab. Tangerang",
                    "province_id": 11,
                    "province_name": "Banten",
                    "status": 1,
                    "postal_code": "15710",
                    "is_default": 1,
                    "latlon": "-6.242832,106.507443",
                    "latitude": "-6.242832",
                    "longitude": "106.507443",
                    "email": "",
                    "address_detail": "Jl. Chalcedony no. 02 No 06  Masuk gerbang belok kanan rumah ke dua pagar putih",
                    "country_name": "Indonesia",
                    "is_fulfillment": false
                  },
                  "checkbox_state": true
                }
              ]
            }
          ],
          "total_product_price": 223000,
          "total_product_count": 1,
          "total_product_error": 0,
          "global_coupon_attr": {
            "description": "Gunakan promo Tokopedia",
            "quantity_label": "5 Kupon"
          },
          "global_checkbox_state": false,
          "tickers": [],
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
                "voucher_orders": []
              },
              "code": "200000"
            },
            "error_default": {
              "title": "promo tidak dapat digunakan",
              "description": "silahkan coba beberapa saat lagi"
            }
          },
          "customer_data": {
            "name": "",
            "email": "",
            "phone": ""
          }
        }
      }
    }
""".trimIndent()

val availableAndErrorCartItemMockData = """
    {
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
            "buttons": []
          },
          "shopping_summary": {
            "total_wording": "Total Harga (1 Barang)",
            "total_value": 2650000,
            "discount_total_wording": "Total Diskon Barang",
            "discount_value": 0,
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
              }
            ],
            "available_group": [
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
                  "shop_id": 1537583,
                  "user_id": 13077594,
                  "admin_ids": [],
                  "shop_name": "dgshop_id",
                  "shop_image": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2016/11/10/1537583/1537583_953bb48c-9cc2-4dd2-aef4-49568d704c81.jpg",
                  "shop_url": "https://www.tokopedia.com/dgshopid",
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
                  "address_id": 2286,
                  "postal_code": "14450",
                  "latitude": "-6.13845",
                  "longitude": "106.785367",
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
                  "shop_domain": "dgshopid",
                  "shop_shipments": [
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
                      "ship_id": 24,
                      "ship_name": "Lion Parcel",
                      "ship_code": "lion",
                      "ship_logo": "https://ecs7.tokopedia.net/img/kurir-lionparcel.png",
                      "ship_prods": [
                        {
                          "ship_prod_id": 47,
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
                "cart_string": "1537583-0-791002",
                "cart_details": [
                  {
                    "cart_id": "1829004756",
                    "product": {
                      "product_information": [],
                      "product_tracker_data": {
                        "attribution": "none/other",
                        "tracker_list_name": "none/other"
                      },
                      "isWishlist": false,
                      "product_id": 1109442445,
                      "product_name": "Mainan anak mobil aki Lambo Urus (MOB-2012)",
                      "product_price_fmt": "Rp2.650.000",
                      "product_price": 2650000,
                      "parent_id": 0,
                      "category_id": 516,
                      "category": "Mainan & Hobi / Mainan Remote Control / Mobil & Truk RC",
                      "catalog_id": 0,
                      "wholesale_price": [],
                      "product_weight": 99900,
                      "product_weight_fmt": "99900gr",
                      "product_condition": 1,
                      "product_status": 1,
                      "product_url": "https://www.tokopedia.com/dgshopid/mainan-anak-mobil-aki-lambo-urus-mob-2012",
                      "product_returnable": 0,
                      "is_freereturns": 0,
                      "is_preorder": 0,
                      "product_cashback": "",
                      "product_min_order": 1,
                      "product_max_order": 32767,
                      "product_rating": "0.000000",
                      "product_invenage_value": 12,
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
                        "original_amount": 2650000,
                        "description": "Harga Normal"
                      },
                      "product_price_currency": 1,
                      "product_image": {
                        "image_src": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/8/19/13077594/13077594_c3e46d81-7f2e-46cb-95e2-421b363553a8_621_621",
                        "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/8/19/13077594/13077594_c3e46d81-7f2e-46cb-95e2-421b363553a8_621_621",
                        "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/8/19/13077594/13077594_c3e46d81-7f2e-46cb-95e2-421b363553a8_621_621",
                        "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/product-1/2020/8/19/13077594/13077594_c3e46d81-7f2e-46cb-95e2-421b363553a8_621_621",
                        "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/product-1/2020/8/19/13077594/13077594_c3e46d81-7f2e-46cb-95e2-421b363553a8_621_621"
                      },
                      "product_all_images": "[{\"file_name\":\"13077594_c3e46d81-7f2e-46cb-95e2-421b363553a8_621_621\",\"file_path\":\"product-1/2020/8/19/13077594\",\"status\":2},{\"file_name\":\"13077594_b6cc7a0d-e82a-4abf-9b34-9cb87aa997d7_1080_1080\",\"file_path\":\"product-1/2020/8/19/13077594\",\"status\":1},{\"file_name\":\"13077594_2bab0fee-2b39-4266-9ac4-fbffdfcde1c5_1080_1080\",\"file_path\":\"product-1/2020/8/19/13077594\",\"status\":1},{\"file_name\":\"13077594_d18a68a6-619c-48dd-ba56-673dbc6224c6_776_776\",\"file_path\":\"product-1/2020/8/19/13077594\",\"status\":1}]",
                      "product_notes": "",
                      "product_quantity": 1,
                      "product_weight_unit_code": 1,
                      "product_weight_unit_text": "gr",
                      "last_update_price": "1597817847",
                      "is_update_price": false,
                      "product_preorder": {
                        "duration_day": "0",
                        "duration_text": "",
                        "duration_unit_code": "0",
                        "duration_unit_text": "",
                        "duration_value": "0"
                      },
                      "product_showcase": {
                        "name": "",
                        "id": 0
                      },
                      "product_alias": "mainan-anak-mobil-aki-lambo-urus-mob-2012",
                      "sku": "",
                      "campaign_id": 0,
                      "product_original_price": 0,
                      "product_price_original_fmt": "",
                      "is_slash_price": false,
                      "free_returns": {
                        "free_returns_logo": ""
                      },
                      "product_finsurance": 1,
                      "is_wishlisted": true,
                      "is_ppp": false,
                      "is_cod": false,
                      "warehouse_id": 791002,
                      "is_parent": false,
                      "is_campaign_error": false,
                      "is_blacklisted": false,
                      "free_shipping": {
                        "eligible": true,
                        "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                      },
                      "booking_stock": 0,
                      "product_variant": {
                        "parent_id": 0,
                        "default_child": 0,
                        "variant": []
                      },
                      "is_product_volume_weight": false,
                      "initial_price": 2650000,
                      "initial_price_fmt": "",
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
                    "checkbox_state": true,
                    "similar_product_url": "",
                    "similar_product": {
                      "text": "",
                      "url": ""
                    },
                    "nicotine_lite_message": {
                      "text": "",
                      "url": ""
                    }
                  }
                ],
                "total_cart_details_error": 0,
                "total_cart_price": 2650000,
                "errors": [],
                "sort_key": 1829004756,
                "is_fulfillment_service": false,
                "warehouse": {
                  "warehouse_id": 791002,
                  "partner_id": 0,
                  "shop_id": 1537583,
                  "warehouse_name": "Shop Location",
                  "district_id": 2286,
                  "district_name": "Penjaringan",
                  "city_id": 177,
                  "city_name": "Jakarta Utara",
                  "province_id": 13,
                  "province_name": "DKI Jakarta",
                  "status": 1,
                  "postal_code": "14450",
                  "is_default": 1,
                  "latlon": "-6.13845,106.785367",
                  "latitude": "-6.13845",
                  "longitude": "106.785367",
                  "email": "",
                  "address_detail": "Jl. Teluk Gong, Pangkalan Bambu No. 28 C Rt. 005 Rw. 017, Kec. Penjaringan, Kel. Pejagalan, Jakarta Utara, 14450. (Gang Helm)",
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
              "selected_unavailable_action_id": 6,
              "unavailable_description": "Tidak bisa dibeli melalui aplikasi Android dan iOS",
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
                  "id": 6,
                  "code": "CHECKOUTBROWSER",
                  "message": "Beli lewat Browser"
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
                    "shop_id": 2596159,
                    "user_id": 19889746,
                    "admin_ids": [],
                    "shop_name": "BERKAT TOPED",
                    "shop_image": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2017/10/18/2596159/2596159_89280fe9-1b72-4c9a-93c8-1445e4ceb87f.jpg",
                    "shop_url": "https://www.tokopedia.com/grouceries",
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
                    "address_id": 1595,
                    "postal_code": "15710",
                    "latitude": "-6.242832",
                    "longitude": "106.507443",
                    "district_id": 1595,
                    "district_name": "",
                    "origin": 0,
                    "address_street": "",
                    "province_id": 0,
                    "city_id": 144,
                    "city_name": "Kab. Tangerang",
                    "province_name": "",
                    "country_name": "",
                    "is_allow_manage": false,
                    "shop_domain": "grouceries",
                    "shop_shipments": [
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
                      }
                    ]
                  },
                  "promo_codes": [],
                  "cart_string": "2596159-0-109685",
                  "cart_details": [
                    {
                      "cart_id": "1825060567",
                      "selected_unavailable_action_link": "",
                      "product": {
                        "product_information": [],
                        "product_tracker_data": {
                          "attribution": "none/other",
                          "tracker_list_name": "none/other"
                        },
                        "isWishlist": false,
                        "product_id": 324954168,
                        "product_name": "Rokok sampoerna mild 16",
                        "product_price_fmt": "Rp223.000",
                        "product_price": 223000,
                        "parent_id": 0,
                        "category_id": 4398,
                        "category": "Kesehatan / Produk Dewasa / Produk Dewasa Lainnya",
                        "catalog_id": 0,
                        "wholesale_price": [],
                        "product_weight": 290,
                        "product_weight_fmt": "290gr",
                        "product_condition": 1,
                        "product_status": 1,
                        "product_url": "https://www.tokopedia.com/grouceries/rokok-sampoerna-mild-16",
                        "product_returnable": 0,
                        "is_freereturns": 0,
                        "is_preorder": 0,
                        "product_cashback": "",
                        "product_min_order": 1,
                        "product_max_order": 32767,
                        "product_rating": "0.000000",
                        "product_invenage_value": 155,
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
                          "original_amount": 223000,
                          "description": "Harga Normal"
                        },
                        "product_price_currency": 1,
                        "product_image": {
                          "image_src": "https://ecs7.tokopedia.net/img/cache/700/attachment/2019/12/5/157550531978767/157550531978767_6d2bbba4-32e4-4d64-b8b3-d0081fd34120.png",
                          "image_src_100_square": "https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/12/5/157550531978767/157550531978767_6d2bbba4-32e4-4d64-b8b3-d0081fd34120.png",
                          "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/12/5/157550531978767/157550531978767_6d2bbba4-32e4-4d64-b8b3-d0081fd34120.png",
                          "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/attachment/2019/12/5/157550531978767/157550531978767_6d2bbba4-32e4-4d64-b8b3-d0081fd34120.png",
                          "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/attachment/2019/12/5/157550531978767/157550531978767_6d2bbba4-32e4-4d64-b8b3-d0081fd34120.png"
                        },
                        "product_all_images": "[{\"file_name\":\"157550531978767_6d2bbba4-32e4-4d64-b8b3-d0081fd34120.png\",\"file_path\":\"attachment/2019/12/5/157550531978767\",\"status\":2}]",
                        "product_notes": "",
                        "product_quantity": 1,
                        "product_weight_unit_code": 1,
                        "product_weight_unit_text": "gr",
                        "last_update_price": "1599044904",
                        "is_update_price": false,
                        "product_preorder": {
                          "duration_day": "0",
                          "duration_text": "",
                          "duration_unit_code": "0",
                          "duration_unit_text": "",
                          "duration_value": "0"
                        },
                        "product_showcase": {
                          "name": "",
                          "id": 0
                        },
                        "product_alias": "rokok-sampoerna-mild-16",
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
                        "warehouse_id": 109685,
                        "is_parent": false,
                        "is_campaign_error": false,
                        "is_blacklisted": true,
                        "free_shipping": {
                          "eligible": false,
                          "badge_url": ""
                        },
                        "booking_stock": 0,
                        "product_variant": {
                          "parent_id": 0,
                          "default_child": 0,
                          "variant": []
                        },
                        "is_product_volume_weight": false,
                        "initial_price": 223000,
                        "initial_price_fmt": "",
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
                      "checkbox_state": true,
                      "similar_product_url": "",
                      "similar_product": {
                        "text": "",
                        "url": ""
                      },
                      "nicotine_lite_message": {
                        "text": "",
                        "url": ""
                      }
                    }
                  ],
                  "total_cart_details_error": 0,
                  "total_cart_price": 223000,
                  "errors": [],
                  "sort_key": 1825060567,
                  "is_fulfillment_service": false,
                  "warehouse": {
                    "warehouse_id": 109685,
                    "partner_id": 0,
                    "shop_id": 2596159,
                    "warehouse_name": "Shop Location",
                    "district_id": 1595,
                    "district_name": "Cikupa",
                    "city_id": 144,
                    "city_name": "Kab. Tangerang",
                    "province_id": 11,
                    "province_name": "Banten",
                    "status": 1,
                    "postal_code": "15710",
                    "is_default": 1,
                    "latlon": "-6.242832,106.507443",
                    "latitude": "-6.242832",
                    "longitude": "106.507443",
                    "email": "",
                    "address_detail": "Jl. Chalcedony no. 02 No 06  Masuk gerbang belok kanan rumah ke dua pagar putih",
                    "country_name": "Indonesia",
                    "is_fulfillment": false
                  },
                  "checkbox_state": true
                }
              ]
            }
          ],
          "total_product_price": 2873000,
          "total_product_count": 2,
          "total_product_error": 0,
          "global_coupon_attr": {
            "description": "Gunakan promo Tokopedia",
            "quantity_label": "5 Kupon"
          },
          "global_checkbox_state": false,
          "tickers": [
            {
              "id": 0,
              "message": "Hai Member Gold, kuota Bebas Ongkir kamu sisa 3x (untuk 1 pesanan/transaksi) buat minggu ini.",
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
                "voucher_orders": []
              },
              "code": "200000"
            },
            "error_default": {
              "title": "promo tidak dapat digunakan",
              "description": "silahkan coba beberapa saat lagi"
            }
          },
          "customer_data": {
            "name": "",
            "email": "",
            "phone": ""
          }
        }
      }
    }
""".trimIndent()