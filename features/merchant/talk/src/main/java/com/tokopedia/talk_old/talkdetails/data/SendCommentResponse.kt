package com.tokopedia.talk_old.talkdetails.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Hendri on 14/09/18.
 */
data class SendCommentResponse(
        @Expose
        @SerializedName("comment_id")
        val comment_id: String = "",
        @Expose
        @SerializedName("is_success")
        val is_success: Int = 0)