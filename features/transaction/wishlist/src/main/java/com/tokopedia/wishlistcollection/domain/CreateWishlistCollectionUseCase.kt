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
import com.tokopedia.wishlistcollection.data.response.CreateWishlistCollectionResponse
import com.tokopedia.wishlistcommon.util.GQL_CREATE_WISHLIST_COLLECTION
import javax.inject.Inject

@GqlQuery("CreateWishlistCollectionMutation", GQL_CREATE_WISHLIST_COLLECTION)
class CreateWishlistCollectionUseCase @Inject constructor(@ApplicationContext private val gqlRepository: GraphqlRepository) :
    UseCase<Result<CreateWishlistCollectionResponse.Data.CreateWishlistCollection>>() {
    private var params: Map<String, Any?>? = null
    private val paramName = "name"

    override suspend fun executeOnBackground(): Result<CreateWishlistCollectionResponse.Data.CreateWishlistCollection> {
        return try {
            val request = GraphqlRequest(
                CreateWishlistCollectionMutation(),
                CreateWishlistCollectionResponse.Data::class.java, params
            )
            val response = gqlRepository.response(listOf(request)).getSuccessData<CreateWishlistCollectionResponse.Data>()
            Success(response.createWishlistCollection)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    fun setParams(name: String) {
        params = mapOf(paramName to name)
    }
}