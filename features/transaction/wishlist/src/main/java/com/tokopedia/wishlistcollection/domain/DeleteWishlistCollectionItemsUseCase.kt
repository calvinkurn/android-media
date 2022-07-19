package com.tokopedia.wishlistcollection.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.wishlistcollection.data.response.DeleteWishlistCollectionItemsResponse
import com.tokopedia.wishlistcommon.util.GQL_DELETE_WISHLIST_COLLECTION_ITEMS
import javax.inject.Inject

@GqlQuery("DeleteWishlistCollectionItemsMutation", GQL_DELETE_WISHLIST_COLLECTION_ITEMS)
class DeleteWishlistCollectionItemsUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) :
    UseCase<Result<DeleteWishlistCollectionItemsResponse.Data.DeleteWishlistCollectionItems>>() {
    private var params: Map<String, Any?>? = null
    private val listProductId = "productIDs"

    override suspend fun executeOnBackground(): Result<DeleteWishlistCollectionItemsResponse.Data.DeleteWishlistCollectionItems> {
        return try {
            val request = GraphqlRequest(
                DeleteWishlistCollectionItemsMutation(),
                DeleteWishlistCollectionItemsResponse.Data::class.java, params
            )
            val response = gqlRepository.response(listOf(request)).getSuccessData<DeleteWishlistCollectionItemsResponse.Data>()
            Success(response.deleteWishlistCollectionItems)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    fun setParams(productIds: List<String>) {
        params = mapOf(listProductId to productIds)
    }
}