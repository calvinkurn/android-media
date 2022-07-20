package com.tokopedia.usercomponents.explicit.domain.model

import com.google.gson.annotations.SerializedName

data class AnswerDataModel(
    @SerializedName("explicitprofileSaveMultiAnswers")
    val explicitprofileSaveMultiAnswers: ExplicitprofileSaveMultiAnswers = ExplicitprofileSaveMultiAnswers()
)

data class ExplicitprofileSaveMultiAnswers(
    @SerializedName("message")
    val message: String = ""
)


