package com.tokopedia.power_merchant.subscribe.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.Header


data class GoldCancellationsQuestionaire(
        @SerializedName("goldCancellationsQuestionaire")
        val result: QuestionnaireData
)

data class QuestionnaireData(
        @SerializedName("header")
        val header: Header,
        @SerializedName("data")
        val data: Data
)

data class Data(
        @SerializedName("question_list")
        val questionList: MutableList<Question>
)

data class Question(
        @SerializedName("priority")
        val priority: Int,
        @SerializedName("question_type")
        val questionType: String,
        @SerializedName("question")
        val question: String,
        @SerializedName("option")
        val option: MutableList<Option>
)

data class Option(
        @SerializedName("priority")
        val priority: Int,
        @SerializedName("value")
        val value: String
)

