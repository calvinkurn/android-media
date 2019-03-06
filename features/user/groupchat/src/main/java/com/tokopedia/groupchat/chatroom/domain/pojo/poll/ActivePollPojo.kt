package com.tokopedia.groupchat.chatroom.domain.pojo.poll

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.groupchat.chatroom.domain.pojo.BaseGroupChatPojo

data class ActivePollPojo (

        @SerializedName("poll_id")
        @Expose
        var pollId: Int = 0,
        @SerializedName("title")
        @Expose
        var title: String = "",
        @SerializedName("description")
        @Expose
        var description: String = "",
        @SerializedName("question")
        @Expose
        var question: String = "",
        @SerializedName("options")
        @Expose
        var options: List<Option> = ArrayList(),
        @SerializedName("poll_type_id")
        @Expose
        var pollTypeId: Int = 0,
        @SerializedName("poll_type")
        @Expose
        var pollType: String = "",
        @SerializedName("option_type_id")
        @Expose
        var optionTypeId: Int = 0,
        @SerializedName("option_type")
        @Expose
        var optionType: String = "",
        @SerializedName("status_id")
        @Expose
        var statusId: Int = 0,
        @SerializedName("status")
        @Expose
        var status: String = "",
        @SerializedName("start_time")
        @Expose
        var startTime: Long = 0,
        @SerializedName("end_time")
        @Expose
        var endTime: Long = 0,
        @SerializedName("statistic")
        @Expose
        var statistic: Statistic? = null,
        @SerializedName("is_answered")
        @Expose
        var isIsAnswered: Boolean = false,
        @SerializedName("winner_url")
        @Expose
        var winnerUrl: String = "",
        @SerializedName("link_url")
        @Expose
        var voteUrl: String = ""
) : BaseGroupChatPojo() {

}
