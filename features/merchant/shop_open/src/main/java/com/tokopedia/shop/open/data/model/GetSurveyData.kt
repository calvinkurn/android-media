package com.tokopedia.shop.open.data.model

import com.google.gson.annotations.SerializedName

data class GetSurveyData(
        @SerializedName("getSurveyData")
        val getSurveyData: GetSurveyDataResult = GetSurveyDataResult()
)

data class GetSurveyDataResult(
        @SerializedName("error")
        val error: Error = Error(),
        @SerializedName("result")
        val result: Result = Result()
)

data class Error(
        @SerializedName("message")
        val message: String = ""
)

data class Result(
        @SerializedName("questions")
        val questions: List<Question> = listOf()
)

data class Question(
        @SerializedName("choices")
        val choices: List<Choice> = listOf(),
        @SerializedName("ID")
        val id: Int = 0,
        @SerializedName("question")
        val question: String = "",
        @SerializedName("type")
        val type: Int = 0
)

data class Choice(
        @SerializedName("choice")
        val choice: String = "",
        @SerializedName("ID")
        val id: Int = 0
)
