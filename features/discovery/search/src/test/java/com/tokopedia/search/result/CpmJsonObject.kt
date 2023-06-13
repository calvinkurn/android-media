package com.tokopedia.search.result

import org.json.JSONObject

internal val cpmJsonString = """
  {
    "status": {
      "error_code": 0,
      "message": "OK"
    },
    "header": {
      "process_time": 0.049827741,
      "total_data": 1
    },
    "data": [
      {
        "id": "36627880",
        "ad_ref_key": "36627880",
        "redirect": "https://www.tokopedia.com/myiphonestore1",
        "headline": {
          "name": "MY IPHONESTORE",
          "image": {
            "full_url": ""
          },
          "shop": {
            "id": "5233813",
            "name": "MY IPHONESTORE",
            "domain": "myiphonestore1",
            "tagline": "RAMAH, CEPAT DAN BERKUALITAS",
            "slogan": "remai",
            "location": "Kota Administrasi Jakarta Utara",
            "city": "Kota Administrasi Jakarta Utara",
            "gold_shop": true,
            "gold_shop_badge": true,
            "shop_is_official": false,
            "product": [
              {
                "id": "450081210",
                "name": "OPPO A 37 2+16GB",
                "price_format": "Rp 1.050.000",
                "applinks": "tokopedia://product/450081210",
                "image_product": {
                  "product_id": "450081210",
                  "product_name": "OPPO A 37 2+16GB",
                  "image_url": "",
                  "image_click_url": ""
                }
              }
            ],
            "image_shop": {
              "cover": "",
              "s_url": "",
              "xs_url": "",
              "cover_ecs": "",
              "s_ecs": "",
              "xs_ecs": ""
            }
          },
          "badges": [
            {
              "image_url": "https://images.tokopedia.net/ta/icon/badge/PM-Badge-80.png",
              "show": true,
              "title": "Power Merchant"
            }
          ],
          "button_text": "Kunjungi Toko",
          "promoted_text": "Promoted by",
          "description": "RAMAH, CEPAT DAN BERKUALITAS",
          "uri": "https://www.tokopedia.com/myiphonestore1"
        },
        "applinks": "tokopedia://shop/5233813"
      }
    ]
  }
""".trimIndent().replace("\n", "")

val cpmJSONObject: JSONObject = JSONObject(cpmJsonString)

val notCpmShopJsonString = """
  {
    "status": {
      "error_code": 0,
      "message": "OK"
    },
    "header": {
      "process_time": 0.013758523,
      "total_data": 1
    },
    "data": [
      {
        "id": "31418575",
        "ad_ref_key": "31418575",
        "redirect": "https://seller.tokopedia.com/edu/tips-menaikkan-posisi-produk/",
        "ad_click_url": "https://ta.tokopedia.com/promo/v1/clicks/8a-xgVY2gmUEHpJ7H_KOop1DUMVj9RzNrc1i6sHDUSC5rfB7q3YXUsthbm-7q3OBUstho_rhH_nFHsKpbm-xgVYpyfYagZUEHmdFHpy76AydHpepH_eao_ydHAeDUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_1o-r7BW_sCsQABE3BPc8ujagfBvq1BR_c2CoJN1e_Vo8BuE_S2SP7NEgpu6zcP2_JoGrB2yQAoiH7203Ao6qMUpZMh-qMY2_1o-r7BWo9giQ1NDZ325q1Y93_C6zcra_BzVPVB1__-qepnF3jzSH7h9z926qjV73uzSPOBWu_VgqMzt_uPuH1B2PfBiH72F3s-DPuKa1MhszsBF3jo-ojBke3BHe72fyfODQMV9o3gsZJ2f3jx3qMzc69Cqe7jfZ32Cq1hAZSgsQ3hXyurOgMHaqIgszsBF3jo-ojBke3BHe72dgVP383u36_CgufzEZ325q1B2PfBsHjNfyfO3gBBXZSgjH7NDZ325q1OAoIPo8BjF_7zgoJOE192ouVVN_BzgoJOJ13BHe72E3_UN8u23692qu7gN3_-Sq1Y2Z9PHqjVE_M2zoJNkgR26zJOd_jz-H1NkgRP6e7tNUiFiP9oBrBY2gmUEUsHdosUhoAribm-pg9opq3YX9fBjUstiy7VD63N5QMKNQJjEeuCC6_ViZVoD8Mxc_MzIH1OzrSPZ9fyaPJhtzpuio3oB9fuc_sgfyjuMQJxCQ1PtrpujHpujr12b3A1FrMBIu7h2_p-D_AP7Z7FFoJxq1VzHb1BzgAJR89gjZ_CNq1z7uszxZAogH9x1rBCVzVo0qjP2ZVzAgO-RZfNt8J1NeBCMzsVHQ_g9_cOAqJ1iwe?is_search=1&ab_test=N&template_id_used=0&cpm_template_req=3&click_ad_type=1&src=search&number_of_ads=1&number_ads_req=1&r=https%3A%2F%2Fseller.tokopedia.com%2Fedu%2Ftips-menaikkan-posisi-produk%2F&alg=def&keywords=redmi+note+5&page=1&keyword_correction=redmi+note+5&sid=cAl9nonh9lI%3AAPA91bHSlzhFNdG1MQrwR_f2tLhG5b5ce_eFN6vbEflHAmGhs5d35dqJKX50riGWLiO2lL7tKL04HZPTL-IQd17yvdI0yiDtV4aH3Y1xTrPEDSkjGiHTCgRwKnhxE9BPfF1Lm6WLmChE&uid=3862147&t=android&n_candidate_ads=1",
        "headline": {
          "template_id": "1",
          "name": "AWARENESS",
          "image": {
            "full_url": "https://ta.tokopedia.com/promo/v1/views/8a-2rOYDQfPwgcV7yZUEHZFiy3zwq3ei6sHhoAJdo_rObm-xgVY789CBUstpbm-FQRo2PcB5QiUEHZFiPcBWgZUEH_1RH_JFHAUdHaFiy3zwrfo5rM1i6snXHAHfoAKf6AH7HpJ7Hs1f6An7bm-pHOYDQfri6i-B812kgJxGgBBXZSgjH7NDZ325q1OAZ9o-Q1dFyfFN8B29zSBgHMP2_fB-P7hke_z6uJJh_92u8jNEgRP68MrO_jzSq1hAZS-q3cFpysoGqOKp_M2iH72DZ325q1OAZ9o-Q_ufyMO6QJBkQfBvuOjF_jzSHjN1z9zguJ7a3jHFHV2JgpoHu7uE_M2CPVB1gRPgQu1h332jqJO9u_V-q9P2ysoGrVtaQIuyHB-Dy7yNrV2AZ_g-qjV2_JoGPMoWQcNxupuMy7xGPB2UuM2jzsBF3jo-ojBke3BHe72fyfODQMV9o3gsHMxfy7yNrV2AZ_g-qjV2_JoG8cz9uSBBusjF3uPj8jBkQfB-q9P2yp-6PMoWuMggQj2fgAo6QJBkQfBoepzR_92gHJYJ3_zo8BVE_uzz81N13_zozVV2_JoG8Bja69BqusBE3BPc8ujagfBvq1BR_c2C8jNk1_z6qMPE_jzo8JNJZ_V6qMPR_joG6ZUDUSupg9-wq3ei6iUp6AyaH_eRUiFirfuprfB5QBY2gmUEUMoCQABXQfNt63h-6jVee_jhyjx_QI2tzjNjzpVo19-R1BYMHSzHqJrOysusguYBzjdfPM-VgMhUe3OIqIHOgAHOgIVGZOKOHI-2zOPHq14aQJFRPJWHHAzU3BC1_mO-13ehoRBfgJjF83BJPVy7y1Kp3_VduI-ez1z_qf2Iq1x1efPZP7WXqIxV61-egjyh_c7fu7hWefxVUS7?src=search&alg=def&is_search=1&number_of_ads=1&sid=cAl9nonh9lI%3AAPA91bHSlzhFNdG1MQrwR_f2tLhG5b5ce_eFN6vbEflHAmGhs5d35dqJKX50riGWLiO2lL7tKL04HZPTL-IQd17yvdI0yiDtV4aH3Y1xTrPEDSkjGiHTCgRwKnhxE9BPfF1Lm6WLmChE&uid=3862147&n_candidate_ads=1&template_id_used=0&keyword_correction=redmi+note+5&t=android&keywords=redmi+note+5&number_ads_req=1&ab_test=N&page=1&cpm_template_req=3",
            "full_ecs": ""
          },
          "shop": {
            "id": "",
            "name": "",
            "domain": "",
            "tagline": "",
            "slogan": "",
            "location": "",
            "city": "",
            "gold_shop": false,
            "gold_shop_badge": false,
            "shop_is_official": false,
            "product": [],
            "image_shop": {
              "cover": "",
              "s_url": "",
              "xs_url": "",
              "cover_ecs": "",
              "s_ecs": "",
              "xs_ecs": ""
            }
          },
          "badges": [],
          "button_text": "",
          "promoted_text": "",
          "description": "",
          "uri": "https://seller.tokopedia.com/edu/tips-menaikkan-posisi-produk/"
        },
        "applinks": ""
      }
    ]
  }
""".trimIndent().replace("\n", "")

internal val notCpmShopJsonObject: JSONObject = JSONObject(notCpmShopJsonString)
