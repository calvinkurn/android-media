package com.tokopedia.loginregister.registerinitial.domain

object RegisterV2Query {

    private const val regType = "\$reg_type"
    private const val fullname = "\$fullname"
    private const val password = "\$password"
    private const val email = "\$email"
    private const val osType = "\$os_type"
    private const val validateToken = "\$validate_token"
    private const val h = "\$h"

    val registerQuery: String = """
        mutation register($regType String!, $fullname: String, $email: String, password: String, $osType: String, $validateToken: String, $h: String) {
            register_v2(input: {
                reg_type: $regType
                fullname: $fullname
                email   : $email
                password: $password
                os_type : $osType
                validate_token: $validateToken
                h:$h
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