package com.tokopedia.common.topupbills.view.listener

import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem

interface OnFavoriteNumberClickListener {
    fun onFavoriteNumberClick(clientNumber: TopupBillsFavNumberItem)
}