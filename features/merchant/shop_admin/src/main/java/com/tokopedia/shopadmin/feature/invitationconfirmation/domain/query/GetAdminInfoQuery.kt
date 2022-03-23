package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.usecase.RequestParams

object GetAdminInfoQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "getAdminType"
    private const val SOURCE_KEY = "source"
    private const val SHOP_ID_KEY = "shop_id"
    private const val SOURCE = "admin-info-android"

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

    fun createRequestParams(shopID: Long): Map<String, Any> {
        return RequestParams.create().apply {
            putString(SOURCE_KEY, SOURCE)
            putLong(SHOP_ID_KEY, shopID)
        }.parameters
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = GQL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME

}