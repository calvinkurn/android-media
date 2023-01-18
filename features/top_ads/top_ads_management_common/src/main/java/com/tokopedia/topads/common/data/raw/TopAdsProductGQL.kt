package com.tokopedia.topads.common.data.raw

const val TOP_ADS_PRODUCT_GQL = """query getProductV3(${'$'}productID:String!, ${'$'}options:OptionV3!){
      getProductV3(productID:${'$'}productID, options: ${'$'}options){
          productID
          productName
          price
          description
      }
}
"""
