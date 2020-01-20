package com.tokopedia.home_wishlist.data.query

object WishlistQuery {
    private const val query = "\$query"
    private const val page = "\$page"
    private const val count = "\$count"
    fun getQuery() = """
        query wishlist( $query: String!, $page: Int, $count: Int){
          wishlist(page: $page, count:$count, query:$query){
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
              }
              pagination {
                next_url
              }
          }
        }
    """.trimIndent()
}