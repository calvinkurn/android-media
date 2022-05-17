package com.tokopedia.tokofood.feature.ordertracking.presentation.adapter

import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.DriverSectionViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.OrderDetailToggleCtaViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.OrderTrackingErrorViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.OrderTrackingTickerViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.TemporaryFinishOrderViewHolder

interface OrderTrackingListener: OrderDetailToggleCtaViewHolder.OrderDetailToggleCtaListener,
        OrderTrackingTickerViewHolder.Listener, TemporaryFinishOrderViewHolder.Listener,
        DriverSectionViewHolder.Listener, OrderTrackingErrorViewHolder.Listener