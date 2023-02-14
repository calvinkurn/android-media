package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlistcollection.data.response.DeleteWishlistCollectionItemsResponse
import javax.inject.Inject

@GqlQuery("DeleteWishlistCollectionItemsMutation", DeleteWishlistCollectionItemsUseCase.query)
class DeleteWishlistCollectionItemsUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<List<String>, DeleteWishlistCollectionItemsResponse>(dispatchers.io) {
    private val listProductId = "productIDs"

    override suspend fun execute(params: List<String>): DeleteWishlistCollectionItemsResponse {
        return repository.request(DeleteWishlistCollectionItemsMutation(), toMap(params))
    }

    override fun graphqlQuery(): String = query

    private fun toMap(productIds: List<String>): Map<String, Any> = mapOf(
        listProductId to productIds
    )

    companion object {
        const val query = """
            mutation DeleteBulkCollectionItems(${'$'}productIDs: [SuperInteger]) {
                  delete_wishlist_collection_items(productIDs: ${'$'}productIDs) {
                    status
                    error_message
                    data {
                      success
                      message
                    }
                  }
                }"""
    }
}
