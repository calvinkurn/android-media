package com.tokopedia.managepassword.changepassword.domain.queries

object ChangePasswordQueries {

    val resetPasswordV2: String = """
        mutation submitResetPassword(${'$'}encode: String!, ${'$'}new_password: String!, ${'$'}repeat_password: String!, ${'$'}validatetoken: String!, ${'$'}h: String!) {
          submitResetPasswordV2(input: {
            encode          : ${'$'}encode
            new_password    : ${'$'}new_password
            repeat_password : ${'$'}repeat_password
            validatetoken   : ${'$'}validatetoken
            h               : ${'$'}h
           }) 
          {
            user_id
            updated
            type
            message
          }
        }
    """.trimIndent()
}