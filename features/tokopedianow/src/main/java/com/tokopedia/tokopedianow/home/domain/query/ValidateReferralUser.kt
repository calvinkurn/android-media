package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.tokopedianow.home.domain.usecase.ValidateReferralUserUseCase

internal object ValidateReferralUser: GqlQueryInterface {

    private const val OPERATION_NAME = "gamiReferralValidateUser"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(${'$'}${ValidateReferralUserUseCase.SLUG}: String!){
            $OPERATION_NAME(slug:${'$'}${ValidateReferralUserUseCase.SLUG}) {
                resultStatus {
                  code
                  message
                  reason
                }
                status
            }
        }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
