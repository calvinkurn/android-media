package com.tokopedia.common_digital.common.util

object CommonDigitalGqlMutation {
    val rechargePushEventRecommendation = """
        mutation rechargePushEventRecommendation(${'$'}categoryID: Int!,${'$'}action: EventRecommendationActionType!) {
            rechargePushEventRecommendation(categoryID:${'$'}categoryID, action:${'$'}action) {
                Message
                IsError
            }
        }
    """.trimIndent()

    val digitalCategoryFavorites = """
        [{
          "query": "mutation RechargeCategoryDetail(${'$'}category_id: Int!,${'$'}is_seller: Int!)  {  \n  recharge_category_detail(category_id:${'$'}category_id, is_seller:${'$'}is_seller) {\n    id\n    name\n    title\n    icon\n    icon_url\n    is_new\n    instant_checkout\n    slug\n    microsite_url\n    default_operator_id\n    operator_style\n    operator_label\n    additional_feature {\n   id\n   text\n   button_text\n   }\n    client_number {\n      name\n      type\n      text\n      placeholder\n      default\n      validation {\n        regex\n        error\n        __typename\n      }\n      __typename\n    }\n    other_banners {\n      type\n      id\n      attributes {\n        title\n        subtitle\n        promocode\n        link\n        image\n        data_title\n      }\n    }\n    banners {\n      type\n      id\n      attributes {\n        title\n        subtitle\n        promocode\n        link\n        image\n        data_title\n      }\n    }\n   guides{\n id\n    type\n  attributes{\n   title\n   source_link\n }\n }\n    operator {\n      type\n      id\n      attributes {\n        name\n        image\n        lastorder_url\n        default_product_id\n        prefix\n        ussd\n        product {\n          type\n          id\n          attributes {\n            desc\n            detail\n            detail_compact\n            detail_url\n            detail_url_text\n            info\n            price\n            price_plain\n            promo {\n              id\n              bonus_text\n              new_price\n              new_price_plain\n              tag\n              terms\n              value_text\n            }\n            status\n          }\n        }\n        rule {\n          maximum_length\n          product_text\n          product_view_style\n          show_price\n          enable_voucher\n          button_text\n        }\n        description\n        first_color\n        second_color\n        fields {\n          name\n          type\n          text\n          placeholder\n          default\n          validation {\n            regex\n            error\n          }\n        }\n      }\n    }\n  }\n}",
          "variables": {
            "category_id": %s,
            "is_seller": %s
          },
          "operationName": "RechargeCategoryDetail"
        },
        {
        	"query": "mutation RechargeFavoriteNum(${'$'}category_id: Int!,${'$'}operatorId: String!,${'$'}productId:String!,${'$'}clientNum:String!)  {\n  \n  recharge_favorite_number(categoryID:${'$'}category_id, operatorId :${'$'}operatorId, productId:${'$'}productId, clientNum:${'$'}clientNum) {\n    client_number\n    operator_id\n    product_id\n    category_id\n    list {\n      client_number\n      operator_id\n      product_id\n      category_id\n      label\n    }\n  }\n\n}",
        	"variables": {
        		"category_id": %s,
        		"operatorId": "%s",
        		"productId": "%s",
        		"clientNum": "%s"
        	},
        	"operationName": "RechargeFavoriteNum"
        }]
    """.trimIndent()

    val digitalCategory = """
        [{
          "query": "mutation RechargeCategoryDetail(${'$'}category_id: Int!,${'$'}is_seller: Int!)  {  \n  recharge_category_detail(category_id:${'$'}category_id, is_seller:${'$'}is_seller) {\n    id\n    name\n    title\n    icon\n    icon_url\n    is_new\n    instant_checkout\n    slug\n    microsite_url\n    default_operator_id\n    operator_style\n    operator_label\n    additional_feature {\n   id\n   text\n   button_text\n   }\n    client_number {\n      name\n      type\n      text\n      placeholder\n      default\n      validation {\n        regex\n        error\n        __typename\n      }\n      __typename\n    }\n    other_banners {\n      type\n      id\n      attributes {\n        title\n        subtitle\n        promocode\n        link\n        image\n        data_title\n      }\n    }\n    banners {\n      type\n      id\n      attributes {\n        title\n        subtitle\n        promocode\n        link\n        image\n        data_title\n      }\n    }\n   guides{\n id\n    type\n  attributes{\n   title\n   source_link\n }\n }\n   operator {\n      type\n      id\n      attributes {\n        name\n        image\n        lastorder_url\n        default_product_id\n        prefix\n        ussd\n        product {\n          type\n          id\n          attributes {\n            desc\n            detail\n            detail_compact\n            detail_url\n            detail_url_text\n            info\n            price\n            price_plain\n            promo {\n              id\n              bonus_text\n              new_price\n              new_price_plain\n              tag\n              terms\n              value_text\n            }\n            status\n          }\n        }\n        rule {\n          maximum_length\n          product_text\n          product_view_style\n          show_price\n          enable_voucher\n          button_text\n        }\n        description\n        first_color\n        second_color\n        fields {\n          name\n          type\n          text\n          placeholder\n          default\n          validation {\n            regex\n            error\n          }\n        }\n      }\n    }\n  }\n}",
          "variables": {
            "category_id": %s,
            "is_seller": %s
          },
          "operationName": "RechargeCategoryDetail"
        }]
    """.trimIndent()
}