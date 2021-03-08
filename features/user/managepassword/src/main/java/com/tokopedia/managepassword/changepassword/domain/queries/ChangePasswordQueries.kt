package com.tokopedia.managepassword.changepassword.domain.queries

object ChangePasswordQueries {

    private const val encode = "\$encode"
    private const val newPassword = "\$new_password"
    private const val repeatPassword = "\$repeat_password"
    private const val validateToken = "\$validatetoken"
    private const val h = "\$h"

    val resetPasswordV2: String = """
        mutation submitResetPassword($encode String!, $newPassword String!, $repeatPassword String!, $validateToken String!, $h String!) {
          submitResetPasswordV2(input: {
            encode          : $encode
            new_password    : $newPassword
            repeat_password : $repeatPassword
            validatetoken   : $validateToken
            h               : $h
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