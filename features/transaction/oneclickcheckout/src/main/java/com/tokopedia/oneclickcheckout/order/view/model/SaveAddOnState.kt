package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.kotlin.extensions.view.EMPTY

data class SaveAddOnState(
    val isSuccess: Boolean = true,
    val message: String = String.EMPTY,
    val throwable: Throwable? = null
)
