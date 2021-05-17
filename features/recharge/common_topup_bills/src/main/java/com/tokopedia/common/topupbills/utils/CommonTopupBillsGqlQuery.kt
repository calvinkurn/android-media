package com.tokopedia.common.topupbills.utils

object CommonTopupBillsGqlQuery {
    private const val RECHARGE_PARAM_ANDROID_DEVICE_ID = 5

    val rechargeCatalogDynamicProductInput = """
        query rechargeCatalogDynamicInput(${'$'}menuID: Int!,${'$'}operator: String!) {
          rechargeCatalogDynamicInput(menuID:${'$'}menuID, platformID: $RECHARGE_PARAM_ANDROID_DEVICE_ID, operator:${'$'}operator) {
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

    val rechargeCatalogProductInput = """
        query voucherGameProductDetail(${'$'}menuID: Int!,${'$'}operator: String!){
          rechargeCatalogProductInput(menuID:${'$'}menuID, platformID: $RECHARGE_PARAM_ANDROID_DEVICE_ID, operator:${'$'}operator) {
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

    val enquiryDigital = """
        query enquiry (${'$'}clientNumber: String!,${'$'}productId: String!) {
          status
          enquiry(
            clientNumber:${'$'}clientNumber,
            productId:${'$'}productId,
            requestType: NEW) {
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

    val catalogMenuDetail = """
        query catalogMenuDetail(${'$'}menuID: Int!){
          rechargeCatalogMenuDetail(menuID:${'$'}menuID, platformID: $RECHARGE_PARAM_ANDROID_DEVICE_ID) {
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

    val prefixSelectTelco = """
        query telcoPrefixSelect(${'$'}menuID: Int!) {
          rechargeCatalogPrefixSelect(menuID:${'$'}menuID, platformID: $RECHARGE_PARAM_ANDROID_DEVICE_ID) {
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