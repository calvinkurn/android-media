package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.param.RemoveCartTokofoodParamNew
import com.tokopedia.tokofood.common.domain.response.CartGeneralRemoveCartData
import com.tokopedia.tokofood.common.domain.response.RemoveCartTokofoodResponseNew
import javax.inject.Inject

// TODO: Remove New

private const val QUERY = """
        mutation RemoveCartTokofoodNew(${'$'}params: RemoveCartTokofoodParam!) {
          cart_general_remove_cart(params: ${'$'}params) {
            data {
              success
              message
            }
          }
        }
    """

@GqlQuery("RemoveCartTokofoodNew", QUERY)
class RemoveCartTokofoodUseCaseNew @Inject constructor(repository: GraphqlRepository) :
    GraphqlUseCase<RemoveCartTokofoodResponseNew>(repository) {

    init {
        setTypeClass(RemoveCartTokofoodResponseNew::class.java)
        setGraphqlQuery(RemoveCartTokofoodNew())
    }

    suspend fun execute(removeCartParam: RemoveCartTokofoodParamNew): CartGeneralRemoveCartData {
        val param = generateParams(removeCartParam)
        setRequestParams(param)
        val response = executeOnBackground()
        if (response.isSuccess()) {
            return response.cartGeneralRemoveCart.data
        } else {
            throw MessageErrorException(response.cartGeneralRemoveCart.data.message)
        }
    }

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(params: RemoveCartTokofoodParamNew): Map<String, Any> {
            return mapOf(PARAMS_KEY to params)
        }
    }

}
