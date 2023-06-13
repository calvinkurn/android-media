package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionByIdResponse
import javax.inject.Inject

@GqlQuery("GetWishlistCollectionByIdQuery", GetWishlistCollectionByIdUseCase.query)
class GetWishlistCollectionByIdUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<String, GetWishlistCollectionByIdResponse>(dispatchers.io) {

    override suspend fun execute(params: String): GetWishlistCollectionByIdResponse {
        return repository.request(GetWishlistCollectionByIdQuery(), toMap(params))
    }

    override fun graphqlQuery(): String = query

    private fun toMap(collectionId: String): Map<String, Any> = mapOf(
        collectionID to collectionId
    )

    companion object {
        const val query = """
            query GetWishlistCollectionById(${'$'}collectionID: SuperInteger) {
                  get_wishlist_collection_by_id(collectionID: ${'$'}collectionID) {
                    error_message
                    status
                    data {
                      ticker {
                        title
                        descriptions
                      }
                      collection {
                        id
                        name
                        access
                      }
                      access_options {
                        id
                        description
                        name
                      }
                    }
                  }
                }"""

        const val collectionID = "collectionID"
    }
}
