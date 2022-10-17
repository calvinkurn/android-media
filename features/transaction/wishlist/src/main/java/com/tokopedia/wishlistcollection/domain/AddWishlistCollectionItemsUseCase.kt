package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlistcollection.data.params.AddWishlistCollectionsHostBottomSheetParams
import com.tokopedia.wishlistcollection.data.response.AddWishlistCollectionItemsResponse
import javax.inject.Inject

@GqlQuery("AddWishlistCollectionItemsMutation", AddWishlistCollectionItemsUseCase.query)
class AddWishlistCollectionItemsUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<AddWishlistCollectionsHostBottomSheetParams, AddWishlistCollectionItemsResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(params: AddWishlistCollectionsHostBottomSheetParams): AddWishlistCollectionItemsResponse {
        return repository.request(AddWishlistCollectionItemsMutation(), params)
    }

    companion object {
        const val query = """
            mutation AddWishlistCollectionItems(${'$'}collection_name: String, ${'$'}product_ids:[SuperInteger]) {
                  add_wishlist_collection_items(collection_name: ${'$'}collection_name, product_ids: ${'$'}product_ids) {
                    status
                    error_message
                    data {
                      success
                      collection_id
                      message
                    }
                  }
                }"""
    }
}