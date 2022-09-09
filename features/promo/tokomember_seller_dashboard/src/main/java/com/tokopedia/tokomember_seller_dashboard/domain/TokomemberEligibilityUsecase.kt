package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.CheckEligibility
import javax.inject.Inject

class TokomemberEligibilityUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<CheckEligibility>(graphqlRepository) {

    @GqlQuery("IsEligible", IS_ELIGIBLE)
    fun checkEligibility(
        success: (CheckEligibility) -> Unit,
        onFail: (Throwable) -> Unit,
        shopId: Int,
        isRegister: Boolean
    ) {
        this.setTypeClass(CheckEligibility::class.java)
        this.setGraphqlQuery(IS_ELIGIBLE)
        this.setRequestParams(getRequestParams(shopId, isRegister))
        this.execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(shopId: Int, isRegister: Boolean): Map<String, Any> {
        return mapOf(SHOP_ID to shopId, IS_REGISTER to isRegister)
    }

    companion object {
        const val SHOP_ID = "shopID"
        const val IS_REGISTER = "isRegister"

    }
}

const val IS_ELIGIBLE = """
    query membershipCheckSellerEligibility(${'$'}shopID: Int!, ${'$'}isRegister: Boolean!){
        membershipCheckSellerEligibility(shopID: ${'$'}shopID, isRegister: ${'$'}isRegister){
            resultStatus {
              code
              reason
            }
            isEligible
            message {
              title
              subtitle
            }
        }
    }

"""