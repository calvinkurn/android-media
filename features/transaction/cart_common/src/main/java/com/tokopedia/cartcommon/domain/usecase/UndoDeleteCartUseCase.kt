package com.tokopedia.cartcommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.cartcommon.data.response.undodeletecart.UndoDeleteCartDataResponse
import com.tokopedia.cartcommon.data.response.undodeletecart.UndoDeleteCartGqlResponse
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class UndoDeleteCartUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) : UseCase<UndoDeleteCartDataResponse>() {

    private var params: Map<String, Any>? = null

    fun setParams(cartIds: List<String>) {
        params = mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_KEY_CART_IDS to cartIds
        )
    }

    override suspend fun executeOnBackground(): UndoDeleteCartDataResponse {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }

        val request = GraphqlRequest(MUTATION, UndoDeleteCartGqlResponse::class.java, params)
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<UndoDeleteCartGqlResponse>()

        if (response.undoDeleteCartDataResponse.status == "OK" && response.undoDeleteCartDataResponse.data.success == 1) {
            return response.undoDeleteCartDataResponse
        } else {
            throw ResponseErrorException(response.undoDeleteCartDataResponse.errorMessage.joinToString(", "))
        }
    }

    companion object {
        private const val PARAM_KEY_LANG = "lang"
        private const val PARAM_VALUE_ID = "id"
        private const val PARAM_KEY_CART_IDS = "cartIds"

        val MUTATION = """
        mutation undo_remove_product_cart(${'$'}cartIds: [String], ${'$'}lang : String) {
          undo_remove_product_cart(cartIds: ${'$'}cartIds, lang: ${'$'}lang ) {
            error_message
            status
            data {
              success
              message
              data {
                cart_ids
              }
            }
          }
        }

        """.trimIndent()
    }

}