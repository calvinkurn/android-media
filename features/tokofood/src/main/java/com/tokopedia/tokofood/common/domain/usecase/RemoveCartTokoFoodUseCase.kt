package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.param.RemoveCartTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodResponse
import com.tokopedia.tokofood.common.domain.response.RemoveCartTokoFoodResponse
import javax.inject.Inject

private const val QUERY = """
        mutation RemoveCartTokofood(${'$'}params: RemoveCartGeneralParams!) {
          remove_cart_general(params: ${'$'}params) {
            message
            status
            data {
              success
              message
              carts {
                success
                message
                business_id
                cart_id
                shop_id
                product_id
              }
            }
          }
        }
    """

@GqlQuery("RemoveCartTokofood", QUERY)
class RemoveCartTokoFoodUseCase @Inject constructor(repository: GraphqlRepository) :
    GraphqlUseCase<RemoveCartTokoFoodResponse>(repository) {

    init {
        setTypeClass(RemoveCartTokoFoodResponse::class.java)
        setGraphqlQuery(RemoveCartTokofood())
    }

    suspend fun execute(removeCartParam: RemoveCartTokoFoodParam): CartTokoFoodResponse {
        val param = generateParams(removeCartParam)
        setRequestParams(param)
        val response = executeOnBackground()
        if (response.cartResponse.isSuccess()) {
            return response.cartResponse
        } else {
            throw MessageErrorException(response.cartResponse.getMessageIfError())
        }
    }

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(params: RemoveCartTokoFoodParam): Map<String, Any> {
            return mapOf(PARAMS_KEY to params)
        }
    }

}