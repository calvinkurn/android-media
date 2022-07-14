package com.tokopedia.wishlistcollection.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.wishlistcollection.data.response.DeleteWishlistCollectionResponse
import com.tokopedia.wishlistcommon.util.GQL_DELETE_WISHLIST_COLLECTION
import javax.inject.Inject

@GqlQuery("DeleteWishlistCollectionMutation", GQL_DELETE_WISHLIST_COLLECTION)
class DeleteWishlistCollectionUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) :
    UseCase<Result<DeleteWishlistCollectionResponse.Data.DeleteWishlistCollection>>() {
    private var params: Map<String, Any?>? = null
    private val collectionId = "collectionID"

    override suspend fun executeOnBackground(): Result<DeleteWishlistCollectionResponse.Data.DeleteWishlistCollection> {
        return try {
            val request = GraphqlRequest(
                DeleteWishlistCollectionMutation(),
                DeleteWishlistCollectionResponse.Data::class.java, params
            )
            val response = gqlRepository.response(listOf(request)).getSuccessData<DeleteWishlistCollectionResponse.Data>()
            Success(response.deleteWishlistCollection)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    fun setParams(collectionIdToBeDeleted: String) {
        params = mapOf(collectionId to collectionIdToBeDeleted)
    }
}