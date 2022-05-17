package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.MemberShipValidateResponse
import javax.inject.Inject

class TmKuponProgramValidateUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<MemberShipValidateResponse>(graphqlRepository) {

    @GqlQuery("TmMemberShipValidate", IS_MEMBERSHIP_VALIDATE)
    fun getMembershipValidateInfo(
        success: (MemberShipValidateResponse) -> Unit,
        onFail: (Throwable) -> Unit,
    ) {
        this.setTypeClass(MemberShipValidateResponse::class.java)
        this.setGraphqlQuery(TmMemberShipValidate.GQL_QUERY)
        this.execute({
            success(it)
        }, {
            onFail(it)
        })
    }

}


const val IS_MEMBERSHIP_VALIDATE = """
    query membershipValidateBenefit(${'$'}shopID: String!, ${'$'}StartTimeUnix: String! , ${'$'}EndTimeUnix: String! , ${'$'}Source: String! ){
        membershipValidateBenefit(shopID: ${'$'}shopID, StartTimeUnix: ${'$'}StartTimeUnix, EndTimeUnix: ${'$'}EndTimeUnix, Source: ${'$'}Source){
      resultStatus {
      code
      message
      reason
    }
    isValidMembershipBenefit
  }
}
 
"""