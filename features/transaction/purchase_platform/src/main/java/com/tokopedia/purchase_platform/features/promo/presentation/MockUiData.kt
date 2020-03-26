package com.tokopedia.purchase_platform.features.promo.presentation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.*

fun mockPromoRecommendation(): PromoRecommendationUiModel {
    return PromoRecommendationUiModel(
            uiData = PromoRecommendationUiModel.UiData().apply {
                promoCount = 10
                promoTotalBenefit = 150000
            },
            uiState = PromoRecommendationUiModel.UiState().apply {
                isButtonSelectEnabled = true
            }
    )
}

fun mockPromoInput(): PromoInputUiModel {
    return PromoInputUiModel(
            uiData = PromoInputUiModel.UiData().apply {
                promoCode = ""
            },
            uiState = PromoInputUiModel.UiState().apply {
                isButtonSelectEnabled = false
                isError = false
            }
    )
}

fun mockEligibleHeader(): PromoEligibilityHeaderUiModel {
    return PromoEligibilityHeaderUiModel(
            uiData = PromoEligibilityHeaderUiModel.UiData().apply {
                title = "Pilih promo"
                subTitle = "Kamu bisa gabungkan promo biar makin hemat"
            },
            uiState = PromoEligibilityHeaderUiModel.UiState().apply {
                isEnabled = true
                isCollapsed = false
            }
    )
}

fun mockEligiblePromoGlobalSection(): List<Visitable<*>> {
    val dataList = ArrayList<Visitable<*>>()
    val promoListHeaderUiModel = PromoListHeaderUiModel(
            uiData = PromoListHeaderUiModel.UiData().apply {
                title = "Kupon saya global"
                subTitle = "Hanya bisa pilih 1"
                iconUrl = ""
                identifierId = 1
            },
            uiState = PromoListHeaderUiModel.UiState().apply {
                isCollapsed = false
                isEnabled = true
            }
    )
    dataList.add(promoListHeaderUiModel)

    val promoListItemUiModel = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                parentIdentifierId = 1
                title = "Promo pertama"
                subTitle = "Berakhir 1 jam lagi"
                promoCode = "TOKOPEDIACASHBACK"
            },
            uiState = PromoListItemUiModel.UiState().apply {

            }
    )
    dataList.add(promoListItemUiModel)

    val promoListItemUiModel1 = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                parentIdentifierId = 1
                title = "Promo kedua"
                subTitle = "Berakhir 2 jam lagi"
                errorMessage = "Kena Error"
            },
            uiState = PromoListItemUiModel.UiState().apply {

            }
    )
    dataList.add(promoListItemUiModel1)

    val promoListItemUiModel2 = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                parentIdentifierId = 1
                title = "Promo ketiga"
                subTitle = "Berakhir 3 jam lagi"
                imageResourceUrls = listOf("https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg")
            },
            uiState = PromoListItemUiModel.UiState().apply {

            }
    )
    dataList.add(promoListItemUiModel2)

    return dataList
}

fun mockEligiblePromoGoldMerchantSection(): List<Visitable<*>> {
    val dataList = ArrayList<Visitable<*>>()
    val promoListItemUiModel3 = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                parentIdentifierId = 2
                title = "Promo pertama stage 2"
                subTitle = "Berakhir 2 jam lagi"
            },
            uiState = PromoListItemUiModel.UiState().apply {

            }
    )
    val promoListHeaderUiModel1 = PromoListHeaderUiModel(
            uiData = PromoListHeaderUiModel.UiData().apply {
                identifierId = 2
                title = "Ini promo power merchant"
                subTitle = "Hanya bisa pilih 1"
                iconUrl = ""
                tmpPromoItemList = arrayListOf(promoListItemUiModel3)
            },
            uiState = PromoListHeaderUiModel.UiState().apply {
                isCollapsed = true
                isEnabled = true
            }
    )
    dataList.add(promoListHeaderUiModel1)

    return dataList
}

fun mockEligiblePromoOfficialStoreSection(): List<Visitable<*>> {
    val dataList = ArrayList<Visitable<*>>()
    val promoListItemUiModel4 = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                parentIdentifierId = 3
                title = "Promo kedua"
                subTitle = "Berakhir 2 jam lagi"
                errorMessage = "Kena Error"
            },
            uiState = PromoListItemUiModel.UiState().apply {

            }
    )
    val promoListHeaderUiModel2 = PromoListHeaderUiModel(
            uiData = PromoListHeaderUiModel.UiData().apply {
                identifierId = 3
                title = "Ini promo official store"
                subTitle = "Hanya bisa pilih 1"
                iconUrl = ""
                tmpPromoItemList = arrayListOf(promoListItemUiModel4)
            },
            uiState = PromoListHeaderUiModel.UiState().apply {
                isCollapsed = true
                isEnabled = true
            }
    )
    dataList.add(promoListHeaderUiModel2)

    return dataList
}

fun mockIneligibleHeader(): PromoEligibilityHeaderUiModel {
    return PromoEligibilityHeaderUiModel(
            uiData = PromoEligibilityHeaderUiModel.UiData().apply {
                title = "Kupon yang belum bisa dipakai"
            },
            uiState = PromoEligibilityHeaderUiModel.UiState().apply {
                isEnabled = false
                isCollapsed = false
            }
    )
}

fun mockIneligiblePromoGlobalSection(): List<Visitable<*>> {
    val dataList = ArrayList<Visitable<*>>()
    val promoListHeaderUiModel = PromoListHeaderUiModel(
            uiData = PromoListHeaderUiModel.UiData().apply {
                title = "Kupon saya global"
                subTitle = "Hanya bisa pilih 1"
                iconUrl = ""
                identifierId = 11
            },
            uiState = PromoListHeaderUiModel.UiState().apply {
                isCollapsed = false
                isEnabled = false
            }
    )
    dataList.add(promoListHeaderUiModel)

    val promoListItemUiModel = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                parentIdentifierId = 11
                title = "Promo pertama"
                subTitle = "Berakhir 1 jam lagi"
                errorMessage = "Tambah Rp27.500 untuk pakai promo."
                imageResourceUrls = listOf("https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg")
            },
            uiState = PromoListItemUiModel.UiState().apply {

            }
    )
    dataList.add(promoListItemUiModel)

    val promoListItemUiModel1 = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                parentIdentifierId = 11
                title = "Promo kedua"
                subTitle = "Berakhir 2 jam lagi"
                errorMessage = "Tambah Rp10.000 untuk pakai promo."
            },
            uiState = PromoListItemUiModel.UiState().apply {

            }
    )
    dataList.add(promoListItemUiModel1)

    val promoListItemUiModel2 = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                parentIdentifierId = 11
                title = "Promo ketiga"
                subTitle = "Berakhir 3 jam lagi"
                errorMessage = "Belanja di kategori fashion wanita, kecantikan atau fashion pria untuk pakai promo ini. "
            },
            uiState = PromoListItemUiModel.UiState().apply {

            }
    )
    dataList.add(promoListItemUiModel2)

    return dataList
}

fun mockIneligiblePromoGoldMerchantSection(): List<Visitable<*>> {
    val dataList = ArrayList<Visitable<*>>()
    val promoListHeaderUiModel1 = PromoListHeaderUiModel(
            uiData = PromoListHeaderUiModel.UiData().apply {
                identifierId = 22
                title = "Ini promo power merchant"
                subTitle = "Hanya bisa pilih 1"
                iconUrl = ""
                tmpPromoItemList = emptyList()
            },
            uiState = PromoListHeaderUiModel.UiState().apply {
                isCollapsed = false
                isEnabled = false
            }
    )
    dataList.add(promoListHeaderUiModel1)

    val promoListItemUiModel3 = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                parentIdentifierId = 22
                title = "Promo pertama stage 2"
                subTitle = "Berakhir 2 jam lagi"
                errorMessage = "Tambah Rp27.500 untuk pakai promo."
            },
            uiState = PromoListItemUiModel.UiState().apply {

            }
    )
    dataList.add(promoListItemUiModel3)

    return dataList
}

fun mockIneligiblePromoOfficialStoreSection(): List<Visitable<*>> {
    val dataList = ArrayList<Visitable<*>>()
    val promoListHeaderUiModel2 = PromoListHeaderUiModel(
            uiData = PromoListHeaderUiModel.UiData().apply {
                identifierId = 33
                title = "Ini promo official store"
                subTitle = "Hanya bisa pilih 1"
                iconUrl = ""
                tmpPromoItemList = emptyList()
            },
            uiState = PromoListHeaderUiModel.UiState().apply {
                isCollapsed = false
                isEnabled = false
            }
    )
    dataList.add(promoListHeaderUiModel2)

    val promoListItemUiModel4 = PromoListItemUiModel(
            uiData = PromoListItemUiModel.UiData().apply {
                parentIdentifierId = 33
                title = "Promo kedua"
                subTitle = "Berakhir 2 jam lagi"
                errorMessage = "Tambah Rp27.500 untuk pakai promo."
            },
            uiState = PromoListItemUiModel.UiState().apply {

            }
    )
    dataList.add(promoListItemUiModel4)

    return dataList
}

fun mockEmptyState(): PromoEmptyStateUiModel {
    return PromoEmptyStateUiModel(
            uiData = PromoEmptyStateUiModel.UiData().apply {
                title = "Yaah, kamu belum punya kupon belanja"
                description = "Ini Sub Title"
                imageUrl = "https://ecs7.tokopedia.net/img/blog/seller/2019/06/newpm-cta-bottom-bg.jpg"
                buttonText = "Click Me"
            },
            uiState = PromoEmptyStateUiModel.UiState().apply {
                isShowButton = true
            }
    )
}

val MOCK_RESPONSE = """
    {
    "coupon_list_recommendation": {
      "message": [],
      "error_code": "200",
      "status": "OK",
      "data": {
        "result_status": {
          "code": "200",
          "message": [
            "Success"
          ],
          "reason": "OK"
        },
        "empty_state": {
          "title": "",
          "description": "",
          "image_url": ""
        },
        "title": "Pilih Promo",
        "sub_title": "Kamu bisa gabungkan promo!",
        "promo_recommendation": {
          "codes": [
            "PROMO1",
            "PROMO2",
            "PROMO3"
          ],
          "message": "Kamu bisa hemat 1000 rupiah"
        },
        "coupon_sections": [
          {
            "title": "Pilih Promo",
            "sub_title": "Kamu bisa gabungkan promo biar makin hemat!",
            "icon_url": "",
            "is_enabled": true,
            "is_collapsed": false,
            "tags": [],
            "coupons": [],
            "sub_sections": [
              {
                "title": "Kupon Saya",
                "sub_title": "Promo dipilih",
                "icon_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                "is_enabled": true,
                "is_collapsed": false,
                "tags": [],
                "coupons": [
                  {
                    "code": "XXXXX",
                    "title": "Gratis Ongkir 20rb",
                    "message": "",
                    "expiry_info": "berakhir 3 hari lagi",
                    "expiry_count_down": 100000,
                    "coupon_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpgm",
                    "coupon_app_link": "//tokopedia",
                    "unique_id": "cart-string",
                    "shop_id": 0,
                    "tag_image_urls": [
                        "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                        "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                        "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                        "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg"
                    ],
                    "benefit_amount": 20000,
                    "is_recommended": false,
                    "is_selected": false,
                    "is_attempted": false,
                    "radio_check_state": "disabled",
                    "clashing_infos": [
                      {
                        "code": "PROMO2",
                        "message": "Kupon ini ga bisa dipake bersamaan dengan Promo PROMO2"
                      },
                      {
                        "code": "PROMO3",
                        "message": "Kupon ini ga bisa dipake bersamaan dengan Promo PROMO3"
                      }
                    ]
                  },
                  {
                    "code": "PROMO1",
                    "title": "Cashback 10rb",
                    "message": "",
                    "expiry_info": "berakhir 6 hari lagi",
                    "expiry_count_down": 200000,
                    "coupon_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                    "coupon_app_link": "//tokopedia",
                    "unique_id": "cart-string",
                    "shop_id": 0,
                    "tag_image_urls": [
                        "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                        "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                        "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg"
                    ],
                    "benefit_amount": 50000,
                    "is_recommended": true,
                    "is_selected": false,
                    "is_attempted": false,
                    "radio_check_state": "enabled",
                    "clashing_infos": []
                  }
                ]
              },
              {
                "title": "Apple Store",
                "sub_title": "Toko Apel",
                "icon_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                "is_enabled": true,
                "is_collapsed": true,
                "tags": [
                  "Promo dipilih"
                ],
                "coupons": [
                  {
                    "code": "PROMO2",
                    "title": "Gratis Ongkir 30rb",
                    "message": "Kupon ini berlaku untuk pembelian kategori fashion",
                    "expiry_info": "berakhir 10 jam lagi",
                    "expiry_count_down": 10001,
                    "coupon_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                    "coupon_app_link": "//tokopedia",
                    "unique_id": "cart-string",
                    "shop_id": 102,
                    "tag_image_urls": [
                      "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg"
                    ],
                    "benefit_amount": 30000,
                    "is_recommended": true,
                    "is_selected": false,
                    "is_attempted": false,
                    "radio_check_state": "enabled",
                    "clashing_infos": [
                      {
                        "code": "XXXXX",
                        "message": "Kupon ini ga bisa dipake bersamaan dengan Promo XXXXX"
                      }
                    ]
                  }
                ]
              },
              {
                "title": "Banana Store",
                "sub_title": "Toko Pisang",
                "icon_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                "is_enabled": true,
                "is_collapsed": false,
                "tags": [],
                "coupons": [
                  {
                    "code": "PROMO3",
                    "title": "Gratis Ongkir 60rb",
                    "message": "Kupon ini berlaku untuk pembelian mainan",
                    "expiry_info": "berakhir 3 jam lagi",
                    "expiry_count_down": 2345,
                    "coupon_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                    "coupon_app_link": "//tokopedia",
                    "unique_id": "cart-string",
                    "shop_id": 103,
                    "tag_image_urls": [
                      "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                      "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg"
                    ],
                    "benefit_amount": 60000,
                    "is_recommended": true,
                    "is_selected": true,
                    "is_attempted": false,
                    "radio_check_state": "enabled",
                    "clashing_infos": [
                      {
                        "code": "XXXXX",
                        "message": "Kupon ini ga bisa dipake bersamaan dengan Promo XXXXX"
                      }
                    ]
                  }
                ]
              }
            ]
          },
          {
            "title": "Kupon yang tidak bisa dipakai",
            "sub_title": "kuponnya ga bisa dipake ya",
            "icon_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
            "is_enabled": false,
            "is_collapsed": false,
            "tags": [],
            "coupons": [],
            "sub_sections": [
              {
                "title": "Kupon Saya Not available",
                "sub_title": "Not avalibale",
                "icon_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                "is_enabled": false,
                "is_collapsed": false,
                "tags": [],
                "coupons": [
                  {
                    "code": "YYYYYY",
                    "title": "Gratis Ongkir 20rb",
                    "message": "Kupon ini berlaku untuk pembelian kategori fashion",
                    "expiry_info": "berakhir 3 hari lagi",
                    "expiry_count_down": 100000,
                    "coupon_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                    "coupon_app_link": "//tokopedia",
                    "unique_id": "cart-string",
                    "shop_id": 0,
                    "tag_image_urls": [
                      "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                      "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg"
                    ],
                    "benefit_amount": 20000,
                    "is_recommended": false,
                    "is_selected": false,
                    "is_attempted": false,
                    "radio_check_state": "hidden",
                    "clashing_infos": []
                  }
                ]
              },
                {
                  "title": "Kupon Saya Not available 2",
                  "sub_title": "Not avalibale",
                  "icon_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                  "is_enabled": false,
                  "is_collapsed": false,
                  "tags": [],
                  "coupons": [
                    {
                      "code": "YYYYYY1",
                      "title": "Gratis Ongkir 400rb",
                      "message": "Kupon ini berlaku untuk pembelian kategori fashion",
                      "expiry_info": "berakhir 3 hari lagi",
                      "expiry_count_down": 100000,
                      "coupon_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                      "coupon_app_link": "//tokopedia",
                      "unique_id": "cart-string",
                      "shop_id": 0,
                      "tag_image_urls": [
                        "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                        "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg"
                      ],
                      "benefit_amount": 20000,
                      "is_recommended": false,
                      "is_selected": false,
                      "is_attempted": false,
                      "radio_check_state": "hidden",
                      "clashing_infos": []
                    },
                    {
                      "code": "YYYYYY2",
                      "title": "Gratis Ongkir 30rb",
                      "message": "Kupon ini berlaku untuk pembelian kategori fashion",
                      "expiry_info": "berakhir 3 hari lagi",
                      "expiry_count_down": 100000,
                      "coupon_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                      "coupon_app_link": "//tokopedia",
                      "unique_id": "cart-string",
                      "shop_id": 0,
                      "tag_image_urls": [
                        "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                        "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg"
                      ],
                      "benefit_amount": 20000,
                      "is_recommended": false,
                      "is_selected": false,
                      "is_attempted": false,
                      "radio_check_state": "hidden",
                      "clashing_infos": []
                    },
                    {
                      "code": "YYYYYY3",
                      "title": "Gratis Ongkir 30rb",
                      "message": "Kupon ini berlaku untuk pembelian kategori fashion",
                      "expiry_info": "berakhir 3 hari lagi",
                      "expiry_count_down": 100000,
                      "coupon_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                      "coupon_app_link": "//tokopedia",
                      "unique_id": "cart-string",
                      "shop_id": 0,
                      "tag_image_urls": [
                        "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                        "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg"
                      ],
                      "benefit_amount": 20000,
                      "is_recommended": false,
                      "is_selected": false,
                      "is_attempted": false,
                      "radio_check_state": "hidden",
                      "clashing_infos": []
                    },
                    {
                      "code": "YYYYYY2",
                      "title": "Gratis Ongkir 30rb",
                      "message": "Kupon ini berlaku untuk pembelian kategori fashion",
                      "expiry_info": "berakhir 3 hari lagi",
                      "expiry_count_down": 100000,
                      "coupon_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                      "coupon_app_link": "//tokopedia",
                      "unique_id": "cart-string",
                      "shop_id": 0,
                      "tag_image_urls": [
                        "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                        "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg"
                      ],
                      "benefit_amount": 20000,
                      "is_recommended": false,
                      "is_selected": false,
                      "is_attempted": false,
                      "radio_check_state": "hidden",
                      "clashing_infos": []
                    }
                  ]
                }

            ]
          }
        ],
        "additional_message": "Kamu bisa hemat",
        "reward_points_info": {
          "message": "Transaksi pakai promo ga bisa dapet tokopoints",
          "gain_reward_points_tnc": {
            "title": "Syarat dapat point",
            "tnc_details": [
              {
                "icon_image_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                "description": "peraturan pertama"
              },
              {
                "icon_image_url": "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg",
                "description": "peraturan kedua"
              }
            ]
          }
        }
      }
    }
  }
""".trimIndent()

val MOCK_RESPONSE_PHONE_NOT_VERIF = """
    {
    "coupon_list_recommendation": {
      "message": [],
      "error_code": "200",
      "status": "OK",
      "data": {
        "result_status": {
          "code": "42049",
          "message": [
            "Nomor telepon anda belum terverifikasi, silahkan lengkapi Profil Anda"
          ],
          "reason": "MSISDN not verified"
        },
        "empty_state": {
          "title": "Nomor belum diverifikasi",
          "description": "Verifikasi nomor dulu ya",
          "image_url": "https://ecs7.tokopedia.net/img/ovo/icon-benefit-2.png"
        },
        "title": "",
        "sub_title": "",
        "promo_recommendation": {
          "codes": [],
          "message": ""
        },
        "coupon_sections": [],
        "additional_message": "",
        "reward_points_info": {
          "message": "",
          "gain_reward_points_tnc": {
            "title": "",
            "tnc_details": []
          }
        }
      }
    }
  }
""".trimIndent()

val MOCK_RESPONSE_BLACKLIST = """
    {
    "coupon_list_recommendation": {
      "message": [],
      "error_code": "200",
      "status": "OK",
      "data": {
        "result_status": {
          "code": "42003",
          "message": [
            "Alamat e-mail telah melanggar Syarat & Ketentuan Tokopedia."
          ],
          "reason": "Blacklisted Account"
        },
        "empty_state": {
          "title": "Akun Anda terblacklist",
          "description": "Silahkan hubungi Tokopedia",
          "image_url": "https://ecs7.tokopedia.net/img/ovo/icon-benefit-2.png"
        },
        "title": "",
        "sub_title": "",
        "promo_recommendation": {
          "codes": [],
          "message": ""
        },
        "coupon_sections": [],
        "additional_message": "",
        "reward_points_info": {
          "message": "",
          "gain_reward_points_tnc": {
            "title": "",
            "tnc_details": []
          }
        }
      }
    }
  }
""".trimIndent()

val MOCK_RESPONSE_EMPTY_PROMO = """
    {
    "coupon_list_recommendation": {
      "message": [],
      "error_code": "200",
      "status": "OK",
      "data": {
        "result_status": {
          "code": "42050",
          "message": [
            "Anda tidak memiliki kupon"
          ],
          "reason": "Coupon List Empty"
        },
        "empty_state": {
            "title": "Kuponnya tidak ada",
            "description": "Banyakin belanja ya biar dapet kupon",
            "image_url": "https://ecs7.tokopedia.net/img/ovo/icon-benefit-2.png"
        },
        "title": "",
        "sub_title": "",
        "promo_recommendation": {
          "codes": [],
          "message": ""
        },
        "coupon_sections": [],
        "additional_message": "",
        "reward_points_info": {
          "message": "",
          "gain_reward_points_tnc": {
            "title": "",
            "tnc_details": []
          }
        }
      }
    }
  }
""".trimIndent()