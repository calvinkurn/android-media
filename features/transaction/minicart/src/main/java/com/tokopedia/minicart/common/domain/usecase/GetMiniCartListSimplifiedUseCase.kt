package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.util.ChosenAddressRequestHelper
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.mapper.MiniCartSimplifiedMapper
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetMiniCartListSimplifiedUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                           private val miniCartSimplifiedMapper: MiniCartSimplifiedMapper,
                                                           private val chosenAddressRequestHelper: ChosenAddressRequestHelper) : UseCase<MiniCartSimplifiedData>() {

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

    override suspend fun executeOnBackground(): MiniCartSimplifiedData {
        val request = GraphqlRequest(QUERY, MiniCartGqlResponse::class.java, params)
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<MiniCartGqlResponse>()

        if (response.miniCart.status == "OK") {
            return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(response.miniCart)
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
              available_section {
                available_group {
                  cart_details {
                    cart_id
                    product {
                      parent_id
                      product_id
                      product_quantity
                      product_notes
                    }
                  }
                }
              }
              unavailable_section {
                unavailable_group {
                  cart_details {
                    cart_id
                    product {
                      parent_id
                      product_id
                      product_quantity
                      product_notes                      
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent()
    }

}