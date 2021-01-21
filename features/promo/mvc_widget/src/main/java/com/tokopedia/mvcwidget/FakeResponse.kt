package com.tokopedia.mvcwidget

object FakeResponse {
    const val FakeTokopointsCatalogMVCSummaryResponse = """
        {
    "tokopointsCatalogMVCSummary": {
      "resultStatus": {
        "code": "200",
        "status": "OK",
        "message": [
          "Success"
        ]
      },
      "isShown": true,
      "subTitle": "Ayo pakai promonya biar makin hemat!",
      "imageURL": "https://ecs7.tokopedia.net/img/tokopoints/MVC/CouponIconWhite/coupon-1.png",
      "titles": [
        {
          "text": "<b>TEST FF MVC2</b>",
          "icon": ""
        }
      ]
    }
  }
    """

    const val FakeTokopointsCatalogMVCListResponse = """
{
    "tokopointsCatalogMVCList": {
      "resultStatus": {
        "code": "200",
        "status": "OK",
        "message": [
          "Success"
        ]
      },
      "followWidget": {
        "isShown": true,
        "type": "membership_open",
        "content": "<b>Jadi member toko untuk dapat kupon:</b> <ul><li>tttttt</li><li>metaaa</li><li>dan 5 kupon lainnya!</li><ul>",
        "iconURL": "https://ecs7.tokopedia.net/img/tokopoints/MVC/MembershipIcon/tokomember.png",
        "membershipCardID":"1",
        "membershipHowTo": [
          {
            "imageURL": "https://ecs7.tokopedia.net/img/tokopoints/MVC/TokoMember-1/tokomember-1.png",
            "description": "TokoMember adalah program membership yang menawarkan keuntungan spesial untuk para pembeli yang terdaftar sebagai member toko."
          },
          {
            "imageURL": "https://ecs7.tokopedia.net/img/tokopoints/MVC/TokoMember-2/tokomember-2.png",
            "description": "Kamu bisa cek berbagai brand yang ikutan program ini dengan buka TokoMember lewat halaman Akun, Rewards, toko, dan detail produk."
          },
          {
            "imageURL": "https://ecs7.tokopedia.net/img/tokopoints/MVC/TokoMember-3/tokomember-3.png",
            "description": "Kalau jadi member, kamu juga bakal dapat kartu virtual khusus member dengan desain spesial, lho!"
          }
        ]
      },
      "shopName": "Toko MZA",
      "catalogList": [],
      "toasterSuccessMessage": "Kamu berhasil jadi member toko ini. Yuk, belanja dan pakai kupon khusus TokoMember!"
    }
  }
    """
    const val FakeMemberShipRegisterSuccess = """
        {
    "membershipRegister": {
      "resultStatus": {
        "code": "200",
        "message": [
          "Success"
        ],
        "reason": "OK"
      },
      "infoMessage": {
        "imageURL": "https://ecs7.tokopedia.net/tokomember/success.png",
        "title": "Selamat, kamu resmi jadi member Premium MZA Store!",
        "subtitle": "Kini kamu bisa mengumpulkan Poin & mendapatkan banyak keuntungan menarik!",
        "cta": {
          "text": "Cek Detail Kartu",
          "url": "https://m-staging.tokopedia.com/membership/shop/detail/3610",
          "appLink": "tokopedia://membership/shop/detail/3610"
        }
      }
    }
  }
    """
    const val FakeMemberShipRegisterFail = """
        {
    "membershipRegister": {
      "resultStatus": {
        "code": "42032",
        "message": [
          "Program tidak valid"
        ],
        "reason": "Program not valid"
      },
      "infoMessage": {
        "imageURL": "",
        "title": "",
        "subtitle": "",
        "cta": {
          "text": "",
          "url": "",
          "appLink": ""
        }
      }
    }
  }
    """
}