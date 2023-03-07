package com.tokopedia.product.addedit.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.detail.domain.model.GetDefaultCommissionRulesRequest
import com.tokopedia.product.addedit.detail.domain.model.GetDefaultCommissionRulesResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetDefaultCommissionRulesUseCase @Inject constructor(
    repository: GraphqlRepository
): GraphqlUseCase<GetDefaultCommissionRulesResponse>(repository) {

    companion object {
        const val PARAM_INPUT = "input"
        private val query = """
            
            query getDefaultCommissionRules(${'$'}input: getDefaultCommissionRulesRequest!) {
              get_default_commission_rules(input:${'$'}input) {
                category_rates {
                  commission_rules {
                    shop_type
                    commission_rate
                  }
                }
              }
            }
            """.trimIndent()
    }

    private val requestParams = RequestParams.create()

    init {
        setGraphqlQuery(query)
        setTypeClass(GetDefaultCommissionRulesResponse::class.java)
    }

    fun setParam(categoryId: Int) {
        requestParams.putObject(PARAM_INPUT, GetDefaultCommissionRulesRequest(listOf(categoryId)))
        setRequestParams(requestParams.parameters)
    }
}
