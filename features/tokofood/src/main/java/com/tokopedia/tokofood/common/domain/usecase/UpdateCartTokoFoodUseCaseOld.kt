package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodResponse
import com.tokopedia.tokofood.common.domain.response.UpdateCartTokoFoodResponse
import com.tokopedia.tokofood.common.presentation.mapper.UpdateProductMapperOld
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import javax.inject.Inject

private const val QUERY = """
        mutation UpdateCartTokofood(${'$'}params: UpdateCartGeneralParams!) {
          update_cart_general(params: ${'$'}params) {
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
                quantity
                metadata
              }
            }
          }
        }
    """

@GqlQuery("UpdateCartTokofoodOld", QUERY)
class UpdateCartTokoFoodUseCaseOld @Inject constructor(
    repository: GraphqlRepository,
    private val chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper
): GraphqlUseCase<UpdateCartTokoFoodResponse>(repository) {

    init {
        setTypeClass(UpdateCartTokoFoodResponse::class.java)
        setGraphqlQuery(UpdateCartTokofoodOld())
    }

    suspend fun execute(updateParam: UpdateParam): CartTokoFoodResponse {
        val param = generateParams(
            updateParam,
            CartAdditionalAttributesTokoFood(chosenAddressRequestHelper.getChosenAddress()).generateString()
        )
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

        private fun generateParams(updateParam: UpdateParam,
                                   additionalAttr: String): Map<String, Any> {
            val cartParam = UpdateProductMapperOld.getUpdateProductParamById(
                updateParam.productList,
                additionalAttr,
                updateParam.shopId
            )
            return mapOf(PARAMS_KEY to cartParam)
        }
    }

}
