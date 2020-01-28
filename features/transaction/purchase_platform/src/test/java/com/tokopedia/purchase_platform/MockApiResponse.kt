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

val apiResponseSAFDisableFeatureDropshipper = """{
    "errors": [],
    "error_code": 0,
    "is_multiple": 0,
    "is_coupon_active": 1,
    "is_one_tab_promo": false,
    "group_address": [
      {
        "errors": [],
        "user_address": {
          "address_id": 99367774,
          "address_name": "Kantor",
          "address": "jl prof dr satrio",
          "postal_code": "11720",
          "phone": "628755464540",
          "receiver_name": "Tester",
          "status": 2,
          "country": "Indonesia",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "city_id": 174,
          "city_name": "Jakarta Barat",
          "district_id": 2253,
          "district_name": "Cengkareng",
          "address_2": "-6.148635,106.735185",
          "latitude": "-6.148635",
          "longitude": "106.735185",
          "corner_id": 0,
          "is_corner": false,
          "is_primary": true,
          "buyer_store_code": "",
          "type": 1
        },
        "group_shop": [
          {
            "errors": [],
            "group_key": "",
            "shop": {
              "shop_id": 1479278,
              "user_id": 12299749,
              "admin_ids": [],
              "shop_name": "Tumbler Starbucks 123",
              "shop_image": "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2018/10/19/1479278/1479278_fa306e59-f15f-4048-b2f7-8ddc5d798926.jpeg",
              "shop_url": "https://www.tokopedia.com/tumblersbux",
              "shop_status": 1,
              "is_gold": 1,
              "is_gold_badge": true,
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
              "address_id": 2267,
              "postal_code": "12750",
              "latitude": "-6.2565354",
              "longitude": "106.85040979999997",
              "district_id": 2267,
              "district_name": "",
              "origin": 0,
              "address_street": "",
              "province_id": 0,
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_name": "",
              "country_name": "",
              "is_allow_manage": false,
              "shop_domain": "",
              "is_bridestory": false
            },
            "cart_string": "1479278-30-740525-99367774",
            "shipping_id": 23,
            "sp_id": 45,
            "rates_id": 0,
            "dropshipper": {
              "name": "",
              "telp_no": ""
            },
            "is_insurance": false,
            "is_cod_available": false,
            "is_order_priority": false,
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 2,
                    "ship_prod_name": "OKE",
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 6,
                    "ship_prod_name": "YES",
                    "ship_group_name": "nextday",
                    "ship_group_id": 1003,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
              },
              {
                "ship_id": 10,
                "ship_name": "GO-JEK",
                "ship_code": "gojek",
                "ship_logo": "https://ecs7.tokopedia.net/img/kurir-gosend.png",
                "ship_prods": [
                  {
                    "ship_prod_id": 28,
                    "ship_prod_name": "Instant Courier",
                    "ship_group_name": "instant",
                    "ship_group_id": 1000,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 20,
                    "ship_prod_name": "Same Day",
                    "ship_group_name": "sameday",
                    "ship_group_id": 1002,
                    "additional_fee": 0,
                    "minimum_weight": 0
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 0
              },
              {
                "ship_id": 13,
                "ship_name": "Grab",
                "ship_code": "grab",
                "ship_logo": "https://ecs7.tokopedia.net/img/kurir-grab.png",
                "ship_prods": [
                  {
                    "ship_prod_id": 37,
                    "ship_prod_name": "Instant",
                    "ship_group_name": "instant",
                    "ship_group_id": 1000,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 24,
                    "ship_prod_name": "Same Day",
                    "ship_group_name": "sameday",
                    "ship_group_id": 1002,
                    "additional_fee": 0,
                    "minimum_weight": 0
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 46,
                    "ship_prod_name": "Next Day",
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
              }
            ],
            "products": [
              {
                "errors": [],
                "cart_id": 1256701880,
                "product_id": 516791889,
                "product_alias": "",
                "parent_id": 0,
                "sku": "",
                "campaign_id": 0,
                "is_big_campaign": false,
                "product_name": "Halo Ini Nama Produk Yang Harus Panjang Banget Supaya Penuh Aja HEHEHE",
                "product_description": "Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi ",
                "product_price_fmt": "Rp12.300",
                "trade_in_info": {
                  "is_valid_trade_in": false,
                  "new_device_price": 0,
                  "new_device_price_fmt": "",
                  "old_device_price": 0,
                  "old_device_price_fmt": "",
                  "drop_off_enable": false
                },
                "product_price": 12300,
                "product_original_price": 0,
                "product_price_original_fmt": "",
                "is_slash_price": false,
                "wholesale_price": [],
                "product_wholesale_price": 12300,
                "product_wholesale_price_fmt": "Rp12.300",
                "product_weight_fmt": "112gr",
                "product_weight": 112,
                "product_condition": 1,
                "product_url": "https://www.tokopedia.com/tumblersbux/halo-ini-nama-produk-yang-harus-panjang-banget-supaya-penuh-aja-hehehe",
                "product_returnable": 0,
                "product_is_free_returns": 0,
                "free_returns": {
                  "is_freereturns": 0,
                  "free_returns_logo": ""
                },
                "product_is_preorder": 1,
                "product_preorder": {
                  "duration_text": "30 Hari",
                  "duration_day": 30,
                  "duration_unit_code": 1,
                  "duration_unit_text": "Day",
                  "duration_value": 30
                },
                "product_cashback": "",
                "product_min_order": 1,
                "product_invenage_value": 9979,
                "product_switch_invenage": 1,
                "currency_rate": 1,
                "product_price_currency": 1,
                "product_image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/7/23/12299749/12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700",
                "product_all_images": "[{\"file_name\":\"12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700\",\"file_path\":\"product-1/2019/7/23/12299749\",\"status\":2}]",
                "product_notes": "",
                "product_quantity": 2,
                "product_menu_id": 5333641,
                "product_finsurance": 0,
                "product_fcancel_partial": 0,
                "product_shipment": [],
                "product_shipment_mapping": [],
                "product_cat_id": 753,
                "product_catalog_id": 0,
                "product_status": 1,
                "product_tracker_data": {
                  "attribution": "none/other",
                  "tracker_list_name": "none/other"
                },
                "product_category": "Buku / Novel \u0026 Sastra / Novel Terjemahan",
                "product_total_weight": 224,
                "product_total_weight_fmt": "224 gr",
                "purchase_protection_plan_data": {
                  "protection_available": false,
                  "protection_type_id": 0,
                  "protection_price_per_product": 0,
                  "protection_price": 0,
                  "protection_title": "",
                  "protection_subtitle": "",
                  "protection_link_text": "",
                  "protection_link_url": "",
                  "protection_opt_in": false,
                  "protection_checkbox_disabled": false
                },
                "product_variants": {
                  "parent_id": 0,
                  "default_child": 0,
                  "variant": [],
                  "children": [],
                  "is_enabled": false,
                  "stock": 0
                },
                "warehouse_id": 740525,
                "cashback_percentage": 0,
                "cashback_amount": 0,
                "is_blacklisted": false,
                "free_shipping": {
                  "eligible": false,
                  "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                }
              }
            ],
            "is_fulfillment_service": false,
            "warehouse": {
              "warehouse_id": 740525,
              "partner_id": 0,
              "shop_id": 1479278,
              "warehouse_name": "Shop Location",
              "district_id": 2267,
              "district_name": "Pancoran",
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_id": 13,
              "province_name": "DKI Jakarta",
              "status": 1,
              "postal_code": "12750",
              "is_default": 1,
              "latlon": "-6.2565354,106.85040979999997",
              "latitude": "-6.2565354",
              "longitude": "106.85040979999997",
              "email": "",
              "address_detail": "Taman Pahlawan, Pancoran, 12750",
              "country_name": "Indonesia",
              "is_fulfillment": false,
              "tkpd_preferred_logistic_spid": []
            },
            "has_promo_list": false,
            "save_state_flag": false,
            "vehicle_leasing": {
              "application_id": 0,
              "dp_price": 0,
              "booking_fee": 0,
              "total_price": 0,
              "product_id": 0,
              "dealer_id": 0,
              "multifinance_name": "",
              "is_leasing_product": false,
              "is_allow_checkout": false,
              "error_message": "",
              "original_price": 0
            },
            "rates_feature": {
              "ontime_delivery_guarantee": {
                "available": false,
                "duration": 0,
                "bom": {
                  "title": "",
                  "notes": "",
                  "url_detail": "",
                  "url_text": ""
                }
              }
            }
          }
        ]
      }
    ],
    "kero_token": "Tokopedia+Kero:pBd7GsNEfng8RYtFmJQxv0Oz+sM\u003d",
    "kero_discom_token": "Tokopedia+Kero:cUFp/U20S2+eXDhJvcr7MOCMafE\u003d",
    "kero_unix_time": 1580188919,
    "enable_partial_cancel": false,
    "donation": {
      "Title": "Donasi untuk Korban Banjir Jabodetabek",
      "Nominal": 2000,
      "Description": "Hingga 31 Januari 2020, donasi dari Toppers akan disalurkan oleh Rumah Zakat untuk membantu korban banjir Jabodetabek."
    },
    "is_one_click_shipment": 0,
    "is_robinhood": 1,
    "is_blackbox": 0,
    "promo_suggestion": {
      "cta": "",
      "cta_color": "",
      "is_visible": 0,
      "promo_code": "",
      "text": ""
    },
    "autoapply": {
      "success": false,
      "code": "",
      "is_coupon": 0,
      "discount_amount": 0,
      "title_description": "",
      "message_success": "",
      "promo_id": 0
    },
    "autoapply_v2": {
      "success": false,
      "message": {
        "state": "",
        "color": "",
        "text": ""
      },
      "code": "",
      "promo_code_id": "",
      "title_description": "",
      "is_coupon": 0,
      "discount_amount": "",
      "discount_amount_fmt": 0
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
      "eligible": false,
      "is_tiering": false,
      "range": {
        "min": 0,
        "max": 0
      },
      "message": {
        "title_text": "",
        "sub_text": "",
        "ticker_text": "",
        "tooltip_text": ""
      }
    },
    "autoapply_stack": {
      "global_success": false,
      "success": false,
      "message": {
        "state": "",
        "color": "",
        "text": ""
      },
      "promo_code_id": 0,
      "title_description": "",
      "discount_amount": 0,
      "cashback_amount": 0,
      "cashback_wallet_amount": 0,
      "cashback_advocate_referral_amount": 0,
      "cashback_voucher_description": "",
      "invoice_description": "",
      "gateway_id": "",
      "is_tokopedia_gerai": false,
      "is_coupon": 0,
      "coupon_description": "",
      "benefit_summary_info": {
        "final_benefit_text": "",
        "final_benefit_amount_str": "",
        "final_benefit_amount": 0
      },
      "clashing_info_detail": {
        "clash_message": "",
        "clash_reason": "",
        "is_clashed_promos": false,
        "options": []
      },
      "tracking_details": [],
      "benefit_details": [],
      "ticker_info": {
        "unique_id": "",
        "status_code": 0,
        "message": ""
      }
    },
    "global_coupon_attr": {
      "description": "Gunakan promo Tokopedia",
      "quantity_label": "3 Kupon"
    },
    "is_show_onboarding": false,
    "is_hide_courier_name": false,
    "is_ineligbile_promo_dialog_enabled": true,
    "is_macro_micro_insurance_enabled": false,
    "donation_checkbox_status": false,
    "tickers": [],
    "hashed_email": "4c338847bdcb1321794cdbd2d28b3fa1",
    "is_new_buyer": true,
    "disabled_features": [
        "dropshipper"
    ],
    "addresses": {
      "active": "",
      "data": {}
    },
    "disabled_features_detail": {
      "disabled_multi_address_message": ""
    }
  }""".trimIndent()

val apiResponseSAFDisableFeatureMultipleAddress = """{
    "errors": [],
    "error_code": 0,
    "is_multiple": 0,
    "is_coupon_active": 1,
    "is_one_tab_promo": false,
    "group_address": [
      {
        "errors": [],
        "user_address": {
          "address_id": 99367774,
          "address_name": "Kantor",
          "address": "jl prof dr satrio",
          "postal_code": "11720",
          "phone": "628755464540",
          "receiver_name": "Tester",
          "status": 2,
          "country": "Indonesia",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "city_id": 174,
          "city_name": "Jakarta Barat",
          "district_id": 2253,
          "district_name": "Cengkareng",
          "address_2": "-6.148635,106.735185",
          "latitude": "-6.148635",
          "longitude": "106.735185",
          "corner_id": 0,
          "is_corner": false,
          "is_primary": true,
          "buyer_store_code": "",
          "type": 1
        },
        "group_shop": [
          {
            "errors": [],
            "group_key": "",
            "shop": {
              "shop_id": 1479278,
              "user_id": 12299749,
              "admin_ids": [],
              "shop_name": "Tumbler Starbucks 123",
              "shop_image": "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2018/10/19/1479278/1479278_fa306e59-f15f-4048-b2f7-8ddc5d798926.jpeg",
              "shop_url": "https://www.tokopedia.com/tumblersbux",
              "shop_status": 1,
              "is_gold": 1,
              "is_gold_badge": true,
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
              "address_id": 2267,
              "postal_code": "12750",
              "latitude": "-6.2565354",
              "longitude": "106.85040979999997",
              "district_id": 2267,
              "district_name": "",
              "origin": 0,
              "address_street": "",
              "province_id": 0,
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_name": "",
              "country_name": "",
              "is_allow_manage": false,
              "shop_domain": "",
              "is_bridestory": false
            },
            "cart_string": "1479278-30-740525-99367774",
            "shipping_id": 23,
            "sp_id": 45,
            "rates_id": 0,
            "dropshipper": {
              "name": "",
              "telp_no": ""
            },
            "is_insurance": false,
            "is_cod_available": false,
            "is_order_priority": false,
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 2,
                    "ship_prod_name": "OKE",
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 6,
                    "ship_prod_name": "YES",
                    "ship_group_name": "nextday",
                    "ship_group_id": 1003,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
              },
              {
                "ship_id": 10,
                "ship_name": "GO-JEK",
                "ship_code": "gojek",
                "ship_logo": "https://ecs7.tokopedia.net/img/kurir-gosend.png",
                "ship_prods": [
                  {
                    "ship_prod_id": 28,
                    "ship_prod_name": "Instant Courier",
                    "ship_group_name": "instant",
                    "ship_group_id": 1000,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 20,
                    "ship_prod_name": "Same Day",
                    "ship_group_name": "sameday",
                    "ship_group_id": 1002,
                    "additional_fee": 0,
                    "minimum_weight": 0
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 0
              },
              {
                "ship_id": 13,
                "ship_name": "Grab",
                "ship_code": "grab",
                "ship_logo": "https://ecs7.tokopedia.net/img/kurir-grab.png",
                "ship_prods": [
                  {
                    "ship_prod_id": 37,
                    "ship_prod_name": "Instant",
                    "ship_group_name": "instant",
                    "ship_group_id": 1000,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 24,
                    "ship_prod_name": "Same Day",
                    "ship_group_name": "sameday",
                    "ship_group_id": 1002,
                    "additional_fee": 0,
                    "minimum_weight": 0
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 46,
                    "ship_prod_name": "Next Day",
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
              }
            ],
            "products": [
              {
                "errors": [],
                "cart_id": 1256701880,
                "product_id": 516791889,
                "product_alias": "",
                "parent_id": 0,
                "sku": "",
                "campaign_id": 0,
                "is_big_campaign": false,
                "product_name": "Halo Ini Nama Produk Yang Harus Panjang Banget Supaya Penuh Aja HEHEHE",
                "product_description": "Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi ",
                "product_price_fmt": "Rp12.300",
                "trade_in_info": {
                  "is_valid_trade_in": false,
                  "new_device_price": 0,
                  "new_device_price_fmt": "",
                  "old_device_price": 0,
                  "old_device_price_fmt": "",
                  "drop_off_enable": false
                },
                "product_price": 12300,
                "product_original_price": 0,
                "product_price_original_fmt": "",
                "is_slash_price": false,
                "wholesale_price": [],
                "product_wholesale_price": 12300,
                "product_wholesale_price_fmt": "Rp12.300",
                "product_weight_fmt": "112gr",
                "product_weight": 112,
                "product_condition": 1,
                "product_url": "https://www.tokopedia.com/tumblersbux/halo-ini-nama-produk-yang-harus-panjang-banget-supaya-penuh-aja-hehehe",
                "product_returnable": 0,
                "product_is_free_returns": 0,
                "free_returns": {
                  "is_freereturns": 0,
                  "free_returns_logo": ""
                },
                "product_is_preorder": 1,
                "product_preorder": {
                  "duration_text": "30 Hari",
                  "duration_day": 30,
                  "duration_unit_code": 1,
                  "duration_unit_text": "Day",
                  "duration_value": 30
                },
                "product_cashback": "",
                "product_min_order": 1,
                "product_invenage_value": 9979,
                "product_switch_invenage": 1,
                "currency_rate": 1,
                "product_price_currency": 1,
                "product_image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/7/23/12299749/12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700",
                "product_all_images": "[{\"file_name\":\"12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700\",\"file_path\":\"product-1/2019/7/23/12299749\",\"status\":2}]",
                "product_notes": "",
                "product_quantity": 2,
                "product_menu_id": 5333641,
                "product_finsurance": 0,
                "product_fcancel_partial": 0,
                "product_shipment": [],
                "product_shipment_mapping": [],
                "product_cat_id": 753,
                "product_catalog_id": 0,
                "product_status": 1,
                "product_tracker_data": {
                  "attribution": "none/other",
                  "tracker_list_name": "none/other"
                },
                "product_category": "Buku / Novel \u0026 Sastra / Novel Terjemahan",
                "product_total_weight": 224,
                "product_total_weight_fmt": "224 gr",
                "purchase_protection_plan_data": {
                  "protection_available": false,
                  "protection_type_id": 0,
                  "protection_price_per_product": 0,
                  "protection_price": 0,
                  "protection_title": "",
                  "protection_subtitle": "",
                  "protection_link_text": "",
                  "protection_link_url": "",
                  "protection_opt_in": false,
                  "protection_checkbox_disabled": false
                },
                "product_variants": {
                  "parent_id": 0,
                  "default_child": 0,
                  "variant": [],
                  "children": [],
                  "is_enabled": false,
                  "stock": 0
                },
                "warehouse_id": 740525,
                "cashback_percentage": 0,
                "cashback_amount": 0,
                "is_blacklisted": false,
                "free_shipping": {
                  "eligible": false,
                  "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                }
              }
            ],
            "is_fulfillment_service": false,
            "warehouse": {
              "warehouse_id": 740525,
              "partner_id": 0,
              "shop_id": 1479278,
              "warehouse_name": "Shop Location",
              "district_id": 2267,
              "district_name": "Pancoran",
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_id": 13,
              "province_name": "DKI Jakarta",
              "status": 1,
              "postal_code": "12750",
              "is_default": 1,
              "latlon": "-6.2565354,106.85040979999997",
              "latitude": "-6.2565354",
              "longitude": "106.85040979999997",
              "email": "",
              "address_detail": "Taman Pahlawan, Pancoran, 12750",
              "country_name": "Indonesia",
              "is_fulfillment": false,
              "tkpd_preferred_logistic_spid": []
            },
            "has_promo_list": false,
            "save_state_flag": false,
            "vehicle_leasing": {
              "application_id": 0,
              "dp_price": 0,
              "booking_fee": 0,
              "total_price": 0,
              "product_id": 0,
              "dealer_id": 0,
              "multifinance_name": "",
              "is_leasing_product": false,
              "is_allow_checkout": false,
              "error_message": "",
              "original_price": 0
            },
            "rates_feature": {
              "ontime_delivery_guarantee": {
                "available": false,
                "duration": 0,
                "bom": {
                  "title": "",
                  "notes": "",
                  "url_detail": "",
                  "url_text": ""
                }
              }
            }
          }
        ]
      }
    ],
    "kero_token": "Tokopedia+Kero:pBd7GsNEfng8RYtFmJQxv0Oz+sM\u003d",
    "kero_discom_token": "Tokopedia+Kero:cUFp/U20S2+eXDhJvcr7MOCMafE\u003d",
    "kero_unix_time": 1580188919,
    "enable_partial_cancel": false,
    "donation": {
      "Title": "Donasi untuk Korban Banjir Jabodetabek",
      "Nominal": 2000,
      "Description": "Hingga 31 Januari 2020, donasi dari Toppers akan disalurkan oleh Rumah Zakat untuk membantu korban banjir Jabodetabek."
    },
    "is_one_click_shipment": 0,
    "is_robinhood": 1,
    "is_blackbox": 0,
    "promo_suggestion": {
      "cta": "",
      "cta_color": "",
      "is_visible": 0,
      "promo_code": "",
      "text": ""
    },
    "autoapply": {
      "success": false,
      "code": "",
      "is_coupon": 0,
      "discount_amount": 0,
      "title_description": "",
      "message_success": "",
      "promo_id": 0
    },
    "autoapply_v2": {
      "success": false,
      "message": {
        "state": "",
        "color": "",
        "text": ""
      },
      "code": "",
      "promo_code_id": "",
      "title_description": "",
      "is_coupon": 0,
      "discount_amount": "",
      "discount_amount_fmt": 0
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
      "eligible": false,
      "is_tiering": false,
      "range": {
        "min": 0,
        "max": 0
      },
      "message": {
        "title_text": "",
        "sub_text": "",
        "ticker_text": "",
        "tooltip_text": ""
      }
    },
    "autoapply_stack": {
      "global_success": false,
      "success": false,
      "message": {
        "state": "",
        "color": "",
        "text": ""
      },
      "promo_code_id": 0,
      "title_description": "",
      "discount_amount": 0,
      "cashback_amount": 0,
      "cashback_wallet_amount": 0,
      "cashback_advocate_referral_amount": 0,
      "cashback_voucher_description": "",
      "invoice_description": "",
      "gateway_id": "",
      "is_tokopedia_gerai": false,
      "is_coupon": 0,
      "coupon_description": "",
      "benefit_summary_info": {
        "final_benefit_text": "",
        "final_benefit_amount_str": "",
        "final_benefit_amount": 0
      },
      "clashing_info_detail": {
        "clash_message": "",
        "clash_reason": "",
        "is_clashed_promos": false,
        "options": []
      },
      "tracking_details": [],
      "benefit_details": [],
      "ticker_info": {
        "unique_id": "",
        "status_code": 0,
        "message": ""
      }
    },
    "global_coupon_attr": {
      "description": "Gunakan promo Tokopedia",
      "quantity_label": "3 Kupon"
    },
    "is_show_onboarding": false,
    "is_hide_courier_name": false,
    "is_ineligbile_promo_dialog_enabled": true,
    "is_macro_micro_insurance_enabled": false,
    "donation_checkbox_status": false,
    "tickers": [],
    "hashed_email": "4c338847bdcb1321794cdbd2d28b3fa1",
    "is_new_buyer": true,
    "disabled_features": [
        "multi_address"
    ],
    "addresses": {
      "active": "",
      "data": {}
    },
    "disabled_features_detail": {
      "disabled_multi_address_message": ""
    }
  }""".trimIndent()

val apiResponseSAFDisableFeatureOrderPrioritas = """{
    "errors": [],
    "error_code": 0,
    "is_multiple": 0,
    "is_coupon_active": 1,
    "is_one_tab_promo": false,
    "group_address": [
      {
        "errors": [],
        "user_address": {
          "address_id": 99367774,
          "address_name": "Kantor",
          "address": "jl prof dr satrio",
          "postal_code": "11720",
          "phone": "628755464540",
          "receiver_name": "Tester",
          "status": 2,
          "country": "Indonesia",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "city_id": 174,
          "city_name": "Jakarta Barat",
          "district_id": 2253,
          "district_name": "Cengkareng",
          "address_2": "-6.148635,106.735185",
          "latitude": "-6.148635",
          "longitude": "106.735185",
          "corner_id": 0,
          "is_corner": false,
          "is_primary": true,
          "buyer_store_code": "",
          "type": 1
        },
        "group_shop": [
          {
            "errors": [],
            "group_key": "",
            "shop": {
              "shop_id": 1479278,
              "user_id": 12299749,
              "admin_ids": [],
              "shop_name": "Tumbler Starbucks 123",
              "shop_image": "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2018/10/19/1479278/1479278_fa306e59-f15f-4048-b2f7-8ddc5d798926.jpeg",
              "shop_url": "https://www.tokopedia.com/tumblersbux",
              "shop_status": 1,
              "is_gold": 1,
              "is_gold_badge": true,
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
              "address_id": 2267,
              "postal_code": "12750",
              "latitude": "-6.2565354",
              "longitude": "106.85040979999997",
              "district_id": 2267,
              "district_name": "",
              "origin": 0,
              "address_street": "",
              "province_id": 0,
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_name": "",
              "country_name": "",
              "is_allow_manage": false,
              "shop_domain": "",
              "is_bridestory": false
            },
            "cart_string": "1479278-30-740525-99367774",
            "shipping_id": 23,
            "sp_id": 45,
            "rates_id": 0,
            "dropshipper": {
              "name": "",
              "telp_no": ""
            },
            "is_insurance": false,
            "is_cod_available": false,
            "is_order_priority": false,
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 2,
                    "ship_prod_name": "OKE",
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 6,
                    "ship_prod_name": "YES",
                    "ship_group_name": "nextday",
                    "ship_group_id": 1003,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
              },
              {
                "ship_id": 10,
                "ship_name": "GO-JEK",
                "ship_code": "gojek",
                "ship_logo": "https://ecs7.tokopedia.net/img/kurir-gosend.png",
                "ship_prods": [
                  {
                    "ship_prod_id": 28,
                    "ship_prod_name": "Instant Courier",
                    "ship_group_name": "instant",
                    "ship_group_id": 1000,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 20,
                    "ship_prod_name": "Same Day",
                    "ship_group_name": "sameday",
                    "ship_group_id": 1002,
                    "additional_fee": 0,
                    "minimum_weight": 0
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 0
              },
              {
                "ship_id": 13,
                "ship_name": "Grab",
                "ship_code": "grab",
                "ship_logo": "https://ecs7.tokopedia.net/img/kurir-grab.png",
                "ship_prods": [
                  {
                    "ship_prod_id": 37,
                    "ship_prod_name": "Instant",
                    "ship_group_name": "instant",
                    "ship_group_id": 1000,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 24,
                    "ship_prod_name": "Same Day",
                    "ship_group_name": "sameday",
                    "ship_group_id": 1002,
                    "additional_fee": 0,
                    "minimum_weight": 0
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 46,
                    "ship_prod_name": "Next Day",
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
              }
            ],
            "products": [
              {
                "errors": [],
                "cart_id": 1256701880,
                "product_id": 516791889,
                "product_alias": "",
                "parent_id": 0,
                "sku": "",
                "campaign_id": 0,
                "is_big_campaign": false,
                "product_name": "Halo Ini Nama Produk Yang Harus Panjang Banget Supaya Penuh Aja HEHEHE",
                "product_description": "Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi ",
                "product_price_fmt": "Rp12.300",
                "trade_in_info": {
                  "is_valid_trade_in": false,
                  "new_device_price": 0,
                  "new_device_price_fmt": "",
                  "old_device_price": 0,
                  "old_device_price_fmt": "",
                  "drop_off_enable": false
                },
                "product_price": 12300,
                "product_original_price": 0,
                "product_price_original_fmt": "",
                "is_slash_price": false,
                "wholesale_price": [],
                "product_wholesale_price": 12300,
                "product_wholesale_price_fmt": "Rp12.300",
                "product_weight_fmt": "112gr",
                "product_weight": 112,
                "product_condition": 1,
                "product_url": "https://www.tokopedia.com/tumblersbux/halo-ini-nama-produk-yang-harus-panjang-banget-supaya-penuh-aja-hehehe",
                "product_returnable": 0,
                "product_is_free_returns": 0,
                "free_returns": {
                  "is_freereturns": 0,
                  "free_returns_logo": ""
                },
                "product_is_preorder": 1,
                "product_preorder": {
                  "duration_text": "30 Hari",
                  "duration_day": 30,
                  "duration_unit_code": 1,
                  "duration_unit_text": "Day",
                  "duration_value": 30
                },
                "product_cashback": "",
                "product_min_order": 1,
                "product_invenage_value": 9979,
                "product_switch_invenage": 1,
                "currency_rate": 1,
                "product_price_currency": 1,
                "product_image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/7/23/12299749/12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700",
                "product_all_images": "[{\"file_name\":\"12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700\",\"file_path\":\"product-1/2019/7/23/12299749\",\"status\":2}]",
                "product_notes": "",
                "product_quantity": 2,
                "product_menu_id": 5333641,
                "product_finsurance": 0,
                "product_fcancel_partial": 0,
                "product_shipment": [],
                "product_shipment_mapping": [],
                "product_cat_id": 753,
                "product_catalog_id": 0,
                "product_status": 1,
                "product_tracker_data": {
                  "attribution": "none/other",
                  "tracker_list_name": "none/other"
                },
                "product_category": "Buku / Novel \u0026 Sastra / Novel Terjemahan",
                "product_total_weight": 224,
                "product_total_weight_fmt": "224 gr",
                "purchase_protection_plan_data": {
                  "protection_available": false,
                  "protection_type_id": 0,
                  "protection_price_per_product": 0,
                  "protection_price": 0,
                  "protection_title": "",
                  "protection_subtitle": "",
                  "protection_link_text": "",
                  "protection_link_url": "",
                  "protection_opt_in": false,
                  "protection_checkbox_disabled": false
                },
                "product_variants": {
                  "parent_id": 0,
                  "default_child": 0,
                  "variant": [],
                  "children": [],
                  "is_enabled": false,
                  "stock": 0
                },
                "warehouse_id": 740525,
                "cashback_percentage": 0,
                "cashback_amount": 0,
                "is_blacklisted": false,
                "free_shipping": {
                  "eligible": false,
                  "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                }
              }
            ],
            "is_fulfillment_service": false,
            "warehouse": {
              "warehouse_id": 740525,
              "partner_id": 0,
              "shop_id": 1479278,
              "warehouse_name": "Shop Location",
              "district_id": 2267,
              "district_name": "Pancoran",
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_id": 13,
              "province_name": "DKI Jakarta",
              "status": 1,
              "postal_code": "12750",
              "is_default": 1,
              "latlon": "-6.2565354,106.85040979999997",
              "latitude": "-6.2565354",
              "longitude": "106.85040979999997",
              "email": "",
              "address_detail": "Taman Pahlawan, Pancoran, 12750",
              "country_name": "Indonesia",
              "is_fulfillment": false,
              "tkpd_preferred_logistic_spid": []
            },
            "has_promo_list": false,
            "save_state_flag": false,
            "vehicle_leasing": {
              "application_id": 0,
              "dp_price": 0,
              "booking_fee": 0,
              "total_price": 0,
              "product_id": 0,
              "dealer_id": 0,
              "multifinance_name": "",
              "is_leasing_product": false,
              "is_allow_checkout": false,
              "error_message": "",
              "original_price": 0
            },
            "rates_feature": {
              "ontime_delivery_guarantee": {
                "available": false,
                "duration": 0,
                "bom": {
                  "title": "",
                  "notes": "",
                  "url_detail": "",
                  "url_text": ""
                }
              }
            }
          }
        ]
      }
    ],
    "kero_token": "Tokopedia+Kero:pBd7GsNEfng8RYtFmJQxv0Oz+sM\u003d",
    "kero_discom_token": "Tokopedia+Kero:cUFp/U20S2+eXDhJvcr7MOCMafE\u003d",
    "kero_unix_time": 1580188919,
    "enable_partial_cancel": false,
    "donation": {
      "Title": "Donasi untuk Korban Banjir Jabodetabek",
      "Nominal": 2000,
      "Description": "Hingga 31 Januari 2020, donasi dari Toppers akan disalurkan oleh Rumah Zakat untuk membantu korban banjir Jabodetabek."
    },
    "is_one_click_shipment": 0,
    "is_robinhood": 1,
    "is_blackbox": 0,
    "promo_suggestion": {
      "cta": "",
      "cta_color": "",
      "is_visible": 0,
      "promo_code": "",
      "text": ""
    },
    "autoapply": {
      "success": false,
      "code": "",
      "is_coupon": 0,
      "discount_amount": 0,
      "title_description": "",
      "message_success": "",
      "promo_id": 0
    },
    "autoapply_v2": {
      "success": false,
      "message": {
        "state": "",
        "color": "",
        "text": ""
      },
      "code": "",
      "promo_code_id": "",
      "title_description": "",
      "is_coupon": 0,
      "discount_amount": "",
      "discount_amount_fmt": 0
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
      "eligible": false,
      "is_tiering": false,
      "range": {
        "min": 0,
        "max": 0
      },
      "message": {
        "title_text": "",
        "sub_text": "",
        "ticker_text": "",
        "tooltip_text": ""
      }
    },
    "autoapply_stack": {
      "global_success": false,
      "success": false,
      "message": {
        "state": "",
        "color": "",
        "text": ""
      },
      "promo_code_id": 0,
      "title_description": "",
      "discount_amount": 0,
      "cashback_amount": 0,
      "cashback_wallet_amount": 0,
      "cashback_advocate_referral_amount": 0,
      "cashback_voucher_description": "",
      "invoice_description": "",
      "gateway_id": "",
      "is_tokopedia_gerai": false,
      "is_coupon": 0,
      "coupon_description": "",
      "benefit_summary_info": {
        "final_benefit_text": "",
        "final_benefit_amount_str": "",
        "final_benefit_amount": 0
      },
      "clashing_info_detail": {
        "clash_message": "",
        "clash_reason": "",
        "is_clashed_promos": false,
        "options": []
      },
      "tracking_details": [],
      "benefit_details": [],
      "ticker_info": {
        "unique_id": "",
        "status_code": 0,
        "message": ""
      }
    },
    "global_coupon_attr": {
      "description": "Gunakan promo Tokopedia",
      "quantity_label": "3 Kupon"
    },
    "is_show_onboarding": false,
    "is_hide_courier_name": false,
    "is_ineligbile_promo_dialog_enabled": true,
    "is_macro_micro_insurance_enabled": false,
    "donation_checkbox_status": false,
    "tickers": [],
    "hashed_email": "4c338847bdcb1321794cdbd2d28b3fa1",
    "is_new_buyer": true,
    "disabled_features": [
        "order_prioritas"
    ],
    "addresses": {
      "active": "",
      "data": {}
    },
    "disabled_features_detail": {
      "disabled_multi_address_message": ""
    }
  }""".trimIndent()

val apiResponseSAFDisableFeatureEGold = """{
    "errors": [],
    "error_code": 0,
    "is_multiple": 0,
    "is_coupon_active": 1,
    "is_one_tab_promo": false,
    "group_address": [
      {
        "errors": [],
        "user_address": {
          "address_id": 99367774,
          "address_name": "Kantor",
          "address": "jl prof dr satrio",
          "postal_code": "11720",
          "phone": "628755464540",
          "receiver_name": "Tester",
          "status": 2,
          "country": "Indonesia",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "city_id": 174,
          "city_name": "Jakarta Barat",
          "district_id": 2253,
          "district_name": "Cengkareng",
          "address_2": "-6.148635,106.735185",
          "latitude": "-6.148635",
          "longitude": "106.735185",
          "corner_id": 0,
          "is_corner": false,
          "is_primary": true,
          "buyer_store_code": "",
          "type": 1
        },
        "group_shop": [
          {
            "errors": [],
            "group_key": "",
            "shop": {
              "shop_id": 1479278,
              "user_id": 12299749,
              "admin_ids": [],
              "shop_name": "Tumbler Starbucks 123",
              "shop_image": "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2018/10/19/1479278/1479278_fa306e59-f15f-4048-b2f7-8ddc5d798926.jpeg",
              "shop_url": "https://www.tokopedia.com/tumblersbux",
              "shop_status": 1,
              "is_gold": 1,
              "is_gold_badge": true,
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
              "address_id": 2267,
              "postal_code": "12750",
              "latitude": "-6.2565354",
              "longitude": "106.85040979999997",
              "district_id": 2267,
              "district_name": "",
              "origin": 0,
              "address_street": "",
              "province_id": 0,
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_name": "",
              "country_name": "",
              "is_allow_manage": false,
              "shop_domain": "",
              "is_bridestory": false
            },
            "cart_string": "1479278-30-740525-99367774",
            "shipping_id": 23,
            "sp_id": 45,
            "rates_id": 0,
            "dropshipper": {
              "name": "",
              "telp_no": ""
            },
            "is_insurance": false,
            "is_cod_available": false,
            "is_order_priority": false,
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 2,
                    "ship_prod_name": "OKE",
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 6,
                    "ship_prod_name": "YES",
                    "ship_group_name": "nextday",
                    "ship_group_id": 1003,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
              },
              {
                "ship_id": 10,
                "ship_name": "GO-JEK",
                "ship_code": "gojek",
                "ship_logo": "https://ecs7.tokopedia.net/img/kurir-gosend.png",
                "ship_prods": [
                  {
                    "ship_prod_id": 28,
                    "ship_prod_name": "Instant Courier",
                    "ship_group_name": "instant",
                    "ship_group_id": 1000,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 20,
                    "ship_prod_name": "Same Day",
                    "ship_group_name": "sameday",
                    "ship_group_id": 1002,
                    "additional_fee": 0,
                    "minimum_weight": 0
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 0
              },
              {
                "ship_id": 13,
                "ship_name": "Grab",
                "ship_code": "grab",
                "ship_logo": "https://ecs7.tokopedia.net/img/kurir-grab.png",
                "ship_prods": [
                  {
                    "ship_prod_id": 37,
                    "ship_prod_name": "Instant",
                    "ship_group_name": "instant",
                    "ship_group_id": 1000,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 24,
                    "ship_prod_name": "Same Day",
                    "ship_group_name": "sameday",
                    "ship_group_id": 1002,
                    "additional_fee": 0,
                    "minimum_weight": 0
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 46,
                    "ship_prod_name": "Next Day",
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
              }
            ],
            "products": [
              {
                "errors": [],
                "cart_id": 1256701880,
                "product_id": 516791889,
                "product_alias": "",
                "parent_id": 0,
                "sku": "",
                "campaign_id": 0,
                "is_big_campaign": false,
                "product_name": "Halo Ini Nama Produk Yang Harus Panjang Banget Supaya Penuh Aja HEHEHE",
                "product_description": "Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi ",
                "product_price_fmt": "Rp12.300",
                "trade_in_info": {
                  "is_valid_trade_in": false,
                  "new_device_price": 0,
                  "new_device_price_fmt": "",
                  "old_device_price": 0,
                  "old_device_price_fmt": "",
                  "drop_off_enable": false
                },
                "product_price": 12300,
                "product_original_price": 0,
                "product_price_original_fmt": "",
                "is_slash_price": false,
                "wholesale_price": [],
                "product_wholesale_price": 12300,
                "product_wholesale_price_fmt": "Rp12.300",
                "product_weight_fmt": "112gr",
                "product_weight": 112,
                "product_condition": 1,
                "product_url": "https://www.tokopedia.com/tumblersbux/halo-ini-nama-produk-yang-harus-panjang-banget-supaya-penuh-aja-hehehe",
                "product_returnable": 0,
                "product_is_free_returns": 0,
                "free_returns": {
                  "is_freereturns": 0,
                  "free_returns_logo": ""
                },
                "product_is_preorder": 1,
                "product_preorder": {
                  "duration_text": "30 Hari",
                  "duration_day": 30,
                  "duration_unit_code": 1,
                  "duration_unit_text": "Day",
                  "duration_value": 30
                },
                "product_cashback": "",
                "product_min_order": 1,
                "product_invenage_value": 9979,
                "product_switch_invenage": 1,
                "currency_rate": 1,
                "product_price_currency": 1,
                "product_image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/7/23/12299749/12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700",
                "product_all_images": "[{\"file_name\":\"12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700\",\"file_path\":\"product-1/2019/7/23/12299749\",\"status\":2}]",
                "product_notes": "",
                "product_quantity": 2,
                "product_menu_id": 5333641,
                "product_finsurance": 0,
                "product_fcancel_partial": 0,
                "product_shipment": [],
                "product_shipment_mapping": [],
                "product_cat_id": 753,
                "product_catalog_id": 0,
                "product_status": 1,
                "product_tracker_data": {
                  "attribution": "none/other",
                  "tracker_list_name": "none/other"
                },
                "product_category": "Buku / Novel \u0026 Sastra / Novel Terjemahan",
                "product_total_weight": 224,
                "product_total_weight_fmt": "224 gr",
                "purchase_protection_plan_data": {
                  "protection_available": false,
                  "protection_type_id": 0,
                  "protection_price_per_product": 0,
                  "protection_price": 0,
                  "protection_title": "",
                  "protection_subtitle": "",
                  "protection_link_text": "",
                  "protection_link_url": "",
                  "protection_opt_in": false,
                  "protection_checkbox_disabled": false
                },
                "product_variants": {
                  "parent_id": 0,
                  "default_child": 0,
                  "variant": [],
                  "children": [],
                  "is_enabled": false,
                  "stock": 0
                },
                "warehouse_id": 740525,
                "cashback_percentage": 0,
                "cashback_amount": 0,
                "is_blacklisted": false,
                "free_shipping": {
                  "eligible": false,
                  "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                }
              }
            ],
            "is_fulfillment_service": false,
            "warehouse": {
              "warehouse_id": 740525,
              "partner_id": 0,
              "shop_id": 1479278,
              "warehouse_name": "Shop Location",
              "district_id": 2267,
              "district_name": "Pancoran",
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_id": 13,
              "province_name": "DKI Jakarta",
              "status": 1,
              "postal_code": "12750",
              "is_default": 1,
              "latlon": "-6.2565354,106.85040979999997",
              "latitude": "-6.2565354",
              "longitude": "106.85040979999997",
              "email": "",
              "address_detail": "Taman Pahlawan, Pancoran, 12750",
              "country_name": "Indonesia",
              "is_fulfillment": false,
              "tkpd_preferred_logistic_spid": []
            },
            "has_promo_list": false,
            "save_state_flag": false,
            "vehicle_leasing": {
              "application_id": 0,
              "dp_price": 0,
              "booking_fee": 0,
              "total_price": 0,
              "product_id": 0,
              "dealer_id": 0,
              "multifinance_name": "",
              "is_leasing_product": false,
              "is_allow_checkout": false,
              "error_message": "",
              "original_price": 0
            },
            "rates_feature": {
              "ontime_delivery_guarantee": {
                "available": false,
                "duration": 0,
                "bom": {
                  "title": "",
                  "notes": "",
                  "url_detail": "",
                  "url_text": ""
                }
              }
            }
          }
        ]
      }
    ],
    "kero_token": "Tokopedia+Kero:pBd7GsNEfng8RYtFmJQxv0Oz+sM\u003d",
    "kero_discom_token": "Tokopedia+Kero:cUFp/U20S2+eXDhJvcr7MOCMafE\u003d",
    "kero_unix_time": 1580188919,
    "enable_partial_cancel": false,
    "donation": {
      "Title": "Donasi untuk Korban Banjir Jabodetabek",
      "Nominal": 2000,
      "Description": "Hingga 31 Januari 2020, donasi dari Toppers akan disalurkan oleh Rumah Zakat untuk membantu korban banjir Jabodetabek."
    },
    "is_one_click_shipment": 0,
    "is_robinhood": 1,
    "is_blackbox": 0,
    "promo_suggestion": {
      "cta": "",
      "cta_color": "",
      "is_visible": 0,
      "promo_code": "",
      "text": ""
    },
    "autoapply": {
      "success": false,
      "code": "",
      "is_coupon": 0,
      "discount_amount": 0,
      "title_description": "",
      "message_success": "",
      "promo_id": 0
    },
    "autoapply_v2": {
      "success": false,
      "message": {
        "state": "",
        "color": "",
        "text": ""
      },
      "code": "",
      "promo_code_id": "",
      "title_description": "",
      "is_coupon": 0,
      "discount_amount": "",
      "discount_amount_fmt": 0
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
      "eligible": false,
      "is_tiering": false,
      "range": {
        "min": 0,
        "max": 0
      },
      "message": {
        "title_text": "",
        "sub_text": "",
        "ticker_text": "",
        "tooltip_text": ""
      }
    },
    "autoapply_stack": {
      "global_success": false,
      "success": false,
      "message": {
        "state": "",
        "color": "",
        "text": ""
      },
      "promo_code_id": 0,
      "title_description": "",
      "discount_amount": 0,
      "cashback_amount": 0,
      "cashback_wallet_amount": 0,
      "cashback_advocate_referral_amount": 0,
      "cashback_voucher_description": "",
      "invoice_description": "",
      "gateway_id": "",
      "is_tokopedia_gerai": false,
      "is_coupon": 0,
      "coupon_description": "",
      "benefit_summary_info": {
        "final_benefit_text": "",
        "final_benefit_amount_str": "",
        "final_benefit_amount": 0
      },
      "clashing_info_detail": {
        "clash_message": "",
        "clash_reason": "",
        "is_clashed_promos": false,
        "options": []
      },
      "tracking_details": [],
      "benefit_details": [],
      "ticker_info": {
        "unique_id": "",
        "status_code": 0,
        "message": ""
      }
    },
    "global_coupon_attr": {
      "description": "Gunakan promo Tokopedia",
      "quantity_label": "3 Kupon"
    },
    "is_show_onboarding": false,
    "is_hide_courier_name": false,
    "is_ineligbile_promo_dialog_enabled": true,
    "is_macro_micro_insurance_enabled": false,
    "donation_checkbox_status": false,
    "tickers": [],
    "hashed_email": "4c338847bdcb1321794cdbd2d28b3fa1",
    "is_new_buyer": true,
    "disabled_features": [
        "egold"
    ],
    "addresses": {
      "active": "",
      "data": {}
    },
    "disabled_features_detail": {
      "disabled_multi_address_message": ""
    }
  }""".trimIndent()

val apiResponseSAFDisableFeaturePPP = """{
    "errors": [],
    "error_code": 0,
    "is_multiple": 0,
    "is_coupon_active": 1,
    "is_one_tab_promo": false,
    "group_address": [
      {
        "errors": [],
        "user_address": {
          "address_id": 99367774,
          "address_name": "Kantor",
          "address": "jl prof dr satrio",
          "postal_code": "11720",
          "phone": "628755464540",
          "receiver_name": "Tester",
          "status": 2,
          "country": "Indonesia",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "city_id": 174,
          "city_name": "Jakarta Barat",
          "district_id": 2253,
          "district_name": "Cengkareng",
          "address_2": "-6.148635,106.735185",
          "latitude": "-6.148635",
          "longitude": "106.735185",
          "corner_id": 0,
          "is_corner": false,
          "is_primary": true,
          "buyer_store_code": "",
          "type": 1
        },
        "group_shop": [
          {
            "errors": [],
            "group_key": "",
            "shop": {
              "shop_id": 1479278,
              "user_id": 12299749,
              "admin_ids": [],
              "shop_name": "Tumbler Starbucks 123",
              "shop_image": "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2018/10/19/1479278/1479278_fa306e59-f15f-4048-b2f7-8ddc5d798926.jpeg",
              "shop_url": "https://www.tokopedia.com/tumblersbux",
              "shop_status": 1,
              "is_gold": 1,
              "is_gold_badge": true,
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
              "address_id": 2267,
              "postal_code": "12750",
              "latitude": "-6.2565354",
              "longitude": "106.85040979999997",
              "district_id": 2267,
              "district_name": "",
              "origin": 0,
              "address_street": "",
              "province_id": 0,
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_name": "",
              "country_name": "",
              "is_allow_manage": false,
              "shop_domain": "",
              "is_bridestory": false
            },
            "cart_string": "1479278-30-740525-99367774",
            "shipping_id": 23,
            "sp_id": 45,
            "rates_id": 0,
            "dropshipper": {
              "name": "",
              "telp_no": ""
            },
            "is_insurance": false,
            "is_cod_available": false,
            "is_order_priority": false,
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 2,
                    "ship_prod_name": "OKE",
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 6,
                    "ship_prod_name": "YES",
                    "ship_group_name": "nextday",
                    "ship_group_id": 1003,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
              },
              {
                "ship_id": 10,
                "ship_name": "GO-JEK",
                "ship_code": "gojek",
                "ship_logo": "https://ecs7.tokopedia.net/img/kurir-gosend.png",
                "ship_prods": [
                  {
                    "ship_prod_id": 28,
                    "ship_prod_name": "Instant Courier",
                    "ship_group_name": "instant",
                    "ship_group_id": 1000,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 20,
                    "ship_prod_name": "Same Day",
                    "ship_group_name": "sameday",
                    "ship_group_id": 1002,
                    "additional_fee": 0,
                    "minimum_weight": 0
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 0
              },
              {
                "ship_id": 13,
                "ship_name": "Grab",
                "ship_code": "grab",
                "ship_logo": "https://ecs7.tokopedia.net/img/kurir-grab.png",
                "ship_prods": [
                  {
                    "ship_prod_id": 37,
                    "ship_prod_name": "Instant",
                    "ship_group_name": "instant",
                    "ship_group_id": 1000,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 24,
                    "ship_prod_name": "Same Day",
                    "ship_group_name": "sameday",
                    "ship_group_id": 1002,
                    "additional_fee": 0,
                    "minimum_weight": 0
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 46,
                    "ship_prod_name": "Next Day",
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
              }
            ],
            "products": [
              {
                "errors": [],
                "cart_id": 1256701880,
                "product_id": 516791889,
                "product_alias": "",
                "parent_id": 0,
                "sku": "",
                "campaign_id": 0,
                "is_big_campaign": false,
                "product_name": "Halo Ini Nama Produk Yang Harus Panjang Banget Supaya Penuh Aja HEHEHE",
                "product_description": "Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi ",
                "product_price_fmt": "Rp12.300",
                "trade_in_info": {
                  "is_valid_trade_in": false,
                  "new_device_price": 0,
                  "new_device_price_fmt": "",
                  "old_device_price": 0,
                  "old_device_price_fmt": "",
                  "drop_off_enable": false
                },
                "product_price": 12300,
                "product_original_price": 0,
                "product_price_original_fmt": "",
                "is_slash_price": false,
                "wholesale_price": [],
                "product_wholesale_price": 12300,
                "product_wholesale_price_fmt": "Rp12.300",
                "product_weight_fmt": "112gr",
                "product_weight": 112,
                "product_condition": 1,
                "product_url": "https://www.tokopedia.com/tumblersbux/halo-ini-nama-produk-yang-harus-panjang-banget-supaya-penuh-aja-hehehe",
                "product_returnable": 0,
                "product_is_free_returns": 0,
                "free_returns": {
                  "is_freereturns": 0,
                  "free_returns_logo": ""
                },
                "product_is_preorder": 1,
                "product_preorder": {
                  "duration_text": "30 Hari",
                  "duration_day": 30,
                  "duration_unit_code": 1,
                  "duration_unit_text": "Day",
                  "duration_value": 30
                },
                "product_cashback": "",
                "product_min_order": 1,
                "product_invenage_value": 9979,
                "product_switch_invenage": 1,
                "currency_rate": 1,
                "product_price_currency": 1,
                "product_image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/7/23/12299749/12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700",
                "product_all_images": "[{\"file_name\":\"12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700\",\"file_path\":\"product-1/2019/7/23/12299749\",\"status\":2}]",
                "product_notes": "",
                "product_quantity": 2,
                "product_menu_id": 5333641,
                "product_finsurance": 0,
                "product_fcancel_partial": 0,
                "product_shipment": [],
                "product_shipment_mapping": [],
                "product_cat_id": 753,
                "product_catalog_id": 0,
                "product_status": 1,
                "product_tracker_data": {
                  "attribution": "none/other",
                  "tracker_list_name": "none/other"
                },
                "product_category": "Buku / Novel \u0026 Sastra / Novel Terjemahan",
                "product_total_weight": 224,
                "product_total_weight_fmt": "224 gr",
                "purchase_protection_plan_data": {
                  "protection_available": false,
                  "protection_type_id": 0,
                  "protection_price_per_product": 0,
                  "protection_price": 0,
                  "protection_title": "",
                  "protection_subtitle": "",
                  "protection_link_text": "",
                  "protection_link_url": "",
                  "protection_opt_in": false,
                  "protection_checkbox_disabled": false
                },
                "product_variants": {
                  "parent_id": 0,
                  "default_child": 0,
                  "variant": [],
                  "children": [],
                  "is_enabled": false,
                  "stock": 0
                },
                "warehouse_id": 740525,
                "cashback_percentage": 0,
                "cashback_amount": 0,
                "is_blacklisted": false,
                "free_shipping": {
                  "eligible": false,
                  "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                }
              }
            ],
            "is_fulfillment_service": false,
            "warehouse": {
              "warehouse_id": 740525,
              "partner_id": 0,
              "shop_id": 1479278,
              "warehouse_name": "Shop Location",
              "district_id": 2267,
              "district_name": "Pancoran",
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_id": 13,
              "province_name": "DKI Jakarta",
              "status": 1,
              "postal_code": "12750",
              "is_default": 1,
              "latlon": "-6.2565354,106.85040979999997",
              "latitude": "-6.2565354",
              "longitude": "106.85040979999997",
              "email": "",
              "address_detail": "Taman Pahlawan, Pancoran, 12750",
              "country_name": "Indonesia",
              "is_fulfillment": false,
              "tkpd_preferred_logistic_spid": []
            },
            "has_promo_list": false,
            "save_state_flag": false,
            "vehicle_leasing": {
              "application_id": 0,
              "dp_price": 0,
              "booking_fee": 0,
              "total_price": 0,
              "product_id": 0,
              "dealer_id": 0,
              "multifinance_name": "",
              "is_leasing_product": false,
              "is_allow_checkout": false,
              "error_message": "",
              "original_price": 0
            },
            "rates_feature": {
              "ontime_delivery_guarantee": {
                "available": false,
                "duration": 0,
                "bom": {
                  "title": "",
                  "notes": "",
                  "url_detail": "",
                  "url_text": ""
                }
              }
            }
          }
        ]
      }
    ],
    "kero_token": "Tokopedia+Kero:pBd7GsNEfng8RYtFmJQxv0Oz+sM\u003d",
    "kero_discom_token": "Tokopedia+Kero:cUFp/U20S2+eXDhJvcr7MOCMafE\u003d",
    "kero_unix_time": 1580188919,
    "enable_partial_cancel": false,
    "donation": {
      "Title": "Donasi untuk Korban Banjir Jabodetabek",
      "Nominal": 2000,
      "Description": "Hingga 31 Januari 2020, donasi dari Toppers akan disalurkan oleh Rumah Zakat untuk membantu korban banjir Jabodetabek."
    },
    "is_one_click_shipment": 0,
    "is_robinhood": 1,
    "is_blackbox": 0,
    "promo_suggestion": {
      "cta": "",
      "cta_color": "",
      "is_visible": 0,
      "promo_code": "",
      "text": ""
    },
    "autoapply": {
      "success": false,
      "code": "",
      "is_coupon": 0,
      "discount_amount": 0,
      "title_description": "",
      "message_success": "",
      "promo_id": 0
    },
    "autoapply_v2": {
      "success": false,
      "message": {
        "state": "",
        "color": "",
        "text": ""
      },
      "code": "",
      "promo_code_id": "",
      "title_description": "",
      "is_coupon": 0,
      "discount_amount": "",
      "discount_amount_fmt": 0
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
      "eligible": false,
      "is_tiering": false,
      "range": {
        "min": 0,
        "max": 0
      },
      "message": {
        "title_text": "",
        "sub_text": "",
        "ticker_text": "",
        "tooltip_text": ""
      }
    },
    "autoapply_stack": {
      "global_success": false,
      "success": false,
      "message": {
        "state": "",
        "color": "",
        "text": ""
      },
      "promo_code_id": 0,
      "title_description": "",
      "discount_amount": 0,
      "cashback_amount": 0,
      "cashback_wallet_amount": 0,
      "cashback_advocate_referral_amount": 0,
      "cashback_voucher_description": "",
      "invoice_description": "",
      "gateway_id": "",
      "is_tokopedia_gerai": false,
      "is_coupon": 0,
      "coupon_description": "",
      "benefit_summary_info": {
        "final_benefit_text": "",
        "final_benefit_amount_str": "",
        "final_benefit_amount": 0
      },
      "clashing_info_detail": {
        "clash_message": "",
        "clash_reason": "",
        "is_clashed_promos": false,
        "options": []
      },
      "tracking_details": [],
      "benefit_details": [],
      "ticker_info": {
        "unique_id": "",
        "status_code": 0,
        "message": ""
      }
    },
    "global_coupon_attr": {
      "description": "Gunakan promo Tokopedia",
      "quantity_label": "3 Kupon"
    },
    "is_show_onboarding": false,
    "is_hide_courier_name": false,
    "is_ineligbile_promo_dialog_enabled": true,
    "is_macro_micro_insurance_enabled": false,
    "donation_checkbox_status": false,
    "tickers": [],
    "hashed_email": "4c338847bdcb1321794cdbd2d28b3fa1",
    "is_new_buyer": true,
    "disabled_features": [
        "ppp"
    ],
    "addresses": {
      "active": "",
      "data": {}
    },
    "disabled_features_detail": {
      "disabled_multi_address_message": ""
    }
  }""".trimIndent()

val apiResponseSAFDisableFeatureDonation = """{
    "errors": [],
    "error_code": 0,
    "is_multiple": 0,
    "is_coupon_active": 1,
    "is_one_tab_promo": false,
    "group_address": [
      {
        "errors": [],
        "user_address": {
          "address_id": 99367774,
          "address_name": "Kantor",
          "address": "jl prof dr satrio",
          "postal_code": "11720",
          "phone": "628755464540",
          "receiver_name": "Tester",
          "status": 2,
          "country": "Indonesia",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "city_id": 174,
          "city_name": "Jakarta Barat",
          "district_id": 2253,
          "district_name": "Cengkareng",
          "address_2": "-6.148635,106.735185",
          "latitude": "-6.148635",
          "longitude": "106.735185",
          "corner_id": 0,
          "is_corner": false,
          "is_primary": true,
          "buyer_store_code": "",
          "type": 1
        },
        "group_shop": [
          {
            "errors": [],
            "group_key": "",
            "shop": {
              "shop_id": 1479278,
              "user_id": 12299749,
              "admin_ids": [],
              "shop_name": "Tumbler Starbucks 123",
              "shop_image": "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2018/10/19/1479278/1479278_fa306e59-f15f-4048-b2f7-8ddc5d798926.jpeg",
              "shop_url": "https://www.tokopedia.com/tumblersbux",
              "shop_status": 1,
              "is_gold": 1,
              "is_gold_badge": true,
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
              "address_id": 2267,
              "postal_code": "12750",
              "latitude": "-6.2565354",
              "longitude": "106.85040979999997",
              "district_id": 2267,
              "district_name": "",
              "origin": 0,
              "address_street": "",
              "province_id": 0,
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_name": "",
              "country_name": "",
              "is_allow_manage": false,
              "shop_domain": "",
              "is_bridestory": false
            },
            "cart_string": "1479278-30-740525-99367774",
            "shipping_id": 23,
            "sp_id": 45,
            "rates_id": 0,
            "dropshipper": {
              "name": "",
              "telp_no": ""
            },
            "is_insurance": false,
            "is_cod_available": false,
            "is_order_priority": false,
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 2,
                    "ship_prod_name": "OKE",
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 6,
                    "ship_prod_name": "YES",
                    "ship_group_name": "nextday",
                    "ship_group_id": 1003,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
              },
              {
                "ship_id": 10,
                "ship_name": "GO-JEK",
                "ship_code": "gojek",
                "ship_logo": "https://ecs7.tokopedia.net/img/kurir-gosend.png",
                "ship_prods": [
                  {
                    "ship_prod_id": 28,
                    "ship_prod_name": "Instant Courier",
                    "ship_group_name": "instant",
                    "ship_group_id": 1000,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 20,
                    "ship_prod_name": "Same Day",
                    "ship_group_name": "sameday",
                    "ship_group_id": 1002,
                    "additional_fee": 0,
                    "minimum_weight": 0
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 0
              },
              {
                "ship_id": 13,
                "ship_name": "Grab",
                "ship_code": "grab",
                "ship_logo": "https://ecs7.tokopedia.net/img/kurir-grab.png",
                "ship_prods": [
                  {
                    "ship_prod_id": 37,
                    "ship_prod_name": "Instant",
                    "ship_group_name": "instant",
                    "ship_group_id": 1000,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 24,
                    "ship_prod_name": "Same Day",
                    "ship_group_name": "sameday",
                    "ship_group_id": 1002,
                    "additional_fee": 0,
                    "minimum_weight": 0
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 46,
                    "ship_prod_name": "Next Day",
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
              }
            ],
            "products": [
              {
                "errors": [],
                "cart_id": 1256701880,
                "product_id": 516791889,
                "product_alias": "",
                "parent_id": 0,
                "sku": "",
                "campaign_id": 0,
                "is_big_campaign": false,
                "product_name": "Halo Ini Nama Produk Yang Harus Panjang Banget Supaya Penuh Aja HEHEHE",
                "product_description": "Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi ",
                "product_price_fmt": "Rp12.300",
                "trade_in_info": {
                  "is_valid_trade_in": false,
                  "new_device_price": 0,
                  "new_device_price_fmt": "",
                  "old_device_price": 0,
                  "old_device_price_fmt": "",
                  "drop_off_enable": false
                },
                "product_price": 12300,
                "product_original_price": 0,
                "product_price_original_fmt": "",
                "is_slash_price": false,
                "wholesale_price": [],
                "product_wholesale_price": 12300,
                "product_wholesale_price_fmt": "Rp12.300",
                "product_weight_fmt": "112gr",
                "product_weight": 112,
                "product_condition": 1,
                "product_url": "https://www.tokopedia.com/tumblersbux/halo-ini-nama-produk-yang-harus-panjang-banget-supaya-penuh-aja-hehehe",
                "product_returnable": 0,
                "product_is_free_returns": 0,
                "free_returns": {
                  "is_freereturns": 0,
                  "free_returns_logo": ""
                },
                "product_is_preorder": 1,
                "product_preorder": {
                  "duration_text": "30 Hari",
                  "duration_day": 30,
                  "duration_unit_code": 1,
                  "duration_unit_text": "Day",
                  "duration_value": 30
                },
                "product_cashback": "",
                "product_min_order": 1,
                "product_invenage_value": 9979,
                "product_switch_invenage": 1,
                "currency_rate": 1,
                "product_price_currency": 1,
                "product_image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/7/23/12299749/12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700",
                "product_all_images": "[{\"file_name\":\"12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700\",\"file_path\":\"product-1/2019/7/23/12299749\",\"status\":2}]",
                "product_notes": "",
                "product_quantity": 2,
                "product_menu_id": 5333641,
                "product_finsurance": 0,
                "product_fcancel_partial": 0,
                "product_shipment": [],
                "product_shipment_mapping": [],
                "product_cat_id": 753,
                "product_catalog_id": 0,
                "product_status": 1,
                "product_tracker_data": {
                  "attribution": "none/other",
                  "tracker_list_name": "none/other"
                },
                "product_category": "Buku / Novel \u0026 Sastra / Novel Terjemahan",
                "product_total_weight": 224,
                "product_total_weight_fmt": "224 gr",
                "purchase_protection_plan_data": {
                  "protection_available": false,
                  "protection_type_id": 0,
                  "protection_price_per_product": 0,
                  "protection_price": 0,
                  "protection_title": "",
                  "protection_subtitle": "",
                  "protection_link_text": "",
                  "protection_link_url": "",
                  "protection_opt_in": false,
                  "protection_checkbox_disabled": false
                },
                "product_variants": {
                  "parent_id": 0,
                  "default_child": 0,
                  "variant": [],
                  "children": [],
                  "is_enabled": false,
                  "stock": 0
                },
                "warehouse_id": 740525,
                "cashback_percentage": 0,
                "cashback_amount": 0,
                "is_blacklisted": false,
                "free_shipping": {
                  "eligible": false,
                  "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                }
              }
            ],
            "is_fulfillment_service": false,
            "warehouse": {
              "warehouse_id": 740525,
              "partner_id": 0,
              "shop_id": 1479278,
              "warehouse_name": "Shop Location",
              "district_id": 2267,
              "district_name": "Pancoran",
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_id": 13,
              "province_name": "DKI Jakarta",
              "status": 1,
              "postal_code": "12750",
              "is_default": 1,
              "latlon": "-6.2565354,106.85040979999997",
              "latitude": "-6.2565354",
              "longitude": "106.85040979999997",
              "email": "",
              "address_detail": "Taman Pahlawan, Pancoran, 12750",
              "country_name": "Indonesia",
              "is_fulfillment": false,
              "tkpd_preferred_logistic_spid": []
            },
            "has_promo_list": false,
            "save_state_flag": false,
            "vehicle_leasing": {
              "application_id": 0,
              "dp_price": 0,
              "booking_fee": 0,
              "total_price": 0,
              "product_id": 0,
              "dealer_id": 0,
              "multifinance_name": "",
              "is_leasing_product": false,
              "is_allow_checkout": false,
              "error_message": "",
              "original_price": 0
            },
            "rates_feature": {
              "ontime_delivery_guarantee": {
                "available": false,
                "duration": 0,
                "bom": {
                  "title": "",
                  "notes": "",
                  "url_detail": "",
                  "url_text": ""
                }
              }
            }
          }
        ]
      }
    ],
    "kero_token": "Tokopedia+Kero:pBd7GsNEfng8RYtFmJQxv0Oz+sM\u003d",
    "kero_discom_token": "Tokopedia+Kero:cUFp/U20S2+eXDhJvcr7MOCMafE\u003d",
    "kero_unix_time": 1580188919,
    "enable_partial_cancel": false,
    "donation": {
      "Title": "Donasi untuk Korban Banjir Jabodetabek",
      "Nominal": 2000,
      "Description": "Hingga 31 Januari 2020, donasi dari Toppers akan disalurkan oleh Rumah Zakat untuk membantu korban banjir Jabodetabek."
    },
    "is_one_click_shipment": 0,
    "is_robinhood": 1,
    "is_blackbox": 0,
    "promo_suggestion": {
      "cta": "",
      "cta_color": "",
      "is_visible": 0,
      "promo_code": "",
      "text": ""
    },
    "autoapply": {
      "success": false,
      "code": "",
      "is_coupon": 0,
      "discount_amount": 0,
      "title_description": "",
      "message_success": "",
      "promo_id": 0
    },
    "autoapply_v2": {
      "success": false,
      "message": {
        "state": "",
        "color": "",
        "text": ""
      },
      "code": "",
      "promo_code_id": "",
      "title_description": "",
      "is_coupon": 0,
      "discount_amount": "",
      "discount_amount_fmt": 0
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
      "eligible": false,
      "is_tiering": false,
      "range": {
        "min": 0,
        "max": 0
      },
      "message": {
        "title_text": "",
        "sub_text": "",
        "ticker_text": "",
        "tooltip_text": ""
      }
    },
    "autoapply_stack": {
      "global_success": false,
      "success": false,
      "message": {
        "state": "",
        "color": "",
        "text": ""
      },
      "promo_code_id": 0,
      "title_description": "",
      "discount_amount": 0,
      "cashback_amount": 0,
      "cashback_wallet_amount": 0,
      "cashback_advocate_referral_amount": 0,
      "cashback_voucher_description": "",
      "invoice_description": "",
      "gateway_id": "",
      "is_tokopedia_gerai": false,
      "is_coupon": 0,
      "coupon_description": "",
      "benefit_summary_info": {
        "final_benefit_text": "",
        "final_benefit_amount_str": "",
        "final_benefit_amount": 0
      },
      "clashing_info_detail": {
        "clash_message": "",
        "clash_reason": "",
        "is_clashed_promos": false,
        "options": []
      },
      "tracking_details": [],
      "benefit_details": [],
      "ticker_info": {
        "unique_id": "",
        "status_code": 0,
        "message": ""
      }
    },
    "global_coupon_attr": {
      "description": "Gunakan promo Tokopedia",
      "quantity_label": "3 Kupon"
    },
    "is_show_onboarding": false,
    "is_hide_courier_name": false,
    "is_ineligbile_promo_dialog_enabled": true,
    "is_macro_micro_insurance_enabled": false,
    "donation_checkbox_status": false,
    "tickers": [],
    "hashed_email": "4c338847bdcb1321794cdbd2d28b3fa1",
    "is_new_buyer": true,
    "disabled_features": [
        "donation"
    ],
    "addresses": {
      "active": "",
      "data": {}
    },
    "disabled_features_detail": {
      "disabled_multi_address_message": ""
    }
  }""".trimIndent()

val apiResponseSAFDisableFeatureAllOldBuyer = """{
    "errors": [],
    "error_code": 0,
    "is_multiple": 0,
    "is_coupon_active": 1,
    "is_one_tab_promo": false,
    "group_address": [
      {
        "errors": [],
        "user_address": {
          "address_id": 99367774,
          "address_name": "Kantor",
          "address": "jl prof dr satrio",
          "postal_code": "11720",
          "phone": "628755464540",
          "receiver_name": "Tester",
          "status": 2,
          "country": "Indonesia",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "city_id": 174,
          "city_name": "Jakarta Barat",
          "district_id": 2253,
          "district_name": "Cengkareng",
          "address_2": "-6.148635,106.735185",
          "latitude": "-6.148635",
          "longitude": "106.735185",
          "corner_id": 0,
          "is_corner": false,
          "is_primary": true,
          "buyer_store_code": "",
          "type": 1
        },
        "group_shop": [
          {
            "errors": [],
            "group_key": "",
            "shop": {
              "shop_id": 1479278,
              "user_id": 12299749,
              "admin_ids": [],
              "shop_name": "Tumbler Starbucks 123",
              "shop_image": "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2018/10/19/1479278/1479278_fa306e59-f15f-4048-b2f7-8ddc5d798926.jpeg",
              "shop_url": "https://www.tokopedia.com/tumblersbux",
              "shop_status": 1,
              "is_gold": 1,
              "is_gold_badge": true,
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
              "address_id": 2267,
              "postal_code": "12750",
              "latitude": "-6.2565354",
              "longitude": "106.85040979999997",
              "district_id": 2267,
              "district_name": "",
              "origin": 0,
              "address_street": "",
              "province_id": 0,
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_name": "",
              "country_name": "",
              "is_allow_manage": false,
              "shop_domain": "",
              "is_bridestory": false
            },
            "cart_string": "1479278-30-740525-99367774",
            "shipping_id": 23,
            "sp_id": 45,
            "rates_id": 0,
            "dropshipper": {
              "name": "",
              "telp_no": ""
            },
            "is_insurance": false,
            "is_cod_available": false,
            "is_order_priority": false,
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 2,
                    "ship_prod_name": "OKE",
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 6,
                    "ship_prod_name": "YES",
                    "ship_group_name": "nextday",
                    "ship_group_id": 1003,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
              },
              {
                "ship_id": 10,
                "ship_name": "GO-JEK",
                "ship_code": "gojek",
                "ship_logo": "https://ecs7.tokopedia.net/img/kurir-gosend.png",
                "ship_prods": [
                  {
                    "ship_prod_id": 28,
                    "ship_prod_name": "Instant Courier",
                    "ship_group_name": "instant",
                    "ship_group_id": 1000,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 20,
                    "ship_prod_name": "Same Day",
                    "ship_group_name": "sameday",
                    "ship_group_id": 1002,
                    "additional_fee": 0,
                    "minimum_weight": 0
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 0
              },
              {
                "ship_id": 13,
                "ship_name": "Grab",
                "ship_code": "grab",
                "ship_logo": "https://ecs7.tokopedia.net/img/kurir-grab.png",
                "ship_prods": [
                  {
                    "ship_prod_id": 37,
                    "ship_prod_name": "Instant",
                    "ship_group_name": "instant",
                    "ship_group_id": 1000,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 24,
                    "ship_prod_name": "Same Day",
                    "ship_group_name": "sameday",
                    "ship_group_id": 1002,
                    "additional_fee": 0,
                    "minimum_weight": 0
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 46,
                    "ship_prod_name": "Next Day",
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
              }
            ],
            "products": [
              {
                "errors": [],
                "cart_id": 1256701880,
                "product_id": 516791889,
                "product_alias": "",
                "parent_id": 0,
                "sku": "",
                "campaign_id": 0,
                "is_big_campaign": false,
                "product_name": "Halo Ini Nama Produk Yang Harus Panjang Banget Supaya Penuh Aja HEHEHE",
                "product_description": "Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi ",
                "product_price_fmt": "Rp12.300",
                "trade_in_info": {
                  "is_valid_trade_in": false,
                  "new_device_price": 0,
                  "new_device_price_fmt": "",
                  "old_device_price": 0,
                  "old_device_price_fmt": "",
                  "drop_off_enable": false
                },
                "product_price": 12300,
                "product_original_price": 0,
                "product_price_original_fmt": "",
                "is_slash_price": false,
                "wholesale_price": [],
                "product_wholesale_price": 12300,
                "product_wholesale_price_fmt": "Rp12.300",
                "product_weight_fmt": "112gr",
                "product_weight": 112,
                "product_condition": 1,
                "product_url": "https://www.tokopedia.com/tumblersbux/halo-ini-nama-produk-yang-harus-panjang-banget-supaya-penuh-aja-hehehe",
                "product_returnable": 0,
                "product_is_free_returns": 0,
                "free_returns": {
                  "is_freereturns": 0,
                  "free_returns_logo": ""
                },
                "product_is_preorder": 1,
                "product_preorder": {
                  "duration_text": "30 Hari",
                  "duration_day": 30,
                  "duration_unit_code": 1,
                  "duration_unit_text": "Day",
                  "duration_value": 30
                },
                "product_cashback": "",
                "product_min_order": 1,
                "product_invenage_value": 9979,
                "product_switch_invenage": 1,
                "currency_rate": 1,
                "product_price_currency": 1,
                "product_image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/7/23/12299749/12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700",
                "product_all_images": "[{\"file_name\":\"12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700\",\"file_path\":\"product-1/2019/7/23/12299749\",\"status\":2}]",
                "product_notes": "",
                "product_quantity": 2,
                "product_menu_id": 5333641,
                "product_finsurance": 0,
                "product_fcancel_partial": 0,
                "product_shipment": [],
                "product_shipment_mapping": [],
                "product_cat_id": 753,
                "product_catalog_id": 0,
                "product_status": 1,
                "product_tracker_data": {
                  "attribution": "none/other",
                  "tracker_list_name": "none/other"
                },
                "product_category": "Buku / Novel \u0026 Sastra / Novel Terjemahan",
                "product_total_weight": 224,
                "product_total_weight_fmt": "224 gr",
                "purchase_protection_plan_data": {
                  "protection_available": false,
                  "protection_type_id": 0,
                  "protection_price_per_product": 0,
                  "protection_price": 0,
                  "protection_title": "",
                  "protection_subtitle": "",
                  "protection_link_text": "",
                  "protection_link_url": "",
                  "protection_opt_in": false,
                  "protection_checkbox_disabled": false
                },
                "product_variants": {
                  "parent_id": 0,
                  "default_child": 0,
                  "variant": [],
                  "children": [],
                  "is_enabled": false,
                  "stock": 0
                },
                "warehouse_id": 740525,
                "cashback_percentage": 0,
                "cashback_amount": 0,
                "is_blacklisted": false,
                "free_shipping": {
                  "eligible": false,
                  "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                }
              }
            ],
            "is_fulfillment_service": false,
            "warehouse": {
              "warehouse_id": 740525,
              "partner_id": 0,
              "shop_id": 1479278,
              "warehouse_name": "Shop Location",
              "district_id": 2267,
              "district_name": "Pancoran",
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_id": 13,
              "province_name": "DKI Jakarta",
              "status": 1,
              "postal_code": "12750",
              "is_default": 1,
              "latlon": "-6.2565354,106.85040979999997",
              "latitude": "-6.2565354",
              "longitude": "106.85040979999997",
              "email": "",
              "address_detail": "Taman Pahlawan, Pancoran, 12750",
              "country_name": "Indonesia",
              "is_fulfillment": false,
              "tkpd_preferred_logistic_spid": []
            },
            "has_promo_list": false,
            "save_state_flag": false,
            "vehicle_leasing": {
              "application_id": 0,
              "dp_price": 0,
              "booking_fee": 0,
              "total_price": 0,
              "product_id": 0,
              "dealer_id": 0,
              "multifinance_name": "",
              "is_leasing_product": false,
              "is_allow_checkout": false,
              "error_message": "",
              "original_price": 0
            },
            "rates_feature": {
              "ontime_delivery_guarantee": {
                "available": false,
                "duration": 0,
                "bom": {
                  "title": "",
                  "notes": "",
                  "url_detail": "",
                  "url_text": ""
                }
              }
            }
          }
        ]
      }
    ],
    "kero_token": "Tokopedia+Kero:pBd7GsNEfng8RYtFmJQxv0Oz+sM\u003d",
    "kero_discom_token": "Tokopedia+Kero:cUFp/U20S2+eXDhJvcr7MOCMafE\u003d",
    "kero_unix_time": 1580188919,
    "enable_partial_cancel": false,
    "donation": {
      "Title": "Donasi untuk Korban Banjir Jabodetabek",
      "Nominal": 2000,
      "Description": "Hingga 31 Januari 2020, donasi dari Toppers akan disalurkan oleh Rumah Zakat untuk membantu korban banjir Jabodetabek."
    },
    "is_one_click_shipment": 0,
    "is_robinhood": 1,
    "is_blackbox": 0,
    "promo_suggestion": {
      "cta": "",
      "cta_color": "",
      "is_visible": 0,
      "promo_code": "",
      "text": ""
    },
    "autoapply": {
      "success": false,
      "code": "",
      "is_coupon": 0,
      "discount_amount": 0,
      "title_description": "",
      "message_success": "",
      "promo_id": 0
    },
    "autoapply_v2": {
      "success": false,
      "message": {
        "state": "",
        "color": "",
        "text": ""
      },
      "code": "",
      "promo_code_id": "",
      "title_description": "",
      "is_coupon": 0,
      "discount_amount": "",
      "discount_amount_fmt": 0
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
      "eligible": false,
      "is_tiering": false,
      "range": {
        "min": 0,
        "max": 0
      },
      "message": {
        "title_text": "",
        "sub_text": "",
        "ticker_text": "",
        "tooltip_text": ""
      }
    },
    "autoapply_stack": {
      "global_success": false,
      "success": false,
      "message": {
        "state": "",
        "color": "",
        "text": ""
      },
      "promo_code_id": 0,
      "title_description": "",
      "discount_amount": 0,
      "cashback_amount": 0,
      "cashback_wallet_amount": 0,
      "cashback_advocate_referral_amount": 0,
      "cashback_voucher_description": "",
      "invoice_description": "",
      "gateway_id": "",
      "is_tokopedia_gerai": false,
      "is_coupon": 0,
      "coupon_description": "",
      "benefit_summary_info": {
        "final_benefit_text": "",
        "final_benefit_amount_str": "",
        "final_benefit_amount": 0
      },
      "clashing_info_detail": {
        "clash_message": "",
        "clash_reason": "",
        "is_clashed_promos": false,
        "options": []
      },
      "tracking_details": [],
      "benefit_details": [],
      "ticker_info": {
        "unique_id": "",
        "status_code": 0,
        "message": ""
      }
    },
    "global_coupon_attr": {
      "description": "Gunakan promo Tokopedia",
      "quantity_label": "3 Kupon"
    },
    "is_show_onboarding": false,
    "is_hide_courier_name": false,
    "is_ineligbile_promo_dialog_enabled": true,
    "is_macro_micro_insurance_enabled": false,
    "donation_checkbox_status": false,
    "tickers": [],
    "hashed_email": "4c338847bdcb1321794cdbd2d28b3fa1",
    "is_new_buyer": false,
    "disabled_features": [
        "dropshipper",
        "multi_address",
        "order_prioritas",
        "egold",
        "ppp",
        "donation"
    ],
    "addresses": {
      "active": "",
      "data": {}
    },
    "disabled_features_detail": {
      "disabled_multi_address_message": ""
    }
  }""".trimIndent()

val apiResponseSAFDisableFeatureAllNewBuyer = """{
    "errors": [],
    "error_code": 0,
    "is_multiple": 0,
    "is_coupon_active": 1,
    "is_one_tab_promo": false,
    "group_address": [
      {
        "errors": [],
        "user_address": {
          "address_id": 99367774,
          "address_name": "Kantor",
          "address": "jl prof dr satrio",
          "postal_code": "11720",
          "phone": "628755464540",
          "receiver_name": "Tester",
          "status": 2,
          "country": "Indonesia",
          "province_id": 13,
          "province_name": "DKI Jakarta",
          "city_id": 174,
          "city_name": "Jakarta Barat",
          "district_id": 2253,
          "district_name": "Cengkareng",
          "address_2": "-6.148635,106.735185",
          "latitude": "-6.148635",
          "longitude": "106.735185",
          "corner_id": 0,
          "is_corner": false,
          "is_primary": true,
          "buyer_store_code": "",
          "type": 1
        },
        "group_shop": [
          {
            "errors": [],
            "group_key": "",
            "shop": {
              "shop_id": 1479278,
              "user_id": 12299749,
              "admin_ids": [],
              "shop_name": "Tumbler Starbucks 123",
              "shop_image": "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2018/10/19/1479278/1479278_fa306e59-f15f-4048-b2f7-8ddc5d798926.jpeg",
              "shop_url": "https://www.tokopedia.com/tumblersbux",
              "shop_status": 1,
              "is_gold": 1,
              "is_gold_badge": true,
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
              "address_id": 2267,
              "postal_code": "12750",
              "latitude": "-6.2565354",
              "longitude": "106.85040979999997",
              "district_id": 2267,
              "district_name": "",
              "origin": 0,
              "address_street": "",
              "province_id": 0,
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_name": "",
              "country_name": "",
              "is_allow_manage": false,
              "shop_domain": "",
              "is_bridestory": false
            },
            "cart_string": "1479278-30-740525-99367774",
            "shipping_id": 23,
            "sp_id": 45,
            "rates_id": 0,
            "dropshipper": {
              "name": "",
              "telp_no": ""
            },
            "is_insurance": false,
            "is_cod_available": false,
            "is_order_priority": false,
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 2,
                    "ship_prod_name": "OKE",
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 6,
                    "ship_prod_name": "YES",
                    "ship_group_name": "nextday",
                    "ship_group_id": 1003,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
              },
              {
                "ship_id": 10,
                "ship_name": "GO-JEK",
                "ship_code": "gojek",
                "ship_logo": "https://ecs7.tokopedia.net/img/kurir-gosend.png",
                "ship_prods": [
                  {
                    "ship_prod_id": 28,
                    "ship_prod_name": "Instant Courier",
                    "ship_group_name": "instant",
                    "ship_group_id": 1000,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 20,
                    "ship_prod_name": "Same Day",
                    "ship_group_name": "sameday",
                    "ship_group_id": 1002,
                    "additional_fee": 0,
                    "minimum_weight": 0
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 0
              },
              {
                "ship_id": 13,
                "ship_name": "Grab",
                "ship_code": "grab",
                "ship_logo": "https://ecs7.tokopedia.net/img/kurir-grab.png",
                "ship_prods": [
                  {
                    "ship_prod_id": 37,
                    "ship_prod_name": "Instant",
                    "ship_group_name": "instant",
                    "ship_group_id": 1000,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 24,
                    "ship_prod_name": "Same Day",
                    "ship_group_name": "sameday",
                    "ship_group_id": 1002,
                    "additional_fee": 0,
                    "minimum_weight": 0
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  },
                  {
                    "ship_prod_id": 46,
                    "ship_prod_name": "Next Day",
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
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
                    "ship_group_name": "regular",
                    "ship_group_id": 1004,
                    "additional_fee": 0,
                    "minimum_weight": 0
                  }
                ],
                "is_dropship_enabled": 1
              }
            ],
            "products": [
              {
                "errors": [],
                "cart_id": 1256701880,
                "product_id": 516791889,
                "product_alias": "",
                "parent_id": 0,
                "sku": "",
                "campaign_id": 0,
                "is_big_campaign": false,
                "product_name": "Halo Ini Nama Produk Yang Harus Panjang Banget Supaya Penuh Aja HEHEHE",
                "product_description": "Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi panjang: \u003c\u003d2000 karakter.Deskripsikan produk secara lengkap \u0026 jelas. Rekomendasi ",
                "product_price_fmt": "Rp12.300",
                "trade_in_info": {
                  "is_valid_trade_in": false,
                  "new_device_price": 0,
                  "new_device_price_fmt": "",
                  "old_device_price": 0,
                  "old_device_price_fmt": "",
                  "drop_off_enable": false
                },
                "product_price": 12300,
                "product_original_price": 0,
                "product_price_original_fmt": "",
                "is_slash_price": false,
                "wholesale_price": [],
                "product_wholesale_price": 12300,
                "product_wholesale_price_fmt": "Rp12.300",
                "product_weight_fmt": "112gr",
                "product_weight": 112,
                "product_condition": 1,
                "product_url": "https://www.tokopedia.com/tumblersbux/halo-ini-nama-produk-yang-harus-panjang-banget-supaya-penuh-aja-hehehe",
                "product_returnable": 0,
                "product_is_free_returns": 0,
                "free_returns": {
                  "is_freereturns": 0,
                  "free_returns_logo": ""
                },
                "product_is_preorder": 1,
                "product_preorder": {
                  "duration_text": "30 Hari",
                  "duration_day": 30,
                  "duration_unit_code": 1,
                  "duration_unit_text": "Day",
                  "duration_value": 30
                },
                "product_cashback": "",
                "product_min_order": 1,
                "product_invenage_value": 9979,
                "product_switch_invenage": 1,
                "currency_rate": 1,
                "product_price_currency": 1,
                "product_image_src_200_square": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/7/23/12299749/12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700",
                "product_all_images": "[{\"file_name\":\"12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700\",\"file_path\":\"product-1/2019/7/23/12299749\",\"status\":2}]",
                "product_notes": "",
                "product_quantity": 2,
                "product_menu_id": 5333641,
                "product_finsurance": 0,
                "product_fcancel_partial": 0,
                "product_shipment": [],
                "product_shipment_mapping": [],
                "product_cat_id": 753,
                "product_catalog_id": 0,
                "product_status": 1,
                "product_tracker_data": {
                  "attribution": "none/other",
                  "tracker_list_name": "none/other"
                },
                "product_category": "Buku / Novel \u0026 Sastra / Novel Terjemahan",
                "product_total_weight": 224,
                "product_total_weight_fmt": "224 gr",
                "purchase_protection_plan_data": {
                  "protection_available": false,
                  "protection_type_id": 0,
                  "protection_price_per_product": 0,
                  "protection_price": 0,
                  "protection_title": "",
                  "protection_subtitle": "",
                  "protection_link_text": "",
                  "protection_link_url": "",
                  "protection_opt_in": false,
                  "protection_checkbox_disabled": false
                },
                "product_variants": {
                  "parent_id": 0,
                  "default_child": 0,
                  "variant": [],
                  "children": [],
                  "is_enabled": false,
                  "stock": 0
                },
                "warehouse_id": 740525,
                "cashback_percentage": 0,
                "cashback_amount": 0,
                "is_blacklisted": false,
                "free_shipping": {
                  "eligible": false,
                  "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                }
              }
            ],
            "is_fulfillment_service": false,
            "warehouse": {
              "warehouse_id": 740525,
              "partner_id": 0,
              "shop_id": 1479278,
              "warehouse_name": "Shop Location",
              "district_id": 2267,
              "district_name": "Pancoran",
              "city_id": 175,
              "city_name": "Jakarta Selatan",
              "province_id": 13,
              "province_name": "DKI Jakarta",
              "status": 1,
              "postal_code": "12750",
              "is_default": 1,
              "latlon": "-6.2565354,106.85040979999997",
              "latitude": "-6.2565354",
              "longitude": "106.85040979999997",
              "email": "",
              "address_detail": "Taman Pahlawan, Pancoran, 12750",
              "country_name": "Indonesia",
              "is_fulfillment": false,
              "tkpd_preferred_logistic_spid": []
            },
            "has_promo_list": false,
            "save_state_flag": false,
            "vehicle_leasing": {
              "application_id": 0,
              "dp_price": 0,
              "booking_fee": 0,
              "total_price": 0,
              "product_id": 0,
              "dealer_id": 0,
              "multifinance_name": "",
              "is_leasing_product": false,
              "is_allow_checkout": false,
              "error_message": "",
              "original_price": 0
            },
            "rates_feature": {
              "ontime_delivery_guarantee": {
                "available": false,
                "duration": 0,
                "bom": {
                  "title": "",
                  "notes": "",
                  "url_detail": "",
                  "url_text": ""
                }
              }
            }
          }
        ]
      }
    ],
    "kero_token": "Tokopedia+Kero:pBd7GsNEfng8RYtFmJQxv0Oz+sM\u003d",
    "kero_discom_token": "Tokopedia+Kero:cUFp/U20S2+eXDhJvcr7MOCMafE\u003d",
    "kero_unix_time": 1580188919,
    "enable_partial_cancel": false,
    "donation": {
      "Title": "Donasi untuk Korban Banjir Jabodetabek",
      "Nominal": 2000,
      "Description": "Hingga 31 Januari 2020, donasi dari Toppers akan disalurkan oleh Rumah Zakat untuk membantu korban banjir Jabodetabek."
    },
    "is_one_click_shipment": 0,
    "is_robinhood": 1,
    "is_blackbox": 0,
    "promo_suggestion": {
      "cta": "",
      "cta_color": "",
      "is_visible": 0,
      "promo_code": "",
      "text": ""
    },
    "autoapply": {
      "success": false,
      "code": "",
      "is_coupon": 0,
      "discount_amount": 0,
      "title_description": "",
      "message_success": "",
      "promo_id": 0
    },
    "autoapply_v2": {
      "success": false,
      "message": {
        "state": "",
        "color": "",
        "text": ""
      },
      "code": "",
      "promo_code_id": "",
      "title_description": "",
      "is_coupon": 0,
      "discount_amount": "",
      "discount_amount_fmt": 0
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
      "eligible": false,
      "is_tiering": false,
      "range": {
        "min": 0,
        "max": 0
      },
      "message": {
        "title_text": "",
        "sub_text": "",
        "ticker_text": "",
        "tooltip_text": ""
      }
    },
    "autoapply_stack": {
      "global_success": false,
      "success": false,
      "message": {
        "state": "",
        "color": "",
        "text": ""
      },
      "promo_code_id": 0,
      "title_description": "",
      "discount_amount": 0,
      "cashback_amount": 0,
      "cashback_wallet_amount": 0,
      "cashback_advocate_referral_amount": 0,
      "cashback_voucher_description": "",
      "invoice_description": "",
      "gateway_id": "",
      "is_tokopedia_gerai": false,
      "is_coupon": 0,
      "coupon_description": "",
      "benefit_summary_info": {
        "final_benefit_text": "",
        "final_benefit_amount_str": "",
        "final_benefit_amount": 0
      },
      "clashing_info_detail": {
        "clash_message": "",
        "clash_reason": "",
        "is_clashed_promos": false,
        "options": []
      },
      "tracking_details": [],
      "benefit_details": [],
      "ticker_info": {
        "unique_id": "",
        "status_code": 0,
        "message": ""
      }
    },
    "global_coupon_attr": {
      "description": "Gunakan promo Tokopedia",
      "quantity_label": "3 Kupon"
    },
    "is_show_onboarding": false,
    "is_hide_courier_name": false,
    "is_ineligbile_promo_dialog_enabled": true,
    "is_macro_micro_insurance_enabled": false,
    "donation_checkbox_status": false,
    "tickers": [],
    "hashed_email": "4c338847bdcb1321794cdbd2d28b3fa1",
    "is_new_buyer": true,
    "disabled_features": [
        "dropshipper",
        "multi_address",
        "order_prioritas",
        "egold",
        "ppp",
        "donation"
    ],
    "addresses": {
      "active": "",
      "data": {}
    },
    "disabled_features_detail": {
      "disabled_multi_address_message": ""
    }
  }""".trimIndent()