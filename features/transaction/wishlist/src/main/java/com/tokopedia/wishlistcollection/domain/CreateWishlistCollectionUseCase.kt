package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlistcollection.data.response.CreateWishlistCollectionResponse
import javax.inject.Inject

@GqlQuery("CreateWishlistCollectionMutation", CreateWishlistCollectionUseCase.query)
class CreateWishlistCollectionUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<String, CreateWishlistCollectionResponse>(dispatchers.io) {
    private val paramName = "name"

    override suspend fun execute(params: String): CreateWishlistCollectionResponse {
        return repository.request(CreateWishlistCollectionMutation(), toMap(params))
    }

    override fun graphqlQuery(): String = query

    private fun toMap(name: String): Map<String, Any> = mapOf(
        paramName to name
    )

    companion object {
        const val query = """
            mutation CreateWishlistCollection(${'$'}name: String) {
                  create_wishlist_collection(name: ${'$'}name) {
                    status
                    error_message
                    data {
                      success
                      id
                      message
                    }
                  }
                }"""
    }
}
