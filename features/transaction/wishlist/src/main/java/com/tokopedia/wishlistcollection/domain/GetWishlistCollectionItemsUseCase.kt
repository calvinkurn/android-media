package com.tokopedia.wishlistcollection.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.wishlistcollection.data.params.GetWishlistCollectionItemsParams
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.util.GQL_GET_WISHLIST_COLLECTION_ITEMS
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.PARAMS
import javax.inject.Inject

@GqlQuery("GetWishlistCollectionItemsQuery", GQL_GET_WISHLIST_COLLECTION_ITEMS)
class GetWishlistCollectionItemsUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) :
    UseCase<Result<GetWishlistCollectionItemsResponse.Data.GetWishlistCollectionItems>>() {
    private var params: Map<String, Any?>? = null

    override suspend fun executeOnBackground(): Result<GetWishlistCollectionItemsResponse.Data.GetWishlistCollectionItems> {
        return try {
            val request = GraphqlRequest(
                GetWishlistCollectionItemsQuery(),
                GetWishlistCollectionItemsResponse.Data::class.java, params
            )
            val response = gqlRepository.response(listOf(request)).getSuccessData<GetWishlistCollectionItemsResponse.Data>()
            Success(response.getWishlistCollectionItems)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    fun setParams(param: GetWishlistCollectionItemsParams) {
        params = mapOf(PARAMS to param)
    }
}