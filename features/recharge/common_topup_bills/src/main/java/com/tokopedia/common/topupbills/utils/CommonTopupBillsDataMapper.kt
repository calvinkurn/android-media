package com.tokopedia.common.topupbills.utils

import com.tokopedia.common.topupbills.data.TopupBillsContact
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.favoritecommon.view.model.contact.TopupBillsContactDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoComplete
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactModel

object CommonTopupBillsDataMapper {

    fun mapContactToDataView(contacts: List<TopupBillsContact>): List<TopupBillsContactDataView> {
        return contacts.map {
            TopupBillsContactDataView(it.name, it.phoneNumber)
        }
    }

    fun mapSeamlessFavNumberItemToContactDataView(clientNumbers: List<TopupBillsSeamlessFavNumberItem>): List<TopupBillsAutoComplete> {
        return clientNumbers.map {
            TopupBillsAutoCompleteContactModel(it.clientName, it.clientNumber)
        }
    }
}