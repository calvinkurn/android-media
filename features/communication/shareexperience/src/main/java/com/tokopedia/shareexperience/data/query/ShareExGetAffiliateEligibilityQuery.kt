package com.tokopedia.shareexperience.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class ShareExGetAffiliateEligibilityQuery : GqlQueryInterface {

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String = """query generateAffiliateLinkEligibility(${'$'}generateAffiliateLinkEligibilityRequest: generateAffiliateLinkEligibilityRequest!) {
                generateAffiliateLinkEligibility(generateAffiliateLinkEligibilityRequest: ${'$'}generateAffiliateLinkEligibilityRequest) {
                        EligibleCommission {
                            IsEligible
                        }
                    }
                }
    """.trimIndent()

    override fun getTopOperationName(): String = OPERATION_NAME

    companion object {
        const val OPERATION_NAME = "generateAffiliateLinkEligibility"
    }
}
