package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetReferralReceiverHome: GqlQueryInterface {

    const val PARAM_SLUG = "slug"

    private const val OPERATION_NAME = "gamiReferralReceiverHome"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query ${OPERATION_NAME}(${'$'}${PARAM_SLUG}: String!){
          ${OPERATION_NAME}(${PARAM_SLUG}:${'$'}${PARAM_SLUG}) {
            resultStatus {
                code
                message
                reason
            }
            reward {
                maxReward
            }
            benefits {
                title
                description
            }
          }
        }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
