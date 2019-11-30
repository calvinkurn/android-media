package com.tokopedia.similarsearch.viewmodel.testinstance

import com.google.gson.Gson
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel

private val similarProductModelOriginalProductWishlistedJSONString = """
{
    "similar_products_image_search": {
      "data": {
        "products": [
          {
            "id": 465335395,
            "name": "Samsung Galaxy A70 - 6GB/128GB - Black - Garansi Resmi 1 Tahun SEIN",
            "url": "https://www.tokopedia.com/ucell-cempaka/samsung-galaxy-a70-6gb-128gb-black-garansi-resmi-1-tahun-sein?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D1_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/7/28/2341036/2341036_e72ffa48-156d-48bb-8ff9-14cf3f11fecd_536_536",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/7/28/2341036/2341036_e72ffa48-156d-48bb-8ff9-14cf3f11fecd_536_536",
            "price": "Rp 4.950.000",
            "shop": {
              "id": 312816,
              "is_gold_shop": false,
              "location": "Jakarta",
              "city": "Jakarta",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/312816",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=312816",
              "is_official": true
            },
            "badges": [
              {
                "title": "Official Store",
                "image_url": "https://ecs7.tokopedia.net/img/official_store_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 5,
            "count_review": 8,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": false
          },
          {
            "id": 550040718,
            "name": "Samsung Galaxy A70 Ram 8/128 GB Garansi Resmi Sein - Hitam",
            "url": "https://www.tokopedia.com/enterphone2/samsung-galaxy-a70-ram-8-128-gb-garansi-resmi-sein-hitam?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D2_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/6/12/11126335/11126335_16b65442-e944-4c81-a57b-c2b45a2a5860_700_700",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/6/12/11126335/11126335_16b65442-e944-4c81-a57b-c2b45a2a5860_700_700",
            "price": "Rp 5.650.000",
            "shop": {
              "id": 1321037,
              "is_gold_shop": false,
              "location": "Jakarta",
              "city": "Jakarta",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/1321037",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=1321037",
              "is_official": true
            },
            "badges": [
              {
                "title": "Official Store",
                "image_url": "https://ecs7.tokopedia.net/img/official_store_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 0,
            "count_review": 0,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": true
          },
          {
            "id": 555072135,
            "name": "Samsung Galaxy A70 Ram 8/128 GB Garansi Resmi Sein - Biru",
            "url": "https://www.tokopedia.com/enterphone2/samsung-galaxy-a70-ram-8-128-gb-garansi-resmi-sein-biru?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D3_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/6/12/11126335/11126335_16b65442-e944-4c81-a57b-c2b45a2a5860_700_700",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/6/12/11126335/11126335_16b65442-e944-4c81-a57b-c2b45a2a5860_700_700",
            "price": "Rp 5.699.000",
            "shop": {
              "id": 1321037,
              "is_gold_shop": false,
              "location": "Jakarta",
              "city": "Jakarta",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/1321037",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=1321037",
              "is_official": true
            },
            "badges": [
              {
                "title": "Official Store",
                "image_url": "https://ecs7.tokopedia.net/img/official_store_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 0,
            "count_review": 0,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": false
          },
          {
            "id": 483396058,
            "name": "Handphone samsung A-70 128gb/6gb white khusus surabaya",
            "url": "https://www.tokopedia.com/gunungsariintan/handphone-samsung-a-70-128gb-6gb-white-khusus-surabaya?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D4_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/5/30/465533/465533_8c8419dd-491d-4d35-b677-8634529e23dc_700_700.jpg",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/5/30/465533/465533_8c8419dd-491d-4d35-b677-8634529e23dc_700_700.jpg",
            "price": "Rp 5.395.000",
            "shop": {
              "id": 465533,
              "is_gold_shop": false,
              "location": "Surabaya",
              "city": "Surabaya",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/465533",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=465533",
              "is_official": true
            },
            "badges": [
              {
                "title": "Official Store",
                "image_url": "https://ecs7.tokopedia.net/img/official_store_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 5,
            "count_review": 2,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": false
          },
          {
            "id": 477806468,
            "name": "Samsung Galaxy A70 128 GB Garansi Resmi SEIN - Hitam",
            "url": "https://www.tokopedia.com/richcell/samsung-galaxy-a70-128-gb-garansi-resmi-sein-hitam?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D5_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/5/20/2254690/2254690_e39bac8d-f662-4de3-8770-c4c61f57ed8c_1066_1066.jpg",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/5/20/2254690/2254690_e39bac8d-f662-4de3-8770-c4c61f57ed8c_1066_1066.jpg",
            "price": "Rp 4.875.000",
            "shop": {
              "id": 2254690,
              "is_gold_shop": false,
              "location": "Surabaya",
              "city": "Surabaya",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/2254690",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=2254690",
              "is_official": false
            },
            "badges": [
              {
                "title": "Power Badge",
                "image_url": "https://ecs7.tokopedia.net/img/power_merchant_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 0,
            "count_review": 0,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": false
          },
          {
            "id": 539668533,
            "name": "Samsung Galaxy A70 8GB A705G Blue",
            "url": "https://www.tokopedia.com/samsung/samsung-galaxy-a70-8gb-a705g-blue?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D6_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/9/11/21722219/21722219_7390eeeb-145e-48bf-8999-54c2b5692993_600_600",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/9/11/21722219/21722219_7390eeeb-145e-48bf-8999-54c2b5692993_600_600",
            "price": "Rp 5.999.000",
            "shop": {
              "id": 2400899,
              "is_gold_shop": false,
              "location": "Jakarta",
              "city": "Jakarta",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/2400899",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=2400899",
              "is_official": true
            },
            "badges": [
              {
                "title": "Official Store",
                "image_url": "https://ecs7.tokopedia.net/img/official_store_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 5,
            "count_review": 8,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": false
          },
          {
            "id": 571884435,
            "name": "Samsung Galaxy A70 8GB/128GB 8/128 Garansi Resmi SEIN Hitam-Black 2019",
            "url": "https://www.tokopedia.com/miworldhome/samsung-galaxy-a70-8gb-128gb-8-128-garansi-resmi-sein-hitam-black-2019?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D7_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/9/27/156959135805212/156959135805212_76a23cb4-dd2c-49d9-9a28-c533a138dbdc.png",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/attachment/2019/9/27/156959135805212/156959135805212_76a23cb4-dd2c-49d9-9a28-c533a138dbdc.png",
            "price": "Rp 5.510.000",
            "shop": {
              "id": 3252295,
              "is_gold_shop": false,
              "location": "Jakarta",
              "city": "Jakarta",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/3252295",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=3252295",
              "is_official": false
            },
            "badges": [
              {
                "title": "Power Badge",
                "image_url": "https://ecs7.tokopedia.net/img/power_merchant_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 5,
            "count_review": 1,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": false
          },
          {
            "id": 571883050,
            "name": "Samsung Galaxy A70 8GB/128GB 8/128 Garansi Resmi SEIN Black-Hitam 2019",
            "url": "https://www.tokopedia.com/miworldhome/samsung-galaxy-a70-8gb-128gb-8-128-garansi-resmi-sein-black-hitam-2019?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D8_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/9/27/156959137835128/156959137835128_a41d19fb-47cd-4d80-8476-4ff22fdef0e5.png",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/attachment/2019/9/27/156959137835128/156959137835128_a41d19fb-47cd-4d80-8476-4ff22fdef0e5.png",
            "price": "Rp 5.485.000",
            "shop": {
              "id": 3252295,
              "is_gold_shop": false,
              "location": "Jakarta",
              "city": "Jakarta",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/3252295",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=3252295",
              "is_official": false
            },
            "badges": [
              {
                "title": "Power Badge",
                "image_url": "https://ecs7.tokopedia.net/img/power_merchant_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 0,
            "count_review": 0,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": false
          },
          {
            "id": 540608575,
            "name": "samsung galaxy A70 8/128 garansi resmi samsung indonesia / SEIN - Hitam",
            "url": "https://www.tokopedia.com/idolacellular/samsung-galaxy-a70-8-128-garansi-resmi-samsung-indonesia-sein-hitam?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D9_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/8/23/2570214/2570214_e1c3ba3d-870c-45ae-9ebd-b720f769ad6a_700_700.jpg",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/8/23/2570214/2570214_e1c3ba3d-870c-45ae-9ebd-b720f769ad6a_700_700.jpg",
            "price": "Rp 5.699.999",
            "shop": {
              "id": 2570214,
              "is_gold_shop": false,
              "location": "Jakarta",
              "city": "Jakarta",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/2570214",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=2570214",
              "is_official": false
            },
            "badges": [
              {
                "title": "Power Badge",
                "image_url": "https://ecs7.tokopedia.net/img/power_merchant_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 0,
            "count_review": 0,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": false
          },
          {
            "id": 550759011,
            "name": "SAMSUNG A50 RAM 6/128GB GARANSI RESMI",
            "url": "https://www.tokopedia.com/mystery-shop/samsung-a50-ram-6-128gb-garansi-resmi?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D10_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/9/4/6704283/6704283_dd71ea71-5c91-4d7c-ae1e-8e8de4f5ddac_700_700.jpg",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/9/4/6704283/6704283_dd71ea71-5c91-4d7c-ae1e-8e8de4f5ddac_700_700.jpg",
            "price": "Rp 4.050.000",
            "shop": {
              "id": 6704283,
              "is_gold_shop": false,
              "location": "Jakarta",
              "city": "Jakarta",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/6704283",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=6704283",
              "is_official": false
            },
            "badges": [
              {
                "title": "Power Badge",
                "image_url": "https://ecs7.tokopedia.net/img/power_merchant_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 5,
            "count_review": 2,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": false
          },
          {
            "id": 548393731,
            "name": "Samsung Galaxy A70 - 8/128 GB - Garansi Resmi Sein",
            "url": "https://www.tokopedia.com/zenzpro/samsung-galaxy-a70-8-128-gb-garansi-resmi-sein?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D11_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/9/1/5966415/5966415_eb1320db-2aa9-496e-b945-56542ba881ca_700_700.jpg",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/9/1/5966415/5966415_eb1320db-2aa9-496e-b945-56542ba881ca_700_700.jpg",
            "price": "Rp 6.250.000",
            "shop": {
              "id": 5966415,
              "is_gold_shop": false,
              "location": "Jakarta",
              "city": "Jakarta",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/5966415",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=5966415",
              "is_official": false
            },
            "badges": [
              {
                "title": "Power Badge",
                "image_url": "https://ecs7.tokopedia.net/img/power_merchant_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 5,
            "count_review": 2,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": false
          },
          {
            "id": 507038807,
            "name": "Samsung Galaxy A70 6GB 128GB 6/128GB Garansi Resmi SEIN - Hitam",
            "url": "https://www.tokopedia.com/garasii/samsung-galaxy-a70-6gb-128gb-6-128gb-garansi-resmi-sein-hitam?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D12_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/7/10/5295084/5295084_6004c125-4d7b-4933-9de6-d66cb696fce0_800_800",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/7/10/5295084/5295084_6004c125-4d7b-4933-9de6-d66cb696fce0_800_800",
            "price": "Rp 5.079.000",
            "shop": {
              "id": 631036,
              "is_gold_shop": false,
              "location": "Jakarta",
              "city": "Jakarta",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/631036",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=631036",
              "is_official": true
            },
            "badges": [
              {
                "title": "Official Store",
                "image_url": "https://ecs7.tokopedia.net/img/official_store_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 0,
            "count_review": 0,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": false
          },
          {
            "id": 555561343,
            "name": "SAMSUNG GALAXI A70 - Putih",
            "url": "https://www.tokopedia.com/servisciacaem/samsung-galaxi-a70-putih?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D13_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/9/10/6778162/6778162_a40d1a8e-2f03-40d2-9e66-8f5d23844c4a_700_700.jpg",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/9/10/6778162/6778162_a40d1a8e-2f03-40d2-9e66-8f5d23844c4a_700_700.jpg",
            "price": "Rp 5.000.000",
            "shop": {
              "id": 6778162,
              "is_gold_shop": false,
              "location": "Medan",
              "city": "Medan",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/6778162",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=6778162",
              "is_official": false
            },
            "badges": [
              {
                "title": "Power Badge",
                "image_url": "https://ecs7.tokopedia.net/img/power_merchant_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 0,
            "count_review": 0,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": false
          },
          {
            "id": 472516506,
            "name": "Samsung A70 - 6/128GB - Garansi Resmi",
            "url": "https://www.tokopedia.com/wiracell58/samsung-a70-6-128gb-garansi-resmi?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D14_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/8/17/50151595/50151595_2b5c276f-b99a-4a79-afa9-e66c7eadb408_700_700",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/8/17/50151595/50151595_2b5c276f-b99a-4a79-afa9-e66c7eadb408_700_700",
            "price": "Rp 5.199.000",
            "shop": {
              "id": 2427698,
              "is_gold_shop": false,
              "location": "Makassar",
              "city": "Makassar",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/2427698",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=2427698",
              "is_official": false
            },
            "badges": [
              {
                "title": "Power Badge",
                "image_url": "https://ecs7.tokopedia.net/img/power_merchant_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 5,
            "count_review": 4,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": false
          },
          {
            "id": 520759787,
            "name": "Samsung Galaxy A70 (6GB/128GB) - Black",
            "url": "https://www.tokopedia.com/ufoelektronika/samsung-galaxy-a70-6gb-128gb-black?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D15_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/7/27/15951098/15951098_4ab6f100-dbea-4eea-b23b-de5331bea3c3_400_400",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/7/27/15951098/15951098_4ab6f100-dbea-4eea-b23b-de5331bea3c3_400_400",
            "price": "Rp 5.799.000",
            "shop": {
              "id": 1810008,
              "is_gold_shop": false,
              "location": "Surabaya",
              "city": "Surabaya",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/1810008",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=1810008",
              "is_official": true
            },
            "badges": [
              {
                "title": "Official Store",
                "image_url": "https://ecs7.tokopedia.net/img/official_store_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 0,
            "count_review": 0,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": false
          },
          {
            "id": 450683630,
            "name": "Samsung Galaxy A70 6GB/128GB - Hitam",
            "url": "https://www.tokopedia.com/indokom/samsung-galaxy-a70-6gb-128gb-hitam?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D16_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/4/18/2573120/2573120_fc76ce5a-8d95-40f9-8ba5-589d2ff00ecc_800_800.jpg",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/4/18/2573120/2573120_fc76ce5a-8d95-40f9-8ba5-589d2ff00ecc_800_800.jpg",
            "price": "Rp 5.529.000",
            "shop": {
              "id": 339707,
              "is_gold_shop": false,
              "location": "Jakarta",
              "city": "Jakarta",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/339707",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=339707",
              "is_official": true
            },
            "badges": [
              {
                "title": "Official Store",
                "image_url": "https://ecs7.tokopedia.net/img/official_store_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 0,
            "count_review": 0,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": false
          }
        ],
        "originalProduct": {
          "id": 553354058,
          "name": "SAMSUNG Galaxy A70 ~ 8 / 128 Garansi Resmi Sein",
          "url": "https://www.tokopedia.com/krisv/samsung-galaxy-a70-8-128-garansi-resmi-sein-biru?trkid=f%3DCa0000L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D32_q%3D_po%3D1_catid%3D3054&whid=0",
          "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/8/29/2779938/2779938_0ce25b8a-e5ca-4aca-8e44-4530cf907169_1080_1080.jpg",
          "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/8/29/2779938/2779938_0ce25b8a-e5ca-4aca-8e44-4530cf907169_1080_1080.jpg",
          "price": "Rp 5.675.000",
          "shop": {
            "id": 2779938,
            "is_gold_shop": false,
            "location": "Jakarta",
            "city": "Jakarta",
            "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/2779938",
            "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=2779938",
            "is_official": false
          },
          "badges": [
            {
              "title": "Power Badge",
              "image_url": "https://ecs7.tokopedia.net/img/power_merchant_badge.png"
            }
          ],
          "category_id": 65,
          "category_name": "Handphone & Tablet",
          "rating": 0,
          "count_review": 0,
          "original_price": "",
          "discount_expired_time": "",
          "discount_start_time": "",
          "discount_percentage": 0,
          "wishlist": true
        }
      }
    }
}
""".trimIndent().replace("\n", "")

internal fun getSimilarProductModelOriginalProductWishlisted() = Gson().fromJson(similarProductModelOriginalProductWishlistedJSONString, SimilarProductModel::class.java)