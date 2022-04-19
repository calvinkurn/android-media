package com.tokopedia.vouchergame.common.util

object VoucherGameGqlQuery {
    val voucherGameProductList = """
        query voucherGameProductList(${'$'}menuID: Int!){
          rechargeCatalogOperatorSelect(menuID:${'$'}menuID, platformID: 5) {
            componentID
            name
            paramName
            text
            operators {
              id
              attributes {
                name
                image
                image_url
                description
                help_cta
                help_text
                help_image
                operator_labels
              }
            }
          }
        }
    """.trimIndent()
}