package com.tokopedia.common.topupbills.utils

import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.view.model.FavoriteNumberDataView

object CommonTopupBillsDataMapper {

    // Data Mapper
    fun mapSeamlessFavNumberItemToDataView(clientNumbers: List<TopupBillsSeamlessFavNumberItem>): List<FavoriteNumberDataView> {
        return clientNumbers.map {
            FavoriteNumberDataView(it)
        }
    }

    fun mapSeamlessDataViewToItem(clientNumbers: List<FavoriteNumberDataView>): List<TopupBillsSeamlessFavNumberItem> {
        return clientNumbers.map {
            it.favoriteNumber
        }
    }
}