package com.tokopedia.deals.ui.home.listener

import com.tokopedia.deals.ui.home.ui.dataview.VoucherPlaceCardDataView
import com.tokopedia.deals.ui.home.ui.dataview.VoucherPlacePopularDataView

interface DealsVoucherPlaceCardListener {
    fun onVoucherPlaceCardBind(voucherPlaceCard: VoucherPlacePopularDataView, position: Int)
    fun onVoucherPlaceCardClicked(voucherPlaceCard: VoucherPlaceCardDataView, position: Int)
}
