package com.tokopedia.logisticcart.datamock

val SHIPPING_PARAM_JSON = """
    {
      "addressId": 99367774,
      "categoryIds": "753",
      "destinationDistrictId": "2253",
      "destinationLatitude": "-6.148635",
      "destinationLongitude": "106.735185",
      "destinationPostalCode": "11720",
      "insurance": 1,
      "isBlackbox": false,
      "isTradeInDropOff": false,
      "isTradein": false,
      "orderValue": 24600,
      "originDistrictId": "2267",
      "originLatitude": "-6.2565354",
      "originLongitude": "106.85040979999997",
      "originPostalCode": "12750",
      "preorder": true,
      "productInsurance": 0,
      "products": [
        {
          "is_free_shipping": false,
          "product_id": 516791889
        }
      ],
      "shopId": "1479278",
      "token": "Tokopedia+Kero:vo2MKxZf6DChiCSX+/MQDBcP4LQ=",
      "uniqueId": "1479278-30-740525-99367774",
      "ut": "1580200911",
      "weightInKilograms": 0.224
    }
""".trimIndent()

val SHOP_SHIPMENTS_JSON = """
    [
      {
        "isDropshipEnabled": true,
        "shipCode": "jne",
        "shipId": 1,
        "shipLogo": "https://images.tokopedia.net/img/kurir-jne.png",
        "shipName": "JNE",
        "shipProds": [
          {
            "additionalFee": 0,
            "minimumWeight": 0,
            "shipGroupId": 1004,
            "shipGroupName": "regular",
            "shipProdId": 1,
            "shipProdName": "Reguler"
          },
          {
            "additionalFee": 0,
            "minimumWeight": 0,
            "shipGroupId": 1004,
            "shipGroupName": "regular",
            "shipProdId": 2,
            "shipProdName": "OKE"
          },
          {
            "additionalFee": 0,
            "minimumWeight": 0,
            "shipGroupId": 1003,
            "shipGroupName": "nextday",
            "shipProdId": 6,
            "shipProdName": "YES"
          }
        ]
      },
      {
        "isDropshipEnabled": true,
        "shipCode": "pos",
        "shipId": 4,
        "shipLogo": "https://images.tokopedia.net/img/kurir-pos.png",
        "shipName": "Pos Indonesia",
        "shipProds": [
          {
            "additionalFee": 0,
            "minimumWeight": 0,
            "shipGroupId": 1004,
            "shipGroupName": "regular",
            "shipProdId": 10,
            "shipProdName": "Pos Kilat Khusus"
          }
        ]
      },
      {
        "isDropshipEnabled": false,
        "shipCode": "gojek",
        "shipId": 10,
        "shipLogo": "https://images.tokopedia.net/img/kurir-gosend.png",
        "shipName": "GO-JEK",
        "shipProds": [
          {
            "additionalFee": 0,
            "minimumWeight": 0,
            "shipGroupId": 1000,
            "shipGroupName": "instant",
            "shipProdId": 28,
            "shipProdName": "Instant Courier"
          },
          {
            "additionalFee": 0,
            "minimumWeight": 0,
            "shipGroupId": 1002,
            "shipGroupName": "sameday",
            "shipProdId": 20,
            "shipProdName": "Same Day"
          }
        ]
      },
      {
        "isDropshipEnabled": false,
        "shipCode": "ninja",
        "shipId": 12,
        "shipLogo": "https://images.tokopedia.net/img/kurir-ninja.png",
        "shipName": "Ninja Xpress",
        "shipProds": [
          {
            "additionalFee": 0,
            "minimumWeight": 0,
            "shipGroupId": 1004,
            "shipGroupName": "regular",
            "shipProdId": 25,
            "shipProdName": "Reguler"
          }
        ]
      },
      {
        "isDropshipEnabled": false,
        "shipCode": "grab",
        "shipId": 13,
        "shipLogo": "https://images.tokopedia.net/img/kurir-grab.png",
        "shipName": "Grab",
        "shipProds": [
          {
            "additionalFee": 0,
            "minimumWeight": 0,
            "shipGroupId": 1000,
            "shipGroupName": "instant",
            "shipProdId": 37,
            "shipProdName": "Instant"
          },
          {
            "additionalFee": 0,
            "minimumWeight": 0,
            "shipGroupId": 1002,
            "shipGroupName": "sameday",
            "shipProdId": 24,
            "shipProdName": "Same Day"
          }
        ]
      },
      {
        "isDropshipEnabled": true,
        "shipCode": "anteraja",
        "shipId": 23,
        "shipLogo": "https://images.tokopedia.net/img/kurir-anteraja.png",
        "shipName": "AnterAja",
        "shipProds": [
          {
            "additionalFee": 0,
            "minimumWeight": 0,
            "shipGroupId": 1004,
            "shipGroupName": "regular",
            "shipProdId": 45,
            "shipProdName": "Reguler"
          },
          {
            "additionalFee": 0,
            "minimumWeight": 0,
            "shipGroupId": 1004,
            "shipGroupName": "regular",
            "shipProdId": 46,
            "shipProdName": "Next Day"
          }
        ]
      },
      {
        "isDropshipEnabled": true,
        "shipCode": "lion",
        "shipId": 24,
        "shipLogo": "https://images.tokopedia.net/img/kurir-lionparcel.png",
        "shipName": "Lion Parcel",
        "shipProds": [
          {
            "additionalFee": 0,
            "minimumWeight": 0,
            "shipGroupId": 1004,
            "shipGroupName": "regular",
            "shipProdId": 47,
            "shipProdName": "Reguler"
          }
        ]
      }
    ]
""".trimIndent()

val PRODUCTS_JSON = """
    [{"is_free_shipping":false,"product_id":516791889}]
""".trimIndent()
