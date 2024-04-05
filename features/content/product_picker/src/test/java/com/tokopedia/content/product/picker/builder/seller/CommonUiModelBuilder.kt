package com.tokopedia.content.product.picker.builder.seller

/**
 * Created By : Jonathan Darwin on September 27, 2023
 */
class CommonUiModelBuilder {

    fun buildException(message: String = "Network issue. Please try again") = Exception(message)
}
