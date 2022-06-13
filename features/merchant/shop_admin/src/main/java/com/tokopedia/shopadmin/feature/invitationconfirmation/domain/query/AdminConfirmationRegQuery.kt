package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.usecase.RequestParams


object AdminConfirmationRegQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "adminConfirmationReg"
    private const val SOURCE = "adminConfirmationReg-android"
    private const val SOURCE_KEY = "source"
    private const val SHOP_ID_KEY = "shopID"
    private const val USER_ID_KEY = "userId"
    private const val ACCEPT_BECOME_ADMIN_KEY = "acceptBecomeAdmin"
    private const val SHOP_MANAGE_ID = "shopManageId"

    private val GQL_QUERY = """
        mutation ${OPERATION_NAME}(${'$'}source: String!, ${'$'}shopID: String!, ${'$'}userId: String!, ${'$'}shopManageId: String!, ${'$'}acceptBecomeAdmin: Boolean!) {
          ${OPERATION_NAME}(input: {
            source: ${'$'}source,
            shopID: ${'$'}shopID,
            userId: ${'$'}userId,
            acceptBecomeAdmin: ${'$'}acceptBecomeAdmin,
            shopManageId: ${'$'}shopManageId
          }) {
              success
              message
              accept_become_admin
            }
        }   
    """.trimIndent()

    fun createRequestParams(shopID: String, userId: String, acceptBecomeAdmin: Boolean, shopManageId: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(SOURCE_KEY, SOURCE)
            putString(SHOP_ID_KEY, shopID)
            putString(USER_ID_KEY, userId)
            putString(SHOP_MANAGE_ID, shopManageId)
            putBoolean(ACCEPT_BECOME_ADMIN_KEY, acceptBecomeAdmin)
        }.parameters
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = GQL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME

}