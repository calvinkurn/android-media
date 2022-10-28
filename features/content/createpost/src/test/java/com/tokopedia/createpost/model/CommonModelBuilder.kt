package com.tokopedia.createpost.model

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */
class CommonModelBuilder {

    fun buildException(message: String = "Something went wrong"): Exception {
        return Exception(message)
    }

}