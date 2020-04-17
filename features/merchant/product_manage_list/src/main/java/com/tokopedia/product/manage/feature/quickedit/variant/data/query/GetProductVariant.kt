package com.tokopedia.product.manage.feature.quickedit.variant.data.query

internal object GetProductVariant {

    val QUERY = """
        query getProductV3(${'$'}productID:String!, ${'$'}options:OptionV3!){
          getProductV3(productID:${'$'}productID, options:${'$'}options){
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
            }
          }
        }
    """.trimIndent()
}