package com.tokopedia.topchat.chatroom.domain.pojo.srw

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatroom.view.custom.SrwTypeFactory

data class QuestionUiModel(
        @SerializedName("content")
        var content: String = "",
        @SerializedName("intent")
        var intent: String = "",
        @SerializedName("label")
        var label: String = ""
) : Visitable<SrwTypeFactory> {
    override fun type(typeFactory: SrwTypeFactory): Int {
        return typeFactory.type(this)
    }
}