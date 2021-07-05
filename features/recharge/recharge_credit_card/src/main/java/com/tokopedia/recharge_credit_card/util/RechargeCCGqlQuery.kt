package com.tokopedia.recharge_credit_card.util

object RechargeCCGqlQuery {
    val creditCardBankList = """
        query rechargeBankList(${'$'}categoryId: Int!) {
          rechargePCIDSSSignature(category_id: ${'$'}categoryId) {
            bank_list{
              name
              image_url
            }
            message_error
          }
        }
        """.trimIndent()

    val catalogMenuDetail = """
        query catalogMenuDetail(${'$'}menuId: Int!) {
          rechargeCatalogMenuDetail(menuID:${'$'}menuId, platformID: 7) {
            menu_name
            menu_label
            category_ids
            catalog {
              id
              name
              label
              icon
              sub_menu {
                id
                name
                label
              }
            }
            tickers {
              ID
              Name
              Content
              Type
            }
            onboarding_descriptions
          }
        }
    """.trimIndent()

    val catalogPrefix = """
        query catalogPrefix(${'$'}menuId: Int!) {
          rechargeCatalogPrefixSelect(menuID:${'$'}menuId, platformID: 5) {
            componentID
            name
            paramName
            text
            prefixes {
              key
              value
              operator {
                id
                attributes {
                  name
                  image_url
                  default_product_id
                }
              }
            }
          }
        }
    """.trimIndent()

    val rechargeCCSignature = """
        query rechargeCCSignature(${'$'}categoryId: Int!) {
          rechargePCIDSSSignature(category_id:${'$'}categoryId) {
            signature
            message_error
          }
        }
    """.trimIndent()
}