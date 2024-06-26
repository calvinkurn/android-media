package com.tokopedia.cartcommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.cartcommon.data.response.deletecart.DeleteCartGqlResponse
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) : UseCase<RemoveFromCartData>() {

    private var params: Map<String, Any>? = null

    fun setParams(cartIdList: List<String>, addToWishlist: Boolean = false) {
        params = mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_KEY_ADD_TO_WISHLIST to if (addToWishlist) 1 else 0,
                PARAM_KEY_CART_IDS to cartIdList
        )
    }

    override suspend fun executeOnBackground(): RemoveFromCartData {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }

        val request = GraphqlRequest(MUTATION, DeleteCartGqlResponse::class.java, params)
        val response = graphqlRepository.response(listOf(request)).getSuccessData<DeleteCartGqlResponse>()

        if (response.removeFromCart.status == "OK" && response.removeFromCart.data.success == 1) {
            return response.removeFromCart
        } else {
            throw ResponseErrorException(response.removeFromCart.errorMessage.joinToString(", "))
        }
    }

    companion object {
        private const val PARAM_KEY_LANG = "lang"
        private const val PARAM_VALUE_ID = "id"
        private const val PARAM_KEY_ADD_TO_WISHLIST = "addWishlist"
        private const val PARAM_KEY_CART_IDS = "cartIds"

        val MUTATION = """
        mutation remove_from_cart(${'$'}addWishlist: Int, ${'$'}cartIds: [String], ${'$'}lang: String){
            remove_from_cart(addWishlist: ${'$'}addWishlist, cartIds:${'$'}cartIds, lang: ${'$'}lang){
            error_message
            status
            data {
                message
                success
            }
          }
        }
        """.trimIndent()
    }

}