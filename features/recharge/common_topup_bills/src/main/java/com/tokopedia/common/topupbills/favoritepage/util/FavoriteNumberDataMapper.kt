package com.tokopedia.common.topupbills.favoritepage.util

import com.tokopedia.common.topupbills.data.TopupBillsContact
import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.favoritepage.view.model.TopupBillsPersoFavNumberDataView
import com.tokopedia.common.topupbills.favoritecommon.view.model.contact.TopupBillsContactDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoComplete
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactDataView
import com.tokopedia.common.topupbills.view.model.search.TopupBillsSearchNumberDataModel

object FavoriteNumberDataMapper {

    fun mapContactToDataView(contacts: List<TopupBillsContact>): List<TopupBillsContactDataView> {
        return contacts.map {
            TopupBillsContactDataView(it.name, it.phoneNumber)
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
                productId = it.trackingData.productId,
                operatorName = it.trackingData.operatorName
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

    fun mapPersoFavNumberItemToSearchDataView(clientNumbers: List<TopupBillsPersoFavNumberItem>): List<TopupBillsSearchNumberDataModel> {
        return clientNumbers.map {
            val (clientName, clientNumber) = if (it.subtitle.isNotEmpty()) {
                it.title to it.subtitle
            } else {
                "" to it.title
            }
            TopupBillsSearchNumberDataModel(
                clientName = clientName,
                clientNumber = clientNumber,
                categoryId = it.trackingData.categoryId,
                operatorId = it.trackingData.operatorId,
                productId = it.trackingData.productId
            )
        }
    }
}