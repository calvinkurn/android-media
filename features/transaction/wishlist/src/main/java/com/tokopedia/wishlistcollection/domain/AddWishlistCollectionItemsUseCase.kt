package com.tokopedia.wishlistcollection.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.wishlistcollection.data.response.AddWishlistCollectionItemsResponse
import com.tokopedia.wishlistcommon.util.GQL_ADD_WISHLIST_COLLECTION_ITEMS
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts
import javax.inject.Inject

@GqlQuery("AddWishlistCollectionItemsMutation", GQL_ADD_WISHLIST_COLLECTION_ITEMS)
class AddWishlistCollectionItemsUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) :
    UseCase<Result<AddWishlistCollectionItemsResponse.Data.AddWishlistCollectionItems>>() {
    private var params: Map<String, Any?>? = null

    override suspend fun executeOnBackground(): Result<AddWishlistCollectionItemsResponse.Data.AddWishlistCollectionItems> {
        return try {
            val request = GraphqlRequest(
                AddWishlistCollectionItemsMutation(),
                AddWishlistCollectionItemsResponse.Data::class.java, params
            )
            val response = gqlRepository.response(listOf(request)).getSuccessData<AddWishlistCollectionItemsResponse.Data>()
            Success(response.addWishlistCollectionItems)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    fun setParams(collectionName: String, productIds: List<String>) {
        params = mapOf(
            WishlistV2CommonConsts.COLLECTION_NAME to collectionName,
            WishlistV2CommonConsts.PRODUCT_IDs to productIds)
    }
}