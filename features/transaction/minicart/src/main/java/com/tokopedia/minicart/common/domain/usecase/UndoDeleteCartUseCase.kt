package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.common.data.response.undodeletecart.UndoDeleteCartDataResponse
import com.tokopedia.minicart.common.data.response.undodeletecart.UndoDeleteCartGqlResponse
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class UndoDeleteCartUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) : UseCase<UndoDeleteCartDataResponse>() {

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

    private var params: Map<String, Any>? = null

    fun setParams(miniCartItemList: List<MiniCartProductUiModel>) {
        val cartIds = mutableListOf<String>()
        miniCartItemList.forEach {
            cartIds.add(it.cartId)
        }

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

}