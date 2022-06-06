package com.tokopedia.sellerorder.detail.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object SomReasonRejectQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "SOMRejectReason"
    private val REJECT_REASON_QUERY = """
            query ${OPERATION_NAME}(${'$'}input:SOMRejectReasonRequest!) {
              som_reject_reason(input:${'$'}input) {
                reason_code
                reason_text
                reason_ticker
                child {
                  reason_code
                  reason_text
                }
              }
            }
        """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = REJECT_REASON_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME

}