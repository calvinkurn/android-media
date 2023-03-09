package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood
import com.tokopedia.tokofood.common.domain.response.AddToCartTokoFoodResponse
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodResponse
import com.tokopedia.tokofood.common.presentation.mapper.UpdateProductMapperOld
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import javax.inject.Inject

private const val QUERY = """
          mutation AddToCartTokofood(${'$'}params: ATCGeneralParams!) {
            add_to_cart_general(params: ${'$'}params) {
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
                bottomsheet {
                  is_show_bottomsheet
                  title
                  description
                  image_url
                  buttons{
                      text
                      color
                      action
                      link
                  }
                }
              }
            }
          }
        """

@GqlQuery("AddToCartTokofoodOld", QUERY)
class AddToCartTokoFoodUseCaseOld @Inject constructor(
    repository: GraphqlRepository,
    private val chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper
): GraphqlUseCase<AddToCartTokoFoodResponse>(repository) {

    init {
        setTypeClass(AddToCartTokoFoodResponse::class.java)
        setGraphqlQuery(AddToCartTokofoodOld())
    }

    suspend fun execute(params: UpdateParam): CartTokoFoodResponse {
        val param = generateParams(
            params,
            CartAdditionalAttributesTokoFood(chosenAddressRequestHelper.getChosenAddress()).generateString()
        )
        setRequestParams(param)

        val response = executeOnBackground()
        if (response.cartResponse.isSuccess() || response.cartResponse.data.bottomSheet.isShowBottomSheet) {
            return response.cartResponse
        } else {
            throw MessageErrorException(response.cartResponse.getMessageIfError())
        }

    }

    companion object {

        private const val PARAMS_KEY = "params"

        private fun generateParams(param: UpdateParam,
                                   additionalAttr: String): Map<String, Any> {
            val cartParam = UpdateProductMapperOld.getAtcProductParamById(
                param.productList,
                additionalAttr,
                param.shopId
            )
            return mapOf(PARAMS_KEY to cartParam)
        }

    }

}
