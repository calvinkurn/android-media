package com.tokopedia.otp.notif.domain.query

/**
 * Created by Ade Fulki on 22/09/20.
 */

object VerifyPushNotifQuery {

    private const val challengeCode = "\$challengeCode"
    private const val signature = "\$signature"
    private const val status = "\$status"

    val query: String = """
        query verify_pushnotif($challengeCode: String!, $signature: String!, $status: String!){
            VerifyPushnotif(challengeCode: $challengeCode, signature: $signature, status: $status) {
                success
                message
                errorMessage
                Imglink
                MessageTitle
                MessageBody
                CtaType
                Status
            }
        }
    """.trimIndent()
}