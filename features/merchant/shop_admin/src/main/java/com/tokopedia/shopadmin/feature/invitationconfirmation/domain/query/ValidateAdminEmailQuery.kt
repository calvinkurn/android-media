package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.usecase.RequestParams

object ValidateAdminEmailQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "validateAdminEmail"
    private const val SHOP_ID_KEY = "shopID"
    private const val MANAGE_ID_KEY = "manageID"
    private const val EMAIL_KEY = "email"

    private val GQL_QUERY = """
        mutation validateAdminEmail(${'$'}email: String!, ${'$'}manageID: String!, ${'$'}${'$'}shopID: String!)
            validateAdminEmail(input: {
                source : "validate-admin-email-android",
                email : ${'$'}email,
                manageID: ${'$'}manageID,
                shopID: ${'$'}shopID
              }) {
                  success
                  message
                  data {
                    newUser
                    existsUser
                    userName
                  }
              }
        }
    """.trimIndent()

    fun createRequestParams(shopID: String, email: String, manageID: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(SHOP_ID_KEY, shopID)
            putString(EMAIL_KEY, email)
            putString(MANAGE_ID_KEY, manageID)
        }.parameters
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = GQL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}