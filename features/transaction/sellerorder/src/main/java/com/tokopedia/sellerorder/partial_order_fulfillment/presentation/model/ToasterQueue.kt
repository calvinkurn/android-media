package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerorder.orderextension.presentation.model.StringRes
import com.tokopedia.unifycomponents.Toaster

data class ToasterQueue(
    val text: StringRes = StringRes(Int.ZERO),
    val type: Int = Toaster.TYPE_NORMAL,
    val duration: Int = Toaster.LENGTH_SHORT
)
