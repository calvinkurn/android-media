package com.tokopedia.topads.dashboard.data.raw

const val SHOP_AD_INFO = """
            query topadsGetShopInfoV2(${'$'}shopID: Int!) {
  topadsGetShopInfoV2(shopID: ${'$'}shopID) {
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