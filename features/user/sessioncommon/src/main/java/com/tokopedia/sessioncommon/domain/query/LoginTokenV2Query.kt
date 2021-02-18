package com.tokopedia.sessioncommon.domain.query

/**
 * Created by Yoris Prayogo on 16/02/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

object LoginTokenV2Query {

    private const val grantType = "\$grant_type"
    private const val username = "\$username"
    private const val password = "\$password"
    private const val hash = "\$hash"

    val loginEmailQuery: String = """
        mutation login_email_v2($grantType: String!, $username: String!, $password: String!, $hash: String!){
            login_token_v2(
                input: {
                    grant_type: $grantType
                    username: $username
                    password: $password
                    h: $hash
                }
            ) {
                acc_sid
                access_token
                expires_in
                refresh_token
                sid
                token_type
                sq_check
                action
                errors {
                    name
                    message
                }
                event_code
                popup_error {
                    header
                    body
                    action
                }
            }
        }
    """.trimIndent()
}