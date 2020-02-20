package com.tokopedia.product.detail.data.util

import java.io.IOException

data class TobacoErrorException(
        val messages: String = "",
        val title: String = "",
        val button: String = "",
        val url: String = ""
) : IOException()