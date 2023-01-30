package com.tokopedia.sellerpersona.data.remote.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

data class FetchPersonaQuestionnaireResponse(
    @SerializedName("fetchUserPersonaQuestionnaire")
    val data: FetchPersonaQuestionnaireModel = FetchPersonaQuestionnaireModel()
)

data class FetchPersonaQuestionnaireModel(
    @SerializedName("error") val error: Boolean = true,
    @SerializedName("errorMsg") val errorMsg: String = String.EMPTY,
    @SerializedName("questionnaire") val questionnaire: List<QuestionnaireModel> = listOf(),
)

data class QuestionnaireModel(
    @SerializedName("id") val id: String = String.EMPTY,
    @SerializedName("question") val question: QuestionModel = QuestionModel(),
    @SerializedName("type") val type: Int = Int.ONE,
    @SerializedName("options") val options: List<OptionModel> = listOf()
) {
    data class QuestionModel(
        @SerializedName("title") val title: String = String.EMPTY,
        @SerializedName("subtitle") val subtitle: String = String.EMPTY
    )

    data class OptionModel(
        @SerializedName("value") val value: String = String.EMPTY,
        @SerializedName("title") val title: String = String.EMPTY
    )
}