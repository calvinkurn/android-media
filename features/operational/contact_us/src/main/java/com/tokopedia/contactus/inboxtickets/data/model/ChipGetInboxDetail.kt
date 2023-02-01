package com.tokopedia.contactus.inboxtickets.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta.FAILED_HIT_API

data class ChipGetInboxDetail(
    @SerializedName("data")
    var data: Data? = null,
    @SerializedName("messageError")
    var messageError: List<String>? = null
) {
    fun getDataTicket() = data?.tickets ?: Tickets()

    fun getErrorMessage() = messageError?.get(0).orEmpty()

    fun getErrorListMessage() = messageError ?: arrayListOf()

    fun isSuccess() = data?.isSuccess ?: FAILED_HIT_API
}
