package com.tokopedia.shop.common.constant

object ShopPageGqlQueryConstant {
    private val BASE_QUERY_GET_SHOP_PRODUCT = """
            query getShopProduct(${'$'}shopId: String!, ${'$'}filter: ProductListFilter!, ${'$'}source: String!){
              GetShopProduct(shopID:${'$'}shopId, filter:${'$'}filter, source:${'$'}source){
                %1s
              }
            }
    """.trimIndent()

    private val GET_SHOP_PRODUCT_QUERY_REQUEST = """
               status
               errors
                data {
                  product_id
                  name
                  product_url
                  app_link
                  stock
                  minimum_order
                  max_order
                  status
                  price{
                    text_idr
                  }
                  flags{
                    isFeatured
                    isPreorder
                    isFreereturn
                    isVariant
                    isWholesale
                    isWishlist
                    isSold
                    supportFreereturn
                    mustInsurance
                    withStock
                  }
                  stats{
                    reviewCount
                    rating
                    viewCount
                    averageRating
                  }
                  campaign{
                    hide_gimmick
                    original_price
                    original_price_fmt
                    discounted_price_fmt
                    discounted_percentage
                    discounted_price
                    is_upcoming
                    stock_sold_percentage
                    custom_stock
                    max_order
                  }
                  primary_image{
                    original
                    thumbnail
                    resize300
                  }
                  cashback{
                    cashback
                    cashback_amount
                  }
                  freeOngkir {
                    isActive
                    imgURL
                  }
                  label_groups {
                    position
                    type
                    title
                    url
                  }
                  hasVariant
                  parent_id
                  show_stockbar
                }
                suggestion {
                  text
                  query
                  response_code
                  keyword_process
                }
                totalData
    """.trimIndent()

    private val GET_SHOP_FILTER_PRODUCT_COUNT_QUERY_REQUEST = """
                totalData
    """.trimIndent()

    fun getShopProductQuery() = String.format(BASE_QUERY_GET_SHOP_PRODUCT, GET_SHOP_PRODUCT_QUERY_REQUEST)
    fun getShopFilterProductCountQuery() = String.format(BASE_QUERY_GET_SHOP_PRODUCT, GET_SHOP_FILTER_PRODUCT_COUNT_QUERY_REQUEST)
}
