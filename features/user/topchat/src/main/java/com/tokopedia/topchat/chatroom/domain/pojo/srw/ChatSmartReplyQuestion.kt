package com.tokopedia.topchat.chatroom.domain.pojo.srw


import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatroom.view.custom.SrwTypeFactory

data class ChatSmartReplyQuestion(
        @SerializedName("hasQuestion")
        var hasQuestion: Boolean = false,
        @SerializedName("isSuccess")
        var isSuccess: Boolean = false,
        @SerializedName("list")
        var questions: List<QuestionUiModel> = listOf(),
        @SerializedName("title")
        var title: String = ""
) {
        val visitables: ArrayList<Visitable<SrwTypeFactory>> = arrayListOf()
}

class SrwTitleUiModel(
        val title: String = ""
) : Visitable<SrwTypeFactory> {
        override fun type(typeFactory: SrwTypeFactory): Int {
                return typeFactory.type(this)
        }
}