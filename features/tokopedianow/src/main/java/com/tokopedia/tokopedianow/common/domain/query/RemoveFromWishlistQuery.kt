package com.tokopedia.tokopedianow.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object RemoveFromWishlistQuery : GqlQueryInterface {
    override fun getOperationNameList() = listOf(
        "wishlist_remove_v2"
    )

    override fun getQuery(): String {
        return """
            mutation wishlist_remove_v2(${'$'}productID: SuperInteger, ${'$'}userID: SuperInteger) {
              wishlist_remove_v2(productID: ${'$'}productID, userID: ${'$'}userID) {
                id
                success
                message
                toaster_color
                button {
                  text
                  action
                  url
                }
              }
            }
        """
    }

    override fun getTopOperationName() = "wishlist_remove_v2"
}
