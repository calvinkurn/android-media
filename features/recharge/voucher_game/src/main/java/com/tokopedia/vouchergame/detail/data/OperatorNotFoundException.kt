package com.tokopedia.vouchergame.detail.data

import com.tokopedia.network.constant.ResponseStatus
import java.lang.RuntimeException

/**
 * @author by jessica on 27/07/21
 */
data class OperatorNotFoundException(
        override val message: String = ResponseStatus.SC_NOT_FOUND.toString()
) : RuntimeException()