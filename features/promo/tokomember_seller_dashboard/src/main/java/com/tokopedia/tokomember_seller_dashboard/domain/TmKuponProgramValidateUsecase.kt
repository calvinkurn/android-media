package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.MemberShipValidateResponse
import com.tokopedia.tokomember_seller_dashboard.util.SOURCE
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
        return mapOf(SHOP_ID to shopId ,START_TIME_UNIX to startTime , END_TIME_UNIX to endTime , SOURCE to source )
    }
}

const val SHOP_ID = "shopID"
const val START_TIME_UNIX = "startTimeUnix"
const val END_TIME_UNIX = "endTimeUnix"

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