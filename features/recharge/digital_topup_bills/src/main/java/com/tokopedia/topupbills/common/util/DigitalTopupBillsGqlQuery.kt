package com.tokopedia.topupbills.common.util

object DigitalTopupBillsGqlQuery {
    val catalogProductTelco = """
        query telcoProductMultiTab(${'$'}menuID: Int!,${'$'}operatorID: String!,${'$'}filterData: [RechargeCatalogFilterData], ${'$'}clientNumber: [String]) {
          rechargeCatalogProductInputMultiTab(menuID:${'$'}menuID, platformID: 5, operator:${'$'}operatorID, filterData:${'$'}filterData, clientNumber:${'$'}clientNumber) {
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
}