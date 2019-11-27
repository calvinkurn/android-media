package com.tokopedia.kol.feature.following_list.domain.query

/**
 * Created by jegul on 2019-10-22
 */
object GetShopFollowingQuery {

    fun getQuery(): String {
        val params = "\$params"

        return """
        query GetUserShopFollow($params: UserShopFollowParam!) {
          userShopFollow(input: $params) {
            result {
              userShopFollowDetail {
                shopID
                shopName
                logo
                url
                stats {
                  totalShowcase
                  productSold
                }
                badge {
                  title
                  imageURL
                }
              }
              haveNext
              totalCount
            }
          }
        }
    """.trimIndent()
    }
}