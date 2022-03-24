package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.usecase.RequestParams

object GetAdminTypeQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "getAdminType"
    private const val SOURCE_KEY = "source"
    private const val SOURCE = "admin-type-android"

    private val GQL_QUERY = """
        query ${OPERATION_NAME}(${'$'}source: String!) {
            ${OPERATION_NAME}(source: ${'$'}source) {
                admin_data {
                    status
                }
                shopID
            }
        }
    """.trimIndent()

    fun createRequestParams(): Map<String, Any> {
        return RequestParams.create().apply {
            putString(SOURCE_KEY, SOURCE)
        }.parameters
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = GQL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME

}