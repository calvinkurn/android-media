package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.minicart.common.data.request.deletecart.RemoveCartRequest
import com.tokopedia.minicart.common.data.response.deletecart.DeleteCartGqlResponse
import com.tokopedia.minicart.common.data.response.deletecart.RemoveFromCartData
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) : UseCase<RemoveFromCartData>() {

    private var removeCartRequest: RemoveCartRequest? = null

    fun setParams(removeCartRequest: RemoveCartRequest) {
        this.removeCartRequest = removeCartRequest
    }

    override suspend fun executeOnBackground(): RemoveFromCartData {
        if (removeCartRequest == null || removeCartRequest?.cartIds?.isEmpty() == true) {
            throw RuntimeException("Parameter is null or empty!")
        }

        val params = mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_KEY_ADD_TO_WISHLIST to removeCartRequest?.addWishlist,
                PARAM_KEY_CART_IDS to removeCartRequest?.cartIds
        )

        val request = GraphqlRequest(QUERY, DeleteCartGqlResponse::class.java, params)
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<DeleteCartGqlResponse>()

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

        val QUERY = """
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