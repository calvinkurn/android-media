package com.tokopedia.vouchercreation.common.exception

class VoucherCancellationException(val voucherId: Int,
                                   message: String): Exception(message)