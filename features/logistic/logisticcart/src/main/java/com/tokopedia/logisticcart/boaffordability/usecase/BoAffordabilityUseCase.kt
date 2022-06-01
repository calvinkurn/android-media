package com.tokopedia.logisticcart.boaffordability.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.logisticcart.boaffordability.model.BoAffordabilityDataResponse
import com.tokopedia.logisticcart.boaffordability.model.BoAffordabilityGqlResponse
import com.tokopedia.logisticcart.boaffordability.model.BoAffordabilityResponse
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class BoAffordabilityUseCase @Inject constructor(@ApplicationContext private val gqlRepository: GraphqlRepository) : UseCase<BoAffordabilityDataResponse>() {

    private var requestParam: RatesParam? = null

    fun setParam(param: RatesParam): BoAffordabilityUseCase {
        requestParam = param
        return this
    }

    @GqlQuery(QUERY_BO_AFFORDABILITY, QUERY)
    override suspend fun executeOnBackground(): BoAffordabilityDataResponse {
        val param = this.requestParam?.toBoAffordabilityMap(GlobalConfig.VERSION_NAME) ?: throw RuntimeException("Param must be initialized")
        val request = GraphqlRequest(
                BoAffordabilityQuery(),
                BoAffordabilityGqlResponse::class.java,
                mapOf("input" to param)
        )
        return gqlRepository.response(listOf(request))
                .getData<BoAffordabilityGqlResponse>(BoAffordabilityGqlResponse::class.java).response.data
    }

    companion object {
        private const val QUERY_BO_AFFORDABILITY = "BoAffordabilityQuery"

        private const val QUERY = """
            query ongkirGetFreeShipping(${'$'}input: OngkirGetFreeShippingInput!) {
                ongkirGetFreeShipping(input: ${'$'}input) {
                    freeshipping {
                        is_promo
                        promo_code
                        shipper_product_id
                        image_url
                        discounted_rate
                        shipping_rate
                        benefit_amount
                        min_transaction
                        bo_type
                        benefit_class
                        bo_weight {
                            is_bo_weight
                            shipping_subsidy
                        }
                        texts {
                            ticker_cart
                            ticker_progressive
                        }
                    }
                }
            }"""
    }
}