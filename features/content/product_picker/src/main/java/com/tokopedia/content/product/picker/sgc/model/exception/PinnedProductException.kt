package com.tokopedia.content.product.picker.sgc.model.exception

import com.tokopedia.network.exception.MessageErrorException

/**
 * Created By : Jonathan Darwin on September 26, 2023
 */
data class PinnedProductException(override val message: String = ""): MessageErrorException()
