package com.tokopedia.cartcommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cartcommon.domain.model.bmgm.request.BmGmGetGroupProductTickerParams
import com.tokopedia.cartcommon.domain.model.bmgm.response.BmGmGetGroupProductTickerResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

@GqlQuery("GetGroupProductTickerQuery", BmGmGetGroupProductTickerUseCase.query)
class BmGmGetGroupProductTickerUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<BmGmGetGroupProductTickerParams, BmGmGetGroupProductTickerResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(params: BmGmGetGroupProductTickerParams): BmGmGetGroupProductTickerResponse {
        return graphqlRepository.request(GetGroupProductTickerQuery(), createVariables(params))
    }

    private fun createVariables(params: BmGmGetGroupProductTickerParams): Map<String, Any> {
        val variables = mutableMapOf<String, Any>()
        variables[KEY_PARAMS] = params
        return variables
    }

    companion object {
        private const val KEY_PARAMS = "params"
        const val query = """
            query getGroupProductTicker(${'$'}params: GetGroupProductTickerParams) {
                get_group_product_ticker(params:${'$'}params) {
                    error_message
                    status
                    data {
                        multiple_data {
                           type
                           action
                           cart_string_order
                           offer_id
                           icon {
                               url
                           }
                           message {
                               text
                               url
                           }
                           discount_amount
                           bmgm {
                               offer_id
                               offer_type_id
                               offer_name
                               offer_icon
                               offer_message
                               offer_landing_page_link
                               offer_json_data
                               total_discount
                               offer_status
                               is_tier_achieved
                               tier_product {
                                   tier_id
                                   tier_name
                                   benefit_quantity
                                   benefit_wording
                                   action_wording
                                   tier_message
                                   tier_discount_text
                                   tier_discount_amount
                                   price_before_benefit
                                   price_after_benefit
                                   list_product {
                                       product_id
                                       warehouse_id
                                       quantity
                                       price_before_benefit
                                       price_after_benefit
                                       cart_id
                                   }
                                   products_benefit {
                                      product_id
                                      product_name
                                      quantity
                                      product_cache_image_url
                                      stock
                                      original_price
                                      final_price
                                   }
                               }
                           }
                        }
                    }
                }
            }
            """
    }
}
