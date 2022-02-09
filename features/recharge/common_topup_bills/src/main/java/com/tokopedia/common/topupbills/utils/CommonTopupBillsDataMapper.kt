package com.tokopedia.common.topupbills.utils

import com.tokopedia.common.topupbills.data.TopupBillsContact
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.data.favorite_number_perso.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.view.model.contact.TopupBillsContactDataView
import com.tokopedia.common.topupbills.view.model.favorite.TopupBillsFavNumberDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoComplete
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactDataView
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberDataView

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

    fun mapPersoFavNumberItemToDataView(clientNumbers: List<TopupBillsPersoFavNumberItem>): List<TopupBillsPersoFavNumberDataView> {
        return clientNumbers.map {
            TopupBillsPersoFavNumberDataView(
                title = it.title,
                subtitle = it.subtitle,
                iconUrl = it.mediaUrl,
                categoryId = it.trackingData.categoryId,
                operatorId = it.trackingData.operatorId,
                productId = it.trackingData.productId
            )
        }
    }

    fun mapPersoFavNumberItemToContactDataView(clientNumbers: List<TopupBillsPersoFavNumberItem>): List<TopupBillsAutoComplete> {
        return clientNumbers.map {
            val (clientName, clientNumber) = if (it.subtitle.isNotEmpty()) {
                it.title to it.subtitle
            } else {
                "" to it.title
            }
            TopupBillsAutoCompleteContactDataView(clientName, clientNumber)
        }
    }
}