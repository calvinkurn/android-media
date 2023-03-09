package com.tokopedia.buyerorderdetail.presentation.adapter.listener

import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofErrorViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofFulfilledToggleViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofHeaderInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofRefundEstimateBottomSheetViewHolder

interface PartialOrderFulfillmentListener :
    PofFulfilledToggleViewHolder.Listener,
    PofRefundEstimateBottomSheetViewHolder.Listener,
    PofErrorViewHolder.Listener,
    PofHeaderInfoViewHolder.Listener
