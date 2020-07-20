package com.tokopedia.createpost.data.raw

const val GQL_SHOP_PRODUCT_SUGGESTION: String = """query ShopProductSuggestion(${'$'}shopID: Int) {
  feedContentTagItems(shopID: ${'$'}shopID, limit: 5, start: 0) {
    tag_items {
      id
      name
      uri
      image_uri
      price
      rating
    }
    error
  }
}"""