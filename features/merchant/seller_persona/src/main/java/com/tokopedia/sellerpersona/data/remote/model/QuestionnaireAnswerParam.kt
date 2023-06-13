package com.tokopedia.sellerpersona.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 06/02/23.
 */

data class QuestionnaireAnswerParam(
    @SerializedName("id") val id: Long,
    @SerializedName("answers") val answers: List<String>,
)