package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionTypeResponse
import javax.inject.Inject

@GqlQuery("GetWishlistCollectionTypeQuery", GetWishlistCollectionTypeUseCase.query)
class GetWishlistCollectionTypeUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<String, GetWishlistCollectionTypeResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(collectionId: String): GetWishlistCollectionTypeResponse {
        return repository.request(GetWishlistCollectionTypeQuery(), createVariables(collectionId))
    }

    private fun createVariables(collectionId: String): Map<String, Any> {
        val params = mutableMapOf<String, String>()
        params[KEY_COLLECTION_ID] = collectionId

        val variables = mutableMapOf<String, Any>()
        variables[KEY_PARAMS] = params

        return variables
    }

    companion object {
        private const val KEY_PARAMS = "params"
        private const val KEY_COLLECTION_ID = "collection_id"
        const val query = """
            query GetWishlistCollectionType(${'$'}params:GetWishlistCollectionItemsParams) {
                 get_wishlist_collection_items(params:${'$'}params){
                    collection_type
                    error_message
                  }
                }"""
    }
}
