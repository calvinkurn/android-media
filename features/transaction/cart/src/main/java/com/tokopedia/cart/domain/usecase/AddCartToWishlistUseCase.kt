package com.tokopedia.cart.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.cart.data.model.request.AddCartToWishlistRequest
import com.tokopedia.cart.data.model.response.addcarttowishlist.AddCartToWishlistGqlResponse
import com.tokopedia.cart.domain.model.cartlist.AddCartToWishlistData
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class AddCartToWishlistUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
) : UseCase<AddCartToWishlistData>() {

    companion object {
        const val PARAM_ADD_CART_TO_WISHLIST_REQUEST = "PARAM_ADD_CART_TO_WISHLIST_REQUEST"

        private const val PARAM = "params"
        private const val PARAM_KEY_CART_IDS = "cart_ids"

        const val QUERY_ADD_CART_TO_WISHLIST = "AddCartToWishlistQuery"
    }

    private var params: Map<String, Any> = emptyMap()

    fun setParams(request: AddCartToWishlistRequest): AddCartToWishlistUseCase {
        params = mapOf(
            PARAM to mapOf(PARAM_KEY_CART_IDS to request.cartIds)
        )
        return this
    }


    @GqlQuery(QUERY_ADD_CART_TO_WISHLIST, ADD_TO_WISHLIST_QUERY)
    override suspend fun executeOnBackground(): AddCartToWishlistData {
        val request = GraphqlRequest(AddCartToWishlistQuery(), AddCartToWishlistGqlResponse::class.java, params)
        val addCartToWishlistGqlResponse = graphqlRepository.response(listOf(request)).getSuccessData<AddCartToWishlistGqlResponse>()
        val addCartToWishlistData = AddCartToWishlistData()
        val response = addCartToWishlistGqlResponse.addCartToWishlistDataResponse
        addCartToWishlistData.status = response.status
        addCartToWishlistData.success = response.data.success

        if (response.data.message.isNotEmpty()) {
            addCartToWishlistData.message = response.data.message[0]
        }
        return addCartToWishlistData
    }
}
