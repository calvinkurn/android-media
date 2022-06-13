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
        shopId: String,
        startTime: String,
        endTime: String,
        source: String,
    ) {
        this.setTypeClass(MemberShipValidateResponse::class.java)
        this.setRequestParams(getRequestParams(shopId,startTime,endTime, source))
        this.setGraphqlQuery(TmMemberShipValidate.GQL_QUERY)
        this.execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(shopId: String, startTime: String, endTime: String, source: String): Map<String, Any> {
        return mapOf("shopID" to shopId ,"startTimeUnix" to startTime , "endTimeUnix" to endTime , "source" to source )
    }

}


const val IS_MEMBERSHIP_VALIDATE = """
    query membershipValidateBenefit(${'$'}shopID: String!, ${'$'}startTimeUnix: String! , ${'$'}endTimeUnix: String! , ${'$'}source: String! ){
        membershipValidateBenefit(shopID: ${'$'}shopID, startTimeUnix: ${'$'}startTimeUnix, endTimeUnix: ${'$'}endTimeUnix, source: ${'$'}source){
      resultStatus {
      code
      message
      reason
    }
    isValidMembershipBenefit
  }
}
 
"""