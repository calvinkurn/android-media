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
import com.tokopedia.wishlistcollection.data.params.UpdateWishlistCollectionNameParams
import com.tokopedia.wishlistcollection.data.response.UpdateWishlistCollectionNameResponse
import com.tokopedia.wishlistcommon.util.GQL_UPDATE_WISHLIST_COLLECTION_NAME
import javax.inject.Inject

@GqlQuery("UpdateWishlistCollectionNameMutation", GQL_UPDATE_WISHLIST_COLLECTION_NAME)
class UpdateWishlistCollectionNameUseCase @Inject constructor(@ApplicationContext private val gqlRepository: GraphqlRepository) :
    UseCase<Result<UpdateWishlistCollectionNameResponse.Data.UpdateWishlistCollectionName>>() {
    private var params: Map<String, Any?>? = null
    private val collectionId = "collectionID"
    private val collectionName = "collectionName"

    override suspend fun executeOnBackground(): Result<UpdateWishlistCollectionNameResponse.Data.UpdateWishlistCollectionName> {
        return try {
            val request = GraphqlRequest(
                UpdateWishlistCollectionNameMutation(),
                UpdateWishlistCollectionNameResponse.Data::class.java, params
            )
            val response = gqlRepository.response(listOf(request)).getSuccessData<UpdateWishlistCollectionNameResponse.Data>()
            Success(response.updateWishlistCollectionName)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    fun setParams(param: UpdateWishlistCollectionNameParams) {
        params = mapOf(collectionId to param.collectionId,
            collectionName to param.collectionName)
    }
}