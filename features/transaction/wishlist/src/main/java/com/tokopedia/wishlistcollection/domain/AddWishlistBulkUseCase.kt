package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlistcollection.data.params.AddWishlistBulkParams
import com.tokopedia.wishlistcollection.data.response.AddWishlistBulkResponse
import javax.inject.Inject

@GqlQuery("AddWishlistBulkMutation", AddWishlistBulkUseCase.query)
class AddWishlistBulkUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<AddWishlistBulkParams, AddWishlistBulkResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(params: AddWishlistBulkParams): AddWishlistBulkResponse {
        return repository.request(AddWishlistBulkMutation(), createVariables(params))
    }

    private fun createVariables(params: AddWishlistBulkParams): Map<String, Any> {
        val variables = mutableMapOf<String, Any>()
        variables[KEY_PARAMS] = params
        return variables
    }

    companion object {
        private const val KEY_PARAMS = "params"
        const val query = """
            mutation AddWishlistBulk(${'$'}params:AddWishlistBulkParams) {
                  add_wishlist_bulk(params:${'$'}params) {
                    product_ids
                    success
                    message
                    error_type
                    toaster_color
                    button {
                        text
                        action
                        url
                    }
                  }
                }"""
    }
}
