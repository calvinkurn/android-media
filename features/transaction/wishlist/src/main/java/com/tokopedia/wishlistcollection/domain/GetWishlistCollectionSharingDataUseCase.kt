package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionSharingDataResponse
import java.util.HashMap
import javax.inject.Inject

@GqlQuery("GetWishlistCollectionSharingDataQuery", GetWishlistCollectionSharingDataUseCase.query)
class GetWishlistCollectionSharingDataUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers) :
    CoroutineUseCase<Long, GetWishlistCollectionSharingDataResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(param: Long): GetWishlistCollectionSharingDataResponse {
        val params = HashMap<String, Any?>()
        params[PARAM_COLLECTION_ID] = param
        return repository.request(GetWishlistCollectionSharingDataQuery(), params)
    }

    companion object {
        const val PARAM_COLLECTION_ID = "collectionID"
        const val query = """
            query GetWishlistCollectionSharingData(${'$'}collectionID: Int64) {
              get_wishlist_collection_sharing_data(collectionID: ${'$'}collectionID){
                status
                error_message
                data{
                  empty_wishlist_image_url
                  collection {
                    id
                    name
                    owner {
                      id
                      name
                    }
                  }
                  share_link {
                    redirection_url
                    deeplink
                  }
                  total_item
                  items {
                    id
                    name
                    image_url
                    price
                  }
                }
              }
            }"""
    }
}
