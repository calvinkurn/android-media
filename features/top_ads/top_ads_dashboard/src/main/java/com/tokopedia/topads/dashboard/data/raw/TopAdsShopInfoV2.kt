package com.tokopedia.topads.dashboard.data.raw

const val SHOP_AD_INFO = """
            query topadsGetShopInfoV2_1(${'$'}shopID: String!, ${'$'}source : String!) {
  topadsGetShopInfoV2_1(shopID: ${'$'}shopID, source: ${'$'}source) {
    data {
      ads {
        type
        is_used
      }
    }
    errors {
      code
      detail
      title
    }
  }
}"""