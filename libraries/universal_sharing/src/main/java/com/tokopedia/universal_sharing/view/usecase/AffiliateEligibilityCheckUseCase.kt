package com.tokopedia.universal_sharing.view.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.universal_sharing.view.model.AffiliatePDPInput
import com.tokopedia.universal_sharing.view.model.EligibleCommission
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility

class AffiliateEligibilityCheckUseCase constructor(
    private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<GenerateAffiliateLinkEligibility>(graphqlRepository) {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): GenerateAffiliateLinkEligibility {
        val gqlRequest = GraphqlRequest(QUERY, GenerateAffiliateLinkEligibility.Response::class.java, params)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
            .Builder(CacheType.CACHE_FIRST).build())

        val response = gqlResponse.getData<GenerateAffiliateLinkEligibility.Response>(GenerateAffiliateLinkEligibility.Response::class.java)
        if (response.generateAffiliateLinkEligibility.affiliateEligibility?.isEligible == true) {
            return response.generateAffiliateLinkEligibility!!
        } else {
            throw MessageErrorException("Error in affiliate eligibility check")
        }
    }

    companion object {

        private const val ELIGIBILITY_REQUEST = "generateAffiliateLinkEligibilityRequest"

        const val QUERY = """query generateAffiliateLinkEligibility(${'$'}generateAffiliateLinkEligibilityRequest: generateAffiliateLinkEligibilityRequest!) {
                generateAffiliateLinkEligibility(generateAffiliateLinkEligibilityRequest: ${'$'}generateAffiliateLinkEligibilityRequest) {
                        Status
                        ShowTicker
                        TickerType
                        Message
                        AffiliateEligibility {
                            IsRegistered
                            IsEligible
                        }
                        EligibleCommission {
                            IsEligible
                            AmountFormatted
                            Amount
                            Message
                        }
                    }
                }
                """

        fun createParam(affiliatePDPInput: AffiliatePDPInput): HashMap<String, Any> {
            return hashMapOf(
                ELIGIBILITY_REQUEST to affiliatePDPInput
            )
        }
    }
}
