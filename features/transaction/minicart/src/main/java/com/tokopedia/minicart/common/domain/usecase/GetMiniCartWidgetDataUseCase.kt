package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.minicart.common.data.response.minicartwidgetdata.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartwidgetdata.MiniCartWidgetGqlResponse
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetMiniCartWidgetDataUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) : UseCase<MiniCartData>() {

    // Todo : set params

    override suspend fun executeOnBackground(): MiniCartData {
        val params = mapOf<String, String>()
        val request = GraphqlRequest(QUERY, MiniCartWidgetGqlResponse::class.java, params)
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<MiniCartWidgetGqlResponse>()

        if (response.miniCart.status == "OK") {
            return response.miniCart
        } else {
            throw ResponseErrorException(response.miniCart.errorMessage.joinToString(", "))
        }
    }

    companion object {
        val QUERY = """
        query mini_cart(${'$'}shopId: String) {
          status
          mini_cart(shopId:${'$'}shopId) {
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