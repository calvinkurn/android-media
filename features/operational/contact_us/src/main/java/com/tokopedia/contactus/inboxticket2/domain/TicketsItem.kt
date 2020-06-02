package com.tokopedia.contactus.inboxticket2.domain

import com.google.gson.annotations.SerializedName

data class TicketsItem(
        @SerializedName("url_detail")
        val urlDetail: String? = null,
        @SerializedName("last_message_plaintext")
        val lastMessagePlaintext: String? = null,
        @SerializedName("subject")
        val subject: String? = null,
        @SerializedName("read_status_id")
        var readStatusId: Int = 0,
        @SerializedName("read_status")
        val readStatus: String? = null,
        @SerializedName("last_message")
        val lastMessage: String? = null,
        @SerializedName("message")
        var message: String? = null,
        @SerializedName("status_id")
        var statusId: Int = 0,
        @SerializedName("create_time_fmt2")
        val createTimeFmt2: String? = null,
        @SerializedName("update_time_fmt2")
        val updateTimeFmt2: String? = null,
        @SerializedName("last_update")
        val lastUpdate: String? = null,
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("status")
        var status: String? = null,
        @SerializedName("need_rating")
        var needRating: Int = 0,
        @SerializedName("is_official_store")
        val isOfficialStore: String? = null,
        var isSelectableMode: Boolean = false,
        val isSelected: Boolean = false
)
