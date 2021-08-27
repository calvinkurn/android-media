package com.tokopedia.common.topupbills.utils

import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.view.fragment.TopupBillsContactListFragment
import com.tokopedia.common.topupbills.view.model.contact.TopupBillsContactDataView
import com.tokopedia.common.topupbills.view.model.favorite.TopupBillsFavNumberDataView

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

    fun mapContactToDataView(contacts: List<TopupBillsContactListFragment.Contact>): List<TopupBillsContactDataView> {
        return contacts.map {
            TopupBillsContactDataView(it.name, it.phoneNumber)
        }
    }
}