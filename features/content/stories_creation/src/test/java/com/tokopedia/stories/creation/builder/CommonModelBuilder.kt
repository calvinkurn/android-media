package com.tokopedia.stories.creation.builder

/**
 * Created By : Jonathan Darwin on October 18, 2023
 */
class CommonModelBuilder {

    fun buildException(message: String = "Network Issue") = Exception(message)
}
