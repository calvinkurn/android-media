package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionNamesResponse
import javax.inject.Inject

@GqlQuery("GetWishlistCollectionNamesQuery", GetWishlistCollectionNamesUseCase.query)
class GetWishlistCollectionNamesUseCase @Inject constructor(@ApplicationContext private val repository: GraphqlRepository,
                                                            dispatcher: CoroutineDispatchers) :
    CoroutineUseCase<Unit, GetWishlistCollectionNamesResponse>(dispatcher.io) {

    override suspend fun execute(params: Unit): GetWishlistCollectionNamesResponse {
        return repository.request(GetWishlistCollectionNamesQuery(), params)
    }

    override fun graphqlQuery(): String = query

    companion object {
        const val query = """
            query GetWishlistCollectionNames {
                  get_wishlist_collection_names{
                    error_message
                    status
                    data{
                      id
                      name
                    }
                  }
                }"""
    }
}