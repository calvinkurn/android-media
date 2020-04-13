package com.tokopedia.search.result.presentation.presenter.product.testinstance

import com.google.gson.Gson
import com.tokopedia.search.result.domain.model.SearchProductModel

private val searchProductModelMultipleCategoriesJSON = """
{
    "searchProduct": {
      "query": "samsung",
      "source": "search",
      "shareUrl": "https://www.tokopedia.com/search?device=android&image_size=200&image_square=true&ob=203&q=samsung&related=true&rows=8&shipping=,&source=search&st=product&unique_id=a9e9525d4f5ab7285514eaaf6404ec03&user_id=19016871&xdevice=lite-0.0",
      "isFilter": false,
      "response_code": 0,
      "keyword_process": "2",
      "count": 734255,
      "count_text": "4.3jt+",
      "additional_params": "",
      "isQuerySafe": true,
      "autocomplete_applink": "tokopedia://search-autocomplete?q=samsung",
      "errorMessage": "",
      "lite_url": "",
      "default_view": 1,
      "redirection": {
        "redirect_applink": ""
      },
      "ticker": {
        "text": "",
        "query": ""
      },
      "suggestion": {
        "currentKeyword": "",
        "suggestion": "",
        "suggestionCount": 0,
        "instead": "",
        "insteadCount": 0,
        "text": "",
        "query": ""
      },
      "related": {
        "related_keyword": "",
        "other_related": []
      },
      "products": [
        {
          "id": 574261652,
          "warehouse_id_default": 0,
          "name": "Samsung Galaxy A50S 6/128 Ram 6gb Rom 128 Garansi Resmi",
          "childs": [],
          "url": "https://www.tokopedia.com/maxiphonecell/samsung-galaxy-a50s-6-128-ram-6gb-rom-128-garansi-resmi?trkid=f%3DCa24L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dsearch_page%3D1_ob%3D203_q%3Dsamsung_bmexp%3D21_po%3D1_catid%3D3054_bmexp%3D21&whid=0",
          "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/9/30/3102419/3102419_98001153-0a84-4281-b450-fa3d5ebe5505_1800_1800.jpg",
          "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/9/30/3102419/3102419_98001153-0a84-4281-b450-fa3d5ebe5505_1800_1800.jpg",
          "price": "Rp 4.155.000",
          "price_range": "",
          "wishlist": false,
          "whole_sale_price": [],
          "courier_count": 9,
          "condition": 1,
          "category_id": 65,
          "category_name": "Handphone & Tablet",
          "category_breadcrumb": "handphone-tablet/handphone/android-os",
          "department_id": 3054,
          "department_name": "Android OS",
          "free_ongkir": {
            "is_active": false,
            "img_url": ""
          },
          "labels": [
            {
              "title": "Tukar Tambah",
              "color": "#42b549"
            }
          ],
          "label_groups": [
            {
              "position": "offers",
              "type": "lightGrey",
              "title": "Bisa Tukar Tambah"
            }
          ],
          "badges": [
            {
              "title": "Official Store",
              "image_url": "https://ecs7.tokopedia.net/img/official_store_badge.png",
              "show": true
            }
          ],
          "is_featured": 0,
          "rating": 5,
          "count_review": 259,
          "original_price": "",
          "discount_expired_time": "",
          "discount_start_time": "",
          "discount_percentage": 0,
          "sku": "",
          "stock": 15,
          "ga_key": "/searchproduct/handphone-tablet/handphone/android-os/samsung/maxiphonecell/samsung-galaxy-a50s-6-128-ram-6gb-rom-128-garansi-resmi",
          "is_preorder": false,
          "shop": {
            "id": 3102419,
            "name": "Maxi phone cell",
            "url": "https://www.tokopedia.com/maxiphonecell",
            "is_gold_shop": false,
            "is_power_badge": true,
            "location": "Jakarta Pusat",
            "city": "Jakarta Pusat",
            "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/3102419",
            "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=3102419",
            "is_official": true
          }
        },
        {
          "id": 440206343,
          "warehouse_id_default": 0,
          "name": "Samsung A50 6/128 Ram 6gb Rom 128gb Garansi Resmi",
          "childs": [],
          "url": "https://www.tokopedia.com/maxiphonecell/samsung-a50-6-128-ram-6gb-rom-128gb-garansi-resmi?trkid=f%3DCa24L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dsearch_page%3D1_ob%3D203_q%3Dsamsung_bmexp%3D21_po%3D2_catid%3D3054_bmexp%3D21&whid=0",
          "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/1/14/18568728/18568728_08a54c4e-1b88-4312-8b7e-e1766ff6e62b_1800_1800",
          "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/1/14/18568728/18568728_08a54c4e-1b88-4312-8b7e-e1766ff6e62b_1800_1800",
          "price": "Rp 3.250.000",
          "price_range": "",
          "wishlist": false,
          "whole_sale_price": [],
          "courier_count": 9,
          "condition": 1,
          "category_id": 1758,
          "category_name": "Fashion Wanita",
          "category_breadcrumb": "handphone-tablet/handphone/android-os",
          "department_id": 3054,
          "department_name": "Android OS",
          "free_ongkir": {
            "is_active": false,
            "img_url": ""
          },
          "labels": [
            {
              "title": "Tukar Tambah",
              "color": "#42b549"
            }
          ],
          "label_groups": [
            {
              "position": "offers",
              "type": "lightGrey",
              "title": "Bisa Tukar Tambah"
            }
          ],
          "badges": [
            {
              "title": "Official Store",
              "image_url": "https://ecs7.tokopedia.net/img/official_store_badge.png",
              "show": true
            }
          ],
          "is_featured": 0,
          "rating": 5,
          "count_review": 434,
          "original_price": "",
          "discount_expired_time": "",
          "discount_start_time": "",
          "discount_percentage": 0,
          "sku": "",
          "stock": 30,
          "ga_key": "/searchproduct/handphone-tablet/handphone/android-os/samsung/maxiphonecell/samsung-a50-6-128-ram-6gb-rom-128gb-garansi-resmi",
          "is_preorder": false,
          "shop": {
            "id": 3102419,
            "name": "Maxi phone cell",
            "url": "https://www.tokopedia.com/maxiphonecell",
            "is_gold_shop": false,
            "is_power_badge": true,
            "location": "Jakarta Pusat",
            "city": "Jakarta Pusat",
            "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/3102419",
            "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=3102419",
            "is_official": true
          }
        },
        {
          "id": 445135974,
          "warehouse_id_default": 0,
          "name": "Samsung S10 plus 128GB Garansi Resmi SEIN / samsung indonesia 1 thn",
          "childs": [],
          "url": "https://www.tokopedia.com/iboxshop/samsung-s10-plus-128gb-garansi-resmi-sein-samsung-indonesia-1-thn?trkid=f%3DCa24L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dsearch_page%3D1_ob%3D203_q%3Dsamsung_bmexp%3D21_po%3D3_catid%3D3054_bmexp%3D21&whid=0",
          "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/4/10/155485416118615/155485416118615_4656079d-d091-42d8-880c-7e59a3b84457.png",
          "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/attachment/2019/4/10/155485416118615/155485416118615_4656079d-d091-42d8-880c-7e59a3b84457.png",
          "price": "Rp 11.450.000",
          "price_range": "",
          "wishlist": false,
          "whole_sale_price": [],
          "courier_count": 11,
          "condition": 1,
          "category_id": 1759,
          "category_name": "Fashion Pria",
          "category_breadcrumb": "handphone-tablet/handphone/android-os",
          "department_id": 3054,
          "department_name": "Android OS",
          "free_ongkir": {
            "is_active": false,
            "img_url": ""
          },
          "labels": [
            {
              "title": "Tukar Tambah",
              "color": "#42b549"
            }
          ],
          "label_groups": [
            {
              "position": "offers",
              "type": "lightGrey",
              "title": "Bisa Tukar Tambah"
            }
          ],
          "badges": [
            {
              "title": "Power Badge",
              "image_url": "https://ecs7.tokopedia.net/img/power_merchant_badge.png",
              "show": true
            }
          ],
          "is_featured": 0,
          "rating": 5,
          "count_review": 123,
          "original_price": "",
          "discount_expired_time": "",
          "discount_start_time": "",
          "discount_percentage": 0,
          "sku": "",
          "stock": 32676,
          "ga_key": "/searchproduct/handphone-tablet/handphone/android-os/samsung/iboxshop/samsung-s10-plus-128gb-garansi-resmi-sein-samsung-indonesia-1-thn",
          "is_preorder": false,
          "shop": {
            "id": 1040262,
            "name": "ibox shop",
            "url": "https://www.tokopedia.com/iboxshop",
            "is_gold_shop": false,
            "is_power_badge": true,
            "location": "Jakarta Pusat",
            "city": "Jakarta Pusat",
            "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/1040262",
            "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=1040262",
            "is_official": false
          }
        },
        {
          "id": 519001114,
          "warehouse_id_default": 0,
          "name": "Samsung Galaxy A80 Ram 8Gb Internal 128Gb Garansi Resmi 1 Tahun",
          "childs": [],
          "url": "https://www.tokopedia.com/elitejaya/samsung-galaxy-a80-ram-8gb-internal-128gb-garansi-resmi-1-tahun?trkid=f%3DCa24L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dsearch_page%3D1_ob%3D203_q%3Dsamsung_bmexp%3D21_po%3D4_catid%3D3054_bmexp%3D21&whid=0",
          "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/8/27/15669196123574/15669196123574_466b6aa9-f325-4072-bbba-0198be3be54e.png",
          "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/attachment/2019/8/27/15669196123574/15669196123574_466b6aa9-f325-4072-bbba-0198be3be54e.png",
          "price": "Rp 7.200.000",
          "price_range": "",
          "wishlist": false,
          "whole_sale_price": [],
          "courier_count": 4,
          "condition": 1,
          "category_id": 65,
          "category_name": "Handphone & Tablet",
          "category_breadcrumb": "handphone-tablet/handphone/android-os",
          "department_id": 3054,
          "department_name": "Android OS",
          "free_ongkir": {
            "is_active": false,
            "img_url": ""
          },
          "labels": [],
          "label_groups": [],
          "badges": [
            {
              "title": "Power Badge",
              "image_url": "https://ecs7.tokopedia.net/img/power_merchant_badge.png",
              "show": true
            }
          ],
          "is_featured": 0,
          "rating": 5,
          "count_review": 25,
          "original_price": "",
          "discount_expired_time": "",
          "discount_start_time": "",
          "discount_percentage": 0,
          "sku": "",
          "stock": 32767,
          "ga_key": "/searchproduct/handphone-tablet/handphone/android-os/samsung/elitejaya/samsung-galaxy-a80-ram-8gb-internal-128gb-garansi-resmi-1-tahun",
          "is_preorder": false,
          "shop": {
            "id": 522082,
            "name": "Elite jaya",
            "url": "https://www.tokopedia.com/elitejaya",
            "is_gold_shop": false,
            "is_power_badge": true,
            "location": "Surabaya",
            "city": "Surabaya",
            "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/522082",
            "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=522082",
            "is_official": false
          }
        },
        {
          "id": 455164814,
          "warehouse_id_default": 0,
          "name": "Samsung A10 32GB SEIN",
          "childs": [],
          "url": "https://www.tokopedia.com/winncell/samsung-a10-32gb-sein?trkid=f%3DCa24L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dsearch_page%3D1_ob%3D203_q%3Dsamsung_bmexp%3D21_po%3D5_catid%3D3054_bmexp%3D21&whid=0",
          "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/4/24/17445982/17445982_3bd4b7bf-24c7-4c54-8da0-367935d44856_795_800",
          "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/4/24/17445982/17445982_3bd4b7bf-24c7-4c54-8da0-367935d44856_795_800",
          "price": "Rp 1.565.000",
          "price_range": "",
          "wishlist": false,
          "whole_sale_price": [],
          "courier_count": 5,
          "condition": 1,
          "category_id": 1759,
          "category_name": "Fashion Pria",
          "category_breadcrumb": "handphone-tablet/handphone/android-os",
          "department_id": 3054,
          "department_name": "Android OS",
          "free_ongkir": {
            "is_active": false,
            "img_url": ""
          },
          "labels": [],
          "label_groups": [],
          "badges": [
            {
              "title": "Power Badge",
              "image_url": "https://ecs7.tokopedia.net/img/power_merchant_badge.png",
              "show": true
            }
          ],
          "is_featured": 0,
          "rating": 5,
          "count_review": 72,
          "original_price": "",
          "discount_expired_time": "",
          "discount_start_time": "",
          "discount_percentage": 0,
          "sku": "",
          "stock": 1,
          "ga_key": "/searchproduct/handphone-tablet/handphone/android-os/samsung/winncell/samsung-a10-32gb-sein",
          "is_preorder": false,
          "shop": {
            "id": 2015537,
            "name": "winn cell",
            "url": "https://www.tokopedia.com/winncell",
            "is_gold_shop": false,
            "is_power_badge": true,
            "location": "Kab. Bekasi",
            "city": "Kab. Bekasi",
            "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/2015537",
            "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=2015537",
            "is_official": false
          }
        },
        {
          "id": 526138788,
          "warehouse_id_default": 0,
          "name": "Samsung A50 |Mulus |Fullset |Ex SEIN ORIGINAL",
          "childs": [],
          "url": "https://www.tokopedia.com/jjstoreid/samsung-a50-mulus-fullset-ex-sein-original?trkid=f%3DCa24L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dsearch_page%3D1_ob%3D203_q%3Dsamsung_bmexp%3D21_po%3D6_catid%3D3054_bmexp%3D21&whid=0",
          "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/8/3/5271884/5271884_7530b394-dd69-48e5-aa52-458e6163c1cd_1512_1512.jpg",
          "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/8/3/5271884/5271884_7530b394-dd69-48e5-aa52-458e6163c1cd_1512_1512.jpg",
          "price": "Rp 2.400.000",
          "price_range": "",
          "wishlist": false,
          "whole_sale_price": [],
          "courier_count": 9,
          "condition": 2,
          "category_id": 65,
          "category_name": "Handphone & Tablet",
          "category_breadcrumb": "handphone-tablet/handphone/android-os",
          "department_id": 3054,
          "department_name": "Android OS",
          "free_ongkir": {
            "is_active": false,
            "img_url": ""
          },
          "labels": [],
          "label_groups": [],
          "badges": [
            {
              "title": "Power Badge",
              "image_url": "https://ecs7.tokopedia.net/img/power_merchant_badge.png",
              "show": true
            }
          ],
          "is_featured": 0,
          "rating": 5,
          "count_review": 39,
          "original_price": "",
          "discount_expired_time": "",
          "discount_start_time": "",
          "discount_percentage": 0,
          "sku": "",
          "stock": 13,
          "ga_key": "/searchproduct/handphone-tablet/handphone/android-os/samsung/jjstoreid/samsung-a50-mulus-fullset-ex-sein-original",
          "is_preorder": false,
          "shop": {
            "id": 5271884,
            "name": "JJStore.ID",
            "url": "https://www.tokopedia.com/jjstoreid",
            "is_gold_shop": false,
            "is_power_badge": true,
            "location": "Jakarta Barat",
            "city": "Jakarta Barat",
            "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/5271884",
            "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=5271884",
            "is_official": false
          }
        },
        {
          "id": 468713348,
          "warehouse_id_default": 0,
          "name": "HP SAMSUNG A30 4/64 GB GARANSI RESMI SEIN",
          "childs": [],
          "url": "https://www.tokopedia.com/adelia89/hp-samsung-a30-4-64-gb-garansi-resmi-sein?trkid=f%3DCa24L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dsearch_page%3D1_ob%3D203_q%3Dsamsung_bmexp%3D21_po%3D7_catid%3D3054_bmexp%3D21&whid=0",
          "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/5/10/58705441/58705441_85d2c9f6-990e-4512-b4e8-ee8277bbe66f_700_700",
          "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/5/10/58705441/58705441_85d2c9f6-990e-4512-b4e8-ee8277bbe66f_700_700",
          "price": "Rp 2.410.000",
          "price_range": "",
          "wishlist": false,
          "whole_sale_price": [],
          "courier_count": 7,
          "condition": 1,
          "category_id": 65,
          "category_name": "Handphone & Tablet",
          "category_breadcrumb": "handphone-tablet/handphone/android-os",
          "department_id": 3054,
          "department_name": "Android OS",
          "free_ongkir": {
            "is_active": false,
            "img_url": ""
          },
          "labels": [],
          "label_groups": [],
          "badges": [
            {
              "title": "Power Badge",
              "image_url": "https://ecs7.tokopedia.net/img/power_merchant_badge.png",
              "show": true
            }
          ],
          "is_featured": 0,
          "rating": 5,
          "count_review": 48,
          "original_price": "",
          "discount_expired_time": "",
          "discount_start_time": "",
          "discount_percentage": 0,
          "sku": "",
          "stock": 9972,
          "ga_key": "/searchproduct/handphone-tablet/handphone/android-os/samsung/adelia89/hp-samsung-a30-4-64-gb-garansi-resmi-sein",
          "is_preorder": false,
          "shop": {
            "id": 5880689,
            "name": "Adelia_S",
            "url": "https://www.tokopedia.com/adelia89",
            "is_gold_shop": false,
            "is_power_badge": true,
            "location": "Jakarta Pusat",
            "city": "Jakarta Pusat",
            "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/5880689",
            "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=5880689",
            "is_official": false
          }
        },
        {
          "id": 569641896,
          "warehouse_id_default": 0,
          "name": "Samsung Galaxy A50S 4/64 Ram 4gb Rom 64gb Garansi Resmi",
          "childs": [],
          "url": "https://www.tokopedia.com/maxiphonecell/samsung-galaxy-a50s-4-64-ram-4gb-rom-64gb-garansi-resmi?trkid=f%3DCa24L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dsearch_page%3D1_ob%3D203_q%3Dsamsung_bmexp%3D21_po%3D8_catid%3D3054_bmexp%3D21&whid=0",
          "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/9/28/569641896/569641896_3a21ae42-10ee-45e6-b7ad-14c7a461de3b_1800_1800.jpg",
          "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/9/28/569641896/569641896_3a21ae42-10ee-45e6-b7ad-14c7a461de3b_1800_1800.jpg",
          "price": "Rp 3.350.000",
          "price_range": "",
          "wishlist": false,
          "whole_sale_price": [],
          "courier_count": 9,
          "condition": 1,
          "category_id": 65,
          "category_name": "Handphone & Tablet",
          "category_breadcrumb": "handphone-tablet/handphone/android-os",
          "department_id": 3054,
          "department_name": "Android OS",
          "free_ongkir": {
            "is_active": false,
            "img_url": ""
          },
          "labels": [
            {
              "title": "Tukar Tambah",
              "color": "#42b549"
            }
          ],
          "label_groups": [
            {
              "position": "offers",
              "type": "lightGrey",
              "title": "Bisa Tukar Tambah"
            }
          ],
          "badges": [
            {
              "title": "Official Store",
              "image_url": "https://ecs7.tokopedia.net/img/official_store_badge.png",
              "show": true
            }
          ],
          "is_featured": 0,
          "rating": 5,
          "count_review": 178,
          "original_price": "",
          "discount_expired_time": "",
          "discount_start_time": "",
          "discount_percentage": 0,
          "sku": "",
          "stock": 61,
          "ga_key": "/searchproduct/handphone-tablet/handphone/android-os/samsung/maxiphonecell/samsung-galaxy-a50s-4-64-ram-4gb-rom-64gb-garansi-resmi",
          "is_preorder": false,
          "shop": {
            "id": 3102419,
            "name": "Maxi phone cell",
            "url": "https://www.tokopedia.com/maxiphonecell",
            "is_gold_shop": false,
            "is_power_badge": true,
            "location": "Jakarta Pusat",
            "city": "Jakarta Pusat",
            "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/3102419",
            "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=3102419",
            "is_official": true
          }
        }
      ]
    }
}
""".trimIndent().replace("\n", "")

val searchProductModelMultipleCategories: SearchProductModel = Gson().fromJson(searchProductModelMultipleCategoriesJSON, SearchProductModel::class.java)
