package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.util.ChosenAddressRequestHelper
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetMiniCartWidgetDataUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                       private val chosenAddressRequestHelper: ChosenAddressRequestHelper) : UseCase<MiniCartData>() {

    private var params: Map<String, Any>? = null

    fun setParams(shopIds: List<String>) {
        params = mapOf(
                "dummy" to 1,
                GetMiniCartListUseCase.PARAM_KEY_LANG to GetMiniCartListUseCase.PARAM_VALUE_ID,
                GetMiniCartListUseCase.PARAM_KEY_ADDITIONAL to mapOf(
                        GetMiniCartListUseCase.PARAM_KEY_SHOP_IDS to shopIds,
                        ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress()
                )
        )
    }

    override suspend fun executeOnBackground(): MiniCartData {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }

        val request = GraphqlRequest(QUERY, MiniCartGqlResponse::class.java, params)
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<MiniCartGqlResponse>()

        if (response.miniCart.status == "OK") {
            return response.miniCart
        } else {
            throw ResponseErrorException(response.miniCart.errorMessage.joinToString(", "))
        }
    }

    companion object {
        val QUERY = """
        query mini_cart(${'$'}dummy: Int, ${'$'}lang: String, ${'$'}additional_params: CartRevampAdditionalParams) {
          status
          mini_cart(dummy: ${'$'}dummy, lang: ${'$'}lang, additional_params: ${'$'}additional_params) {
            error_message
            status
            data {
              errors
              total_product_count
              total_product_error
              total_product_price
            }
          }
        }
        """.trimIndent()
    }

}