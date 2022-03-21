package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object ValidateAdminEmailQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "validateAdminEmail"

    private val GQL_QUERY = """
        mutation validateAdminEmail(${'$'}email: String!, ${'$'}manageID: String!, ${'$'}${'$'}shopID: String!)
            validateAdminEmail(input: {
                source : "validate-email-android"
                email : ${'$'}email
                manageID: ${'$'}manageID
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

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = GQL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}