package com.tokopedia.loginregister.common.domain.query

/**
 * Created by Ade Fulki on 01/06/20.
 */

object QueryActivateUser {

    private const val email = "\$email"
    private const val validateToken = "\$validateToken"

    val query: String = """
        mutation activateUser($email: String!, $validateToken: String!){
            activate_user(input: {
                email: $email
                validate_token: $validateToken
            }) {
                is_success
                message
                sid
                access_token
                refresh_token
                token_type
            }
        }
    """.trimIndent()
}