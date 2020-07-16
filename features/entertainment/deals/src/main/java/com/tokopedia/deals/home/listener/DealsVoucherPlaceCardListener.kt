package com.tokopedia.deals.home.listener

import android.view.View
import com.tokopedia.deals.home.ui.dataview.VoucherPlaceCardDataView

interface DealsVoucherPlaceCardListener {
    fun onVoucherPlaceCardClicked(
        voucherPlaceCard: VoucherPlaceCardDataView,
        position: Int
    )
}