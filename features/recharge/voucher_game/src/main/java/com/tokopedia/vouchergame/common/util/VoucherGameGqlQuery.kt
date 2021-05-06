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

    val voucherGameProducts = """
        query voucherGameProductDetail(${'$'}menuID: Int!,${'$'}operator: String!){
          rechargeCatalogProductInput(menuID:${'$'}menuID, platformID: 5, operator:${'$'}operator) {
            needEnquiry
            isShowingProduct
            enquiryFields {
              id
              param_name
              name
              style
              text
              placeholder
              help
              data_collections {
                value
              }
              validations {
                rule
                title
              }
            }
            product {
              name
              text
              dataCollections {
                name
                products {
                  id
                  attributes {
                    desc
                    price
                    price_plain
                    promo {
                      id
                      new_price
                    }
                    product_labels
                    detail
                    detail_compact
                    detail_url
                    detail_url_text
                  }
                }
              }
            }
          }
        }
    """.trimIndent()
}