package com.tokopedia.loginregister.registerinitial.domain

object RegisterV2Query {
    val registerQuery: String = """
        mutation register(${'$'}reg_type: String!, ${'$'}fullname: String!, ${'$'}email: String!, ${'$'}password: String!, ${'$'}os_type: String!, ${'$'}validate_token: String!, ${'$'}h: String!) {
            register_v2(input: {
                reg_type: ${'$'}reg_type
                fullname: ${'$'}fullname
                email   : ${'$'}email
                password: ${'$'}password
                os_type : ${'$'}os_type
                validate_token: ${'$'}validate_token
                h:${'$'}h
            }) {
                user_id
                sid
                access_token
                refresh_token
                token_type
                is_active
                action
                errors {
                    name
                    message
                }
            }
        }
    """.trimIndent()
}