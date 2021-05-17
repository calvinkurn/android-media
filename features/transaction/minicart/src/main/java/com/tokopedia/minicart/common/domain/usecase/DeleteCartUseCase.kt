package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.minicart.common.data.response.deletecart.DeleteCartGqlResponse
import com.tokopedia.minicart.common.data.response.deletecart.RemoveFromCartData
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) : UseCase<RemoveFromCartData>() {

    // Todo : set params

    override suspend fun executeOnBackground(): RemoveFromCartData {
        val params = mapOf<String, String>()
        val request = GraphqlRequest(QUERY, DeleteCartGqlResponse::class.java, params)
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<DeleteCartGqlResponse>()

        if (response.removeFromCart.status == "OK" && response.removeFromCart.data.success == 1) {
            return response.removeFromCart
        } else {
            throw ResponseErrorException(response.removeFromCart.errorMessage.joinToString(", "))
        }
    }

    // Todo : set query
    companion object {
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