package com.tokopedia.wishlistcollection.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.wishlistcollection.data.response.CollectionWishlistResponse
import com.tokopedia.wishlistcommon.util.GQL_GET_WISHLIST_COLLECTION
import javax.inject.Inject

@GqlQuery("GetWishlistCollectionQuery", GQL_GET_WISHLIST_COLLECTION)
class GetWishlistCollectionUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) :
    UseCase<Result<CollectionWishlistResponse.Data.GetWishlistCollections>>() {

    override suspend fun executeOnBackground(): Result<CollectionWishlistResponse.Data.GetWishlistCollections> {
        return try {
            val request = GraphqlRequest(
                GetWishlistCollectionQuery(),
                CollectionWishlistResponse.Data::class.java
            )
            val response = gqlRepository.response(listOf(request)).getSuccessData<CollectionWishlistResponse.Data>()
            Success(response.getWishlistCollections)
        } catch (e: Exception) {
            Fail(e)
        }
    }
}