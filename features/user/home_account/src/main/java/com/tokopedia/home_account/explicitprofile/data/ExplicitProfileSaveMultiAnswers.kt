package com.tokopedia.home_account.explicitprofile.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ExplicitProfileSaveMultiAnswers(
    @SerializedName("explicitprofileSaveMultiAnswers")
    val response:ResponseMutlipleAnswerDataModel = ResponseMutlipleAnswerDataModel()
) {
    data class ResponseMutlipleAnswerDataModel(
        @SerializedName("message")
        val message: String = ""
    )
}


data class SaveMultipleAnswersParam(
    @SerializedName("input")
    var input: InputParam = InputParam()
): GqlParam {

    data class InputParam(
        @SuppressLint("Invalid Data Type")
        @SerializedName("templateId")
        var templateId: Int = 0,
        @SerializedName("templateName")
        var templateName: String = "",
        @SerializedName("sections")
        var sections: MutableList<SectionsParam> = mutableListOf()
    ) {
        data class SectionsParam(
            @SuppressLint("Invalid Data Type")
            @SerializedName("sectionId")
            var sectionId: Int = 0,
            @SerializedName("questions")
            var questions: MutableList<QuestionsParam> = mutableListOf()
        ) {
            data class QuestionsParam(
                @SuppressLint("Invalid Data Type")
                @SerializedName("questionId")
                var questionId: Int = 0,
                @SerializedName("answerValue")
                var answerValue: String = ""
            )
        }
    }
}