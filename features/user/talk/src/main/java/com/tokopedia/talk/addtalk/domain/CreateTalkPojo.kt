package com.tokopedia.talk.addtalk.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.talk.common.domain.InboxTalkItemPojo
import com.tokopedia.talk.common.domain.Paging
import com.tokopedia.talk.common.domain.UnreadCount

/**
 * @author : Steven 17/09/18
 */
data class CreateTalkPojo(
        @Expose
        @SerializedName("talk_id")
        val talkId: Int,
        @Expose
        @SerializedName("is_success")
        val isSuccess: Int
)