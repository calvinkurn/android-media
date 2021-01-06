package com.tokopedia.discovery2.data

/**
 * get channel detail error
 */
const val RESPONSE_MOCK_DISCOVERY_PAGE_DATA = """
{
  "data": {
    "discoveryPageInfo": {
      "data": {
        "title": "topads test 2",
        "components": [
          {
            "data": [
              {
                "background": "#ffffff",
                "params_mobile": "item=1&dep_id=642&src=intermediary&template_id=3&ep=headline&device=android",
                "title": "CPM",
                "title_color": "#ffffff",
                "url": ""
              }
            ],
            "id": 10200,
            "name": "topads",
            "render_by_default": true,
            "skiprender": false,
            "target": [],
            "title": ""
          },
          {
            "data": [
              {
                "background": "#ffffff",
                "size_desktop": "16",
                "size_mobile": "16"
              }
            ],
            "id": 6526,
            "name": "margin",
            "render_by_default": true,
            "skiprender": false,
            "target": [],
            "title": ""
          },
          {
            "creative_name": "",
            "data": [],
            "id": 34946,
            "name": "product_card_carousel",
            "properties": {
              "limit_number": 20,
              "limit_product": false
            },
            "render_by_default": true,
            "skiprender": false,
            "target": [],
            "title": ""
          },
          {
            "data": [
              {
                "background_image": "https://images.tokopedia.net/img/cache/480/attachment/2020/3/15/53558266/53558266_df954b0c-0f5f-41b0-a17d-7f7dec60294a.jpg.webp?width=480&height=172",
                "filter": "",
                "filter_value": "",
                "name": "Alat Tulis",
                "target_component_id": "35003",
                "text_color": "#ffffff"
              },
              {
                "background_image": "https://images.tokopedia.net/img/cache/480/attachment/2020/3/15/53558266/53558266_df954b0c-0f5f-41b0-a17d-7f7dec60294a.jpg.webp?width=480&height=172",
                "filter": "",
                "filter_value": "",
                "name": "Perlengkapan Menggambar",
                "target_component_id": "35004",
                "text_color": "#ffffff"
              },
              {
                "background_image": "https://images.tokopedia.net/img/cache/480/attachment/2020/3/15/53558266/53558266_df954b0c-0f5f-41b0-a17d-7f7dec60294a.jpg.webp?width=480&height=172",
                "filter": "",
                "filter_value": "",
                "name": "Office Kit",
                "target_component_id": "35005",
                "text_color": "#ffffff"
              }
            ],
            "id": 35001,
            "name": "tabs",
            "properties": {
              "background": "color",
              "dynamic": false,
              "sticky": false
            },
            "render_by_default": true,
            "skiprender": false,
            "target": [],
            "title": ""
          },
          {
            "data": [],
            "id": 35003,
            "name": "product_card_revamp",
            "render_by_default": false,
            "skiprender": false,
            "target": [],
            "title": ""
          },
          {
            "data": [],
            "id": 35004,
            "name": "product_card_revamp",
            "render_by_default": false,
            "skiprender": false,
            "target": [],
            "title": ""
          },
          {
            "data": [],
            "id": 35005,
            "name": "product_card_revamp",
            "render_by_default": false,
            "skiprender": false,
            "target": [],
            "title": ""
          }
        ],
        "page_info": {
          "Tags": "{\"categories\":{}}",
          "Id": 11346,
          "Identifier": "topads-test-2",
          "Name": "topads test 2",
          "Path": "discovery/topads-test-2",
          "Type": "category_intermediary",
          "TTL": 5,
          "meta_title": "Cek topads test 2 | Tokopedia",
          "meta_description": "Cek topads test 2! Belanja bebas ongkir dengan harga terbaik hanya di Tokopedia",
          "robot": "index, follow",
          "og_image": "https://ecs7.tokopedia.net/img/attachment/2019/5/9/22765891/22765891_454bf242-5c25-4158-a6f5-a0e711536974.jpg?width=600&height=314",
          "search_applink": "tokopedia://search-autocomplete",
          "search_url": "https://m.tokopedia.com/searchbox",
          "share_config": {
            "description": "Beli Produk Office & Stationery Berkualitas, Terbaru & Lengkap Dengan Harga Terbaik di Tokopedia. Belanja Cicilan Kredit 0%, & Pengiriman Cepat.",
            "enabled": true,
            "image": "https://ecs7.tokopedia.net/img/attachment/2019/5/9/22765891/22765891_454bf242-5c25-4158-a6f5-a0e711536974.jpg?width=600&height=314",
            "title": "Jual Aneka Alat Tulis Kantor - Harga ATK Terbaik | Tokopedia",
            "url": "https://www.tokopedia.com/discovery/topads-test-2"
          },
          "campaign_code": "",
          "search_title": "Cari di Tokopedia"
        },
        "additional_info": {}
      }
    }
  }
}
"""

/**
 * get channel detail error
 */
const val RESPONSE_MOCK_CAROUSEL_DATA = """
{
    "data": {
        "idx": "32",
        "component": {
            "name": "product_card_carousel",
            "target": [],
            "data": [
                {
                    "discount_percentage": 0,
                    "cashback": 0,
                    "shop_applink": "tokopedia://shop/7732992",
                    "url_mobile": "https://www.tokopedia.com/pedagangbermimpi/banh-trang-rice-paper-400gr-22inch-kulit-lumpia-vietnam-rice-paper",
                    "gold_merchant": true,
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/VqbcmM/2020/11/20/7c7c5861-5b0f-43b8-ab97-d5021ba1a421.jpg",
                    "department_id": 2731,
                    "labels": [
                        {
                            "title": "Gratis Ongkir",
                            "position": "promo",
                            "type": "lightGreen"
                        },
                        {
                            "type": "lightGreen",
                            "title": "Cashback",
                            "position": "promo"
                        },
                        {
                            "title": "Cashback",
                            "position": "price",
                            "type": "lightGreen"
                        },
                        {
                            "type": "textDarkGrey",
                            "title": "Terjual 1,6 rb",
                            "position": "integrity"
                        }
                    ],
                    "shop_url_mobile": "https://www.tokopedia.com/pedagangbermimpi",
                    "price": "Rp 43.000",
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=2731&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=826846477&pos=1&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/pedagangbermimpi/banh-trang-rice-paper-400gr-22inch-kulit-lumpia-vietnam-rice-paper?catid2731=%26ref=default_pa1_po1%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    },
                    "product_id": 826846477,
                    "is_topads": false,
                    "shop_name": "PedagangBermimpiBesar777",
                    "shop_location": "Jakarta Barat",
                    "rating_average": "4.9",
                    "official_store": false,
                    "url_desktop": "https://www.tokopedia.com/pedagangbermimpi/banh-trang-rice-paper-400gr-22inch-kulit-lumpia-vietnam-rice-paper",
                    "count_review": 274,
                    "name": "Banh Trang Rice Paper 400gr 22inch | Kulit Lumpia Vietnam | Rice Paper",
                    "applink": "tokopedia://product/826846477",
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/7732992",
                    "discounted_price": "",
                    "shop_url_desktop": "https://www.tokopedia.com/pedagangbermimpi",
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/VqbcmM/2020/11/20/7c7c5861-5b0f-43b8-ab97-d5021ba1a421.jpg",
                    "rating": 5
                },
                {
                    "department_id": 2753,
                    "gold_merchant": true,
                    "count_review": 234,
                    "shop_url_mobile": "https://www.tokopedia.com/granovagranola",
                    "shop_name": "Granova Granola",
                    "price": "Rp 16.119",
                    "url_mobile": "https://www.tokopedia.com/granovagranola/organic-premium-chia-seed-halal-100g",
                    "shop_url_desktop": "https://www.tokopedia.com/granovagranola",
                    "product_id": 662602576,
                    "official_store": true,
                    "discounted_price": "Rp 19.900",
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=2753&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=662602576&pos=2&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/granovagranola/organic-premium-chia-seed-halal-100g?catid2753=%26ref=default_pa1_po2%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    },
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/1170227",
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/attachment/2020/8/14/66236640/66236640_6e451fe5-ac32-43a6-8c89-fd3830602eb9.jpg",
                    "discount_percentage": 19,
                    "name": "ORGANIC PREMIUM CHIA SEED HALAL 100g",
                    "applink": "tokopedia://product/662602576",
                    "is_topads": false,
                    "rating": 5,
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/attachment/2020/8/14/66236640/66236640_6e451fe5-ac32-43a6-8c89-fd3830602eb9.jpg",
                    "cashback": 0,
                    "shop_location": "Kota Bandung",
                    "shop_applink": "tokopedia://shop/1170227",
                    "labels": [
                        {
                            "title": "Cashback",
                            "position": "promo",
                            "type": "lightGreen"
                        },
                        {
                            "title": "Terjual 728",
                            "position": "integrity",
                            "type": "textDarkGrey"
                        }
                    ],
                    "rating_average": "5.0",
                    "url_desktop": "https://www.tokopedia.com/granovagranola/organic-premium-chia-seed-halal-100g"
                },
                {
                    "url_desktop": "https://www.tokopedia.com/greenaraid/gojiberry-250gr-kiche-goji-berry",
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2020/9/17/40477907/40477907_ae005b6c-ab9b-4ba2-a7d3-16ca26ee2693_1080_1080",
                    "cashback": 0,
                    "price": "Rp 58.000",
                    "count_review": 123,
                    "shop_url_desktop": "https://www.tokopedia.com/greenaraid",
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2020/9/17/40477907/40477907_ae005b6c-ab9b-4ba2-a7d3-16ca26ee2693_1080_1080",
                    "shop_applink": "tokopedia://shop/4128311",
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=2705&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=423530416&pos=3&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/greenaraid/gojiberry-250gr-kiche-goji-berry?catid2705=%26ref=default_pa1_po3%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "product_id": 423530416,
                    "official_store": true,
                    "labels": [
                        {
                            "title": "Terjual 291",
                            "position": "integrity",
                            "type": "textDarkGrey"
                        }
                    ],
                    "is_topads": false,
                    "rating": 5,
                    "applink": "tokopedia://product/423530416",
                    "rating_average": "4.9",
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/4128311",
                    "shop_location": "Jakarta Utara",
                    "discount_percentage": 9,
                    "name": "Gojiberry 250gr / kiche / goji berry",
                    "discounted_price": "Rp 63.900",
                    "shop_name": "greenara.id",
                    "url_mobile": "https://www.tokopedia.com/greenaraid/gojiberry-250gr-kiche-goji-berry",
                    "department_id": 2705,
                    "gold_merchant": true,
                    "shop_url_mobile": "https://www.tokopedia.com/greenaraid",
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    }
                },
                {
                    "applink": "tokopedia://product/407231267",
                    "discount_percentage": 0,
                    "name": "Chunjang Jjajang Halal Pasta Kedelai Hitam 300g Saus jjajangmyeon",
                    "shop_location": "Jakarta Barat",
                    "gold_merchant": true,
                    "shop_applink": "tokopedia://shop/2491196",
                    "discounted_price": "",
                    "rating": 5,
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/VqbcmM/2020/11/3/3a1a9ef2-8b4b-477f-a90e-b4b604daf854.jpg",
                    "rating_average": "4.9",
                    "count_review": 367,
                    "shop_url_mobile": "https://www.tokopedia.com/kimchioppaya",
                    "shop_url_desktop": "https://www.tokopedia.com/kimchioppaya",
                    "free_ongkir": {
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png",
                        "is_active": true
                    },
                    "price": "Rp 35.000",
                    "labels": [
                        {
                            "title": "Cashback",
                            "position": "promo",
                            "type": "lightGreen"
                        },
                        {
                            "title": "Grosir",
                            "position": "offers",
                            "type": "lightGrey"
                        },
                        {
                            "title": "Cashback",
                            "position": "price",
                            "type": "lightGreen"
                        },
                        {
                            "type": "lightGreen",
                            "title": "Grosir",
                            "position": "price"
                        },
                        {
                            "title": "Terjual 1,9 rb",
                            "position": "integrity",
                            "type": "textDarkGrey"
                        }
                    ],
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/VqbcmM/2020/11/3/3a1a9ef2-8b4b-477f-a90e-b4b604daf854.jpg",
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=1162&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=407231267&pos=4&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/kimchioppaya/chunjang-jjajang-halal-pasta-kedelai-hitam-300g-saus-jjajangmyeon?catid1162=%26ref=default_pa1_po4%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "product_id": 407231267,
                    "department_id": 1162,
                    "url_mobile": "https://www.tokopedia.com/kimchioppaya/chunjang-jjajang-halal-pasta-kedelai-hitam-300g-saus-jjajangmyeon",
                    "is_topads": false,
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/2491196",
                    "cashback": 0,
                    "shop_name": "kimchi oppaya",
                    "official_store": false,
                    "url_desktop": "https://www.tokopedia.com/kimchioppaya/chunjang-jjajang-halal-pasta-kedelai-hitam-300g-saus-jjajangmyeon"
                },
                {
                    "price": "Rp 750.000",
                    "url_desktop": "https://www.tokopedia.com/liquor-1/baileys-strawberries-and-cream-limited-edition-700ml",
                    "shop_url_mobile": "https://www.tokopedia.com/liquor-1",
                    "department_id": 4580,
                    "official_store": true,
                    "shop_applink": "tokopedia://shop/8816413",
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    },
                    "rating": 5,
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/hDjmkQ/2020/9/16/543f208f-c9bc-4ad8-ad78-8b3c04435c37.jpg",
                    "shop_name": "Liquor_Avenue",
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/hDjmkQ/2020/9/16/543f208f-c9bc-4ad8-ad78-8b3c04435c37.jpg",
                    "shop_location": "Jakarta Utara",
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/8816413",
                    "url_mobile": "https://www.tokopedia.com/liquor-1/baileys-strawberries-and-cream-limited-edition-700ml",
                    "shop_url_desktop": "https://www.tokopedia.com/liquor-1",
                    "discounted_price": "",
                    "count_review": 107,
                    "name": "Baileys Strawberries and Cream Limited Edition 700ml",
                    "cashback": 0,
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=4580&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=1186845983&pos=5&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/liquor-1/baileys-strawberries-and-cream-limited-edition-700ml?catid4580=%26ref=default_pa1_po5%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "discount_percentage": 0,
                    "gold_merchant": true,
                    "labels": [
                        {
                            "title": "Terjual 261",
                            "position": "integrity",
                            "type": "textDarkGrey"
                        }
                    ],
                    "product_id": 1186845983,
                    "applink": "tokopedia://product/1186845983",
                    "rating_average": "4.9",
                    "is_topads": false
                },
                {
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2019/3/19/5116808/5116808_86a99cce-a431-4d5d-b361-9aaa2753535f_1813_1813.jpg",
                    "gold_merchant": true,
                    "shop_applink": "tokopedia://shop/608953",
                    "rating": 5,
                    "discounted_price": "Rp 28.000",
                    "labels": [
                        {
                            "position": "offers",
                            "type": "lightGrey",
                            "title": "Grosir"
                        },
                        {
                            "position": "integrity",
                            "type": "textDarkGrey",
                            "title": "Terjual 961"
                        }
                    ],
                    "url_mobile": "https://www.tokopedia.com/genkiplant/extra-virgin-coconut-oil-100-organic-cold-processed-60ml",
                    "shop_location": "Jakarta Utara",
                    "shop_url_mobile": "https://www.tokopedia.com/genkiplant",
                    "department_id": 2785,
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/608953",
                    "shop_url_desktop": "https://www.tokopedia.com/genkiplant",
                    "price": "Rp 19.600",
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2019/3/19/5116808/5116808_86a99cce-a431-4d5d-b361-9aaa2753535f_1813_1813.jpg",
                    "name": "Extra Virgin Coconut Oil - 100% Organic & Cold Processed (60ml)",
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    },
                    "official_store": true,
                    "applink": "tokopedia://product/33329334",
                    "product_id": 33329334,
                    "discount_percentage": 30,
                    "is_topads": false,
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=2785&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=33329334&pos=6&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/genkiplant/extra-virgin-coconut-oil-100-organic-cold-processed-60ml?catid2785=%26ref=default_pa1_po6%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "url_desktop": "https://www.tokopedia.com/genkiplant/extra-virgin-coconut-oil-100-organic-cold-processed-60ml",
                    "cashback": 0,
                    "shop_name": "Genki Plant",
                    "rating_average": "4.8",
                    "count_review": 208
                },
                {
                    "rating": 5,
                    "price": "Rp 36.000",
                    "count_review": 1044,
                    "rating_average": "4.9",
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2016/11/25/1420963/1420963_cab0641a-7136-4d65-94d0-e1994ecfb453.jpg",
                    "url_desktop": "https://www.tokopedia.com/berashitamorgan/beras-hitam-organik-kualitas-ekspor-bersertifikat",
                    "official_store": false,
                    "gold_merchant": true,
                    "shop_url_mobile": "https://www.tokopedia.com/berashitamorgan",
                    "is_topads": false,
                    "discount_percentage": 0,
                    "shop_applink": "tokopedia://shop/1420963",
                    "department_id": 2735,
                    "name": "Beras Hitam Organik Kualitas Ekspor (bersertifikat)",
                    "product_id": 99555091,
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2016/11/25/1420963/1420963_cab0641a-7136-4d65-94d0-e1994ecfb453.jpg",
                    "url_mobile": "https://www.tokopedia.com/berashitamorgan/beras-hitam-organik-kualitas-ekspor-bersertifikat",
                    "applink": "tokopedia://product/99555091",
                    "shop_location": "Kab. Tangerang",
                    "shop_name": "Natural Sehat Sentosa",
                    "discounted_price": "",
                    "shop_url_desktop": "https://www.tokopedia.com/berashitamorgan",
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/1420963",
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=2735&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=99555091&pos=7&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/berashitamorgan/beras-hitam-organik-kualitas-ekspor-bersertifikat?catid2735=%26ref=default_pa1_po7%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "labels": [
                        {
                            "title": "Grosir",
                            "position": "offers",
                            "type": "lightGrey"
                        },
                        {
                            "title": "Grosir",
                            "position": "price",
                            "type": "lightGreen"
                        },
                        {
                            "title": "Terjual 5,1 rb",
                            "position": "integrity",
                            "type": "textDarkGrey"
                        }
                    ],
                    "cashback": 0,
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    }
                },
                {
                    "department_id": 2741,
                    "url_desktop": "https://www.tokopedia.com/cahayametta/mie-mi-hokkian-hokian-lo-mie-1kg",
                    "discounted_price": "",
                    "shop_name": "Cahaya Metta",
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/4048557",
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    },
                    "product_id": 768984308,
                    "cashback": 0,
                    "shop_url_mobile": "https://www.tokopedia.com/cahayametta",
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=2741&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=768984308&pos=8&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/cahayametta/mie-mi-hokkian-hokian-lo-mie-1kg?catid2741=%26ref=default_pa1_po8%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "shop_applink": "tokopedia://shop/4048557",
                    "labels": [
                        {
                            "position": "promo",
                            "type": "lightGreen",
                            "title": "Gratis Ongkir"
                        },
                        {
                            "title": "Grosir",
                            "position": "offers",
                            "type": "lightGrey"
                        },
                        {
                            "title": "Grosir",
                            "position": "price",
                            "type": "lightGreen"
                        },
                        {
                            "title": "Terjual 1,5 rb",
                            "position": "integrity",
                            "type": "textDarkGrey"
                        }
                    ],
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2020/8/25/4591063/4591063_335e76f3-d9f9-4d77-834e-15f31b597e8c_2048_2048",
                    "shop_url_desktop": "https://www.tokopedia.com/cahayametta",
                    "discount_percentage": 0,
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2020/8/25/4591063/4591063_335e76f3-d9f9-4d77-834e-15f31b597e8c_2048_2048",
                    "rating": 5,
                    "is_topads": false,
                    "url_mobile": "https://www.tokopedia.com/cahayametta/mie-mi-hokkian-hokian-lo-mie-1kg",
                    "count_review": 189,
                    "official_store": false,
                    "applink": "tokopedia://product/768984308",
                    "shop_location": "Jakarta Barat",
                    "gold_merchant": true,
                    "name": "Mie / Mi Hokkian / Hokian Lo Mie @ 1kg",
                    "rating_average": "5.0",
                    "price": "Rp 13.000"
                },
                {
                    "official_store": true,
                    "count_review": 446,
                    "shop_url_desktop": "https://www.tokopedia.com/littlemaria",
                    "cashback": 0,
                    "shop_name": "Little Maria",
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=1162&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=527573278&pos=9&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/littlemaria/littlemaria-blackpepper-sauce-saus-lada-hitam-500g?catid1162=%26ref=default_pa1_po9%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "shop_location": "Jakarta Barat",
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    },
                    "rating_average": "4.9",
                    "department_id": 1162,
                    "name": "LittleMaria Blackpepper Sauce/Saus Lada Hitam 500g",
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2020/9/8/16710512/16710512_67f91a73-43e6-4835-b5e3-dc0444764279_2048_2048",
                    "applink": "tokopedia://product/527573278",
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/2897773",
                    "shop_applink": "tokopedia://shop/2897773",
                    "rating": 5,
                    "labels": [
                        {
                            "title": "Gratis Ongkir",
                            "position": "promo",
                            "type": "lightGreen"
                        },
                        {
                            "title": "Cashback",
                            "position": "promo",
                            "type": "lightGreen"
                        },
                        {
                            "position": "integrity",
                            "type": "textDarkGrey",
                            "title": "Terjual 1,5 rb"
                        }
                    ],
                    "is_topads": false,
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2020/9/8/16710512/16710512_67f91a73-43e6-4835-b5e3-dc0444764279_2048_2048",
                    "price": "Rp 62.100",
                    "discount_percentage": 10,
                    "shop_url_mobile": "https://www.tokopedia.com/littlemaria",
                    "discounted_price": "Rp 69.000",
                    "product_id": 527573278,
                    "url_mobile": "https://www.tokopedia.com/littlemaria/littlemaria-blackpepper-sauce-saus-lada-hitam-500g",
                    "gold_merchant": true,
                    "url_desktop": "https://www.tokopedia.com/littlemaria/littlemaria-blackpepper-sauce-saus-lada-hitam-500g"
                },
                {
                    "shop_location": "Jakarta Timur",
                    "labels": [
                        {
                            "title": "Gratis Ongkir",
                            "position": "promo",
                            "type": "lightGreen"
                        },
                        {
                            "type": "textDarkGrey",
                            "title": "Terjual 451",
                            "position": "integrity"
                        }
                    ],
                    "department_id": 2708,
                    "shop_name": "HalalChicken",
                    "shop_url_desktop": "https://www.tokopedia.com/halalchicken",
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/8221578",
                    "shop_applink": "tokopedia://shop/8221578",
                    "rating_average": "5.0",
                    "cashback": 0,
                    "price": "Rp 38.999",
                    "rating": 5,
                    "applink": "tokopedia://product/875007541",
                    "name": "Kulit Ayam HALAL",
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=2708&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=875007541&pos=10&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/halalchicken/kulit-ayam-halal?catid2708=%26ref=default_pa1_po10%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "product_id": 875007541,
                    "count_review": 112,
                    "free_ongkir": {
                        "is_active": false,
                        "img_url": ""
                    },
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/attachment/2020/8/15/-1/-1_3cd7700d-f91e-4809-bd9c-053f8a0b5263.jpg",
                    "discounted_price": "Rp 49.999",
                    "discount_percentage": 22,
                    "url_desktop": "https://www.tokopedia.com/halalchicken/kulit-ayam-halal",
                    "is_topads": false,
                    "shop_url_mobile": "https://www.tokopedia.com/halalchicken",
                    "official_store": true,
                    "url_mobile": "https://www.tokopedia.com/halalchicken/kulit-ayam-halal",
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/attachment/2020/8/15/-1/-1_3cd7700d-f91e-4809-bd9c-053f8a0b5263.jpg",
                    "gold_merchant": true
                },
                {
                    "discount_percentage": 0,
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=2794&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=241904490&pos=11&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/lovebakingshop/promo-goldenfil-selai-chocomaltine-1kg-original-chocolate-choco-crunch?catid2794=%26ref=default_pa1_po11%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "rating": 5,
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/VqbcmM/2020/10/12/c6cda30d-0d91-46df-bdc2-1d6c2f280a7a.jpg",
                    "price": "Rp 40.000",
                    "is_topads": false,
                    "official_store": false,
                    "product_id": 241904490,
                    "count_review": 4249,
                    "department_id": 2794,
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    },
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/VqbcmM/2020/10/12/c6cda30d-0d91-46df-bdc2-1d6c2f280a7a.jpg",
                    "shop_name": "Love Baking Shop",
                    "shop_url_mobile": "https://www.tokopedia.com/lovebakingshop",
                    "shop_applink": "tokopedia://shop/2172179",
                    "shop_url_desktop": "https://www.tokopedia.com/lovebakingshop",
                    "discounted_price": "",
                    "url_mobile": "https://www.tokopedia.com/lovebakingshop/promo-goldenfil-selai-chocomaltine-1kg-original-chocolate-choco-crunch",
                    "rating_average": "4.9",
                    "gold_merchant": true,
                    "url_desktop": "https://www.tokopedia.com/lovebakingshop/promo-goldenfil-selai-chocomaltine-1kg-original-chocolate-choco-crunch",
                    "name": "PROMO GOLDENFIL Selai CHOCOMALTINE 1KG original Chocolate CHOCO CRUNCH",
                    "cashback": 0,
                    "applink": "tokopedia://product/241904490",
                    "shop_location": "Jakarta Utara",
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/2172179",
                    "labels": [
                        {
                            "position": "integrity",
                            "type": "textDarkGrey",
                            "title": "Terjual 21 rb"
                        }
                    ]
                },
                {
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/VqbcmM/2021/1/6/9b8506d5-6849-4eb1-9119-bc9ac3057f04.jpg",
                    "official_store": true,
                    "product_id": 930837516,
                    "gold_merchant": true,
                    "rating": 5,
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    },
                    "price": "Rp 42.750",
                    "shop_name": "greenara.id",
                    "cashback": 0,
                    "labels": [
                        {
                            "title": "Terjual 597",
                            "position": "integrity",
                            "type": "textDarkGrey"
                        }
                    ],
                    "shop_location": "Jakarta Utara",
                    "discount_percentage": 5,
                    "url_desktop": "https://www.tokopedia.com/greenaraid/greenara-organic-coconut-sugar-1kg-gula-kelapa",
                    "discounted_price": "Rp 45.000",
                    "applink": "tokopedia://product/930837516",
                    "url_mobile": "https://www.tokopedia.com/greenaraid/greenara-organic-coconut-sugar-1kg-gula-kelapa",
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=2743&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=930837516&pos=12&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/greenaraid/greenara-organic-coconut-sugar-1kg-gula-kelapa?catid2743=%26ref=default_pa1_po12%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "department_id": 2743,
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/4128311",
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/VqbcmM/2021/1/6/9b8506d5-6849-4eb1-9119-bc9ac3057f04.jpg",
                    "rating_average": "5.0",
                    "is_topads": false,
                    "count_review": 124,
                    "shop_url_desktop": "https://www.tokopedia.com/greenaraid",
                    "name": "Greenara Organic Coconut Sugar 1Kg / Gula Kelapa",
                    "shop_url_mobile": "https://www.tokopedia.com/greenaraid",
                    "shop_applink": "tokopedia://shop/4128311"
                },
                {
                    "shop_url_mobile": "https://www.tokopedia.com/bunanik",
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=2813&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=1271978635&pos=13&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/bunanik/serabi-durian-asli-khas-bangka-khusus-gojek-grab?catid2813=%26ref=default_pa1_po13%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "name": "Serabi Durian Asli khas Bangka (KHUSUS GOJEK GRAB)",
                    "price": "Rp 16.000",
                    "is_topads": false,
                    "cashback": 0,
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/VqbcmM/2020/10/20/22ca300c-2a31-4797-a86d-8e9081c54f17.png",
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/VqbcmM/2020/10/20/22ca300c-2a31-4797-a86d-8e9081c54f17.png",
                    "rating_average": "4.9",
                    "shop_name": "Pisang Goreng Madu Bu Nanik",
                    "count_review": 226,
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/2794483",
                    "shop_url_desktop": "https://www.tokopedia.com/bunanik",
                    "applink": "tokopedia://product/1271978635",
                    "shop_applink": "tokopedia://shop/2794483",
                    "product_id": 1271978635,
                    "rating": 5,
                    "department_id": 2813,
                    "discounted_price": "",
                    "url_desktop": "https://www.tokopedia.com/bunanik/serabi-durian-asli-khas-bangka-khusus-gojek-grab",
                    "official_store": false,
                    "discount_percentage": 0,
                    "url_mobile": "https://www.tokopedia.com/bunanik/serabi-durian-asli-khas-bangka-khusus-gojek-grab",
                    "shop_location": "Jakarta Barat",
                    "gold_merchant": true,
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    },
                    "labels": [
                        {
                            "title": "Gratis Ongkir",
                            "position": "promo",
                            "type": "lightGreen"
                        },
                        {
                            "title": "Terjual 906",
                            "position": "integrity",
                            "type": "textDarkGrey"
                        }
                    ]
                },
                {
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    },
                    "discounted_price": "Rp 138.000",
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=2811&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=39665406&pos=14&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/nourishindonesia/nutritional-yeast-powder-250-gr-vegan-cheese-non-dairy?catid2811=%26ref=default_pa1_po14%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "applink": "tokopedia://product/39665406",
                    "is_topads": false,
                    "shop_name": "Nourish Indonesia",
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/429323",
                    "labels": [
                        {
                            "position": "promo",
                            "type": "lightGreen",
                            "title": "Gratis Ongkir"
                        },
                        {
                            "position": "integrity",
                            "type": "textDarkGrey",
                            "title": "Terjual 834"
                        }
                    ],
                    "shop_url_mobile": "https://www.tokopedia.com/nourishindonesia",
                    "shop_applink": "tokopedia://shop/429323",
                    "discount_percentage": 15,
                    "rating": 5,
                    "shop_url_desktop": "https://www.tokopedia.com/nourishindonesia",
                    "shop_location": "Jakarta Timur",
                    "department_id": 2811,
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2020/8/22/3324938/3324938_ae2d6ea2-6e6f-49b6-9690-3dfe5142fbc8_1080_1080",
                    "count_review": 187,
                    "url_desktop": "https://www.tokopedia.com/nourishindonesia/nutritional-yeast-powder-250-gr-vegan-cheese-non-dairy",
                    "product_id": 39665406,
                    "cashback": 0,
                    "price": "Rp 117.300",
                    "gold_merchant": true,
                    "rating_average": "4.9",
                    "name": "Nutritional Yeast Powder 250 gr (Vegan Cheese) Non dairy",
                    "official_store": true,
                    "url_mobile": "https://www.tokopedia.com/nourishindonesia/nutritional-yeast-powder-250-gr-vegan-cheese-non-dairy",
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2020/8/22/3324938/3324938_ae2d6ea2-6e6f-49b6-9690-3dfe5142fbc8_1080_1080"
                },
                {
                    "url_mobile": "https://www.tokopedia.com/bombsquad/susu-greenfields-1l-fresh-milk",
                    "shop_name": "BombSquad",
                    "official_store": false,
                    "is_topads": false,
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/VqbcmM/2020/5/26/44c16ed6-a362-43e7-8116-e31869140b2d.jpg",
                    "discounted_price": "",
                    "name": "Susu Greenfields 1L fresh milk",
                    "url_desktop": "https://www.tokopedia.com/bombsquad/susu-greenfields-1l-fresh-milk",
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/VqbcmM/2020/5/26/44c16ed6-a362-43e7-8116-e31869140b2d.jpg",
                    "shop_location": "Jakarta Barat",
                    "discount_percentage": 0,
                    "rating_average": "5.0",
                    "count_review": 613,
                    "price": "Rp 19.500",
                    "cashback": 0,
                    "labels": [
                        {
                            "title": "Terjual 15 rb",
                            "position": "integrity",
                            "type": "textDarkGrey"
                        }
                    ],
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=2715&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=867546056&pos=15&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/bombsquad/susu-greenfields-1l-fresh-milk?catid2715=%26ref=default_pa1_po15%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "shop_url_mobile": "https://www.tokopedia.com/bombsquad",
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/7697667",
                    "department_id": 2715,
                    "shop_applink": "tokopedia://shop/7697667",
                    "product_id": 867546056,
                    "rating": 5,
                    "applink": "tokopedia://product/867546056",
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    },
                    "shop_url_desktop": "https://www.tokopedia.com/bombsquad",
                    "gold_merchant": true
                },
                {
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=2785&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=36359315&pos=16&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/tokupedia/sipahh-fun-straw-rasa-cookies-and-cream-isi-5?catid2785=%26ref=default_pa1_po16%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "url_mobile": "https://www.tokopedia.com/tokupedia/sipahh-fun-straw-rasa-cookies-and-cream-isi-5",
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/964084",
                    "shop_url_desktop": "https://www.tokopedia.com/tokupedia",
                    "is_topads": false,
                    "shop_applink": "tokopedia://shop/964084",
                    "name": "Sipahh fun straw rasa cookies and cream isi 5",
                    "shop_name": "toku pedia",
                    "rating_average": "4.9",
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2016/3/15/8040781/8040781_c1dc9e9e-a730-4eb6-b4ae-16522023c2e1.jpg",
                    "price": "Rp 20.000",
                    "count_review": 82,
                    "rating": 5,
                    "shop_url_mobile": "https://www.tokopedia.com/tokupedia",
                    "department_id": 2785,
                    "cashback": 0,
                    "url_desktop": "https://www.tokopedia.com/tokupedia/sipahh-fun-straw-rasa-cookies-and-cream-isi-5",
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    },
                    "gold_merchant": true,
                    "product_id": 36359315,
                    "discount_percentage": 0,
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2016/3/15/8040781/8040781_c1dc9e9e-a730-4eb6-b4ae-16522023c2e1.jpg",
                    "shop_location": "Jakarta Utara",
                    "labels": [
                        {
                            "title": "Gratis Ongkir",
                            "position": "promo",
                            "type": "lightGreen"
                        },
                        {
                            "title": "Cashback",
                            "position": "promo",
                            "type": "lightGreen"
                        },
                        {
                            "title": "Cashback",
                            "position": "price",
                            "type": "lightGreen"
                        },
                        {
                            "title": "Terjual 494",
                            "position": "integrity",
                            "type": "textDarkGrey"
                        }
                    ],
                    "applink": "tokopedia://product/36359315",
                    "official_store": false,
                    "discounted_price": ""
                },
                {
                    "cashback": 0,
                    "department_id": 2762,
                    "free_ongkir": {
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png",
                        "is_active": true
                    },
                    "rating_average": "4.8",
                    "is_topads": false,
                    "shop_location": "Jakarta Selatan",
                    "official_store": true,
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/2603035",
                    "product_id": 218713387,
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2020/7/30/23528349/23528349_720f9f59-9843-438c-ac81-88ba7e5a8074_500_500",
                    "discounted_price": "Rp 7.000",
                    "url_mobile": "https://www.tokopedia.com/rose-brand/tapioka-rose-brand-500-gram",
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=2762&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=218713387&pos=17&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/rose-brand/tapioka-rose-brand-500-gram?catid2762=%26ref=default_pa1_po17%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "shop_name": "Rose Brand Store",
                    "shop_url_desktop": "https://www.tokopedia.com/rose-brand",
                    "shop_applink": "tokopedia://shop/2603035",
                    "url_desktop": "https://www.tokopedia.com/rose-brand/tapioka-rose-brand-500-gram",
                    "labels": [
                        {
                            "type": "textDarkGrey",
                            "title": "Terjual 1,7 rb",
                            "position": "integrity"
                        }
                    ],
                    "rating": 5,
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2020/7/30/23528349/23528349_720f9f59-9843-438c-ac81-88ba7e5a8074_500_500",
                    "price": "Rp 5.100",
                    "name": "Tapioka Rose Brand - 500 gram",
                    "discount_percentage": 27,
                    "gold_merchant": true,
                    "count_review": 80,
                    "shop_url_mobile": "https://www.tokopedia.com/rose-brand",
                    "applink": "tokopedia://product/218713387"
                },
                {
                    "rating": 5,
                    "shop_applink": "tokopedia://shop/698090",
                    "gold_merchant": true,
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2020/5/5/571854521/571854521_80fd3f18-8c13-4907-a3cb-05c8a7740ab6_1080_1080.jpg",
                    "is_topads": false,
                    "shop_location": "Jakarta Pusat",
                    "count_review": 2509,
                    "shop_name": "Fave Store",
                    "applink": "tokopedia://product/571854521",
                    "labels": [
                        {
                            "position": "offers",
                            "type": "lightGrey",
                            "title": "Grosir"
                        },
                        {
                            "title": "Grosir",
                            "position": "price",
                            "type": "lightGreen"
                        },
                        {
                            "position": "integrity",
                            "type": "textDarkGrey",
                            "title": "Terjual 7,9 rb"
                        }
                    ],
                    "product_id": 571854521,
                    "discounted_price": "",
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=1161&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=571854521&pos=18&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/favestore/himalayan-pink-salt-1-kg-garam-himalaya?catid1161=%26ref=default_pa1_po18%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/698090",
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2020/5/5/571854521/571854521_80fd3f18-8c13-4907-a3cb-05c8a7740ab6_1080_1080.jpg",
                    "name": "Himalayan Pink Salt 1 kg Garam Himalaya",
                    "url_mobile": "https://www.tokopedia.com/favestore/himalayan-pink-salt-1-kg-garam-himalaya",
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    },
                    "rating_average": "4.9",
                    "url_desktop": "https://www.tokopedia.com/favestore/himalayan-pink-salt-1-kg-garam-himalaya",
                    "discount_percentage": 0,
                    "cashback": 0,
                    "official_store": false,
                    "shop_url_desktop": "https://www.tokopedia.com/favestore",
                    "department_id": 1161,
                    "shop_url_mobile": "https://www.tokopedia.com/favestore",
                    "price": "Rp 50.000"
                },
                {
                    "shop_name": "greenara.id",
                    "shop_url_desktop": "https://www.tokopedia.com/greenaraid",
                    "url_desktop": "https://www.tokopedia.com/greenaraid/roasted-almond-1kg-kacang-almond-panggang-1kg",
                    "applink": "tokopedia://product/407623382",
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/4128311",
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2020/8/6/40477907/40477907_edb426c1-4a2e-4e9f-a72b-eb6268e2d0da_1080_1080",
                    "product_id": 407623382,
                    "rating_average": "4.9",
                    "name": "Roasted almond 1kg / kacang almond panggang 1kg",
                    "rating": 5,
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2020/8/6/40477907/40477907_edb426c1-4a2e-4e9f-a72b-eb6268e2d0da_1080_1080",
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=2804&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=407623382&pos=19&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/greenaraid/roasted-almond-1kg-kacang-almond-panggang-1kg?catid2804=%26ref=default_pa1_po19%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    },
                    "discounted_price": "Rp 205.000",
                    "cashback": 0,
                    "labels": [
                        {
                            "title": "Terjual 2,7 rb",
                            "position": "integrity",
                            "type": "textDarkGrey"
                        }
                    ],
                    "shop_url_mobile": "https://www.tokopedia.com/greenaraid",
                    "gold_merchant": true,
                    "discount_percentage": 12,
                    "is_topads": false,
                    "shop_location": "Jakarta Utara",
                    "shop_applink": "tokopedia://shop/4128311",
                    "official_store": true,
                    "count_review": 1059,
                    "price": "Rp 180.000",
                    "department_id": 2804,
                    "url_mobile": "https://www.tokopedia.com/greenaraid/roasted-almond-1kg-kacang-almond-panggang-1kg"
                },
                {
                    "department_id": 2783,
                    "name": "BRAGG Apple cider vinegar 946ml / cuka apel 946 ml",
                    "product_id": 151278952,
                    "cashback": 0,
                    "price": "Rp 135.000",
                    "shop_applink": "tokopedia://shop/529111",
                    "image_url_desktop": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2019/2/20/151278952/151278952_51299882-af35-446e-8c4f-ce7b9c732e13_700_700.jpg",
                    "rating": 5,
                    "url_mobile": "https://www.tokopedia.com/phinovstore/bragg-apple-cider-vinegar-946ml-cuka-apel-946-ml",
                    "url_desktop": "https://www.tokopedia.com/phinovstore/bragg-apple-cider-vinegar-946ml-cuka-apel-946-ml",
                    "rating_average": "4.9",
                    "shop_url_mobile": "https://www.tokopedia.com/phinovstore",
                    "topads_click_url": "https://ace.tokopedia.com/r3/v2/track?category=2783&event=click&experimentVersion=v1.4&page=1&page_name=default&pid=151278952&pos=20&recommendation=trend&rectype=trend&redirect=https://www.tokopedia.com/phinovstore/bragg-apple-cider-vinegar-946ml-cuka-apel-946-ml?catid2783=%26ref=default_pa1_po20%26src=r3trend&tid=4qWlkFceYWib-5jl5te6zsraLsw=&uid=0&unique_id=&utime=1609946053&widget=r3_widget&xdevice=android",
                    "discounted_price": "",
                    "free_ongkir": {
                        "is_active": true,
                        "img_url": "https://ecs7-p.tokopedia.net/img/ic_bebas_ongkir.png"
                    },
                    "count_review": 3162,
                    "shop_reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/529111",
                    "is_topads": false,
                    "labels": [
                        {
                            "title": "Grosir",
                            "position": "offers",
                            "type": "lightGrey"
                        },
                        {
                            "title": "Grosir",
                            "position": "price",
                            "type": "lightGreen"
                        },
                        {
                            "title": "Terjual 11 rb",
                            "position": "integrity",
                            "type": "textDarkGrey"
                        }
                    ],
                    "shop_name": "Phinov Store",
                    "shop_location": "Jakarta Barat",
                    "image_url_mobile": "https://ecs7-p.tokopedia.net/img/cache/300-square/product-1/2019/2/20/151278952/151278952_51299882-af35-446e-8c4f-ce7b9c732e13_700_700.jpg",
                    "official_store": false,
                    "discount_percentage": 0,
                    "applink": "tokopedia://product/151278952",
                    "gold_merchant": true,
                    "shop_url_desktop": "https://www.tokopedia.com/phinovstore"
                }
            ],
            "additional_info": {},
            "properties": {
                "limit_number": 20,
                "limit_product": false
            },
            "title": "",
            "skiprender": false,
            "render_by_default": true,
            "creative_name": "",
            "id": 34946
        }
    },
}
"""