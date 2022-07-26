package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.wishlistcollection.data.params.AddWishlistCollectionsHostBottomSheetParams
import com.tokopedia.wishlistcollection.data.response.AddWishlistCollectionItemsResponse
import com.tokopedia.wishlistcommon.util.GQL_ADD_WISHLIST_COLLECTION_ITEMS
import javax.inject.Inject

@GqlQuery("AddWishlistCollectionItemsMutation", GQL_ADD_WISHLIST_COLLECTION_ITEMS)
class AddWishlistCollectionItemsUseCase @Inject constructor(@ApplicationContext private val gqlRepository: GraphqlRepository) :
    UseCase<Result<AddWishlistCollectionItemsResponse.Data.AddWishlistCollectionItems>>() {
    private var params: Map<String, Any?>? = null
    private val collectionId = "collection_id"
    private val collectionName = "collection_name"
    private val productIds = "product_ids"

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

    fun setParams(param: AddWishlistCollectionsHostBottomSheetParams) {
        params = mapOf(collectionId to param.collectionId,
            collectionName to param.collectionName,
            productIds to param.productIds)
    }
}