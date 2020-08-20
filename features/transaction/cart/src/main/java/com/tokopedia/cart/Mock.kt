package com.tokopedia.cart

val RESPONSE = """
    {
      "status": "OK",
      "shop_group_simplified": {
        "status": "OK",
        "error_message": [],
        "data": {
          "errors": [],
          "empty_cart": {
            "image": "ecs7.tokopedia.com/................",
            "title": "Wah, keranjang belanjamu kosong",
            "description": "Daripada dianggurin,..............",
            "buttons": [
              {
                "id": "1",
                "code": "STARTSHOPPING",
                "message": "Mulai Belanja",
                "color": "green"
              }
            ]
          },
          "out_of_service": {
            "id": 1,
            "image": "ecs7.tokopedia.com/................",
            "title": "Waktunya kerja bakti!",
            "description": "Kami sedang bersih-bersih........",
            "buttons": [
              {
                "id": "1",
                "code": "RETRY",
                "message": "Coba Lagi",
                "color": "green"
              },
              {
                "id": "2",
                "code": "SETTING",
                "message": "Ke Pengaturan",
                "color": "white"
              }
            ]
          },
          "is_coupon_active": 0,
          "is_one_tab_promo": false,
          "max_quantity": 0,
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
          "shopping_summary": {
            "total_wording": "Total Harga (x barang)",
            "total_value": 22650000,
            "discount_total_wording": "Total Diskon Barang",
            "discount_value": 650000,
            "payment_total_wording": "Total Bayar",
            "promo_wording": "Hemat Pakai Promo",
            "promo_value": 50000,
            "seller_cashback_wording": "Cashback Penjual",
            "seller_cashback_value": 25000
          },
          "fulfillment_message": "Dilayani Tokopedia",
          "available_section": {
            "action": [
              {
                "id": "1",
                "code": "NOTIFY",
                "message": "Notify me when the stock is ready"
              },
              {
                "id": "2",
                "code": "DELETE",
                "message": "Hapus barang"
              },
              {
                "id": "3",
                "code": "WISHLIST",
                "message": "Pindahkan ke wishlist"
              },
              {
                "id": "4",
                "code": "NOTES",
                "message": "Catatan untuk penjual"
              },
              {
                "id": "5",
                "code": "SHOWLESS",
                "message": "Tampilkan Lebih Sedikit"
              },
              {
                "id": "6",
                "code": "SHOWMORE",
                "message": "Tampilkan Lebih Banyak"
              }
            ],
            "available_group": [
              {
                "user_address_id": 0,
                "shipment_information": {
                  "shop_location": "Jakarta Selatan",
                  "estimation": "Estimasi Tiba: 2 hari",
                  "free_shipping": {
                    "eligible": true,
                    "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                  },
                  "Preorder": {
                    "is_preorder": true,
                    "duration": "Pre order 3 hari"
                  }
                },
                "shop": {
                  "shop_alert_message": "FOR URGENCY NEEDS",
                  "shop_id": 479986,
                  "user_id": 5512189,
                  "admin_ids": [],
                  "shop_name": "Zelda OS Testing 01",
                  "shop_image": "https://ecs7.tokopedia.net/img/default_v3-shopnophoto.png",
                  "shop_url": "https://staging.tokopedia.com/zos1",
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
                  "address_id": 2270,
                  "postal_code": "12930",
                  "latitude": "-6.221180109754967",
                  "longitude": "106.81955066523437",
                  "district_id": 2270,
                  "district_name": "Setiabudi",
                  "origin": 2270,
                  "address_street": "Jalan Karet Sawah, Kecamatan Setiabudi, 12930",
                  "province_id": 13,
                  "city_id": 175,
                  "city_name": "Jakarta Selatan",
                  "province_name": "DKI Jakarta",
                  "country_name": "Indonesia",
                  "is_allow_manage": false,
                  "shop_domain": "zos1"
                },
                "cart_string": "479986-0-1886",
                "cart_details": [
                  {
                    "cart_id": 33376874,
                    "product": {
                      "variant_description_detail": {
                        "variant_name": [
                          "blue",
                          "23"
                        ],
                        "variant_description": "Varian: blue,23"
                      },
                      "product_id": 15262849,
                      "product_name": "Test vdoang",
                      "product_alias": "test-vdoang",
                      "parent_id": 0,
                      "variant": {
                        "parent_id": 0,
                        "is_parent": false,
                        "is_variant": false,
                        "children_id": 0
                      },
                      "sku": "",
                      "campaign_id": 0,
                      "is_big_campaign": false,
                      "initial_price": 0,
                      "initial_price_fmt": "",
                      "product_price_fmt": "Rp10.000",
                      "product_price": 10000,
                      "product_original_price": 0,
                      "product_price_original_fmt": "",
                      "slash_price_label": "50%",
                      "is_slash_price": false,
                      "category_id": 636,
                      "category": "Elektronik / Tool & Kit",
                      "catalog_id": 0,
                      "wholesale_price": [],
                      "product_weight_fmt": "1gr",
                      "product_condition": 1,
                      "product_status": 3,
                      "product_url": "https://staging.tokopedia.com//test-vdoang",
                      "product_returnable": 0,
                      "is_freereturns": 0,
                      "free_returns": {
                        "free_returns_logo": "https://ecs7.tokopedia.net/img/icon-frs.png"
                      },
                      "is_preorder": 0,
                      "product_cashback": "",
                      "product_cashback_value": 0,
                      "product_min_order": 1,
                      "product_max_order": 10000,
                      "product_rating": 0,
                      "product_invenage_value": 2,
                      "product_switch_invenage": 1,
                      "product_warning_message": "sisa 3",
                      "product_alert_message": "FOR URGENCY NEEDS",
                      "currency_rate": 1,
                      "product_price_currency": 1,
                      "product_image": {
                        "image_src": "https://cdn-staging.tokopedia.com/img/cache/700/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                        "image_src_100_square": "https://cdn-staging.tokopedia.com/img/cache/100-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                        "image_src_200_square": "https://cdn-staging.tokopedia.com/img/cache/200-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                        "image_src_300": "https://cdn-staging.tokopedia.com/img/cache/300/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                        "image_src_square": "https://cdn-staging.tokopedia.com/img/cache/500-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919"
                      },
                      "product_all_images": "[{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919\",\"status\":2},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_417e3582-9671-46b4-af26-2c61d0051444_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_8854f7d1-ab75-4b9c-ba15-0a88b6ae27b1_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_345508de-2bfe-4bbd-90f6-7ce86f436f41_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_33a77436-9623-4ceb-93d6-7af16a035eb6_1919_1919\",\"status\":1}]",
                      "product_notes": "",
                      "product_quantity": 7,
                      "price_changes": {
                        "changes_state": 0,
                        "amount_difference": 0,
                        "original_amount": 0,
                        "description": ""
                      },
                      "product_weight": 1,
                      "product_weight_unit_code": 1,
                      "product_weight_unit_text": "gr",
                      "last_update_price": 1569480222,
                      "is_update_price": false,
                      "product_preorder": {},
                      "product_showcase": {
                        "name": "testing",
                        "id": 1405133
                      },
                      "product_finsurance": 1,
                      "product_shop_id": 480829,
                      "is_wishlisted": false,
                      "product_tracker_data": {
                        "attribution": "none/other",
                        "tracker_list_name": "none/other"
                      },
                      "is_ppp": false,
                      "is_cod": false,
                      "warehouse_id": 96,
                      "warehouses": {
                        "96": {
                          "warehouseID": 96,
                          "product_price": 10000,
                          "price_currency": 1,
                          "price_currency_name": "IDR",
                          "product_price_idr": 10000,
                          "last_update_price": {
                            "unix": 1569505422,
                            "yyyymmddhhmmss": ""
                          },
                          "product_switch_invenage": 1,
                          "product_invenage_value": 2
                        }
                      },
                      "is_parent": false,
                      "is_campaign_error": false,
                      "campaign_type_name": "",
                      "hide_gimmick": false,
                      "is_blacklisted": false,
                      "categories": [
                        {
                          "category_id": 60,
                          "category_name": "Elektronik"
                        },
                        {
                          "category_id": 636,
                          "category_name": "Tool & Kit"
                        }
                      ],
                      "free_shipping": {
                        "eligible": false,
                        "badge_url": ""
                      }
                    },
                    "errors": [],
                    "messages": [],
                    "checkbox_state": true,
                    "similar_product_url": "tokopedia://rekomendasi/15262849?ref=cart",
                    "similar_product": {
                      "text": "Lihat Produk Serupa",
                      "url": "tokopedia://rekomendasi/15262849?ref=cart"
                    }
                  }
                ],
                "total_cart_details_error": 1,
                "total_cart_price": 2400000,
                "errors": [],
                "sort_key": 33388925,
                "is_fulfillment_service": false,
                "warehouse": {
                  "warehouse_id": 1886,
                  "partner_id": 0,
                  "shop_id": 479986,
                  "warehouse_name": "Shop location",
                  "district_id": 2270,
                  "district_name": "Setiabudi",
                  "city_id": 175,
                  "city_name": "Jakarta Selatan",
                  "province_id": 13,
                  "province_name": "DKI Jakarta",
                  "status": 1,
                  "postal_code": "12930",
                  "is_default": 1,
                  "latlon": "-6.221180109754967,106.81955066523437",
                  "latitude": "-6.221180109754967",
                  "longitude": "106.81955066523437",
                  "email": "",
                  "address_detail": "Jalan Karet Sawah, Kecamatan Setiabudi, 12930",
                  "country_name": "Indonesia",
                  "is_fulfillment": false,
                  "tkpd_preferred_logistic_spid": []
                },
                "checkbox_state": true
              },
              {
                "user_address_id": 0,
                "shipment_information": {
                  "shop_location": "Jakarta Selatan",
                  "estimation": "Estimasi Tiba: 2 hari",
                  "is_free_shipping": true
                },
                "shop": {
                  "shop_alert_message": "FOR URGENCY NEEDS",
                  "shop_id": 480138,
                  "user_id": 5512941,
                  "admin_ids": [],
                  "shop_name": "Jualan di staging",
                  "shop_image": "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2017/9/19/5512941/5512941_d1667bf4-9b51-4d56-a8fc-444d8628cb49.jpg",
                  "shop_url": "https://staging.tokopedia.com/jualands",
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
                  "address_id": 2270,
                  "postal_code": "12950",
                  "latitude": "-6.221308097583197",
                  "longitude": "106.82650295100098",
                  "district_id": 2270,
                  "district_name": "Setiabudi",
                  "origin": 2270,
                  "address_street": "Jalan Pedurenan Mesjid Raya, Kecamatan Setiabudi, 12940",
                  "province_id": 13,
                  "city_id": 175,
                  "city_name": "Jakarta Selatan",
                  "province_name": "DKI Jakarta",
                  "country_name": "Indonesia",
                  "is_allow_manage": false,
                  "shop_domain": "jualands"
                },
                "cart_string": "480138-0-2003",
                "cart_details": [
                  {
                    "cart_id": 33376874,
                    "product": {
                      "variant_description_detail": {
                        "variant_name": [
                          "blue",
                          "23"
                        ],
                        "variant_description": "Varian: blue,23"
                      },
                      "product_id": 15262849,
                      "product_name": "Test vdoang",
                      "product_alias": "test-vdoang",
                      "parent_id": 0,
                      "variant": {
                        "parent_id": 0,
                        "is_parent": false,
                        "is_variant": false,
                        "children_id": 0
                      },
                      "sku": "",
                      "campaign_id": 0,
                      "is_big_campaign": false,
                      "initial_price": 0,
                      "initial_price_fmt": "",
                      "product_price_fmt": "Rp10.000",
                      "product_price": 10000,
                      "product_original_price": 0,
                      "product_price_original_fmt": "",
                      "slash_price_label": "50%",
                      "is_slash_price": false,
                      "category_id": 636,
                      "category": "Elektronik / Tool & Kit",
                      "catalog_id": 0,
                      "wholesale_price": [],
                      "product_weight_fmt": "1gr",
                      "product_condition": 1,
                      "product_status": 3,
                      "product_url": "https://staging.tokopedia.com//test-vdoang",
                      "product_returnable": 0,
                      "is_freereturns": 0,
                      "free_returns": {
                        "free_returns_logo": "https://ecs7.tokopedia.net/img/icon-frs.png"
                      },
                      "is_preorder": 0,
                      "product_cashback": "",
                      "product_cashback_value": 0,
                      "product_min_order": 1,
                      "product_max_order": 10000,
                      "product_rating": 0,
                      "product_invenage_value": 2,
                      "product_switch_invenage": 1,
                      "product_warning_message": "sisa 3",
                      "product_alert_message": "FOR URGENCY NEEDS",
                      "currency_rate": 1,
                      "product_price_currency": 1,
                      "product_image": {
                        "image_src": "https://cdn-staging.tokopedia.com/img/cache/700/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                        "image_src_100_square": "https://cdn-staging.tokopedia.com/img/cache/100-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                        "image_src_200_square": "https://cdn-staging.tokopedia.com/img/cache/200-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                        "image_src_300": "https://cdn-staging.tokopedia.com/img/cache/300/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                        "image_src_square": "https://cdn-staging.tokopedia.com/img/cache/500-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919"
                      },
                      "product_all_images": "[{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919\",\"status\":2},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_417e3582-9671-46b4-af26-2c61d0051444_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_8854f7d1-ab75-4b9c-ba15-0a88b6ae27b1_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_345508de-2bfe-4bbd-90f6-7ce86f436f41_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_33a77436-9623-4ceb-93d6-7af16a035eb6_1919_1919\",\"status\":1}]",
                      "product_notes": "",
                      "product_quantity": 7,
                      "price_changes": {
                        "changes_state": 0,
                        "amount_difference": 0,
                        "original_amount": 0,
                        "description": ""
                      },
                      "product_weight": 1,
                      "product_weight_unit_code": 1,
                      "product_weight_unit_text": "gr",
                      "last_update_price": 1569480222,
                      "is_update_price": false,
                      "product_preorder": {},
                      "product_showcase": {
                        "name": "testing",
                        "id": 1405133
                      },
                      "product_finsurance": 1,
                      "product_shop_id": 480829,
                      "is_wishlisted": false,
                      "product_tracker_data": {
                        "attribution": "none/other",
                        "tracker_list_name": "none/other"
                      },
                      "is_ppp": false,
                      "is_cod": false,
                      "warehouse_id": 96,
                      "warehouses": {
                        "96": {
                          "warehouseID": 96,
                          "product_price": 10000,
                          "price_currency": 1,
                          "price_currency_name": "IDR",
                          "product_price_idr": 10000,
                          "last_update_price": {
                            "unix": 1569505422,
                            "yyyymmddhhmmss": ""
                          },
                          "product_switch_invenage": 1,
                          "product_invenage_value": 2
                        }
                      },
                      "is_parent": false,
                      "is_campaign_error": false,
                      "campaign_type_name": "",
                      "hide_gimmick": false,
                      "is_blacklisted": false,
                      "categories": [
                        {
                          "category_id": 60,
                          "category_name": "Elektronik"
                        },
                        {
                          "category_id": 636,
                          "category_name": "Tool & Kit"
                        }
                      ],
                      "free_shipping": {
                        "eligible": false,
                        "badge_url": ""
                      }
                    },
                    "errors": [],
                    "messages": [],
                    "checkbox_state": true,
                    "similar_product_url": "tokopedia://rekomendasi/15262849?ref=cart",
                    "similar_product": {
                      "text": "Lihat Produk Serupa",
                      "url": "tokopedia://rekomendasi/15262849?ref=cart"
                    }
                  }
                ],
                "total_cart_details_error": 1,
                "total_cart_price": 3000,
                "errors": [],
                "sort_key": 33388830,
                "is_fulfillment_service": false,
                "warehouse": {
                  "warehouse_id": 2003,
                  "partner_id": 0,
                  "shop_id": 480138,
                  "warehouse_name": "Shop location",
                  "district_id": 2270,
                  "district_name": "Setiabudi",
                  "city_id": 175,
                  "city_name": "Jakarta Selatan",
                  "province_id": 13,
                  "province_name": "DKI Jakarta",
                  "status": 1,
                  "postal_code": "12950",
                  "is_default": 1,
                  "latlon": "-6.221308097583197,106.82650295100098",
                  "latitude": "-6.221308097583197",
                  "longitude": "106.82650295100098",
                  "email": "",
                  "address_detail": "Jalan Pedurenan Mesjid Raya, Kecamatan Setiabudi, 12940",
                  "country_name": "Indonesia",
                  "is_fulfillment": false,
                  "tkpd_preferred_logistic_spid": []
                },
                "checkbox_state": true
              }
            ]
          },
          "unavailable_ticker": "Terdapat {{NumberOfProducts}} bermasalah dalam keranjang",
          "unavailable_section": [
            {
              "title": "Barang Dilarang",
              "unavailable_description": "Tidak bisa dibeli di ios dan android",
              "action": [
                {
                  "id": 1,
                  "code": "NOTIFY",
                  "message": "Notify me when the stock is ready"
                },
                {
                  "id": 2,
                  "code": "DELETE",
                  "message": "Hapus barang"
                },
                {
                  "id": 3,
                  "code": "CHECKOUTBROWSER",
                  "message": "Checkout di browser"
                }
              ],
              "unavailable_group": [
                {
                  "user_address_id": 0,
                  "shipment_information": {
                    "shop_location": "",
                    "estimation": "",
                    "is_free_shipping": false
                  },
                  "shop": {
                    "shop_id": 480829,
                    "user_id": 5510908,
                    "admin_ids": [],
                    "shop_name": "mattleeshoppe",
                    "shop_image": "https://ecs7.tokopedia.net/img/default_v3-shopnophoto.png",
                    "shop_url": "https://staging.tokopedia.com/mattleeshoppe",
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
                    "address_id": 1,
                    "postal_code": "123456",
                    "latitude": "123456",
                    "longitude": "7890",
                    "district_id": 1,
                    "district_name": "Kaway XVI",
                    "origin": 1,
                    "address_street": "Alamat ini milik Dragon",
                    "province_id": 1,
                    "city_id": 1,
                    "city_name": "Kab. Aceh Barat",
                    "province_name": "D.I. Aceh",
                    "country_name": "Indonesia",
                    "is_allow_manage": false,
                    "shop_domain": "mattleeshoppe"
                  },
                  "cart_string": "480829-0-96",
                  "cart_details": [
                    {
                      "cart_id": 33376874,
                      "product": {
                        "variant_description_detail": {
                          "variant_name": [
                            "blue",
                            "23"
                          ],
                          "variant_description": "Varian: blue,23"
                        },
                        "product_id": 15262849,
                        "product_name": "Test vdoang",
                        "product_alias": "test-vdoang",
                        "parent_id": 0,
                        "variant": {
                          "parent_id": 0,
                          "is_parent": false,
                          "is_variant": false,
                          "children_id": 0
                        },
                        "sku": "",
                        "campaign_id": 0,
                        "is_big_campaign": false,
                        "initial_price": 0,
                        "initial_price_fmt": "",
                        "product_price_fmt": "Rp10.000",
                        "product_price": 10000,
                        "product_original_price": 0,
                        "product_price_original_fmt": "",
                        "slash_price_label": "50%",
                        "is_slash_price": false,
                        "category_id": 636,
                        "category": "Elektronik / Tool & Kit",
                        "catalog_id": 0,
                        "wholesale_price": [],
                        "product_weight_fmt": "1gr",
                        "product_condition": 1,
                        "product_status": 3,
                        "product_url": "https://staging.tokopedia.com//test-vdoang",
                        "product_returnable": 0,
                        "is_freereturns": 0,
                        "free_returns": {
                          "free_returns_logo": "https://ecs7.tokopedia.net/img/icon-frs.png"
                        },
                        "is_preorder": 0,
                        "product_cashback": "",
                        "product_cashback_value": 0,
                        "product_min_order": 1,
                        "product_max_order": 10000,
                        "product_rating": 0,
                        "product_invenage_value": 2,
                        "product_switch_invenage": 1,
                        "product_warning_message": "sisa 3",
                        "product_alert_message": "FOR URGENCY NEEDS",
                        "currency_rate": 1,
                        "product_price_currency": 1,
                        "product_image": {
                          "image_src": "https://cdn-staging.tokopedia.com/img/cache/700/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                          "image_src_100_square": "https://cdn-staging.tokopedia.com/img/cache/100-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                          "image_src_200_square": "https://cdn-staging.tokopedia.com/img/cache/200-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                          "image_src_300": "https://cdn-staging.tokopedia.com/img/cache/300/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                          "image_src_square": "https://cdn-staging.tokopedia.com/img/cache/500-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919"
                        },
                        "product_all_images": "[{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919\",\"status\":2},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_417e3582-9671-46b4-af26-2c61d0051444_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_8854f7d1-ab75-4b9c-ba15-0a88b6ae27b1_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_345508de-2bfe-4bbd-90f6-7ce86f436f41_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_33a77436-9623-4ceb-93d6-7af16a035eb6_1919_1919\",\"status\":1}]",
                        "product_notes": "",
                        "product_quantity": 7,
                        "price_changes": {
                          "changes_state": 0,
                          "amount_difference": 0,
                          "original_amount": 0,
                          "description": ""
                        },
                        "product_weight": 1,
                        "product_weight_unit_code": 1,
                        "product_weight_unit_text": "gr",
                        "last_update_price": 1569480222,
                        "is_update_price": false,
                        "product_preorder": {},
                        "product_showcase": {
                          "name": "testing",
                          "id": 1405133
                        },
                        "product_finsurance": 1,
                        "product_shop_id": 480829,
                        "is_wishlisted": false,
                        "product_tracker_data": {
                          "attribution": "none/other",
                          "tracker_list_name": "none/other"
                        },
                        "is_ppp": false,
                        "is_cod": false,
                        "warehouse_id": 96,
                        "warehouses": {
                          "96": {
                            "warehouseID": 96,
                            "product_price": 10000,
                            "price_currency": 1,
                            "price_currency_name": "IDR",
                            "product_price_idr": 10000,
                            "last_update_price": {
                              "unix": 1569505422,
                              "yyyymmddhhmmss": ""
                            },
                            "product_switch_invenage": 1,
                            "product_invenage_value": 2
                          }
                        },
                        "is_parent": false,
                        "is_campaign_error": false,
                        "campaign_type_name": "",
                        "hide_gimmick": false,
                        "is_blacklisted": false,
                        "categories": [
                          {
                            "category_id": 60,
                            "category_name": "Elektronik"
                          },
                          {
                            "category_id": 636,
                            "category_name": "Tool & Kit"
                          }
                        ],
                        "free_shipping": {
                          "eligible": false,
                          "badge_url": ""
                        }
                      },
                      "errors": [
                        "Stok barang ini kosong."
                      ],
                      "messages": [],
                      "checkbox_state": true,
                      "similar_product_url": "tokopedia://rekomendasi/15262849?ref=cart",
                      "similar_product": {
                        "text": "Lihat Produk Serupa",
                        "url": "tokopedia://rekomendasi/15262849?ref=cart"
                      }
                    }
                  ],
                  "total_cart_details_error": 1,
                  "total_cart_price": 0,
                  "errors": [],
                  "sort_key": 33376874,
                  "is_fulfillment_service": false,
                  "warehouse": {
                    "warehouse_id": 96,
                    "partner_id": 0,
                    "shop_id": 480829,
                    "warehouse_name": "Shop Location",
                    "district_id": 1,
                    "district_name": "Kaway XVI",
                    "city_id": 1,
                    "city_name": "Kab. Aceh Barat",
                    "province_id": 1,
                    "province_name": "D.I. Aceh",
                    "status": 1,
                    "postal_code": "123456",
                    "is_default": 1,
                    "latlon": "123456,7890",
                    "latitude": "123456",
                    "longitude": "7890",
                    "email": "",
                    "address_detail": "Alamat ini milik Dragon",
                    "country_name": "Indonesia",
                    "is_fulfillment": false,
                    "tkpd_preferred_logistic_spid": []
                  },
                  "checkbox_state": true
                }
              ]
            },
            {
              "title": "Toko Tutup",
              "unavailable_description": "",
              "action": [
                {
                  "id": 1,
                  "code": "NOTIFY",
                  "message": "Notify me when the stock is ready"
                },
                {
                  "id": 2,
                  "code": "DELETE",
                  "message": "Hapus barang"
                },
                {
                  "id": 3,
                  "code": "CHECKOUTBROWSER",
                  "message": "Checkout di browser"
                }
              ],
              "unavailable_group": [
                {
                  "user_address_id": 0,
                  "shipment_information": {
                    "shop_location": "",
                    "estimation": "",
                    "is_free_shipping": false
                  },
                  "shop": {
                    "shop_id": 480829,
                    "user_id": 5510908,
                    "admin_ids": [],
                    "shop_name": "mattleeshoppe",
                    "shop_image": "https://ecs7.tokopedia.net/img/default_v3-shopnophoto.png",
                    "shop_url": "https://staging.tokopedia.com/mattleeshoppe",
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
                    "address_id": 1,
                    "postal_code": "123456",
                    "latitude": "123456",
                    "longitude": "7890",
                    "district_id": 1,
                    "district_name": "Kaway XVI",
                    "origin": 1,
                    "address_street": "Alamat ini milik Dragon",
                    "province_id": 1,
                    "city_id": 1,
                    "city_name": "Kab. Aceh Barat",
                    "province_name": "D.I. Aceh",
                    "country_name": "Indonesia",
                    "is_allow_manage": false,
                    "shop_domain": "mattleeshoppe"
                  },
                  "cart_string": "480829-0-96",
                  "cart_details": [
                    {
                      "cart_id": 33376874,
                      "product": {
                        "variant_description_detail": {
                          "variant_name": [
                            "blue",
                            "23"
                          ],
                          "variant_description": "Varian: blue,23"
                        },
                        "product_id": 15262849,
                        "product_name": "Test vdoang",
                        "product_alias": "test-vdoang",
                        "parent_id": 0,
                        "variant": {
                          "parent_id": 0,
                          "is_parent": false,
                          "is_variant": false,
                          "children_id": 0
                        },
                        "sku": "",
                        "campaign_id": 0,
                        "is_big_campaign": false,
                        "initial_price": 0,
                        "initial_price_fmt": "",
                        "product_price_fmt": "Rp10.000",
                        "product_price": 10000,
                        "product_original_price": 0,
                        "product_price_original_fmt": "",
                        "slash_price_label": "50%",
                        "is_slash_price": false,
                        "category_id": 636,
                        "category": "Elektronik / Tool & Kit",
                        "catalog_id": 0,
                        "wholesale_price": [],
                        "product_weight_fmt": "1gr",
                        "product_condition": 1,
                        "product_status": 3,
                        "product_url": "https://staging.tokopedia.com//test-vdoang",
                        "product_returnable": 0,
                        "is_freereturns": 0,
                        "free_returns": {
                          "free_returns_logo": "https://ecs7.tokopedia.net/img/icon-frs.png"
                        },
                        "is_preorder": 0,
                        "product_cashback": "",
                        "product_cashback_value": 0,
                        "product_min_order": 1,
                        "product_max_order": 10000,
                        "product_rating": 0,
                        "product_invenage_value": 2,
                        "product_switch_invenage": 1,
                        "product_warning_message": "sisa 3",
                        "product_alert_message": "FOR URGENCY NEEDS",
                        "currency_rate": 1,
                        "product_price_currency": 1,
                        "product_image": {
                          "image_src": "https://cdn-staging.tokopedia.com/img/cache/700/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                          "image_src_100_square": "https://cdn-staging.tokopedia.com/img/cache/100-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                          "image_src_200_square": "https://cdn-staging.tokopedia.com/img/cache/200-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                          "image_src_300": "https://cdn-staging.tokopedia.com/img/cache/300/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919",
                          "image_src_square": "https://cdn-staging.tokopedia.com/img/cache/500-square/product-1/2019/9/26/5510908/5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919"
                        },
                        "product_all_images": "[{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_9aae54ab-e648-4baf-b28c-819b0128b63f_1919_1919\",\"status\":2},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_417e3582-9671-46b4-af26-2c61d0051444_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_8854f7d1-ab75-4b9c-ba15-0a88b6ae27b1_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_345508de-2bfe-4bbd-90f6-7ce86f436f41_1919_1919\",\"status\":1},{\"file_path\":\"product-1/2019/9/26/5510908\",\"file_name\":\"5510908_33a77436-9623-4ceb-93d6-7af16a035eb6_1919_1919\",\"status\":1}]",
                        "product_notes": "",
                        "product_quantity": 7,
                        "price_changes": {
                          "changes_state": 0,
                          "amount_difference": 0,
                          "original_amount": 0,
                          "description": ""
                        },
                        "product_weight": 1,
                        "product_weight_unit_code": 1,
                        "product_weight_unit_text": "gr",
                        "last_update_price": 1569480222,
                        "is_update_price": false,
                        "product_preorder": {},
                        "product_showcase": {
                          "name": "testing",
                          "id": 1405133
                        },
                        "product_finsurance": 1,
                        "product_shop_id": 480829,
                        "is_wishlisted": false,
                        "product_tracker_data": {
                          "attribution": "none/other",
                          "tracker_list_name": "none/other"
                        },
                        "is_ppp": false,
                        "is_cod": false,
                        "warehouse_id": 96,
                        "warehouses": {
                          "96": {
                            "warehouseID": 96,
                            "product_price": 10000,
                            "price_currency": 1,
                            "price_currency_name": "IDR",
                            "product_price_idr": 10000,
                            "last_update_price": {
                              "unix": 1569505422,
                              "yyyymmddhhmmss": ""
                            },
                            "product_switch_invenage": 1,
                            "product_invenage_value": 2
                          }
                        },
                        "is_parent": false,
                        "is_campaign_error": false,
                        "campaign_type_name": "",
                        "hide_gimmick": false,
                        "is_blacklisted": false,
                        "categories": [
                          {
                            "category_id": 60,
                            "category_name": "Elektronik"
                          },
                          {
                            "category_id": 636,
                            "category_name": "Tool & Kit"
                          }
                        ],
                        "free_shipping": {
                          "eligible": true,
                          "badge_url": "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
                        }
                      },
                      "errors": [
                        "Stok barang ini kosong."
                      ],
                      "messages": [],
                      "checkbox_state": true,
                      "similar_product_url": "tokopedia://rekomendasi/15262849?ref=cart",
                      "similar_product": {
                        "text": "Lihat Produk Serupa",
                        "url": "tokopedia://rekomendasi/15262849?ref=cart"
                      }
                    }
                  ],
                  "total_cart_details_error": 1,
                  "total_cart_price": 0,
                  "errors": [],
                  "sort_key": 33376874,
                  "is_fulfillment_service": false,
                  "warehouse": {
                    "warehouse_id": 96,
                    "partner_id": 0,
                    "shop_id": 480829,
                    "warehouse_name": "Shop Location",
                    "district_id": 1,
                    "district_name": "Kaway XVI",
                    "city_id": 1,
                    "city_name": "Kab. Aceh Barat",
                    "province_id": 1,
                    "province_name": "D.I. Aceh",
                    "status": 1,
                    "postal_code": "123456",
                    "is_default": 1,
                    "latlon": "123456,7890",
                    "latitude": "123456",
                    "longitude": "7890",
                    "email": "",
                    "address_detail": "Alamat ini milik Dragon",
                    "country_name": "Indonesia",
                    "is_fulfillment": false,
                    "tkpd_preferred_logistic_spid": []
                  },
                  "checkbox_state": true
                }
              ]
            }
          ],
          "total_product_price": 2403000,
          "total_product_count": 2,
          "total_product_error": 1,
          "global_coupon_attr": {
            "description": "Gunakan promo Tokopedia",
            "quantity_label": ""
          },
          "global_checkbox_state": false,
          "tickers": [],
          "hashed_email": "fe51cd30c27c5c660de629bd1c58a1aa",
          "promo": {
            "last_apply": {
              "code": "200000",
              "data": {
                "global_success": true,
                "success": true,
                "message": {
                  "state": "green",
                  "color": "#ade3af",
                  "text": "Yay, kamu dapat diskon Rp10.000!"
                },
                "codes": [
                  "DISC10TOPED"
                ],
                "promo_code_id": 0,
                "title_description": "Promo Testing Discount 10%",
                "discount_amount": 10000,
                "cashback_wallet_amount": 0,
                "cashback_advocate_referral_amount": 0,
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
                "voucher_orders": [
                  {
                    "code": "STACKCBMERCHANT",
                    "success": true,
                    "unique_id": "841188-0-447668-32509476",
                    "order_id": 0,
                    "shop_id": 12345,
                    "is_po": 0,
                    "duration": "0",
                    "warehouse_id": 447668,
                    "address_id": 32509476,
                    "type": "merchant",
                    "cashback_wallet_amount": 10000,
                    "discount_amount": 0,
                    "title_description": "STACKCBMERCHANT - utk test",
                    "invoice_description": "",
                    "message": {
                      "state": "green",
                      "color": "#ade3af",
                      "text": "Kamu berpotensi dapat cashback sebesar Rp10.000 setelah pesanan selesai"
                    },
                    "benefit_details": [
                      {
                        "code": "STACKCBMERCHANT",
                        "type": "",
                        "order_id": 1,
                        "unique_id": "841188-0-447668-32509476",
                        "discount_amount": 0,
                        "discount_details": [],
                        "cashback_amount": 10000,
                        "cashback_details": [
                          {
                            "amount_idr": 10000,
                            "amount_points": 10000,
                            "benefit_type": "cashback"
                          }
                        ],
                        "promo_type": {
                          "is_exclusive_shipping": false,
                          "is_bebas_ongkir": false
                        },
                        "benefit_product_details": [
                          {
                            "product_id": 72857852,
                            "cashback_amount": 0,
                            "cashback_amount_idr": 0,
                            "discount_amount": 0,
                            "is_bebas_ongkir": false
                          }
                        ]
                      }
                    ]
                  }
                ],
                "benefit_summary_info": {
                  "final_benefit_text": "Total benefit anda: ",
                  "final_benefit_amount_str": "Rp20.000",
                  "final_benefit_amount": 20000,
                  "summaries": [
                    {
                      "section_name": "",
                      "section_description": "",
                      "description": "Total cashback: ",
                      "type": "cashback",
                      "amount_str": "Rp10.000",
                      "amount": 10000,
                      "details": [
                        {
                          "description": "Total Cashback OVO Points ",
                          "type": "ovo_points",
                          "amount_str": "Rp10.000",
                          "amount": 10000,
                          "section_name": "",
                          "points": 10000,
                          "points_str": ""
                        }
                      ]
                    },
                    {
                      "section_name": "",
                      "section_description": "",
                      "description": "Total diskon: ",
                      "type": "discount",
                      "amount_str": "Rp10.000",
                      "amount": 10000,
                      "details": [
                        {
                          "description": "Total Diskon Barang ",
                          "type": "product_discount",
                          "amount_str": "Rp10.000",
                          "amount": 10000,
                          "section_name": "",
                          "points": 0,
                          "points_str": ""
                        }
                      ]
                    }
                  ]
                },
                "benefit_details": [
                  {
                    "code": "DISC10TOPED",
                    "type": "",
                    "order_id": 1,
                    "unique_id": "841188-0-447668-32509476",
                    "discount_amount": 5714,
                    "discount_details": [
                      {
                        "amount": 5714,
                        "data_type": "total_product_price"
                      }
                    ],
                    "cashback_amount": 0,
                    "cashback_details": [],
                    "promo_type": {
                      "is_exclusive_shipping": false,
                      "is_bebas_ongkir": false
                    },
                    "benefit_product_details": [
                      {
                        "product_id": 72857852,
                        "cashback_amount": 0,
                        "cashback_amount_idr": 0,
                        "discount_amount": 0,
                        "is_bebas_ongkir": false
                      }
                    ]
                  },
                  {
                    "code": "DISC10TOPED",
                    "type": "",
                    "order_id": 2,
                    "unique_id": "951922-0-3961147-32509476",
                    "discount_amount": 4286,
                    "discount_details": [
                      {
                        "amount": 4286,
                        "data_type": "total_product_price"
                      }
                    ],
                    "cashback_amount": 0,
                    "cashback_details": [],
                    "promo_type": {
                      "is_exclusive_shipping": false,
                      "is_bebas_ongkir": false
                    },
                    "benefit_product_details": [
                      {
                        "product_id": 51435326,
                        "cashback_amount": 0,
                        "cashback_amount_idr": 0,
                        "discount_amount": 0,
                        "is_bebas_ongkir": false
                      }
                    ]
                  }
                ],
                "tracking_details": [
                  {
                    "product_id": 72857852,
                    "promo_codes_tracking": "DISC103RDPARTY|STACKCBMERCHANT1",
                    "promo_details_tracking": "G:2:5714:green|M:1:10000:green"
                  },
                  {
                    "product_id": 51435326,
                    "promo_codes_tracking": "DISC103RDPARTY",
                    "promo_details_tracking": "G:2:4286:green"
                  }
                ],
                "ticker_info": {
                  "unique_id": "",
                  "status_code": 0,
                  "message": ""
                },
                "additional_info": {
                  "message_info": {
                    "message": "Kamu hemat Rp.20.000",
                    "detail": "2 promo dipakai"
                  },
                  "error_detail": {
                    "message": ""
                  },
                  "cart_empty_info": {
                    "image_url": "http://image_url",
                    "message": "Ada promo menunggumu, nih!",
                    "detail": "Ayo belanja min. Rp100.000 untuk pakai promo promo global title"
                  },
                  "sp_ids": [
                    1,
                    6,
                    18
                  ]
                }
              }
            }
          }
        },
        "error_reporter": null
      }
    }
""".trimIndent()