package com.tokopedia.deals.home.ui.dataview

import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView

data class VoucherPlacePopularDataView(
        val title: String = "",
        val subtitle: String = "",
        val voucherPlaceCards: MutableList<VoucherPlaceCardDataView> = mutableListOf()
) : DealsBaseItemDataView()