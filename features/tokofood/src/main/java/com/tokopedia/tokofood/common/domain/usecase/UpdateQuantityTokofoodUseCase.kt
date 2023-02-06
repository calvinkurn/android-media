package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.param.UpdateQuantityTokofoodParam
import com.tokopedia.tokofood.common.domain.response.CartGeneralUpdateCartQuantityData
import com.tokopedia.tokofood.common.domain.response.UpdateQuantityTokofoodResponse
import javax.inject.Inject

// TODO: Remove New

private const val QUERY = """
        mutation UpdateQuantityTokofood(${'$'}params: UpdateQuantityTokofoodParam!) {
          cart_general_update_cart_quantity(params: ${'$'}params) {
            data {
              success
              message
            }
          }
        }
    """

@GqlQuery("RemoveCartTokofoodNew", QUERY)
class UpdateQuantityTokofoodUseCase @Inject constructor(
    repository: GraphqlRepository
): GraphqlUseCase<UpdateQuantityTokofoodResponse>(repository) {

    init {
        setTypeClass(UpdateQuantityTokofoodResponse::class.java)
        setGraphqlQuery(UpdateQuantityTokofood())
    }

    suspend fun execute(updateQuantityParam: UpdateQuantityTokofoodParam): CartGeneralUpdateCartQuantityData {
        val param = generateParams(updateQuantityParam)
        setRequestParams(param)
        val response = executeOnBackground()
        if (response.isSuccess()) {
            return response.cartGeneralUpdateCartQuantity.data
        } else {
            throw MessageErrorException(response.cartGeneralUpdateCartQuantity.data.message)
        }
    }

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(params: UpdateQuantityTokofoodParam): Map<String, Any> {
            return mapOf(PARAMS_KEY to params)
        }
    }

}
