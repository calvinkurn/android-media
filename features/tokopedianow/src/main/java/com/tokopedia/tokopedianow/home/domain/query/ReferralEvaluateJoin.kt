package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object ReferralEvaluateJoin : GqlQueryInterface {

    private const val OPERATION_NAME = "gamiReferralEvaluateJoin"
    const val REFERRAL_CODE = "referralCode"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query $OPERATION_NAME(${'$'}$REFERRAL_CODE: String!){
                $OPERATION_NAME(referralCode:${'$'}$REFERRAL_CODE){
                    resultStatus {
                      code
                      message
                      reason
                    }
                    asset {
                      logoImageURL
                      title
                      subtitle
                      description
                    }
                    actionButton {
                      text
                      url
                      appLink
                      type
                    }
                }
            }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
       return OPERATION_NAME
    }
}
