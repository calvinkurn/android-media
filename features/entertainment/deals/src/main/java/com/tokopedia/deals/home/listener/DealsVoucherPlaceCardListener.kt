package com.tokopedia.deals.home.listener

import android.view.View
import com.tokopedia.deals.home.ui.dataview.VoucherPlaceCardDataView
import com.tokopedia.deals.home.ui.dataview.VoucherPlacePopularDataView

interface DealsVoucherPlaceCardListener {
    fun onVoucherPlaceCardBind(voucherPlaceCard: VoucherPlacePopularDataView, position: Int)
    fun onVoucherPlaceCardClicked(voucherPlaceCard: VoucherPlaceCardDataView, position: Int)
}