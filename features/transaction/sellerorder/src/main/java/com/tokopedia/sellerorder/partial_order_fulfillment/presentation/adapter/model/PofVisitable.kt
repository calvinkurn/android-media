package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactory

interface PofVisitable : Visitable<PofAdapterTypeFactory> {
    fun areItemsTheSame(other: PofVisitable): Boolean
    fun areContentsTheSame(other: PofVisitable): Boolean
}
