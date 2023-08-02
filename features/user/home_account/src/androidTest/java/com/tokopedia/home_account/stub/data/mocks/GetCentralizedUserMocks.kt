package com.tokopedia.home_account.stub.data.mocks

object GetCentralizedUserMocks {

    val userPageResponse = """
    {
      "GetCentralizedUserAssetConfig":{
        "asset_config_horizontal":[],
        "asset_config_vertical":[],
        "asset_config":[
          {
            "title":"GoPay \u0026 Coins",
            "subtitle_color":"#03AC0E",
            "subtitle":"Gopay",
            "applink":"tokopedia://webview?url=https://www.tokopedia.com/gopay",
            "icon":"https://images.tokopedia.net/img/user/asset/icon/gopay.png",
            "is_active":false,
            "id":"gopay"
          },
          {
            "title":"OVO",
            "subtitle_color":"#03AC0E",
            "subtitle":"2.000.000 Points",
            "applink":"tokopedia://webview?url=https://www.tokopedia.com/ovo",
            "icon":"https://images.tokopedia.net/img/user/asset/icon/ovo.png",
            "is_active":true,
            "id":"ovo"
          },
          {
            "title":"Cek Saldo",
            "subtitle_color":"#03AC0E",
            "subtitle":"Saldo Tokopedia",
            "applink":"tokopedia://saldo",
            "icon":"https://images.tokopedia.net/img/user/asset/icon/saldo.png",
            "is_active":true,
            "id":"saldo"
          },
          {
            "title":"Tokopoints",
            "subtitle_color":"#03AC0E",
            "subtitle":"1.000.000 Points",
            "applink":"tokopedia://webview?url=https://www.tokopedia.com/points-center",
            "icon":"https://images.tokopedia.net/img/img/user/asset/icon/tokopoints.png",
            "is_active":false,
            "id":"tokopoints"
          }
        ]
      }
    }
    """.trimIndent()

    val assetPageResponse = """
    {
      "GetCentralizedUserAssetConfig": {
        "asset_config_horizontal": [
          {
            "title": "Tokopedia Card",
            "subtitle_color": "#03AC0E",
            "subtitle": "Kartu kredit cicilan 0%",
            "applink": "tokopedia://",
            "icon": "https://images.tokopedia.net/img/user/asset/icon/tokopediacard.png",
            "is_active": true,
            "id": "cobrandcc",
            "hide_title": true
          }
        ],
        "asset_config_vertical": [
          {
            "title": "GoPay \u0026 Coins",
            "subtitle_color": "#03AC0E",
            "subtitle": "Gopay",
            "applink": "tokopedia://webview?url\u003dhttps://www.tokopedia.com/gopay",
            "icon": "https://images.tokopedia.net/img/user/asset/icon/gopay.png",
            "is_active": false,
            "id": "gopay",
            "hide_title": false
          },
          {
            "title": "GoPayLater",
            "subtitle_color": "#03AC0E",
            "subtitle": "GoPayLater",
            "applink": "tokopedia://webview?url\u003dhttps://www.tokopedia.com/gopay",
            "icon": "https://images.tokopedia.net/img/img/user/asset/icon/gopaylater.png",
            "is_active": false,
            "id": "gopaylater",
            "hide_title": true
          },
          {
            "title": "OVO",
            "subtitle_color": "#03AC0E",
            "subtitle": "2.000.000 Points",
            "applink": "tokopedia://webview?url\u003dhttps://www.tokopedia.com/ovo",
            "icon": "https://images.tokopedia.net/img/user/asset/icon/ovo.png",
            "is_active": false,
            "id": "ovo",
            "hide_title": false
          },
          {
            "title": "Cek Saldo",
            "subtitle_color": "#03AC0E",
            "subtitle": "Saldo Tokopedia",
            "applink": "tokopedia://saldo",
            "icon": "https://images.tokopedia.net/img/user/asset/icon/saldo.png",
            "is_active": true,
            "id": "saldo",
            "hide_title": true
          }
        ],
        "asset_config": []
      }
    }
    """.trimIndent()
}
