package com.tokopedia.otp.notif.domain.query

/**
 * Created by Ade Fulki on 22/09/20.
 */

object VerifyPushNotifExpQuery {

    private const val challengeCode = "\$challengeCode"
    private const val signature = "\$signature"
    private const val status = "\$status"

    val query: String = """
        query verify_pushnotif_exp($challengeCode: String!, $signature: String!, $status: String!){
            VerifyPushnotifExpiration(challengeCode: $challengeCode, signature: $signature, status: $status) {
                is_success
                is_expire
                is_trusted
                error_message
                image_link
                title
                description
                button_type
                status
            }
        }
    """.trimIndent()
}