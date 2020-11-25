package com.tokopedia.shop.open.data.model

import com.google.gson.annotations.SerializedName

data class SendSurveyDataInput(
        @SerializedName("input")
        val input: List<DataInput> = listOf()
)

data class DataInput(
        @SerializedName("surveyID")
        val surveyID: Int = 1,
        @SerializedName("questions")
        val questions: List<QuestionInput> = listOf()
)

data class QuestionInput(
        @SerializedName("questionID")
        val questionID: Int = 0,
        @SerializedName("choice")
        val choice: List<Int> = listOf()
)
