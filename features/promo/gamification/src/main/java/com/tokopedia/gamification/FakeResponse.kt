package com.tokopedia.gamification

const val FAKE_GAMI_LUCKY_HOME = """
    {
      "gamiLuckyHome": {
        "resultStatus": {
          "code": "200",
          "message": [
            "Success"
          ],
          "reason": "OK"
        },
        "liveFeedChannelID": 1,
        "tokensUser": {
          "state": "active",
          "campaignSlug": "TAPTAPKOTAK:7OCT:JANSPORT",
          "title": "Tap kotak ini untuk \nbuka hadiahmu!",
          "text": "Hadiah yang bisa kamu dapatkan:",
          "desc": ""
        },
        "actionButton": [],
        "tokenAsset": {
          "backgroundImgURL": "https://ecs7.tokopedia.net/img/blog/promo/2020/09/PNG-PLAY-SCREEN-MOBILE.jpg.png",
          "seamlessImgURL": "",
          "imageURL": "",
          "imageV2URLs": [
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/Box-Bawah.png",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/GIF_WIBBOX_TOPONLY_BINTANG.gif",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/frame_0_delay-0.01s.png",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/frame_1_delay-0.01s.png",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/frame_2_delay-0.01s.png",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/frame_3_delay-0.01s.png",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/frame_4_delay-0.01s.png",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/frame_5_delay-0.01s.png",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/frame_6_delay-0.01s.png",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/frame_7_delay-0.01s.png"
          ]
        },
        "prizeList": [
          {
            "isSpecial": false,
            "imageURL": "https://ecs7.tokopedia.net/img/blog/promo/2020/03/coupon3x.png",
            "text": [
              "Kupon"
            ]
          },
          {
            "isSpecial": true,
            "imageURL": "https://ecs7.tokopedia.net/img/blog/promo/2020/09/JPEG-Disclaimer-500x500.jpg",
            "text": [
              "Hadiah langsung",
              "Untuk pengguna baru, aktif dan dikangen yang telah disesuaikan dengan persona kamu",
              ""
            ]
          }
        ],
        "bottomSheetButtonText": "Cek hadiah yang bisa didapat",
        "prizeDetailList": [
          {
            "isSpecial": true,
            "imageURL": "http://tokopedia.com/tokopoints.png",
            "text": ""
          },
          {
            "isSpecial": false,
            "imageURL": "https://ecs7.tokopedia.net/img/blog/promo/2020/03/ovo%403x.png",
            "text": "OVO Points"
          },
          {
            "isSpecial": false,
            "imageURL": "https://ecs7.tokopedia.net/img/blog/promo/2020/03/coupon3x.png",
            "text": "Kupon"
          }
        ],
        "prizeDetailListButton": {
          "text": "Cek Syarat Ketentuan",
          "url": "https://www.tokopedia.com/pelajari",
          "applink": "tokopedia://giftbox/pelajari"
        },
        "reminder": {
          "text": "Mau dapat hadiah lagi? Jangan lupa buka Kotak Kejutan besok!",
          "textSet": "Kamu akan diingatkan lagi besok untuk buka Kotak Kejutan",
          "textUnset": "Pengingat dihapus.",
          "buttonSet": "Ingatkan Besok",
          "buttonUnset": "Hapus Pengingat",
          "enableText": "Ingatkan Besok",
          "disableText": "Pengingat Terpasang"
        },
        "infoURL": "tokopedia://webview?url=https%3A%2F%2Fwww.tokopedia.com%2Fpromo%2Ftap-tap-kotak"
      }
    }
"""

const val FAKE_GAMI_LUCKY_HOME_EMPTY = """
    {
      "gamiLuckyHome": {
        "resultStatus": {
          "code": "200",
          "message": [
            "Success"
          ],
          "reason": "OK"
        },
        "liveFeedChannelID": 1,
        "tokensUser": {
          "state": "empty",
          "campaignSlug": "TAPTAPKOTAK:7OCT:JANSPORT",
          "title": "",
          "text": "Kamu sudah ambil hadiah hari ini. \nBuka lagi besok, ya!",
          "desc": ""
        },
        "actionButton": [],
        "tokenAsset": {
          "backgroundImgURL": "https://ecs7.tokopedia.net/img/blog/promo/2020/09/PNG-PLAY-SCREEN-MOBILE.jpg.png",
          "seamlessImgURL": "",
          "imageURL": "",
          "imageV2URLs": [
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/Box-Bawah.png",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/GIF_WIBBOX_TOPONLY_BINTANG.gif",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/frame_0_delay-0.01s.png",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/frame_1_delay-0.01s.png",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/frame_2_delay-0.01s.png",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/frame_3_delay-0.01s.png",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/frame_4_delay-0.01s.png",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/frame_5_delay-0.01s.png",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/frame_6_delay-0.01s.png",
            "https://ecs7.tokopedia.net/assets/images/gamification/taptap/frame_7_delay-0.01s.png"
          ]
        },
        "prizeList": [
          {
            "isSpecial": false,
            "imageURL": "https://ecs7.tokopedia.net/img/blog/promo/2020/03/coupon3x.png",
            "text": [
              "Kupon"
            ]
          },
          {
            "isSpecial": true,
            "imageURL": "https://ecs7.tokopedia.net/img/blog/promo/2020/09/JPEG-Disclaimer-500x500.jpg",
            "text": [
              "Hadiah langsung",
              "Untuk pengguna baru, aktif dan dikangen yang telah disesuaikan dengan persona kamu",
              ""
            ]
          }
        ],
        "bottomSheetButtonText": "Cek hadiah yang bisa didapat",
        "prizeDetailList": [
          {
            "isSpecial": true,
            "imageURL": "http://tokopedia.com/tokopoints.png",
            "text": ""
          },
          {
            "isSpecial": false,
            "imageURL": "https://ecs7.tokopedia.net/img/blog/promo/2020/03/ovo%403x.png",
            "text": "OVO Points"
          },
          {
            "isSpecial": false,
            "imageURL": "https://ecs7.tokopedia.net/img/blog/promo/2020/03/coupon3x.png",
            "text": "Kupon"
          }
        ],
        "prizeDetailListButton": {
          "text": "Cek Syarat Ketentuan",
          "url": "https://www.tokopedia.com/pelajari",
          "applink": "tokopedia://giftbox/pelajari"
        },
        "reminder": {
          "text": "Mau dapat hadiah lagi? Jangan lupa buka Kotak Kejutan besok!",
          "textSet": "Kamu akan diingatkan lagi besok untuk buka Kotak Kejutan",
          "textUnset": "Pengingat dihapus.",
          "buttonSet": "Ingatkan Besok",
          "buttonUnset": "Hapus Pengingat",
          "enableText": "Ingatkan Besok",
          "disableText": "Pengingat Terpasang"
        },
        "infoURL": "tokopedia://webview?url=https%3A%2F%2Fwww.tokopedia.com%2Fpromo%2Ftap-tap-kotak"
      }
    }
"""

const val FAKE_GAMI_CRACK="""
    {
      "gamiCrack": {
        "resultStatus": {
          "code": "200",
          "message": [
            "success"
          ],
          "status": ""
        },
        "imageUrl": "",
        "benefitText": [
          "Yeay kamu dapat",
          "Kupon Cashback \u0026 500 OVO Points"
        ],
        "benefits": [
          {
            "text": "Kupon Cashback",
            "color": "#ffffff",
            "imageUrl": "https://ecs7.tokopedia.net/img/blog/promo/2020/03/icon-coupon.png",
            "benefitType": "coupon",
            "isBigPrize": false,
            "isAutoApply": true,
            "autoApplyMsg": "Kupon Cashback udah terpasang ya. Ayo beli sekarang!",
            "dummyCode": "qweqw",
            "referenceID": "123"
          },
          {
            "text": "Kupon Cashback",
            "color": "#ffffff",
            "imageUrl": "https://ecs7.tokopedia.net/img/blog/promo/2020/03/icon-coupon.png",
            "benefitType": "coupon",
            "isBigPrize": false,
            "dummyCode": "asdas",
            "referenceID": "222",
            "isAutoApply": false,
            "autoApplyMsg": ""
          },
          {
            "text": "500 OVO Points",
            "color": "#ffffff",
            "imageUrl": "https://ecs7.tokopedia.net/img/blog/promo/2020/03/icon-ovo.png",
            "benefitType": "ovo_points",
            "isBigPrize": false,
            "dummyCode": "",
            "referenceID": "0",
            "isAutoApply": false,
            "autoApplyMsg": ""
          }
        ],
        "actionButton": [
          {
            "text": "Pakai TokoPoints",
            "url": "https://tokopedia.com/tokopoints",
            "applink": "tokopedia://webview?url\u003dhttps%3A%2F%2Fm.tokopedia.com%2Ftokopoints%2Fmobile\u0026title\u003dTokoPoints",
            "type": "redirect",
            "backgroundColor": "orange"
          }
        ],
        "recommendation": {
            "isShow": true,
            "shopID": "12345",
            "pageName": "gamepage"
        }
      }
    }
"""

const val FAKE_COUPON_RESPONSE = """
      {
        "id": 2446,
        "catalog_type": 1,
        "icon": "https://ecs7.tokopedia.net/img/blog/promo/2017/12/06140338/market-place%402x.png",
        "expired": " 1 Jan 1970",
        "points": 0,
        "title": "Kupon Diskon Ticket Event",
        "sub_title": "",
        "expired_label": "",
        "expired_str": "",
        "is_disabled": true,
        "is_disabled_button": true,
        "disable_error_message": "",
        "upper_text_desc": [],
        "description": "Kupon Diskon Ticket Event",
        "overview": "",
        "how_to_use": "<ol><li>Pilih ticketnya dulu</li><li>baru pke kuponnya di halaman checkout</li><li>jangan lupa bayar!</li></ol>",
        "thumbnail_url": "https://ecs7.tokopedia.net/img/blog/promo/2019/07/RM-update-576.jpg",
        "thumbnail_url_mobile": "https://ecs7.tokopedia.net/img/blog/promo/2019/07/RM-update-576.jpg",
        "image_url": "https://ecs7.tokopedia.net/img/blog/promo/2019/07/RM-update-960.jpg",
        "image_url_mobile": "https://ecs7.tokopedia.net/img/blog/promo/2019/07/RM-update-640.jpg",
        "thumbnail_v2_url": "https://ecs7.tokopedia.net/img/blog/promo/2019/07/RM-update-360.jpg",
        "thumbnail_v2_url_mobile": "",
        "minimumUsage": "Rp100.000",
        "minimumUsageLabel": "Minimum transaksi"
      }
"""