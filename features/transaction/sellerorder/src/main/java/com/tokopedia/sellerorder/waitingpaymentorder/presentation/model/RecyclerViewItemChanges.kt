package com.tokopedia.sellerorder.waitingpaymentorder.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class RecyclerViewItemChanges<T : Visitable<*>>(
        val oldData: T,
        val newData: T
)