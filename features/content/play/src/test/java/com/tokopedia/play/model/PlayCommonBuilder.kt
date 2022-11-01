package com.tokopedia.play.model

/**
 * Created By : Jonathan Darwin on October 20, 2022
 */
class PlayCommonBuilder {

    fun buildException(message: String = "Something went wrong") = Exception(message)
}
