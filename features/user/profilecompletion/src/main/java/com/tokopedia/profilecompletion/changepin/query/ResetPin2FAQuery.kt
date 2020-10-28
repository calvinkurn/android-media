package com.tokopedia.profilecompletion.changepin.query

object ResetPin2FAQuery {

    private const val userId = "\$user_id"
    private const val validateToken = "\$validate_token"
    private const val grantType = "\$grant_type"
    private const val pin = "\$pin"
    private const val action = "\$action"

    val resetQuery: String = """
        query resetUserPin(
            $userId: Int!,
            $validateToken: String!,
            $grantType: String!
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

    val checkPinQuery: String = """
        query check_pin(
            $pin: String,
            $validateToken: String,
            $action: String,
            $userId: Int
        ){
            check_pin(
                pin: $pin,
                validate_token: $validateToken,
                action: $action,
                user_id: $userId
            ) {
                valid
                error_message
            }
        }
    """.trimIndent()

}