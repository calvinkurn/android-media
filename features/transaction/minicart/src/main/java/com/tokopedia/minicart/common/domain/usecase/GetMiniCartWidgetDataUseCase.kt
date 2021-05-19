package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetMiniCartWidgetDataUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) : UseCase<MiniCartData>() {

    // Todo : set params

    override suspend fun executeOnBackground(): MiniCartData {
        throw ResponseErrorException()
//        return MiniCartData(
//                miniCartData = Data(
//                        totalProductCount = 10,
//                        totalProductError = 1,
//                        totalProductPrice = 5000000
//                )
//        )
/*
        val params = mapOf<String, String>()
        val request = GraphqlRequest(QUERY, MiniCartGqlResponse::class.java, params)
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<MiniCartGqlResponse>()

        if (response.miniCart.status == "OK") {
            return response.miniCart
        } else {
            throw ResponseErrorException(response.miniCart.errorMessage.joinToString(", "))
        }
*/
    }

    companion object {
        val QUERY = """
        query mini_cart(${'$'}lang: String, ${'$'}additional_params: CartRevampAdditionalParams, ${'$'}shop_ids : [String]) {
          status
          mini_cart(lang:${'$'}lang, additional_params:"${'$'}"additional_params, shop_ids:${'$'}shop_ids}) {
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