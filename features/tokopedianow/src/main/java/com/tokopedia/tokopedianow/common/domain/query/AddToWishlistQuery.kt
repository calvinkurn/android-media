package com.tokopedia.tokopedianow.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object AddToWishlistQuery : GqlQueryInterface {
    override fun getOperationNameList() = listOf(
        "wishlist_add_v2"
    )

    override fun getQuery(): String {
        return """
            mutation wishlist_add_v2(${'$'}productID: SuperInteger, ${'$'}userID: SuperInteger) {
              wishlist_add_v2(productID: ${'$'}productID, userID: ${'$'}userID) {
                id
                success
                message
                toaster_color
                error_type
                button {
                  text
                  action
                  url
                }
              }
            }
        """
    }

    override fun getTopOperationName() = "wishlist_add_v2"
}
