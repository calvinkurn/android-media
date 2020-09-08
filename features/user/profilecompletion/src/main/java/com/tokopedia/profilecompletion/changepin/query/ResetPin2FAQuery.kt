package com.tokopedia.profilecompletion.changepin.query

object OtpValidateQuery2FA {

    private const val userId = "\$user_id"
    private const val validateToken = "\$validate_token"
    private const val grantType = "\$grant_type"

    val query: String = """
        query reset_user_pin(
            $userId: String,
            $validateToken: String,
            $grantType: String
        ){
            resetUserPin(
                user_id: $userId,
                validate_token: $validateToken,
                grant_type: $grantType
            ) {
                is_success
                user_id 
                access_token
                sid
                refresh_token
                expires_in
                error
            }
        }
    """.trimIndent()
}