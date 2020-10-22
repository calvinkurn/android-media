package com.tokopedia.otp.notif.domain.query

/**
 * Created by Ade Fulki on 22/09/20.
 */

object DeviceStatusPushNotifQuery {

    val query: String = """
        query device_status_pushnotif(){
            DeviceStatusPushnotif() {
                success
                errorMessage
                isTrusted
                listDevices{
                    deviceName
                }
                isActive
            }
        }
    """.trimIndent()
}