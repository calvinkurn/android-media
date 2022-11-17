package com.tokopedia.product.manage.common.feature.variant.data.query

internal object GetProductVariant {

    val QUERY = """
        query getProductV3(${'$'}productID:String!, ${'$'}options:OptionV3!, ${'$'}extraInfo:ExtraInfoV3!, ${'$'}warehouseID:String){
          getProductV3(productID:${'$'}productID, options:${'$'}options, extraInfo:${'$'}extraInfo, warehouseID:${'$'}warehouseID){
            productName
            notifymeCount
            variant{
              products{
                productID
                status
                combination
                isPrimary
                isCampaign
                price
                sku
                stock
                stockAlertCount
                stockAlertStatus
                isBelowStockAlert
                notifymeCount
                pictures {
                  picID
                  description
                  filePath
                  fileName
                  width
                  height
                  isFromIG
                  urlOriginal
                  urlThumbnail
                  url300
                  status
                }
                campaign_types{
                  id
                  name
                  icon_url
                }
                
              }
              selections {
                  variantID
                  variantName
                  unitName
                  unitID
                  unitName
                  identifier
                  options {
                    unitValueID
                    value
                    hexCode
                  }
              }
              sizecharts {
                  picID
                  description
                  filePath
                  fileName
                  width
                  height
                  isFromIG
                  urlOriginal
                  urlThumbnail
                  url300
                  status
              }
            }
          }
        }
    """.trimIndent()
}