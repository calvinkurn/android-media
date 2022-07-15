package com.tokopedia.common.topupbills.favoritepdp.data.mapper

import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumberData
import com.tokopedia.common.topupbills.favoritepdp.data.model.PersoFavNumberGroup
import com.tokopedia.common.topupbills.favoritepdp.domain.model.AutoCompleteModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteChipModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteGroupModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.PrefillModel
import javax.inject.Inject

class DigitalPersoMapper @Inject constructor() {

    fun mapDigiPersoFavoriteToModel(data: PersoFavNumberGroup): FavoriteGroupModel {
         return FavoriteGroupModel(
             prefill = mapDigiPersoPrefillToModel(data.favoriteNumberPrefill),
             autoCompletes = mapDigiPersoListToModel(data.favoriteNumberList),
             favoriteChips = mapDigiPersoChipsToModel(data.favoriteNumberChips)
         )
    }

    private fun mapDigiPersoPrefillToModel(data: TopupBillsPersoFavNumberData): PrefillModel {
        if (data.persoFavoriteNumber.items.isEmpty()) return PrefillModel()
        val persoPrefillData = data.persoFavoriteNumber.items.first()

        return PrefillModel(
            clientName = persoPrefillData.subtitle,
            clientNumber = persoPrefillData.title,
            token = persoPrefillData.token,
            operatorId = persoPrefillData.operatorId,
            productId = persoPrefillData.productId,
        )
    }

    private fun mapDigiPersoListToModel(data: TopupBillsPersoFavNumberData): List<AutoCompleteModel> {
        return data.persoFavoriteNumber.items.map {

            val (clientName, clientNumber) = if (it.subtitle.isNotEmpty())
                it.title to it.subtitle
            else "" to it.title

            AutoCompleteModel(
                clientName = clientName,
                clientNumber = clientNumber,
                token = it.token,
            )
        }
    }

    private fun mapDigiPersoChipsToModel(data: TopupBillsPersoFavNumberData): List<FavoriteChipModel> {
        return data.persoFavoriteNumber.items.map {

            val (clientName, clientNumber) = if (it.subtitle.isNotEmpty())
                it.title to it.subtitle
            else "" to it.title

            FavoriteChipModel(
                clientName = clientName,
                clientNumber = clientNumber,
                operatorId = it.trackingData.operatorId,
                token = it.token,
            )
        }
    }
}