package com.tokopedia.shop.favourite.data.query

import com.tokopedia.feedcomponent.data.query.QueryProvider

/**
 * Created by jegul on 2019-11-05
 */
object GetShopFollowerListQueryProvider : QueryProvider {

    override fun getQuery(): String {
        val shopId = "\$shopId"
        val page = "\$page"
        val perPage = "\$perPage"

        return """
            query GetShopFollowerList($shopId: String!, $page: Int, $perPage: Int) {
              shopFollowerList(shopID: $shopId, page: $page, perPage: $perPage) {
                data {
                  followerID
                  followerName
                  profileURL
                  photo
                }
                haveNext
                error {
                  message
                }
              }
            }
        """.trimIndent()
    }
}