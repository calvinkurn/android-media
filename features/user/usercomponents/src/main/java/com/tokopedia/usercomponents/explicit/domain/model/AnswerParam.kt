package com.tokopedia.usercomponents.explicit.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class QuestionsItemParam(
    @SuppressLint("Invalid Data Type")
    @SerializedName("questionId")
    var questionId: Int = 0,

    @SerializedName("answerValue")
    var answerValue: String = ""
)

data class InputParam(
    @SerializedName("templateName")
    var templateName: String = "",

    @SuppressLint("Invalid Data Type") @SerializedName("templateId")
    var templateId: Int = 0,

    @SerializedName("sections")
    val sections: List<SectionsItemParam> = listOf(SectionsItemParam())
)

data class SectionsItemParam(
    @SerializedName("questions")
    val questions: List<QuestionsItemParam> = listOf(QuestionsItemParam()),

    @SuppressLint("Invalid Data Type") @SerializedName("sectionId")
    var sectionId: Int = 0
)
