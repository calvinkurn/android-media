package com.tokopedia.play.data.interactive

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 30/03/22
 */
data class AnswerQuizResponse (
    @SerializedName("playInteractiveAnswerQuiz")
    val data: Data = Data()

){
    data class Data(
        @SerializedName("correctAnswerID")
        val correctAnswerID: String = ""
    )
}