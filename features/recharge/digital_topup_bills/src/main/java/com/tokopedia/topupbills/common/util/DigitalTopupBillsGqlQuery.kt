package com.tokopedia.topupbills.common.util

object DigitalTopupBillsGqlQuery {
    val catalogProductTelco = """
        query telcoProductMultiTab(${'$'}menuID: Int!,${'$'}operatorID: String!,${'$'}filterData: [RechargeCatalogFilterData]) {
          rechargeCatalogProductInputMultiTab(menuID:${'$'}menuID, platformID: 5, operator:${'$'}operatorID, filterData:${'$'}filterData) {
            productInputs {
              label
              needEnquiry
              isShowingProduct
              enquiryFields {
                id
                param_name
                name
              }
              product {
                id
                name
                text
                placeholder
                validations {
                  rule
                }
                dataCollections {
                  name
                  products {
                    id
                    attributes {
                      product_labels
                      desc
                      detail
                      detail_url
                      detail_url_text
                      info
                      price
                      price_plain
                      status
                      detail_compact
                      category_id
                      operator_id
                      promo {
                        id
                        bonus_text
                        new_price
                        new_price_plain
                        value_text
                      }
                    }
                  }
                }
              }
              filterTagComponents {
                name
                text
                param_name
                data_collections {
                  key
                  value
                }
              }
            }
          }
        }
    """.trimIndent()

    val prefixSelectTelco = """
        query telcoPrefixSelect(${'$'}menuID: Int!) {
          rechargeCatalogPrefixSelect(menuID:${'$'}menuID, platformID: 5) {
            componentID
            name
            paramName
            text
            help
            placeholder
            validations {
              id
              title
              message
              rule
            }
            prefixes {
              key
              value
              operator {
                id
                attributes {
                  name
                  default_product_id
                  image_url
                }
              }
            }
          }
        }

    """.trimIndent()
}