package com.tokopedia.people.model

/**
 * Created By : Jonathan Darwin on July 05, 2022
 */
class CommonModelBuilder {

    fun buildException(
        message: String = "Terjadi kesalahan.",
    ) = Exception(message)
}
