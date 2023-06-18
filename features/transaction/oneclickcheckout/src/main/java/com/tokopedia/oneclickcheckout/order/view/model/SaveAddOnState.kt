package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

data class SaveAddOnState(
    val isSuccess: Boolean = true,
    val message: String = String.EMPTY,
    val status: Int = Int.ZERO
)
