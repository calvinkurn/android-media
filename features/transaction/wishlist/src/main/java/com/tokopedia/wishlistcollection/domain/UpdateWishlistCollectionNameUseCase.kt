package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlistcollection.data.params.UpdateWishlistCollectionNameParams
import com.tokopedia.wishlistcollection.data.response.UpdateWishlistCollectionNameResponse
import javax.inject.Inject

@GqlQuery("UpdateWishlistCollectionNameMutation", UpdateWishlistCollectionNameUseCase.query)
class UpdateWishlistCollectionNameUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) :
    CoroutineUseCase<UpdateWishlistCollectionNameParams, UpdateWishlistCollectionNameResponse>(dispatcher.io) {

    override suspend fun execute(params: UpdateWishlistCollectionNameParams): UpdateWishlistCollectionNameResponse {
        return repository.request(UpdateWishlistCollectionNameMutation(), params)
    }

    override fun graphqlQuery(): String = query

    companion object {
        const val query = """
            mutation UpdateWishlistCollectionName(${'$'}collectionID: SuperInteger, ${'$'}collectionName: String) {
                  update_wishlist_collection_name(collectionID: ${'$'}collectionID, collectionName: ${'$'}collectionName) {
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
