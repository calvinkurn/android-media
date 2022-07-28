package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.MembershipData
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetSellerOnboarding
import com.tokopedia.tokomember_seller_dashboard.model.TmOnboardingResponse
import javax.inject.Inject

class TmOnBoardingCheckUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<MembershipData>(graphqlRepository) {

    @GqlQuery("TmSellerOnboardCheck", TM_SELLER_ONBOARD_CHECK)
    fun getMemberOnboardingInfo(
        success: (MembershipData) -> Unit,
        onFail: (Throwable) -> Unit,
        shopID: Int
    ) {
        this.setTypeClass(MembershipData::class.java)
        this.setRequestParams(getRequestParams(shopID))
        this.setGraphqlQuery(TmSellerOnboardCheck.GQL_QUERY)
        execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(cardId: Int): Map<String, Any> {
        return mapOf(SHOP_ID to cardId)
    }

    companion object {
        const val SHOP_ID = "shopID"
    }
}

const val TM_SELLER_ONBOARD_CHECK = """
    query membershipGetSellerOnboarding(${'$'}shopID: Int!) {
    membershipGetSellerOnboarding(shopID: ${'$'}shopID) {
    resultStatus {
      code
      message
      reason
    }
    cardID
    sellerHomeContent {
      isShowContent
    }
  }
}
"""