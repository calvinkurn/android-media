package com.tokopedia.common.topupbills.view.listener

import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem

interface FavoriteNumberMenuListener {
    fun onChangeNameMenuClicked(favNumberItem: TopupBillsSeamlessFavNumberItem)
    fun onDeleteContactClicked(favNumberItem: TopupBillsSeamlessFavNumberItem)
}