package com.tokopedia.home_wishlist.data.query

object WishlistQuery {
    private const val query = "\$query"
    private const val page = "\$page"
    private const val count = "\$count"
    private const val additionalParams = "\$additionalParams"
    fun getQuery() = """
        query getWishlist($query: String!, $page: Int, $count: Int, $additionalParams: WishlistAdditionalParams){
          wishlist(page: $page, count:$count, query:$query, additionalParams: $additionalParams){
              has_next_page
              total_data
              items {
                id
                name
                url
                image_url
                raw_price
                condition
                available
                status
                price
                rating
                review_count
                minimum_order
                slash_price
                discount_percentage
                wholesale_price {
                  minimum
                  maximum
                  price
                }
                shop {
                  id
                  name
                  url
                  gold_merchant
                  official_store
                  status
                  location

                }
                preorder
                badges {
                  title
                  image_url
                }
                label_group {
                    title
                    type
                    position
                }
                free_ongkir{
                  is_active
                  image_url
                }
                free_ongkir_extra{
                  is_active
                  image_url
                }
              }
              pagination {
                next_url
              }
          }
        }
    """.trimIndent()
}