package com.tokopedia.purchase_platform

val apiResponseAvailableShopJson = """
    {
      "shop_group_simplified": {
        "status": "OK",
        "error_message": [],
        "data": {
          "errors": [],
          "is_coupon_active": 1,
          "is_one_tab_promo": false,
          "max_quantity": 10000,
          "max_char_note": 144,
          "messages": {
            "ErrorFieldBetween": "Jumlah harus diisi antara 1 - {{value}}",
            "ErrorFieldMaxChar": "Catatan terlalu panjang, maks. {{value}} karakter.",
            "ErrorFieldRequired": "Jumlah harus diisi",
            "ErrorProductAvailableStock": "Stok tersedia:{{value}}",
            "ErrorProductAvailableStockDetail": "Harap kurangi jumlah barang",
            "ErrorProductMaxQuantity": "Maks. pembelian barang ini {{value}} item, Kurangi pembelianmu, ya!",
            "ErrorProductMinQuantity": "Min. pembelian produk ini {{value}} barang. Yuk, atur ulang pembelianmu."
          },
          "donation": {
            "Title": "TopDonasi500",
            "Nominal": 500,
            "Description": "Hingga 24 Oktober 2019, donasi Toppers akan disalurkan melalui Yayasan Sekolah Relawan untuk mitigasi kebakaran hutan di daerah Sumatera dan Kalimantan serta membantu warga terdampak."
          },
          "autoapply_stack": {
            "global_success": false,
            "success": false,
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
            "gateway_id": "",
            "is_coupon": 0,
            "coupon_description": "",
            "voucher_orders": [],
            "benefit_summary_info": {
              "final_benefit_text": "",
              "final_benefit_amount": 0,
              "final_benefit_amount_str": "",
              "summaries": []
            },
            "clashing_info_detail": {
              "clash_message": "",
              "clash_reason": "",
              "is_clashed_promos": false,
              "options": []
            },
            "tracking_details": [],
            "ticker_info": {
              "unique_id": "",
              "status_code": 0,
              "message": ""
            },
            "benefit_details": []
          },
          "global_coupon_attr": {
            "description": "Gunakan promo Tokopedia",
            "quantity_label": "2 Kupon"
          },
          "tickers": [
            {
              "id": 0,
              "message": "Pesanan di keranjangmu berpotensi dapat Bebas Ongkir",
              "page": "cart"
            }
          ],
          "default_promo_dialog_tab": "voucher",
          "total_product_price": "747000",
          "total_product_count": 1,
          "total_product_error": 0,
          "global_checkbox_state": true,
          "hashed_email": "cf3b720c4156488b8e0d8c8911f61550",
          "shop_group_available": [
            {
              "errors": [],
              "user_address_id": 0,
              "cart_string": "1882775-0-9876",
              "total_cart_details_error": 0,
              "total_cart_price": "747000",
              "sort_key": 1150920510,
              "is_fulfillment_service": false,
              "has_promo_list": true,
              "shop": {
                "shop_id": 1882775,
                "user_id": 16234150,
                "shop_name": "HP Official",
                "shop_image": "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2017/5/29/1882775/1882775_b4d09938-3009-4fd7-9918-e4fe2c482844.png",
                "shop_url": "https://www.tokopedia.com/hp",
                "shop_status": 1,
                "is_gold": 1,
                "is_gold_badge": true,
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
                "address_id": 2285,
                "postal_code": "14430",
                "latitude": "-6.138409999999999",
                "longitude": "106.83214999999996",
                "district_id": 2285,
                "district_name": "",
                "origin": 0,
                "address_street": "",
                "province_id": 0,
                "city_id": 177,
                "city_name": "Jakarta Utara",
                "province_name": "",
                "country_name": "",
                "is_allow_manage": false,
                "shop_domain": "hp"
              },
              "warehouse": {
                "warehouse_id": 9876,
                "partner_id": 0,
                "shop_id": 1882775,
                "warehouse_name": "Shop Location",
                "district_id": 2285,
                "district_name": "Pademangan",
                "city_id": 177,
                "city_name": "Jakarta Utara",
                "province_id": 13,
                "province_name": "DKI Jakarta",
                "status": 1,
                "postal_code": "14430",
                "is_default": 1,
                "latlon": "-6.138409999999999,106.83214999999996",
                "latitude": "-6.138409999999999",
                "longitude": "106.83214999999996",
                "email": "",
                "address_detail": "Jl. Gunung Sahari Raya No.1 Rukan Mangga Dua Square Basment Blok F No.9 Ancol Pademangan",
                "country_name": "Indonesia",
                "is_fulfillment": false
              },
              "checkbox_state": true,
              "cart_details": [
                {
                  "errors": [],
                  "cart_id": "1150920510",
                  "checkbox_state": true,
                  "similar_product_url": "",
                  "similar_product": {
                    "text": "",
                    "url": ""
                  },
                  "messages": [],
                  "product": {
                    "product_tracker_data": {
                      "attribution": "none/other",
                      "tracker_list_name": "none/other"
                    },
                    "isWishlist": false,
                    "product_id": 353641684,
                    "product_name": "HP 678 Tri-clr/Blk Ink Crtg Combo 2-Pk [L0S24AA]",
                    "product_price_fmt": "Rp249.000",
                    "product_price": 249000,
                    "parent_id": 0,
                    "category_id": 4000,
                    "category": "Elektronik / Printer / Tinta Printer",
                    "catalog_id": 0,
                    "wholesale_price": [],
                    "product_weight_fmt": "1000gr",
                    "product_weight": 1000,
                    "product_condition": 1,
                    "product_status": 1,
                    "product_url": "https://www.tokopedia.com/hp/hp-678-tri-clr-blk-ink-crtg-combo-2-pk-l0s24aa",
                    "product_returnable": 0,
                    "is_freereturns": 0,
                    "is_preorder": 0,
                    "product_cashback": "",
                    "product_min_order": 1,
                    "product_max_order": 10000,
                    "product_rating": "0.000000",
                    "product_invenage_value": 629,
                    "product_switch_invenage": 1,
                    "product_price_currency": 1,
                    "product_invenage_total": {
                      "is_counted_by_user": false,
                      "is_counted_by_product": false,
                      "by_user": {
                        "in_cart": 0,
                        "last_stock_less_than": 0
                      },
                      "by_user_text": {
                        "in_cart": "",
                        "last_stock_less_than": "",
                        "complete": ""
                      },
                      "by_product": {
                        "in_cart": 0,
                        "last_stock_less_than": 0
                      },
                      "by_product_text": {
                        "in_cart": "",
                        "last_stock_less_than": "",
                        "complete": ""
                      }
                    },
                    "price_changes": {
                      "changes_state": 0,
                      "amount_difference": 0,
                      "original_amount": 249000,
                      "description": "Harga Normal"
                    },
                    "product_image": {
                      "image_src": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/10/28/1882775/1882775_20fce93b-806f-46aa-8e45-dfca255a2c75.jpg",
                      "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/10/28/1882775/1882775_20fce93b-806f-46aa-8e45-dfca255a2c75.jpg",
                      "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/product-1/2019/10/28/1882775/1882775_20fce93b-806f-46aa-8e45-dfca255a2c75.jpg",
                      "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/product-1/2019/10/28/1882775/1882775_20fce93b-806f-46aa-8e45-dfca255a2c75.jpg"
                    },
                    "product_all_images": "[{\"FilePath\":\"product-1/2019/10/28/1882775\",\"FileName\":\"1882775_20fce93b-806f-46aa-8e45-dfca255a2c75.jpg\",\"Status\":2}]",
                    "product_notes": "",
                    "product_quantity": 3,
                    "product_weight_unit_code": 1,
                    "product_weight_unit_text": "gr",
                    "last_update_price": "1571630758",
                    "is_update_price": false,
                    "product_preorder": {
                      "duration_text": "",
                      "duration_day": "0",
                      "duration_unit_code": "0",
                      "duration_unit_text": "",
                      "duration_value": "0"
                    },
                    "product_showcase": {
                      "id": 18629162
                    },
                    "product_alias": "hp-678-tri-clr-blk-ink-crtg-combo-2-pk-l0s24aa",
                    "sku": "L0S24AA",
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
                    "warehouse_id": 9876,
                    "is_parent": false,
                    "is_campaign_error": false
                  }
                }
              ]
            }
          ],
          "shop_group_with_errors": []
        }
      }
    }
""".trimIndent()

val apiResponseShopErrorJson = """
    {
      "shop_group_simplified": {
        "status": "OK",
        "error_message": [],
        "data": {
          "errors": [],
          "is_coupon_active": 1,
          "is_one_tab_promo": false,
          "max_quantity": 10000,
          "max_char_note": 144,
          "messages": {
            "ErrorFieldBetween": "Jumlah harus diisi antara 1 - {{value}}",
            "ErrorFieldMaxChar": "Catatan terlalu panjang, maks. {{value}} karakter.",
            "ErrorFieldRequired": "Jumlah harus diisi",
            "ErrorProductAvailableStock": "Stok tersedia:{{value}}",
            "ErrorProductAvailableStockDetail": "Harap kurangi jumlah barang",
            "ErrorProductMaxQuantity": "Maks. pembelian barang ini {{value}} item, Kurangi pembelianmu, ya!",
            "ErrorProductMinQuantity": "Min. pembelian produk ini {{value}} barang. Yuk, atur ulang pembelianmu."
          },
          "donation": {
            "Title": "TopDonasi500",
            "Nominal": 500,
            "Description": "Hingga 24 Oktober 2019, donasi Toppers akan disalurkan melalui Yayasan Sekolah Relawan untuk mitigasi kebakaran hutan di daerah Sumatera dan Kalimantan serta membantu warga terdampak."
          },
          "autoapply_stack": {
            "global_success": false,
            "success": false,
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
            "gateway_id": "",
            "is_coupon": 0,
            "coupon_description": "",
            "voucher_orders": [],
            "benefit_summary_info": {
              "final_benefit_text": "",
              "final_benefit_amount": 0,
              "final_benefit_amount_str": "",
              "summaries": []
            },
            "clashing_info_detail": {
              "clash_message": "",
              "clash_reason": "",
              "is_clashed_promos": false,
              "options": []
            },
            "tracking_details": [],
            "ticker_info": {
              "unique_id": "",
              "status_code": 0,
              "message": ""
            },
            "benefit_details": []
          },
          "global_coupon_attr": {
            "description": "Gunakan promo Tokopedia",
            "quantity_label": "1 Kupon"
          },
          "tickers": [],
          "default_promo_dialog_tab": "voucher",
          "total_product_price": "0",
          "total_product_count": 0,
          "total_product_error": 1,
          "global_checkbox_state": false,
          "hashed_email": "4c338847bdcb1321794cdbd2d28b3fa1",
          "shop_group_available": [],
          "shop_group_with_errors": [
            {
              "errors": [
                "Bukan Barang Testing",
                "Bukan Barang Testing"
              ],
              "user_address_id": 0,
              "cart_string": "3987128-0-2987341",
              "total_cart_details_error": 1,
              "total_cart_price": "50000",
              "sort_key": 1151266921,
              "is_fulfillment_service": false,
              "has_promo_list": false,
              "shop": {
                "shop_id": 3987128,
                "user_id": 39467501,
                "shop_name": "Meraih mimpi",
                "shop_image": "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2018/9/4/39467501/39467501_0b1e17e3-3302-4b04-8718-5c77dd813ddb.png",
                "shop_url": "https://www.tokopedia.com/totokaku",
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
                "official_store": {
                  "is_official": 0,
                  "os_logo_url": ""
                },
                "address_id": 2209,
                "postal_code": "17115",
                "latitude": "-6.275157600000001",
                "longitude": "107.00107939999998",
                "district_id": 2209,
                "district_name": "",
                "origin": 0,
                "address_street": "",
                "province_id": 0,
                "city_id": 167,
                "city_name": "Kota Bekasi",
                "province_name": "",
                "country_name": "",
                "is_allow_manage": false,
                "shop_domain": "totokaku"
              },
              "warehouse": {
                "warehouse_id": 2987341,
                "partner_id": 0,
                "shop_id": 3987128,
                "warehouse_name": "Shop location",
                "district_id": 2209,
                "district_name": "Rawalumbu",
                "city_id": 167,
                "city_name": "Kota Bekasi",
                "province_id": 12,
                "province_name": "Jawa Barat",
                "status": 1,
                "postal_code": "17115",
                "is_default": 1,
                "latlon": "-6.275157600000001,107.00107939999998",
                "latitude": "-6.275157600000001",
                "longitude": "107.00107939999998",
                "email": "",
                "address_detail": "Penegak J No 95 rawalumbu bekasi",
                "country_name": "Indonesia",
                "is_fulfillment": false
              },
              "checkbox_state": true,
              "cart_details": [
                {
                  "errors": [],
                  "cart_id": "1151266921",
                  "checkbox_state": true,
                  "similar_product_url": "",
                  "similar_product": {
                    "text": "",
                    "url": ""
                  },
                  "messages": [],
                  "product": {
                    "product_tracker_data": {
                      "attribution": "none/other",
                      "tracker_list_name": "none/other"
                    },
                    "isWishlist": false,
                    "product_id": 524034580,
                    "product_name": "Baju daster no variant",
                    "product_price_fmt": "Rp50.000",
                    "product_price": 50000,
                    "parent_id": 0,
                    "category_id": 1792,
                    "category": "Fashion Wanita / Batik Wanita / Dress Batik Wanita",
                    "catalog_id": 0,
                    "wholesale_price": [],
                    "product_weight_fmt": "20gr",
                    "product_weight": 20,
                    "product_condition": 1,
                    "product_status": 1,
                    "product_url": "https://www.tokopedia.com/totokaku/baju-daster-no-variant",
                    "product_returnable": 0,
                    "is_freereturns": 0,
                    "is_preorder": 0,
                    "product_cashback": "",
                    "product_min_order": 1,
                    "product_max_order": 10000,
                    "product_rating": "0.000000",
                    "product_invenage_value": 999999,
                    "product_switch_invenage": 0,
                    "product_price_currency": 1,
                    "product_invenage_total": {
                      "is_counted_by_user": false,
                      "is_counted_by_product": false,
                      "by_user": {
                        "in_cart": 0,
                        "last_stock_less_than": 0
                      },
                      "by_user_text": {
                        "in_cart": "",
                        "last_stock_less_than": "",
                        "complete": ""
                      },
                      "by_product": {
                        "in_cart": 0,
                        "last_stock_less_than": 0
                      },
                      "by_product_text": {
                        "in_cart": "",
                        "last_stock_less_than": "",
                        "complete": ""
                      }
                    },
                    "price_changes": {
                      "changes_state": 0,
                      "amount_difference": 0,
                      "original_amount": 50000,
                      "description": "Harga Normal"
                    },
                    "product_image": {
                      "image_src": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/7/31/39467501/39467501_2646796b-2fed-414b-b806-17e90b8141f0_1000_1000",
                      "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/7/31/39467501/39467501_2646796b-2fed-414b-b806-17e90b8141f0_1000_1000",
                      "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/product-1/2019/7/31/39467501/39467501_2646796b-2fed-414b-b806-17e90b8141f0_1000_1000",
                      "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/product-1/2019/7/31/39467501/39467501_2646796b-2fed-414b-b806-17e90b8141f0_1000_1000"
                    },
                    "product_all_images": "[{\"FilePath\":\"product-1/2019/7/31/39467501\",\"FileName\":\"39467501_2646796b-2fed-414b-b806-17e90b8141f0_1000_1000\",\"Status\":2}]",
                    "product_notes": "",
                    "product_quantity": 1,
                    "product_weight_unit_code": 1,
                    "product_weight_unit_text": "gr",
                    "last_update_price": "1564566614",
                    "is_update_price": false,
                    "product_preorder": {
                      "duration_text": "",
                      "duration_day": "0",
                      "duration_unit_code": "0",
                      "duration_unit_text": "",
                      "duration_value": "0"
                    },
                    "product_showcase": {
                      "id": 14344831
                    },
                    "product_alias": "baju-daster-no-variant",
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
                    "warehouse_id": 2987341,
                    "is_parent": false,
                    "is_campaign_error": false
                  }
                }
              ]
            }
          ]
        }
      }
    }
""".trimIndent()

val apiResponseAllShopWithWholeSaleJson = """
    {
      "shop_group_simplified": {
        "status": "OK",
        "error_message": [],
        "data": {
          "errors": [],
          "is_coupon_active": 1,
          "is_one_tab_promo": false,
          "max_quantity": 10000,
          "max_char_note": 144,
          "messages": {
            "ErrorFieldBetween": "Jumlah harus diisi antara 1 - {{value}}",
            "ErrorFieldMaxChar": "Catatan terlalu panjang, maks. {{value}} karakter.",
            "ErrorFieldRequired": "Jumlah harus diisi",
            "ErrorProductAvailableStock": "Stok tersedia:{{value}}",
            "ErrorProductAvailableStockDetail": "Harap kurangi jumlah barang",
            "ErrorProductMaxQuantity": "Maks. pembelian barang ini {{value}} item, Kurangi pembelianmu, ya!",
            "ErrorProductMinQuantity": "Min. pembelian produk ini {{value}} barang. Yuk, atur ulang pembelianmu."
          },
          "donation": {
            "Title": "TopDonasi500",
            "Nominal": 500,
            "Description": "Hingga 24 Oktober 2019, donasi Toppers akan disalurkan melalui Yayasan Sekolah Relawan untuk mitigasi kebakaran hutan di daerah Sumatera dan Kalimantan serta membantu warga terdampak."
          },
          "autoapply_stack": {
            "global_success": true,
            "success": true,
            "message": {
              "state": "grey",
              "color": "#dfdfdf",
              "text": "Promo dr tokped"
            },
            "codes": ["123qwerty"],
            "promo_code_id": 0,
            "title_description": "ini promo dr tokped",
            "discount_amount": 0,
            "cashback_wallet_amount": 0,
            "cashback_advocate_referral_amount": 0,
            "cashback_voucher_description": "",
            "invoice_description": "",
            "gateway_id": "",
            "is_coupon": 0,
            "coupon_description": "",
            "voucher_orders": [],
            "benefit_summary_info": {
              "final_benefit_text": "",
              "final_benefit_amount": 0,
              "final_benefit_amount_str": "",
              "summaries": []
            },
            "clashing_info_detail": {
              "clash_message": "",
              "clash_reason": "",
              "is_clashed_promos": false,
              "options": []
            },
            "tracking_details": [],
            "ticker_info": {
              "unique_id": "",
              "status_code": 0,
              "message": ""
            },
            "benefit_details": []
          },
          "global_coupon_attr": {
            "description": "Gunakan promo Tokopedia",
            "quantity_label": "1 Kupon"
          },
          "tickers": [],
          "default_promo_dialog_tab": "voucher",
          "total_product_price": "104000",
          "total_product_count": 2,
          "total_product_error": 1,
          "global_checkbox_state": false,
          "hashed_email": "4c338847bdcb1321794cdbd2d28b3fa1",
          "shop_group_available": [
            {
              "errors": [],
              "user_address_id": 0,
              "cart_string": "956167-20-1164955",
              "total_cart_details_error": 0,
              "total_cart_price": "4000",
              "sort_key": 1151336322,
              "is_fulfillment_service": false,
              "has_promo_list": false,
              "shop": {
                "shop_id": 956167,
                "user_id": 7977957,
                "shop_name": "Toko JK Seller",
                "shop_image": "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2018/8/21/956167/956167_95bccdd1-8b5b-4efd-bf89-e212b74144d2.jpg",
                "shop_url": "https://www.tokopedia.com/jkseller",
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
                "official_store": {
                  "is_official": 0,
                  "os_logo_url": ""
                },
                "address_id": 2254,
                "postal_code": "11450",
                "latitude": "-6.1608776337432225",
                "longitude": "106.79237956190184",
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
                "shop_domain": "jkseller"
              },
              "warehouse": {
                "warehouse_id": 1164955,
                "partner_id": 0,
                "shop_id": 956167,
                "warehouse_name": "Shop Location",
                "district_id": 2254,
                "district_name": "Grogol",
                "city_id": 174,
                "city_name": "Jakarta Barat",
                "province_id": 13,
                "province_name": "DKI Jakarta",
                "status": 1,
                "postal_code": "11450",
                "is_default": 1,
                "latlon": "-6.1608776337432225,106.79237956190184",
                "latitude": "-6.1608776337432225",
                "longitude": "106.79237956190184",
                "email": "",
                "address_detail": "Jalan Dokter Makaliwe Gang 2, Grogol petamburan, 11450",
                "country_name": "Indonesia",
                "is_fulfillment": false
              },
              "checkbox_state": true,
              "cart_details": [
                {
                  "errors": [],
                  "cart_id": "1151336322",
                  "checkbox_state": true,
                  "similar_product_url": "",
                  "similar_product": {
                    "text": "",
                    "url": ""
                  },
                  "messages": [],
                  "product": {
                    "product_tracker_data": {
                      "attribution": "none/other",
                      "tracker_list_name": "none/other"
                    },
                    "isWishlist": false,
                    "product_id": 48146508,
                    "product_name": "kucing2",
                    "product_price_fmt": "Rp4.000",
                    "product_price": 4000,
                    "parent_id": 0,
                    "category_id": 2266,
                    "category": "Kecantikan / Perawatan Wajah / Cleanser Wajah",
                    "catalog_id": 0,
                    "wholesale_price": [
                      {
                        "qty_min_fmt": "",
                        "qty_max_fmt": "",
                        "qty_min": 20,
                        "qty_max": 10000,
                        "prd_prc": 3000,
                        "prd_prc_fmt": "Rp3.000"
                      }
                    ],
                    "product_weight_fmt": "150gr",
                    "product_weight": 150,
                    "product_condition": 1,
                    "product_status": 1,
                    "product_url": "https://www.tokopedia.com/jkseller/kucing2",
                    "product_returnable": 0,
                    "is_freereturns": 0,
                    "is_preorder": 1,
                    "product_cashback": "",
                    "product_min_order": 1,
                    "product_max_order": 10000,
                    "product_rating": "0.000000",
                    "product_invenage_value": 999999,
                    "product_switch_invenage": 0,
                    "product_price_currency": 1,
                    "product_invenage_total": {
                      "is_counted_by_user": false,
                      "is_counted_by_product": false,
                      "by_user": {
                        "in_cart": 0,
                        "last_stock_less_than": 0
                      },
                      "by_user_text": {
                        "in_cart": "",
                        "last_stock_less_than": "",
                        "complete": ""
                      },
                      "by_product": {
                        "in_cart": 0,
                        "last_stock_less_than": 0
                      },
                      "by_product_text": {
                        "in_cart": "",
                        "last_stock_less_than": "",
                        "complete": ""
                      }
                    },
                    "price_changes": {
                      "changes_state": 0,
                      "amount_difference": 0,
                      "original_amount": 4000,
                      "description": "Harga Normal"
                    },
                    "product_image": {
                      "image_src": "https://ecs7.tokopedia.net/img/cache/700/product-1/2016/6/7/956167/956167_c6e7b764-6743-415f-8978-2ac88a74fa25.jpg",
                      "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2016/6/7/956167/956167_c6e7b764-6743-415f-8978-2ac88a74fa25.jpg",
                      "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/product-1/2016/6/7/956167/956167_c6e7b764-6743-415f-8978-2ac88a74fa25.jpg",
                      "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/product-1/2016/6/7/956167/956167_c6e7b764-6743-415f-8978-2ac88a74fa25.jpg"
                    },
                    "product_all_images": "[{\"FilePath\":\"product-1/2016/6/7/956167\",\"FileName\":\"956167_c6e7b764-6743-415f-8978-2ac88a74fa25.jpg\",\"Status\":2}]",
                    "product_notes": "",
                    "product_quantity": 1,
                    "product_weight_unit_code": 1,
                    "product_weight_unit_text": "gr",
                    "last_update_price": "1465811841",
                    "is_update_price": false,
                    "product_preorder": {
                      "duration_text": "20 Hari",
                      "duration_day": "20",
                      "duration_unit_code": "1",
                      "duration_unit_text": "Day",
                      "duration_value": "20"
                    },
                    "product_showcase": {
                      "id": 3225335
                    },
                    "product_alias": "kucing2",
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
                    "warehouse_id": 1164955,
                    "is_parent": false,
                    "is_campaign_error": false
                  }
                }
              ]
            },
            {
              "errors": [],
              "user_address_id": 0,
              "cart_string": "956167-0-1164955",
              "total_cart_details_error": 0,
              "total_cart_price": "100000",
              "sort_key": 1151321358,
              "is_fulfillment_service": false,
              "has_promo_list": false,
              "shop": {
                "shop_id": 956167,
                "user_id": 7977957,
                "shop_name": "Toko JK Seller",
                "shop_image": "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2018/8/21/956167/956167_95bccdd1-8b5b-4efd-bf89-e212b74144d2.jpg",
                "shop_url": "https://www.tokopedia.com/jkseller",
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
                "official_store": {
                  "is_official": 0,
                  "os_logo_url": ""
                },
                "address_id": 2254,
                "postal_code": "11450",
                "latitude": "-6.1608776337432225",
                "longitude": "106.79237956190184",
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
                "shop_domain": "jkseller"
              },
              "warehouse": {
                "warehouse_id": 1164955,
                "partner_id": 0,
                "shop_id": 956167,
                "warehouse_name": "Shop Location",
                "district_id": 2254,
                "district_name": "Grogol",
                "city_id": 174,
                "city_name": "Jakarta Barat",
                "province_id": 13,
                "province_name": "DKI Jakarta",
                "status": 1,
                "postal_code": "11450",
                "is_default": 1,
                "latlon": "-6.1608776337432225,106.79237956190184",
                "latitude": "-6.1608776337432225",
                "longitude": "106.79237956190184",
                "email": "",
                "address_detail": "Jalan Dokter Makaliwe Gang 2, Grogol petamburan, 11450",
                "country_name": "Indonesia",
                "is_fulfillment": false
              },
              "checkbox_state": true,
              "cart_details": [
                {
                  "errors": [],
                  "cart_id": "1151321358",
                  "checkbox_state": true,
                  "similar_product_url": "",
                  "similar_product": {
                    "text": "",
                    "url": ""
                  },
                  "messages": [],
                  "product": {
                    "product_tracker_data": {
                      "attribution": "none/other",
                      "tracker_list_name": "none/other"
                    },
                    "isWishlist": false,
                    "product_id": 528845437,
                    "product_name": "Sepeda Biru",
                    "product_price_fmt": "Rp100.000",
                    "product_price": 100000,
                    "parent_id": 0,
                    "category_id": 1405,
                    "category": "",
                    "catalog_id": 0,
                    "wholesale_price": [],
                    "product_weight_fmt": "1kg",
                    "product_weight": 1,
                    "product_condition": 1,
                    "product_status": 1,
                    "product_url": "https://www.tokopedia.com/jkseller/sepeda-biru",
                    "product_returnable": 0,
                    "is_freereturns": 0,
                    "is_preorder": 0,
                    "product_cashback": "",
                    "product_min_order": 1,
                    "product_max_order": 10000,
                    "product_rating": "0.000000",
                    "product_invenage_value": 999992,
                    "product_switch_invenage": 0,
                    "product_price_currency": 1,
                    "product_invenage_total": {
                      "is_counted_by_user": false,
                      "is_counted_by_product": false,
                      "by_user": {
                        "in_cart": 0,
                        "last_stock_less_than": 0
                      },
                      "by_user_text": {
                        "in_cart": "",
                        "last_stock_less_than": "",
                        "complete": ""
                      },
                      "by_product": {
                        "in_cart": 0,
                        "last_stock_less_than": 0
                      },
                      "by_product_text": {
                        "in_cart": "",
                        "last_stock_less_than": "",
                        "complete": ""
                      }
                    },
                    "price_changes": {
                      "changes_state": 0,
                      "amount_difference": 0,
                      "original_amount": 100000,
                      "description": "Harga Normal"
                    },
                    "product_image": {
                      "image_src": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/8/8/7977957/7977957_8133fef3-e573-48d3-a6fd-34977db187e4_602_602",
                      "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/8/8/7977957/7977957_8133fef3-e573-48d3-a6fd-34977db187e4_602_602",
                      "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/product-1/2019/8/8/7977957/7977957_8133fef3-e573-48d3-a6fd-34977db187e4_602_602",
                      "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/product-1/2019/8/8/7977957/7977957_8133fef3-e573-48d3-a6fd-34977db187e4_602_602"
                    },
                    "product_all_images": "[{\"FilePath\":\"product-1/2019/8/8/7977957\",\"FileName\":\"7977957_8133fef3-e573-48d3-a6fd-34977db187e4_602_602\",\"Status\":2}]",
                    "product_notes": "testing 123",
                    "product_quantity": 1,
                    "product_weight_unit_code": 2,
                    "product_weight_unit_text": "kg",
                    "last_update_price": "1570437322",
                    "is_update_price": false,
                    "product_preorder": {
                      "duration_text": "",
                      "duration_day": "0",
                      "duration_unit_code": "0",
                      "duration_unit_text": "",
                      "duration_value": "0"
                    },
                    "product_showcase": {
                      "id": 2887291
                    },
                    "product_alias": "sepeda-biru",
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
                    "warehouse_id": 1164955,
                    "is_parent": false,
                    "is_campaign_error": false
                  }
                }
              ]
            }
          ],
          "shop_group_with_errors": [
            {
              "errors": [
                "Bukan Barang Testing",
                "Bukan Barang Testing"
              ],
              "user_address_id": 0,
              "cart_string": "3987128-0-2987341",
              "total_cart_details_error": 1,
              "total_cart_price": "50000",
              "sort_key": 1151266921,
              "is_fulfillment_service": false,
              "has_promo_list": false,
              "shop": {
                "shop_id": 3987128,
                "user_id": 39467501,
                "shop_name": "Meraih mimpi",
                "shop_image": "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2018/9/4/39467501/39467501_0b1e17e3-3302-4b04-8718-5c77dd813ddb.png",
                "shop_url": "https://www.tokopedia.com/totokaku",
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
                "official_store": {
                  "is_official": 0,
                  "os_logo_url": ""
                },
                "address_id": 2209,
                "postal_code": "17115",
                "latitude": "-6.275157600000001",
                "longitude": "107.00107939999998",
                "district_id": 2209,
                "district_name": "",
                "origin": 0,
                "address_street": "",
                "province_id": 0,
                "city_id": 167,
                "city_name": "Kota Bekasi",
                "province_name": "",
                "country_name": "",
                "is_allow_manage": false,
                "shop_domain": "totokaku"
              },
              "warehouse": {
                "warehouse_id": 2987341,
                "partner_id": 0,
                "shop_id": 3987128,
                "warehouse_name": "Shop location",
                "district_id": 2209,
                "district_name": "Rawalumbu",
                "city_id": 167,
                "city_name": "Kota Bekasi",
                "province_id": 12,
                "province_name": "Jawa Barat",
                "status": 1,
                "postal_code": "17115",
                "is_default": 1,
                "latlon": "-6.275157600000001,107.00107939999998",
                "latitude": "-6.275157600000001",
                "longitude": "107.00107939999998",
                "email": "",
                "address_detail": "Penegak J No 95 rawalumbu bekasi",
                "country_name": "Indonesia",
                "is_fulfillment": false
              },
              "checkbox_state": true,
              "cart_details": [
                {
                  "errors": [],
                  "cart_id": "1151266921",
                  "checkbox_state": true,
                  "similar_product_url": "",
                  "similar_product": {
                    "text": "",
                    "url": ""
                  },
                  "messages": [],
                  "product": {
                    "product_tracker_data": {
                      "attribution": "none/other",
                      "tracker_list_name": "none/other"
                    },
                    "isWishlist": false,
                    "product_id": 524034580,
                    "product_name": "Baju daster no variant",
                    "product_price_fmt": "Rp50.000",
                    "product_price": 50000,
                    "parent_id": 0,
                    "category_id": 1792,
                    "category": "Fashion Wanita / Batik Wanita / Dress Batik Wanita",
                    "catalog_id": 0,
                    "wholesale_price": [],
                    "product_weight_fmt": "20gr",
                    "product_weight": 20,
                    "product_condition": 1,
                    "product_status": 1,
                    "product_url": "https://www.tokopedia.com/totokaku/baju-daster-no-variant",
                    "product_returnable": 0,
                    "is_freereturns": 0,
                    "is_preorder": 0,
                    "product_cashback": "",
                    "product_min_order": 1,
                    "product_max_order": 10000,
                    "product_rating": "0.000000",
                    "product_invenage_value": 999999,
                    "product_switch_invenage": 0,
                    "product_price_currency": 1,
                    "product_invenage_total": {
                      "is_counted_by_user": false,
                      "is_counted_by_product": false,
                      "by_user": {
                        "in_cart": 0,
                        "last_stock_less_than": 0
                      },
                      "by_user_text": {
                        "in_cart": "",
                        "last_stock_less_than": "",
                        "complete": ""
                      },
                      "by_product": {
                        "in_cart": 0,
                        "last_stock_less_than": 0
                      },
                      "by_product_text": {
                        "in_cart": "",
                        "last_stock_less_than": "",
                        "complete": ""
                      }
                    },
                    "price_changes": {
                      "changes_state": 0,
                      "amount_difference": 0,
                      "original_amount": 50000,
                      "description": "Harga Normal"
                    },
                    "product_image": {
                      "image_src": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/7/31/39467501/39467501_2646796b-2fed-414b-b806-17e90b8141f0_1000_1000",
                      "image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/7/31/39467501/39467501_2646796b-2fed-414b-b806-17e90b8141f0_1000_1000",
                      "image_src_300": "https://ecs7.tokopedia.net/img/cache/300/product-1/2019/7/31/39467501/39467501_2646796b-2fed-414b-b806-17e90b8141f0_1000_1000",
                      "image_src_square": "https://ecs7.tokopedia.net/img/cache/500-square/product-1/2019/7/31/39467501/39467501_2646796b-2fed-414b-b806-17e90b8141f0_1000_1000"
                    },
                    "product_all_images": "[{\"FilePath\":\"product-1/2019/7/31/39467501\",\"FileName\":\"39467501_2646796b-2fed-414b-b806-17e90b8141f0_1000_1000\",\"Status\":2}]",
                    "product_notes": "",
                    "product_quantity": 1,
                    "product_weight_unit_code": 1,
                    "product_weight_unit_text": "gr",
                    "last_update_price": "1564566614",
                    "is_update_price": false,
                    "product_preorder": {
                      "duration_text": "",
                      "duration_day": "0",
                      "duration_unit_code": "0",
                      "duration_unit_text": "",
                      "duration_value": "0"
                    },
                    "product_showcase": {
                      "id": 14344831
                    },
                    "product_alias": "baju-daster-no-variant",
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
                    "warehouse_id": 2987341,
                    "is_parent": false,
                    "is_campaign_error": false
                  }
                }
              ]
            }
          ]
        }
      }
    }
""".trimIndent()