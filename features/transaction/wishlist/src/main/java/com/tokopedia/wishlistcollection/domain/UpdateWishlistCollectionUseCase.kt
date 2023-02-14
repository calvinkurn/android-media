package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlistcollection.data.params.UpdateWishlistCollectionParams
import com.tokopedia.wishlistcollection.data.response.UpdateWishlistCollectionResponse
import javax.inject.Inject

@GqlQuery("UpdateWishlistCollectionMutation", UpdateWishlistCollectionUseCase.query)
class UpdateWishlistCollectionUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<UpdateWishlistCollectionParams, UpdateWishlistCollectionResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(params: UpdateWishlistCollectionParams): UpdateWishlistCollectionResponse {
        return repository.request(UpdateWishlistCollectionMutation(), createVariables(params))
    }

    private fun createVariables(params: UpdateWishlistCollectionParams): Map<String, Any> {
        val collectionParams = mutableMapOf<String, Any>()
        collectionParams[KEY_COLLECTION] = params
        val variables = mutableMapOf<String, Any>()
        variables[KEY_PARAMS] = collectionParams
        return variables
    }

    companion object {
        private const val KEY_PARAMS = "params"
        private const val KEY_COLLECTION = "collection"
        const val query = """
            mutation UpdateWishlistCollection(${'$'}params:UpdateWishlistCollectionParams) {
                 update_wishlist_collection(params:${'$'}params){
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
