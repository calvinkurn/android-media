package com.tokopedia.power_merchant.subscribe.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.Header


data class GoldCancellationsQuestionaire(
        @SerializedName("goldCancellationsQuestionaire")
        val result: QuestionnaireData = QuestionnaireData()
)

data class QuestionnaireData(
        @SerializedName("header")
        val header: Header =  Header(),
        @SerializedName("data")
        val data: Data = Data()
)

data class Data(
        @SerializedName("question_list")
        val questionList: MutableList<Question> = mutableListOf()
)

data class Question(
        @SerializedName("priority")
        val priority: Int = 0,
        @SerializedName("question_type")
        val questionType: String = "",
        @SerializedName("question")
        val question: String = "",
        @SerializedName("option")
        val option: MutableList<Option> = mutableListOf()
)

data class Option(
        @SerializedName("priority")
        val priority: Int = 0,
        @SerializedName("value")
        val value: String = ""
)

