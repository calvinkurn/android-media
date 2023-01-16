package com.tokopedia.buyerorderdetail.presentation.adapter.listener

import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofFulfilledToggleViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofRefundEstimateViewHolder

interface PartialOrderFulfillmentListener :
    PofFulfilledToggleViewHolder.Listener,
    PofRefundEstimateViewHolder.Listener
