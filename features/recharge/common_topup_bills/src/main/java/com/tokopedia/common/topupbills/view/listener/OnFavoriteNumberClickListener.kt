package com.tokopedia.common.topupbills.view.listener

import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem

interface OnFavoriteNumberClickListener {
    fun onFavoriteNumberClick(clientNumber: TopupBillsSeamlessFavNumberItem)
    fun onFavoriteNumberMenuClick(favNumberItem: TopupBillsSeamlessFavNumberItem)
}