package com.tokopedia.universal_sharing.view.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility
import javax.inject.Inject

class AffiliateEligibilityCheckUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GenerateAffiliateLinkEligibility>(graphqlRepository) {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): GenerateAffiliateLinkEligibility {
        val gqlRequest = GraphqlRequest(QUERY, GenerateAffiliateLinkEligibility.Response::class.java, params)
        val gqlResponse = graphqlRepository.response(
            listOf(gqlRequest),
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build()
        )

        val response = gqlResponse.getData<GenerateAffiliateLinkEligibility.Response>(GenerateAffiliateLinkEligibility.Response::class.java)
        return response.generateAffiliateLinkEligibility
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
                        Banner {
                            Icon
                            Title
                            Message
                            CtaLink
                        }
                        EligibleCommission {
                            IsEligible
                            AmountFormatted
                            Amount
                            Message
                            Badge
                            ExpiredDate
                            ExpiredDateFormatted
                            SellerAmount
                            SellerAmountFormatted
                            SsaStatus
                        }
                    }
                }
                """

        fun createParam(affiliateInput: AffiliateInput): HashMap<String, Any> {
            return hashMapOf(
                ELIGIBILITY_REQUEST to affiliateInput
            )
        }
    }
}
