package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.minicart.common.data.response.minicartlistsimplified.MiniCartSimplifiedGqlResponse
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.mapper.MiniCartSimplifiedMapper
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetMiniCartListSimplifiedUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                           private val miniCartSimplifiedMapper: MiniCartSimplifiedMapper) : UseCase<MiniCartSimplifiedData>() {

    // Todo : set params

    override suspend fun executeOnBackground(): MiniCartSimplifiedData {
        val params = mapOf<String, String>()
        val request = GraphqlRequest(QUERY, MiniCartSimplifiedGqlResponse::class.java, params)
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<MiniCartSimplifiedGqlResponse>()

        if (response.miniCart.status == "OK") {
            return miniCartSimplifiedMapper.mapMiniCartSimplifiedData(response.miniCart)
        } else {
            throw ResponseErrorException(response.miniCart.errorMessage.joinToString(", "))
        }
    }

    // Todo : set query
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
              available_section {
                available_group {
                  cart_details {
                    cart_id
                    product {
                      parent_id
                      product_id
                      product_quantity
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