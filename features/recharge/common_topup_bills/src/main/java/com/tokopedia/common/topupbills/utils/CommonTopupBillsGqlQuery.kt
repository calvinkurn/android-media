package com.tokopedia.common.topupbills.utils

object CommonTopupBillsGqlQuery {
    val rechargeCatalogProductInput = """
        query rechargeCatalogDynamicInput(${'$'}menuID: Int!,${'$'}operator: String!) {
          rechargeCatalogDynamicInput(menuID:${'$'}menuID, platformID: 5, operator:${'$'}operator) {
            needEnquiry
            isShowingProduct
            dynamicFields {
              param_name
              name
              text
              style
              placeholder
              help
              data_collections {
                name
                products {
                  id
                  attributes {
                    price
                    price_plain
                    promo {
                        id
                        new_price
                    }
                    desc
                    detail
                    detail_compact
                    info
                    detail_url
                    detail_url_text
                    category_id
                    product_labels
                    product_descriptions
                    hide_from_subscription
                  }
                }
              }
              validations {
                rule
                title
              }
            }
          }
        }
    """.trimIndent()

    val catalogMenuDetail = """
        query catalogMenuDetail(${'$'}menuID: Int!){
          rechargeCatalogMenuDetail(menuID:${'$'}menuID, platformID: 5) {
            catalog {
              id
              name
              label
              icon
            }
            recommendations {
              iconUrl
              title
              clientNumber
              appLink
              webLink
              type
              categoryId
              productId
              isATC
              description
              operatorID
            }
            promos {
              id
              filename
              filename_webp
              img_url
              status
              title
              subtitle
              promo_code
            }
            tickers {
              ID
              Name
              Content
              Type
              Environment
              ActionText
              ActionLink
            }
            banners {
              id
              title
              img_url: filename
              link_url: img_url
              promo_code
              app_link
            }
            express_checkout
          }
        }
    """.trimIndent()

    val rechargeCatalogPlugin = """
        query rechargeCatalogPlugin(${'$'}filters: [RechargeCatalogPluginFilter]) {
          rechargeCatalogPlugin(filters:${'$'}filters){
            add_to_mybills {
              is_enabled
              attributes {
                client_numbers {
                  number
                  status
                }
              }
            }
            instant_checkout {
              is_enabled
              attributes {
                text
              }
            }
            buy_button {
              is_enabled
              attributes {
                text
              }
            }
          }
        }
    """.trimIndent()

    val rechargeInquiry = """
        query enquiry (${'$'}fields: [RechargeKeyValue]!) {
          status
          rechargeInquiry(fields:${'$'}fields) {
            status: Status
            retrySec: RetrySec
            attributes: Attributes {
              UserID
              ProductID
              ClientNumber
              Title
              Price
              PricePlain
              mainInfo: MainInfo {
                label: Label
                value: Value
              }
            }
          }
        }
    """.trimIndent()
}