package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.usecase.RequestParams

internal object UpdateUserProfileQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "userProfileUpdate"
    private const val EMAIL_KEY = "email"

    private val GQL_QUERY = """
        mutation ${OPERATION_NAME}(${'$'}source: String!, ${'$'}shopID: String!, ${'$'}userId: String!, ${'$'}email: String!, ${'$'}otpToken: String!, ${'$'}shopManageID: String!, ${'$'}acceptBecomeAdmin: Boolean!) {
          ${OPERATION_NAME}(input: {
            email: ${'$'}email
          }) {
              isSuccess
              errors
            }
        }   
    """.trimIndent()

    fun createRequestParams(email: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(EMAIL_KEY, email)
        }.parameters
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = GQL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}