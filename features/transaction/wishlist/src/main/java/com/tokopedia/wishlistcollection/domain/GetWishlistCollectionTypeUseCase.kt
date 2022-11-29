package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlistcollection.data.params.GetWishlistCollectionTypeParams
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionTypeResponse
import javax.inject.Inject

@GqlQuery("GetWishlistCollectionTypeQuery", GetWishlistCollectionTypeUseCase.query)
class GetWishlistCollectionTypeUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<GetWishlistCollectionTypeParams, GetWishlistCollectionTypeResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(params: GetWishlistCollectionTypeParams): GetWishlistCollectionTypeResponse {
        return repository.request(GetWishlistCollectionTypeQuery(), createVariables(params))
    }

    private fun createVariables(params: GetWishlistCollectionTypeParams): Map<String, Any> {
        val variables = mutableMapOf<String, Any>()
        variables[KEY_PARAMS] = params
        return variables
    }

    companion object {
        private const val KEY_PARAMS = "params"
        const val query = """
            query GetWishlistCollectionType(${'$'}params:GetWishlistCollectionItemsParams) {
                 get_wishlist_collection_items(params:${'$'}params){
                    collection_type
                    error_message
                  }
                }"""
    }
}
