package com.tokopedia.topads.dashboard.data.raw

const val SHOP_AD_INFO = """
            query topadsGetShopInfoV2_1(${'$'}shopID: String!) {
  topadsGetShopInfoV2_1(shopID: ${'$'}shopID) {
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