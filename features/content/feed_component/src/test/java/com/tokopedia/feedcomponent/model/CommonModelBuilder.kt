package com.tokopedia.feedcomponent.model

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class CommonModelBuilder {

    fun buildException(
        message: String = "Terjadi kesalahan"
    ) = Exception(message)
}