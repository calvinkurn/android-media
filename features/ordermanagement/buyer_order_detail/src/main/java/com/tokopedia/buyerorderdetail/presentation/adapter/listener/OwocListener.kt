package com.tokopedia.buyerorderdetail.presentation.adapter.listener

import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocErrorStateViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocProductListToggleViewHolder

interface OwocListener:
    OwocProductListToggleViewHolder.Listener,
    OwocErrorStateViewHolder.Listener
