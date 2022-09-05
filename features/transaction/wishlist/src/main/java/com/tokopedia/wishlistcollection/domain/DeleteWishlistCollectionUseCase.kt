package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlistcollection.data.response.DeleteWishlistCollectionResponse
import javax.inject.Inject

@GqlQuery("DeleteWishlistCollectionMutation", DeleteWishlistCollectionUseCase.query)
class DeleteWishlistCollectionUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<String, DeleteWishlistCollectionResponse>(dispatchers.io) {

    override suspend fun execute(params: String): DeleteWishlistCollectionResponse {
        return repository.request(DeleteWishlistCollectionMutation(), toMap(params))
    }

    override fun graphqlQuery(): String = query

    private fun toMap(collectionIdToBeDeleted: String): Map<String, Any> = mapOf(
        collectionId to collectionIdToBeDeleted
    )

    companion object {
        const val query = """
            mutation DeleteWishlistCollection(${'$'}collectionID: SuperInteger) {
                  delete_wishlist_collection(collectionID: ${'$'}collectionID) {
                    status
                    error_message
                    data {
                      success
                      message
                    }
                  }
                }"""

        const val collectionId = "collectionID"
    }
}