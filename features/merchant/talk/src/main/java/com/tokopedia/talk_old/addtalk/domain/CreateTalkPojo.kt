package com.tokopedia.talk_old.addtalk.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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