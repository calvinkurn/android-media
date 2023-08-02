package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.param.RemoveCartTokofoodParam
import com.tokopedia.tokofood.common.domain.response.CartGeneralRemoveCartData
import com.tokopedia.tokofood.common.domain.response.RemoveCartTokofoodResponseNew
import javax.inject.Inject

private const val QUERY = """
        mutation RemoveCartTokofood(${'$'}params: CartGeneralRemoveCartParams) {
          cart_general_remove_cart(params: ${'$'}params) {
            data {
              success
              message
            }
          }
        }
    """

@GqlQuery("RemoveCartTokofood", QUERY)
class RemoveCartTokofoodUseCase @Inject constructor(repository: GraphqlRepository) :
    GraphqlUseCase<RemoveCartTokofoodResponseNew>(repository) {

    init {
        setTypeClass(RemoveCartTokofoodResponseNew::class.java)
        setGraphqlQuery(RemoveCartTokofood())
    }

    suspend fun execute(removeCartParam: RemoveCartTokofoodParam): CartGeneralRemoveCartData {
        if (removeCartParam.getIsCartIdsEmpty()) {
            throw MessageErrorException(ERROR_NO_CART_ID_MESSAGE)
        } else {
            val param = generateParams(removeCartParam)
            setRequestParams(param)
            val response = executeOnBackground()
            if (response.isSuccess()) {
                return response.cartGeneralRemoveCart.data
            } else {
                throw MessageErrorException(response.cartGeneralRemoveCart.data.message)
            }
        }
    }

    companion object {
        private const val PARAMS_KEY = "params"

        private const val ERROR_NO_CART_ID_MESSAGE = "Cannot delete product as of now because of empty cart id"

        private fun generateParams(params: RemoveCartTokofoodParam): Map<String, Any> {
            return mapOf(PARAMS_KEY to params)
        }
    }

}
