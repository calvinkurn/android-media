package com.tokopedia.product.manage.common.feature.variant.data.query

internal object GetProductVariant {

    val QUERY = """
        query getProductV3(${'$'}productID:String!, ${'$'}options:OptionV3!){
          getProductV3(productID:${'$'}productID, options:${'$'}options){
            productName
            variant{
              products{
                productID
                status
                combination
                isPrimary
                price
                sku
                stock
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