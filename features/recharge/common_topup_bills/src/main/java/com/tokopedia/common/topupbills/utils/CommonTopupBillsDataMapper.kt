package com.tokopedia.common.topupbills.utils

import com.tokopedia.common.topupbills.data.TopupBillsContact
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.favoritecommon.view.model.contact.TopupBillsContactDataView
import com.tokopedia.common.topupbills.view.model.favorite.TopupBillsFavNumberDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoComplete
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactDataView
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsPersoFavNumberDataView

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

    fun mapContactToDataView(contacts: List<TopupBillsContact>): List<TopupBillsContactDataView> {
        return contacts.map {
            TopupBillsContactDataView(it.name, it.phoneNumber)
        }
    }

    fun mapSeamlessFavNumberItemToContactDataView(clientNumbers: List<TopupBillsSeamlessFavNumberItem>): List<TopupBillsAutoComplete> {
        return clientNumbers.map {
            TopupBillsAutoCompleteContactDataView(it.clientName, it.clientNumber)
        }
    }
}