package com.tokopedia.sessioncommon.domain.query

/**
 * Created by Yoris Prayogo on 16/02/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

object LoginTokenV2Query {
    val loginEmailQuery: String = """
        mutation login_email_v2(${'$'}grant_type: String!, ${'$'}username: String!, ${'$'}password: String!, ${'$'}h: String!){
            login_token_v2(
                input: {
                    grant_type: ${'$'}grant_type
                    username: ${'$'}username
                    password: ${'$'}password
                    h: ${'$'}h
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