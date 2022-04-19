package com.tokopedia.entertainment.pdp.data.pdp

data class EventPDPErrorEntity(
        var error : Boolean = false,
        var throwable: Throwable = Throwable()
)