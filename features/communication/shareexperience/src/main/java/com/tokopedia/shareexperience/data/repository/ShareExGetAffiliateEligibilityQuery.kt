package com.tokopedia.shareexperience.data.repository

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class ShareExGetAffiliateEligibilityQuery : GqlQueryInterface {

    private val operationName = "generateAffiliateLinkEligibility"
    override fun getOperationNameList(): List<String> {
        return listOf(operationName)
    }

    override fun getQuery(): String = """query generateAffiliateLinkEligibility(${'$'}generateAffiliateLinkEligibilityRequest: generateAffiliateLinkEligibilityRequest!) {
                generateAffiliateLinkEligibility(generateAffiliateLinkEligibilityRequest: ${'$'}generateAffiliateLinkEligibilityRequest) {
                        EligibleCommission {
                            IsEligible
                        }
                    }
                }
    """.trimIndent()

    override fun getTopOperationName(): String = operationName
}
