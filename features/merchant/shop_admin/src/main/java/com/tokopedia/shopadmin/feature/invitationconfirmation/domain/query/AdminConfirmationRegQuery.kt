package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.usecase.RequestParams


object AdminConfirmationRegQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "adminConfirmationReg"
    private const val SOURCE = "adminConfirmationReg-android"
    private const val SOURCE_KEY = "source"
    private const val SHOP_ID_KEY = "shopID"
    private const val USER_ID_KEY = "userId"
    private const val EMAIL_KEY = "email"
    private const val OTP_TOKEN_KEY = "otpToken"
    private const val ACCEPT_BECOME_ADMIN_KEY = "accept_become_admin"
    private const val SHOP_MANAGE_ID = "shopManageId"

    private val GQL_QUERY = """
        mutation ${OPERATION_NAME}(${'$'}source: String!, ${'$'}shopID: String!, ${'$'}userId: String!, ${'$'}email: String!, ${'$'}otpToken: String!, ${'$'}shopManageID: String!, ${'$'}accept_become_admin: Boolean!) {
          adminConfirmationReg(input: {
            source: "adminConfirmationReg-android",
            shopID: ${'$'}shopID,
            userId: ${'$'}userId,
            email: ${'$'}email
            otpToken: ${'$'}otpToken,
            accept_become_admin: ${'$'}accept_become_admin,
            shopManageId: ${'$'}shopManageID
          }) {
              success
              message
              accept_become_admin
            }
        }   
    """.trimIndent()

    fun createRequestParams(shopID: String, userId: String, email: String, otpToken: String,
                                    acceptBecomeAdmin: Boolean, manageID: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(SOURCE_KEY, SOURCE)
            putString(SHOP_ID_KEY, shopID)
            putString(USER_ID_KEY, userId)
            putString(EMAIL_KEY, email)
            putString(OTP_TOKEN_KEY, otpToken)
            putString(SHOP_MANAGE_ID, manageID)
            putBoolean(ACCEPT_BECOME_ADMIN_KEY, acceptBecomeAdmin)
        }.parameters
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = GQL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME

}