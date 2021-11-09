package com.tokopedia.tokopoints.view.model

import com.tokopedia.network.exception.MessageErrorException
import java.io.IOException

data class CatalogGqlError(
    val messageErrorException: MessageErrorException,
    val errorCode: String = "",
    val developerMessage: String = ""
) : IOException()

