package com.tokopedia.wishlistcollection.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionNamesResponse
import com.tokopedia.wishlistcommon.util.GQL_GET_WISHLIST_COLLECTION_NAMES
import javax.inject.Inject

@GqlQuery("GetWishlistCollectionNamesQuery", GQL_GET_WISHLIST_COLLECTION_NAMES)
class GetWishlistCollectionNamesUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) :
    UseCase<Result<GetWishlistCollectionNamesResponse.Data.GetWishlistCollectionNames>>() {

    override suspend fun executeOnBackground(): Result<GetWishlistCollectionNamesResponse.Data.GetWishlistCollectionNames> {
        return try {
            val request = GraphqlRequest(
                GetWishlistCollectionNamesQuery(),
                GetWishlistCollectionNamesResponse.Data::class.java
            )
            val response = gqlRepository.response(listOf(request)).getSuccessData<GetWishlistCollectionNamesResponse.Data>()
            Success(response.getWishlistCollectionNames)
        } catch (e: Exception) {
            Fail(e)
        }
    }
}