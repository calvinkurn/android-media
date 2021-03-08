package com.tokopedia.managepassword.addpassword.domain

object AddPasswordQueries {
    private const val password_confirm = "\$password_confirm"
    private const val password = "\$password"
    private const val h = "\$h"

    val addPasswordV2: String = """
        mutation addPassword($password: String!, $password_confirm: String!, $h: String!) {
          addPasswordV2(password: $password, password_confirm: $password_confirm, h: $h) {
            is_success
            error_code
            error_message
          }
        }    
        """.trimIndent()
}