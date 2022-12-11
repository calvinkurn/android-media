package com.tokopedia.mvc.data.exception

class VoucherCancellationException(
    val voucherId: Int,
    message: String
) : Exception(message)
