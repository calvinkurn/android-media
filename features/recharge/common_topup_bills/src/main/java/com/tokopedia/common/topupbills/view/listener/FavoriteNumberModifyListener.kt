package com.tokopedia.common.topupbills.view.listener

import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem

interface FavoriteNumberModifyListener {
    fun onChangeName(newName: String, favNumberItem: TopupBillsSeamlessFavNumberItem)
}