package com.tokopedia.inbox.domain.data.notification

import com.google.gson.annotations.SerializedName

abstract class BaseNotification {

    @SerializedName("chat_int")
    var chatInt: Int = 0
        set(value) {
            field = value
            if (field < 0) {
                field = 0
            }
            updateTotal()
        }

    @SerializedName("notifcenter_int")
    var notifcenterInt: Int = 0
        set(value) {
            field = value
            updateTotal()
        }

    @SerializedName("talk_int")
    var talkInt: Int = 0
        set(value) {
            field = value
            if (field < 0) {
                field = 0
            }
            updateTotal()
        }

    @SerializedName("review_int")
    var reviewInt: Int = 0
        set(value) {
            field = value
            if (field < 0) {
                field = 0
            }
            updateTotal()
        }

    @SerializedName("total_int")
    var totalInt: Int = 0

    private fun updateTotal() {
        totalInt = chatInt + notifcenterInt + talkInt + reviewInt
    }
}