package com.tokopedia.otp.notif.domain.query

/**
 * Created by Ade Fulki on 22/09/20.
 */

object ChangeStatusPushNotifQuery {

    private const val status = "\$status"

    val query: String = """
        query change_status_push_notif($status: Int!){
            ChangeStatusPushNotif(status: $status) {
                success
                errorMessage
            }
        }
    """.trimIndent()
}