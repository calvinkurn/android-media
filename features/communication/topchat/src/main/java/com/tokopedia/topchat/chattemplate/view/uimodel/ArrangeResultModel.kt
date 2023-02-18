package com.tokopedia.topchat.chattemplate.view.uimodel

data class ArrangeResultModel(
    var to: Int,
    var from: Int,
    var error: Throwable? = null
)
