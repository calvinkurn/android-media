package com.tokopedia.common.topupbills.utils

import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberDataView

object CommonTopupBillsDataMapper {

    // Data Mapper
    fun mapSeamlessFavNumberItemToDataView(clientNumbers: List<TopupBillsSeamlessFavNumberItem>): List<TopupBillsFavNumberDataView> {
        return clientNumbers.map {
            TopupBillsFavNumberDataView(it)
        }
    }

    fun mapSeamlessDataViewToItem(clientNumbers: List<TopupBillsFavNumberDataView>): List<TopupBillsSeamlessFavNumberItem> {
        return clientNumbers.map {
            it.favoriteNumber
        }
    }
}